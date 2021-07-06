import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        loadGUIs();
    }
    
     
    public static void loadGUIs()
    {
        File folder = new File("C:/Users/modes/IdeaProjects/M0-OnlinePlayersGUI/src/test/resources");
        
        if(folder != null)
        {
            for (File file : folder.listFiles())
            {
                if (file.isFile() && file.getName().endsWith(".yml"))
                {
                    System.out.println(file);
                
                    
                
                
                }
            }
        }
        
        
    }
}
