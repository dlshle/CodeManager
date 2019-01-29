/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Management;

import FileImpl.FileImpl;
import FileImpl.FileTree.FileTree;
import MainLogic.Document.Document;
import MainLogic.Indexing.ProjectIndex;
import MainLogic.VersionControl.Version;
import MainLogic.VersionControl.VersionIndex;
import java.util.ArrayList;

/**
 *
 * @author Xuri
 */
public class Driver {
    
    private final FileImpl fileImpl;
    private ProjectIndex projectIndex;
    
    public Driver(){
        fileImpl = new FileImpl();
        if(!init())
        	System.out.println("null project index");
        	
    }
    
    public Driver(String path) {
    	fileImpl = new FileImpl(path);
        if(!init())
        	System.out.println("null project index");
    }
    
    private boolean init(){
        projectIndex = fileImpl.loadProjectIndex();
        return projectIndex!=null;
    }
    
    public int getNumberOfProjects() {
    	return projectIndex.getNumberOfProjects();
    }
    
    public ArrayList<VersionIndex> getAllProjects(){
        return projectIndex.getAllProjects(fileImpl);
    }
    
    public ArrayList<String> getProjectNames(){
        return projectIndex.getProjectNames();
    }
    
    public VersionIndex getProject(int index){
        return projectIndex.getProjectById(fileImpl, index);
    }
    
    public VersionIndex getProject(String name){
        return projectIndex.getProjectByName(fileImpl, name);
    }
    
    public ArrayList<Version> getAllVersions(VersionIndex vIndex){
        return vIndex.getVersions(fileImpl);
    }
    
    public ArrayList<String> getVersionNames(VersionIndex vIndex){
        return vIndex.getVersionNames();
    }
    
    public Version getVersion(VersionIndex vIndex, int index){
        return vIndex.getVersionById(fileImpl, index);
    }
    
    public Version getVersion(VersionIndex vIndex, String version){
        return vIndex.getVersionByVersion(fileImpl, version);
    }

    public Document getDocument(Version version, int index){
        return version.getDocument(index);
    }
    
    public Document getDocument(Version version, String document){
        return version.getDocumentByName(document);
    }
    
    public ArrayList<Document> getDocuments(Version version){
        return version.getDocs();
    }
    
    public boolean addProject(String projectName, String projectDirectory, String[] authors, String description, String firstVersionDescription){
        return projectIndex.newProject(fileImpl, projectName, projectDirectory, authors, description, firstVersionDescription);
    }
    
    public boolean addVersion(VersionIndex vIndex, String[] authors, String version, String versionDescription, String originalDocumentsPath){
        return vIndex.addNewVersion(fileImpl, authors, version, versionDescription, originalDocumentsPath);
    }
    
    public boolean removeProject(int index, String[] userNames){
        return projectIndex.removeProject(fileImpl, index, userNames);
    }
    
    public boolean removeProject(String projectName, String[] userNames){
        return projectIndex.removeProject(fileImpl, projectName, userNames);
    }
    
    public boolean removeVersion(VersionIndex vIndex, int id, String[] userNames){
        return vIndex.removeVersionById(fileImpl, id, userNames);
    }
    
    public boolean removeVersion(VersionIndex vIndex, String version, String[] userNames){
        return vIndex.removeVersion(fileImpl, version, userNames);
    }
    
    public String getVersionFileStructure(Version version){
        FileTree tree = version.getDirectoryStructure();
        return tree.toString();
    }
}
