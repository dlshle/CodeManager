package FileImpl;

import FileImpl.FileTree.FileTree;
import MainLogic.Document.Document;
import MainLogic.Indexing.ProjectIndex;
import MainLogic.VersionControl.Version;
import MainLogic.VersionControl.VersionIndex;

public interface iFile {
    public ProjectIndex loadProjectIndex();
    public boolean saveProjectIndex(ProjectIndex projectIndex);
    public VersionIndex openVersionIndex(int versionIndexId);
    public VersionIndex openVersionIndex(ProjectIndex projectIndex, String projectName);
    public boolean saveVersionIndex(VersionIndex versionIndex);
    public Version openVersionFile(VersionIndex index, int versionId);
    public Version openVersionFile(VersionIndex index, String version);
    public boolean saveVersionFile(Version version);
    public boolean copyFilesInDirectory(String sourceDir, String destDir);
    public FileTree traverseDirectory(String dir);
    public Document[] makeDocs(String path);
    public boolean renameDirectory(String originalPath, String newName);
    public boolean removeDirectory(String path);
    public boolean removeFile(String filePath);
    public boolean makeDirectory(String dirPath);
    public String getRootDirectory();
}
