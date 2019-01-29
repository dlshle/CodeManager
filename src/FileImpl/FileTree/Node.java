package FileImpl.FileTree;

public class Node {
    private Node parent;
    private String path;
    private Node[] children;
    
    public Node(Node parent, String path){
        this.parent = parent;
        this.path = path;
        children = null;
    }
    
    public Node(Node parent, String path, Node[] children){
        this.parent = parent;
        this.path = path;
        this.children = children;
    }
    
    public String getShortPath(){
        return path;
    }
    
    public String getWholePath() {
        if (this.parent == null) {
            return path;
        }
        String newPath = path;
        Node temp = getParent();
        newPath = temp.getShortPath() + "\\" + newPath;
        while (temp.getParent() != null) {
            temp = temp.getParent();
            newPath = temp.getShortPath() + "\\" + newPath;
        }
        return newPath;
    }
    
    public Node getTopNode(){
        if(getParent()==null)
            return this;
        Node temp = getParent();
        while(temp.getParent()!=null)
            temp=temp.getParent();
        return temp;
    }
    
    public String getRelativePath(){
        if (this.parent == null) {
            return path;
        }
        if(this.parent.getParent()==null)
            return path;
        String newPath = path;
        Node temp = getParent();
        newPath = temp.getShortPath() + "\\" + newPath;
        while (temp.getParent()!= null) {
            temp = temp.getParent();            
            if(temp.getParent()==null)
                break;
            newPath = temp.getShortPath() + "\\" + newPath;
        }
        return newPath;        
    }
    
    public Node getParent(){
        return parent;
    }
    
    public Node getChild(int index){
        return children[index];
    }
    
    public Node[] getChildren(){
        return children;
    }
    
    public int getNumberOfChildren(){
        return children.length;
    }
    
    public void setChild(int index, Node child){
        children[index] = child;
    }
    
    public boolean isDirectory(){
        if(children==null)
            return false;
        return children.length>0;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\n");
        if(!isDirectory())
            return sb.toString();
        sb.append("{").append("\n");
        for(Node child:children){
            sb.append(child.toString());
        }
        sb.append("}").append("\n");
        return sb.toString();
    }
    
    public int getNumberOfPosterities(){
        int count = 0;
        for(Node child:children){
            if(child.isDirectory())
                count+=child.getNumberOfPosterities();
            else
                count++;
        }
        return count;
    }
    
    public int getNumberOfAncesters(){
        int count = 0;
        Node temp = getParent();
        while(temp!=null){
            count++;
            temp = temp.getParent();
        }
        return count;
    }
}
