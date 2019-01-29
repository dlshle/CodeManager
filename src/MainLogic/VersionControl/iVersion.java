package MainLogic.VersionControl;

import FileImpl.FileTree.FileTree;
import MainLogic.Document.Document;
import java.util.ArrayList;

public interface iVersion {
    public String getVersion();
    public String getVersionInfo();
    public String getVersionId();
    public String getProjectId();
    public String getProjectName();
    public String[] getAuthors();
    public String getDate();
    public String getDescription();
    public String getDocumentDirectory(); 
    public Document getDoc(String docDirectory);
    public ArrayList<Document> getDocs();
    public String toFileFormat();
    public Document getDocument(int index);
    public Document getDocumentByName(String name);
    public FileTree getDirectoryStructure();
    public String getRootDirectory();
    public String getVersionDirectory();
    public String[] getDocumentNames();
}
