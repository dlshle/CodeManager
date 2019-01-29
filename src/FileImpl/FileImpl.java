package FileImpl;

import FileImpl.FileTree.FileTree;
import MainLogic.Document.Document;
import MainLogic.Indexing.ProjectIndex;
import MainLogic.VersionControl.Version;
import MainLogic.VersionControl.VersionIndex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class FileImpl implements iFile {

    private String rootDirectory;

    public FileImpl(String path) {
        rootDirectory = path;
    }

    public FileImpl() {
        rootDirectory = "";
    }

    @Override
    public boolean copyFilesInDirectory(String sourceDir, String destDir) {
        File sourceDirectory = new File(sourceDir);
        File destDirectory = new File(destDir);
        if (!sourceDirectory.isDirectory()) {
            System.out.println(sourceDir + " is not a directory!");
            return false;
        }
        if (destDirectory.isDirectory()) {
            System.out.println("Invalid destDir!");
            return false;
        }
        //make the destination directory
        destDirectory.mkdir();
        if (!copyFiles(new File(sourceDir), new File(destDir))) {
            return false;
        }
        return true;
    }

    private boolean copyFiles(File source, File dest) {
        File temp;
        File[] files;
        if (!source.exists()) {
            System.out.println(source.getName() + " does not exist!");
            return false;
        }
        if (source.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            files = source.listFiles();
            for (final File file : files) {
                if (file.isDirectory()) {
                    //if the file in the file directory is a directory
                    temp = new File(dest.getAbsoluteFile() + "\\" + file.getName());
                    if (!copyFiles(file, temp)) {
                        return false;
                    }
                } else {
                    //just copy the file
                    temp = new File(dest.getAbsoluteFile() + "\\" + file.getName());
                    if (!copyFile(file, temp)) {
                        return false;
                    }
                }
            }
        } else {
            //if source is not a directory
            copyFile(source, dest);
        }
        return true;
    }

    private boolean copyFile(File source, File dest) {
        try {
            FileUtils.copyFile(source, dest);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public ProjectIndex loadProjectIndex() {
        File projectIndexFile = new File(rootDirectory + "/ProjectIndex.idx");
        if (!projectIndexFile.exists()) {
            System.out.println("Error, project index file missing!");
            if(restoreProjectIndexFile())
                return loadProjectIndex();
            return null;
        }
        return readProjectIndex(projectIndexFile);
    }
    
    /**
     * 
     * @return true if prjindexfile is restored, vice versa
     */
    private boolean restoreProjectIndexFile() {
        ArrayList<String> dirNames;
        ArrayList<String> projectNames = new ArrayList<>();
        //cehck if it's the first time
        FileTree tree = traverseDirectory(rootDirectory);
        dirNames = tree.getDirectoryNamesUnder(tree.getRootNode());
        if (dirNames.size() > 0) {
            //sort dir names
            dirNames.sort((Object o1, Object o2) -> {
                String a = (String) o1;
                String b = (String) o2;
                return a.compareTo(b);
            });
            //check if there exists continues named directory
            int index = dirNames.indexOf("0");
            int numOfProjects = 0;
            if (index < 0) {
                return generateNewProjectIndex(); //fail to generate new prj index
            }
            numOfProjects++;
            //try to get the name of the first project
            VersionIndex temp = openVersionIndex(0);
            if(temp==null)
                //if the folder is not valid, generate a new project index
                return generateNewProjectIndex();
            projectNames.add(temp.getProjectName());
            //check how many dirs
            while (index != -1) {
                index = dirNames.indexOf(String.valueOf(numOfProjects));
                if (index > -1) {
                    temp = openVersionIndex(index);
                    if(temp==null)
                        break;
                    projectNames.add(temp.getProjectName());
                    numOfProjects++;
                }
            }
            ProjectIndex result = new ProjectIndex(projectNames, "");
            return saveProjectIndex(result);
        }
        return generateNewProjectIndex();
    }

    private boolean generateNewProjectIndex(){
        String content = "0\n%\n%";
        return saveFile(content, rootDirectory + "/ProjectIndex.idx");
    }

    private ProjectIndex readProjectIndex(File projectIndexFile) {
        try {
            Scanner sc = new Scanner(projectIndexFile);
            ProjectIndex index;
            int projectCount = 0;
            String[] projectNames;
            String descriptions = "";
            //TODO: finish the reading process
            //# of projects
            projectCount = Integer.valueOf(sc.nextLine());
            if (projectCount < 0) {
                return null;
            }
            projectNames = new String[projectCount];

            //[pid] = name
            if (!sc.nextLine().equals("%")) {
                System.out.println("ERROR, Incorrect file format!");
                return null;
            }
            for (int i = 0; i < projectCount; i++) {
                projectNames[i] = sc.nextLine();
            }
            if (!sc.nextLine().equals("%")) {
                System.out.println("ERROR, Incorrect file format!");
                return null;
            }
            while(sc.hasNextLine()){
                descriptions+=sc.nextLine();
            }
            return new ProjectIndex(projectNames, descriptions);
        } catch (FileNotFoundException | NumberFormatException ex) {
            return null;
        } catch (IndexOutOfBoundsException nobex) {
            System.out.println("ERROR, Incorrect file format!");
            return null;
        }
    }

    @Override
    public VersionIndex openVersionIndex(int versionIndexId) {
        File versionIndexFile = new File(rootDirectory + "/" + versionIndexId + "/project.idx");
        return readVersionIndex(versionIndexFile);
    }

    @Override
    public VersionIndex openVersionIndex(ProjectIndex projectIndex, String projectName) {
        return openVersionIndex(projectIndex.getProjectIdByName(projectName));
    }

    public VersionIndex readVersionIndex(File versionIndexFile) {
        String versionId;
        String projectName;
        int numOfAuthors;
        String[] authors = null;
        String startDate, lastDate;
        int numOfVersions;
        String[] versions = null;
        String descriptions = new String();
        try {
            Scanner sc = new Scanner(versionIndexFile);
            //TODO: finish the versionindex reading part

            versionId = sc.nextLine();
            if (!isNumericString(versionId)) {
                return null;
            }
            projectName = sc.nextLine();
            numOfAuthors = Integer.valueOf(sc.nextLine());
            if (numOfAuthors < 0) {
                return null;
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            authors = new String[numOfAuthors];
            for (int i = 0; i < numOfAuthors; i++) {
                authors[i] = sc.nextLine();
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            startDate = sc.nextLine();
            lastDate = sc.nextLine();
            numOfVersions = Integer.valueOf(sc.nextLine());
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            versions = new String[numOfVersions];
            for (int i = 0; i < numOfVersions; i++) {
                versions[i] = sc.nextLine();
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            //descriptions
            while (sc.hasNextLine()) {
                descriptions += sc.nextLine();
            }
            String rootPath = versionIndexFile.getPath();
            rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
            return new VersionIndex(versionId, projectName, authors, startDate, lastDate, Arrays.asList(versions), descriptions, rootPath);
        } catch (FileNotFoundException | NumberFormatException ex) {
            return null;
        } catch (IndexOutOfBoundsException iobex) {
            System.out.println("ERROR, Incorrect file format!");
            return null;
        }
    }

    @Override
    public boolean saveVersionIndex(VersionIndex versionIndex) {
        return saveFile(versionIndex.toFileFormat(), rootDirectory + "/" + versionIndex.getId() + "/project.idx");
    }

    @Override
    public Version openVersionFile(VersionIndex index, int versionId) {
        return readVersionFile(index.getProjectName(), new File(rootDirectory + "/" + index.getId() + "/" + versionId + "/version.info"));
    }

    @Override
    public Version openVersionFile(VersionIndex index, String version) {
        return readVersionFile(index.getProjectName(), new File(rootDirectory + "/" + index.getId() + "/" + index.getVersionIdByVersion(version) + "/version.info"));
    }

    public Version readVersionFile(String projectName, File versionFile) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String id;
        String projectId;
        String version;
        int numOfAuthors = 0;
        String[] authors;
        String date;
        int numOfFiles = 0;
        String[] filePaths;
        String tempPath;
        String fileType;
        File temp;
        Document[] documents;
        String descriptions = new String();
        try {
            Scanner sc = new Scanner(versionFile);
            id = sc.nextLine();
            if (!isNumericString(id)) {
                return null;
            }
            projectId = sc.nextLine();
            version = sc.nextLine();
            numOfAuthors = Integer.valueOf(sc.nextLine());
            if (numOfAuthors < 0) {
                return null;
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            authors = new String[numOfAuthors];
            for (int i = 0; i < numOfAuthors; i++) {
                authors[i] = sc.nextLine();
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            date = sc.nextLine();
            numOfFiles = Integer.valueOf(sc.nextLine());
            if (numOfFiles < 0) {
                return null;
            }
            filePaths = new String[numOfFiles];
            documents = new Document[numOfFiles];
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            for (int i = 0; i < numOfFiles; i++) {
                filePaths[i] = sc.nextLine();
                //tempPath = versionFile.getAbsolutePath()+filePaths[i];
                tempPath = versionFile.getPath().substring(0, versionFile.getAbsolutePath().length() - 12);
                tempPath += filePaths[i];
                temp = new File(tempPath);
                fileType = Files.probeContentType(Paths.get(tempPath));
                documents[i] = new Document(tempPath, fileType == null ? "unknown" : fileType, sdf.format(temp.lastModified()));
            }
            if (!sc.nextLine().equals("%")) {
                return null;
            }
            while (sc.hasNextLine()) {
                descriptions += sc.nextLine();
            }
            tempPath = versionFile.getPath();
            tempPath = tempPath.substring(0,tempPath.lastIndexOf("\\"));
            return new Version(id, projectId, projectName, version, authors, date, descriptions, documents, tempPath);
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR, File missing!");
            return null;
        } catch (IndexOutOfBoundsException | NumberFormatException fex) {
            System.out.println("ERROR, Incorrect file format!");
            return null;
        } catch (IOException ex) {
            System.out.println("ERROR, IOException!");
            return null;
        }
    }

    @Override
    public boolean saveVersionFile(Version version) {
        return saveFile(version.toFileFormat(),version.getVersionDirectory()+"\\version.info");
    }

    @Override
    public FileTree traverseDirectory(String dir) {
        return new FileTree(dir);
    }

    public boolean isNumericString(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public boolean saveFile(String data, String filePath) {
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileWriter fW = new FileWriter(filePath);
            PrintWriter out = new PrintWriter(fW);
            out.print(data);
            fW.close();
            out.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR, Can not save the file(FileNotFound)!");
            return false;
        } catch (IOException ex) {
            System.out.println("ERROR, Can not save the file(IOException)!");
            return false;
        }
    }

    @Override
    public boolean saveProjectIndex(ProjectIndex projectIndex) {
        return saveFile(projectIndex.toFileFormat(), rootDirectory + "/ProjectIndex.idx");
    }

    @Override
    public Document[] makeDocs(String path) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        FileTree tree = traverseDirectory(path);
        String fileType;
        if (tree == null) {
            return null;
        }
        String[] relativePaths = tree.getAllRelativePaths(tree.getRootNode());
        Document[] documents = new Document[relativePaths.length];
        try {
            for (int i = 0; i < relativePaths.length; i++) {
                fileType = Files.probeContentType(Paths.get(path + relativePaths[i]));
                documents[i] = new Document(relativePaths[i], fileType == null ? "unknown" : fileType, sdf.format(System.currentTimeMillis()));
            }
            return documents;
        } catch (IOException ioe) {
            return null;
        }
    }

    @Override
    public boolean renameDirectory(String originalPath, String newName) {
        File oFile = new File(originalPath);
        if(!oFile.exists()||!oFile.isDirectory())
            return false;
        File newDir = new File(oFile.getParent() + "\\" + newName);
        if(!oFile.renameTo(newDir))
            return false;
        return true;
    }
    
    @Override
    public boolean removeDirectory(String path){
        File dir = new File(path);
        if(!dir.exists()||!dir.isDirectory())
            return false;        
        //empty the directory first and remove it
        File[] files = dir.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                if(!removeFile(f.getPath()))
                    return false;
            } else {
                if(!removeDirectory(f.getPath()))
                    return false;
            }
        }
        //use path to remove the directory to avoid errors
        Path fp = dir.toPath();
        try {
            //dir has to be empty
            Files.delete(fp);
            return true;
        } catch (IOException ex) {
            System.out.println("Unable to remove the directory(the directory is locked/lack of permission/opened by another process).");
            return false;
        }
    }
    
    @Override
    public boolean removeFile(String filePath){
        File file = new File(filePath);
        if(!file.exists())
            return false;
        if(file.isDirectory())
            return false;
        return file.delete();
    }
    
    @Override
    public boolean makeDirectory(String dirPath){
        File dir = new File(dirPath);
        //check if the parent path is valid
        if(!dir.getParentFile().isDirectory())
            return false;
        return dir.mkdir();
    }
    
    @Override
    public String getRootDirectory(){
        return rootDirectory;
    }
}
