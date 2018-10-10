
package console_chat;

import java.io.File;
import java.io.Serializable;

public class Message implements Serializable
{
    public static int id = 0;
    
    private String msg;
    private String name;
    
    public Message(String name, String msg)
    {
        ++id;
        this.msg = msg;
        this.name = name;
    }
    
    public String getMessage()
    {
        return msg;
    }
    
    public String getName()
    {
        return name;
    }
}