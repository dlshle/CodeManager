package MainLogic.Indexing;

import FileImpl.FileImpl;
import FileImpl.iFile;
import MainLogic.VersionControl.VersionIndex;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ProjectIndex implements iProjectIndex {
    
    private ArrayList<String> projects;
    private String descriptions;

    public ProjectIndex(String[] projectNames, String descriptions) {
        projects = new ArrayList<>(Arrays.asList(projectNames));
        this.descriptions = descriptions;
    }
    
    public ProjectIndex(ArrayList<String> projectNames, String descriptions){
        projects = projectNames;
        this.descriptions = descriptions;
    }
    
    public VersionIndex getProjectById(FileImpl fileImpl, int id){
        if(id<0||id>=projects.size())
            return null;
        return fileImpl.openVersionIndex(id);
    }
    
    public VersionIndex getProjectByName(iFile fileImpl, String name){
        int index = projects.indexOf(name);
        if(index == -1)
            return null;
        return fileImpl.openVersionIndex(index);
    }
    
    public int getProjectIdByName(String projectName){
        return projects.indexOf(projectName);
    }

    @Override
    public boolean removeProject(FileImpl fileImpl, int id, String[] userNames) {
        if(id<0||id>=projects.size())
            return false;
        int oldLen = projects.size();
        String oldProjectName = projects.get(id);
        //remove directory
        if(!fileImpl.removeDirectory(fileImpl.getRootDirectory()+"\\"+id))
            return false;
        //rename directories
        for(int i=id+1;i<oldLen;i++){
            if(!fileImpl.renameDirectory(fileImpl.getRootDirectory()+"\\"+i, fileImpl.getRootDirectory()+"\\"+(i-1)))
                return false;
        }
        projects.remove(id);
        descriptions+="\nOn "+new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(System.currentTimeMillis())+"\n";
        for(String name:userNames)
            descriptions+=name+", ";
        descriptions+="removed project "+oldProjectName+"("+id+").";
        return fileImpl.saveProjectIndex(this);
    }

    @Override
    public boolean removeProject(FileImpl fileImpl, String name, String[] userNames) {
        return removeProject(fileImpl, projects.indexOf(name), userNames);
    }

    @Override
    public ArrayList<VersionIndex> getAllProjects(FileImpl fileImpl) {
        ArrayList<VersionIndex> allProjects = new ArrayList<>();
        for(int i=0;i<projects.size();i++)
            allProjects.add(getProjectById(fileImpl, i));
        return allProjects;
    }
    
    @Override
    public ArrayList<String> getProjectNames(){
        return projects;
    }
    
    @Override
    public int getNumberOfProjects() {
    	return projects.size();
    }

    @Override
    public boolean newProject(FileImpl fileImpl, String projectName, String projectDirectory, String[] authors, String description, String firstVersionDescription) {
        String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(System.currentTimeMillis());
        String newPid = String.valueOf(projects.size());
        String newProjectPath = fileImpl.getRootDirectory()+"\\"+projects.size();
        //make the project directory
        if(!fileImpl.makeDirectory(newProjectPath))
            return false;
        //make the VersionIndex with empty version list
        VersionIndex newVersionIndex = new VersionIndex(newPid, projectName, authors, date, date, new ArrayList<>(), description, newProjectPath);
        //make the first version
        if(!newVersionIndex.addNewVersion(fileImpl, authors, "1.0", firstVersionDescription, projectDirectory))
            return false;
        //add the new project index
        projects.add(newVersionIndex.getProjectName());
        //update files(no need to update the version file as it's already been updated when adding the new version
        if(!(fileImpl.saveVersionIndex(newVersionIndex) && fileImpl.saveProjectIndex(this)))
            return false;
        return true;
    }
    
    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(projects.size());
        sb.append("\n");
        sb.append("%").append("\n");
        for (String projectName : projects) {
            sb.append(projectName).append("\n");
        }
        sb.append("%");
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Projecs:{");
        for(String s:projects)
            sb.append(s).append(" ");
        sb.append("},").append("Descriptions:").append(descriptions);
        return sb.toString();
    }
}
