package console_chat;

import console_chat.gui.ClientPanel;
import java.io.*;
import java.net.*;

public class consoleClient //implements Runnable
{
    private final String name;
    private final String ip;
    private final int port;
    
    private Socket s;
    private ObjectOutputStream Oout;
    private ObjectInputStream Oin;
    
    public consoleClient(String name, String ip)
    {
        port = 8080;
        this.name = name;
        this.ip = ip;
        System.out.println(name + "> started");
    }
    
    public boolean connect()
    {
        try
        {
            s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 10000);
            Oout = new ObjectOutputStream(s.getOutputStream());
            Oin = new ObjectInputStream(s.getInputStream());
            System.out.println(name + "> IO created");  
            Oout.writeObject(new Message(name, "%join"));
            return true;
        }
        catch (SocketException e)
        {
            System.out.println(name + "> unable to connect during 10sec\n" + e.toString());
            return false;
        }
        catch(IOException e)
        {
            System.out.println(name + "> error in connect():\n" + e.toString());
            return false;
        } 
    }
    
    public boolean send(String m)
    {
        if (m.isEmpty()) { return false; }
        
        Message msg = new Message(name, m);
        try 
        {
            System.out.println(name + "> in send()\n");
            Oout.writeObject(msg);
            Oout.flush();
            ClientPanel.messageArea.append(name + "|: " + msg.getMessage() + "\n");
            return true;
        } 
        catch (IOException ex) 
        {
            System.out.println(name + "> error in send():\n" + ex.toString());
            return false;
        }
    }
    
    public Message receiveOnce() throws Exception
    {   
        Message msg;
        msg = (Message)Oin.readObject();
        return msg;
    }
    
    public void receive() //значення не вертає, юзається тільки для TUI
    {
        try 
        {
            while (isReady())
            {
                Message msg = receiveOnce();
                ClientPanel.messageArea.append(name + "> received:\n" + 
                        msg.getName() + " > " + msg.getMessage());
            }
            stop();
        } 
        catch (IOException ex) 
        {
            System.out.println(name + " error in receive():\n" + ex.toString());
        } 
        catch (Exception ex) 
        {
            System.out.println(name + " error in receive():\n" + ex.toString());
        }
    }
    
    public boolean isReady()
    {
        return s.isConnected();
    }
    
    public boolean stop()
    {
        send(" " + name + "left :(");
        send("%off");
        try 
        {
            Oout.flush();
            Oout.close();
            Oin.close();
            s.close();
            return true;
        }
        catch(IOException e)
        {
            System.out.println(name + "> error in stop():\n" + e.toString());
            return false;
        }
        finally
        {
            System.out.println(name + "> stopped");
        }
    }
}