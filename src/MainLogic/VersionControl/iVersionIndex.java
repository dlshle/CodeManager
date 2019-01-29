package MainLogic.VersionControl;

import FileImpl.FileImpl;
import java.util.ArrayList;

public interface iVersionIndex {
    public int getNumOfVersions();
    public String getProjectName();
    public ArrayList<Version> getVersions(FileImpl fileImpl);
    public ArrayList<String> getVersionNames();
    public Version getVersionById(FileImpl fileImpl, int id);
    public int getVersionIdByVersion(String version);
    public Version getVersionByVersion(FileImpl fileImpl,String version);
    public boolean addNewVersion(FileImpl fileImpl, String[] authors, String version, String versionDescription, String originalDocumentsPath);
    public boolean addNewVersion(FileImpl fileImpl, Version version);
    public boolean removeVersion(FileImpl fileImpl, String version, String[] userNames);
    public boolean removeVersionById(FileImpl fileImpl, int versionId, String[] userNames);    
    public String toFileFormat();
    public String getRootDirectory();
}
