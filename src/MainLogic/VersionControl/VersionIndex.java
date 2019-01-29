package MainLogic.VersionControl;

import FileImpl.FileImpl;
import MainLogic.Document.Document;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VersionIndex implements iVersionIndex{
    private String projectName;
    private String[] authors;
    private String id;
    private String createdDate;
    private String lastModifiedDate;
    private ArrayList<String> versions;
    private String descriptions;
    private String rootDirectory;

    public VersionIndex(String id, String projectName, String[] authors, String createdDate, String lastModifiedDate, List<String> versions, String descriptions, String rootDirectory) {
        this.projectName = projectName;
        this.authors = authors;
        this.id = id;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.versions = new ArrayList<>(versions);
        this.descriptions = descriptions;
        this.rootDirectory = rootDirectory;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getDescriptions() {
        return descriptions;
    }  

    @Override
    public int getNumOfVersions() {
        return versions.size();
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public ArrayList<Version> getVersions(FileImpl fileImpl) {
        Version[] theVersions = new Version[versions.size()];
        for(int i=0;i<versions.size();i++){
            theVersions[i] = getVersionById(fileImpl, i);
        }
        return new ArrayList<>(Arrays.asList(theVersions));
    }

    @Override
    public ArrayList<String> getVersionNames(){
        return versions;
    }
    
    @Override
    public Version getVersionById(FileImpl fileImpl, int id) {
        try{
            if(id<0||id>=versions.size())
                return null;
            return fileImpl.openVersionFile(this, id);
        } catch(NumberFormatException nfe){
            return null;
        }
    }

    @Override
    public boolean addNewVersion(FileImpl fileImpl, String[] authors, String version, String versionDescription, String originalDocumentsPath) {
        if(versions.indexOf(version)>-1)
            return false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = sdf.format(System.currentTimeMillis());
        String dir = rootDirectory+"\\"+versions.size();
        Document[] documents;
        //Make documents
        if(!fileImpl.copyFilesInDirectory(originalDocumentsPath, dir))
            return false;
        System.out.println("Files copy finished.");
        documents = fileImpl.makeDocs(dir);
        //(String id, String pid, String projectName, String version, String[] authors, String date, String description, Document[] docs, String rootDir)
        return addNewVersion(fileImpl, new Version(String.valueOf(versions.size()), id, projectName, version, authors, date, versionDescription, documents, dir));
    }

    @Override
    public boolean addNewVersion(FileImpl fileImpl, Version version) {
        if(!fileImpl.saveVersionFile(version))
            return false;
        versions.add(version.getVersion());
        lastModifiedDate = version.getDate();
        if(!fileImpl.saveVersionIndex(this))
            return false;
        return true;
    }

    @Override
    public boolean removeVersion(FileImpl fileImpl, String version, String[] userNames) {
        int index = versions.indexOf(version);
        return removeVersionById(fileImpl, index, userNames);
    }

    @Override
    public boolean removeVersionById(FileImpl fileImpl, int versionId, String[] userNames) {
        if(versionId<0||versionId>=versions.size())
            return false;
        int oldLen = versions.size();
        String removedVersion = versions.get(versionId);
        versions.remove(versionId);
        //remove the versionId directory and rename all directories after the versionId directory(-1)
        if(!fileImpl.removeDirectory(rootDirectory+"\\"+versionId))
            return false;
        for(int i=versionId+1;i<oldLen;i++){
            if(!fileImpl.renameDirectory(rootDirectory+"\\"+i, rootDirectory+"\\"+(i-1)))
                return false;
        }
        descriptions+="\nOn "+new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(System.currentTimeMillis())+"\n";
        for(String name:userNames)
            descriptions+=name+", ";
        descriptions+="removed version "+removedVersion+"("+versionId+").";
        return fileImpl.saveVersionIndex(this);
    }

    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\n");
        sb.append(projectName).append("\n");
        sb.append(authors.length).append("\n");
        sb.append("%").append("\n");
        for(String author:authors)
            sb.append(author).append("\n");
        sb.append("%").append("\n");
        sb.append(createdDate).append("\n");
        sb.append(lastModifiedDate).append("\n");
        sb.append(versions.size()).append("\n");
        sb.append("%").append("\n");
        for(String version:versions)
            sb.append(version).append("\n");
        sb.append("%").append("\n");
        sb.append(descriptions);
        return sb.toString();
    }    

    @Override
    public int getVersionIdByVersion(String version) {
        return versions.indexOf(version);
    }
    
    @Override 
    public String getRootDirectory(){
        return rootDirectory;
    }
    
    @Override
    public Version getVersionByVersion(FileImpl fileImpl, String version){
        return getVersionById(fileImpl, getVersionIdByVersion(version));
    }
}
