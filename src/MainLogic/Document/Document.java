package MainLogic.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Document implements iDocument{
    private String path;
    private String type;
    private String lastModifiedDate;

    public Document(String path, String type, String lastModifiedDate) {
        this.path = path;
        this.type = type;
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
    public String getContent() {
        if(!type.equals("text/plain"))
            return "Unsupported Type";
        try {
            Scanner reader = new Scanner(new File(path));
            StringBuilder sb = new StringBuilder();
            while(reader.hasNextLine())
                sb.append(reader.nextLine()).append("\n<br>\n");
            return sb.toString();
        } catch (FileNotFoundException ex) {
            return "ERROR, File not found!";
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public String getPath() {
        return path;
    }    

    @Override
    public String getFileName() {
        int lastSlash = path.lastIndexOf("\\");
        if(lastSlash>-1)
            return path.substring(lastSlash+1);
        return path;
    }
}
