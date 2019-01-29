package FileImpl.FileTree;

import java.util.ArrayList;

public interface iFileTree {
    
    public Node traverseDirectory(Node root, String path);
    public String getRootDirectory();
    public boolean makeDirectoryAt(Node node);
    public ArrayList<String> getFileNamesUnder(Node node);
    public ArrayList<String> getDirectoryNamesUnder(Node node);
}
