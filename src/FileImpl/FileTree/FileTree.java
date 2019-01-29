package FileImpl.FileTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Xuri
 */
public class FileTree implements iFileTree {
    
    private Node root;
    
    public FileTree(String path){
        root = traverseDirectory(null, path);
    }
    
    public FileTree(Node root){
        this.root = root;
    }

    @Override
    public String getRootDirectory() {
        return root.getWholePath();
    }

    @Override
    public boolean makeDirectoryAt(Node node) {
        if(node.isDirectory())
            return false;
        File file = new File(node.getWholePath());
        file.mkdir();
        return true;
    }

    @Override
    public Node traverseDirectory(Node root, String path) {
        File dir = new File(path);
        if(!dir.exists())
            return null;
        if(!dir.isDirectory())
            return new Node(root, path);
        int count = 0;
        //if this is a directory with a root node, the path should be the simplified path(replace the root path with ""
        String newPath = root==null?path:path.replace(root.getWholePath(), "");
        Node[] dirChildren = new Node[dir.listFiles().length];
        Node rootDir = new Node(root, newPath, dirChildren);
        Node temp;
        for(File file:dir.listFiles()){
            if(file.isDirectory()){
                temp = traverseDirectory(rootDir, file.getPath());
                rootDir.setChild(count, temp);
            }else{
                rootDir.setChild(count, new Node(rootDir, file.getPath().replace(path+"\\", "")));
            }
            count++;
        }
        return rootDir;
    }

    @Override
    public ArrayList<String> getFileNamesUnder(Node node) {
        String[] fileNames = new String[node.getNumberOfChildren()];
        for(int i=0;i<node.getNumberOfChildren();i++){
            fileNames[i]=node.getChild(i).getWholePath().substring(node.getChild(i).getWholePath().lastIndexOf("//")+1);
        }
        return new ArrayList<>(Arrays.asList(fileNames));
    }

    @Override
    public ArrayList<String> getDirectoryNamesUnder(Node node) {
        ArrayList<String> dirNames = new ArrayList<>();
        Node temp;
        String tempName;
        int count = 0;
        for(int i=0;i<node.getNumberOfChildren();i++){
            temp = node.getChild(i);
            if(temp.isDirectory()){
                tempName = node.getChild(i).getWholePath();
                tempName = tempName.substring(tempName.lastIndexOf("\\")+1);
                dirNames.add(tempName);
            }
        }
        return dirNames;
    }
    
    public Node getRootNode(){
        return root;
    }
    
    @Override
    public String toString(){
        return root.toString();
    }
    
    public String getRelativePaths(Node node){
        StringBuilder sb = new StringBuilder();
        for(Node child:node.getChildren()){
            if(!child.isDirectory())
                sb.append(child.getRelativePath()).append("\n");
            else
                sb.append(getRelativePaths(child));
        }
        return sb.toString();
    }
    
    public int getNumberOfFiles(){
        return root.getNumberOfPosterities();
    }
    
    public String[] getAllRelativePaths(Node node){
        ArrayList<String> paths = new ArrayList<>();
        String[] result;
        for(Node child:node.getChildren()){
            if(child.isDirectory())
                paths.addAll(Arrays.asList(getAllRelativePaths(child)));
            else
                paths.add(child.getRelativePath());
        }
        result = new String[paths.size()];
        return paths.toArray(result);
    }
}
