package MainLogic.VersionControl;

import FileImpl.FileTree.FileTree;
import MainLogic.Document.Document;
import java.util.ArrayList;
import java.util.Arrays;

public class Version implements iVersion {
    private String projectName;
    private String id;
    private String pid;
    private String version;
    private String[] authors;
    private String date;
    private String descriptions;
    private Document[] docs;
    private String rootDir;

    public Version(String id, String pid, String projectName, String version, String[] authors, String date, String description, Document[] docs, String rootDir) {
        this.projectName = projectName;
        this.id = id;
        this.pid = pid;
        this.version = version;
        this.authors = authors;
        this.date = date;
        this.descriptions = description;
        this.docs = docs;
        this.rootDir = rootDir;
    }

    @Override
    public String getVersion(){
        return version;
    }
    
    @Override
    public String getVersionId() {
        return id;
    }

    @Override
    public String[] getAuthors() {
        return Arrays.copyOfRange(authors, 0, authors.length);
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return descriptions;
    }

    @Override
    public String getDocumentDirectory() {
        return "./"+pid+"/"+id;
    }
    
    @Override
    public String[] getDocumentNames() {
    	String[] docNames = new String[docs.length];
    	for(int i=0;i<docs.length;i++) {
    		docNames[i]=docs[i].getFileName();
    	}
    	return docNames;
    }
    
    @Override
    public String getVersionInfo() {
        StringBuilder out = new StringBuilder(projectName);
        out.append("{" + "projectId:").append(pid).append(", VersionId")
                .append(id).append(", Version:").append(version)
                .append(", Authors:");
        for(int i=0;i<authors.length;i++)
            out.append(authors[i]).append(", ");
        out.append("Created Date:").append(date).append(", ");
        out.append("Description:").append(descriptions).append(" ,");
        out.append("Documents directory:").append(getDocumentDirectory()).append("}");
        return out.toString();
    }

    @Override
    public ArrayList<Document> getDocs() {
        return new ArrayList<Document>(Arrays.asList(docs));
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public String getProjectId() {
        return pid;
    }

    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\n");
        sb.append(pid).append("\n");
        sb.append(version).append("\n");
        sb.append(authors.length).append("\n").append("%").append("\n");
        for(String author:authors)
            sb.append(author).append("\n");
        sb.append("%\n");
        sb.append(date).append("\n");
        sb.append(docs.length).append("\n").append("%").append("\n");
        for(Document doc:docs)
            sb.append(doc.getPath()).append("\n");        
        sb.append("%\n");
        sb.append(descriptions);
        return sb.toString();
    }

    @Override
    public Document getDocument(int index) {
        if(index<0||index>docs.length){
            return null;
        }
        return docs[index];
    }

    @Override
    public Document getDocumentByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Document getDoc(String docDirectory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FileTree getDirectoryStructure() {
        return new FileTree(rootDir);
    }
    
    @Override
    public String getRootDirectory(){
        return rootDir;
    }
    
    @Override
    public String getVersionDirectory(){
        return rootDir;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Project:").append(getProjectName()).append("(").append(pid).append(")");
        sb.append("{").append("Version:").append(getVersion()).append("(").append(getVersionId()).append("),");
        sb.append("authors:{");
        for(String name:authors)
            sb.append(name).append(" ");
        sb.append("},");
        sb.append("lastModifiedDate:").append(date).append(",");
        sb.append("descriptions:").append(descriptions).append(",");
        sb.append("NumberOfDocuments:").append(docs.length).append(",");
        sb.append("rootDirectory:").append(rootDir).append("}");
        return sb.toString();
    }
}

