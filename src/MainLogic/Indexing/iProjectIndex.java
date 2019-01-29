package MainLogic.Indexing;

import FileImpl.FileImpl;
import MainLogic.VersionControl.VersionIndex;
import java.util.ArrayList;

public interface iProjectIndex {
    public boolean removeProject(FileImpl fileImpl, int id, String[] userNames);
    public boolean removeProject(FileImpl fileImpl, String name, String[] userNames);
    public ArrayList<VersionIndex> getAllProjects(FileImpl fileImpl);
    public boolean newProject(FileImpl fileImpl, String projectName, String projectDirectory, String[] authors, String description, String firstVersionDescription);
    public String toFileFormat();
    public ArrayList<String> getProjectNames();
    public int getNumberOfProjects();
}
