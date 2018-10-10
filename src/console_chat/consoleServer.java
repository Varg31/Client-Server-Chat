package console_chat;

import console_chat.gui.ClientPanel;
import java.util.*;
import java.net.*;
import java.io.*;

public class consoleServer 
{
    private static String ip;
    private int port;
    private ArrayList<ObjectOutputStream> outs = new ArrayList<>();
    
    
    public consoleServer()
    {
        port = 8080;
        System.out.println("SERVER> created");
    }
    
    public static String getIP() throws IOException
    {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while(en.hasMoreElements())
        {
            NetworkInterface ni=(NetworkInterface) en.nextElement();
            Enumeration ee = ni.getInetAddresses();
            while(ee.hasMoreElements()) 
            {
                InetAddress ia= (InetAddress) ee.nextElement();
                String a = ia.getHostAddress();
                if (a.contains("192"))
                    ip = a;
            }
        }
        return ip;
    }
    
    public void addClient()
    {
        while (true)
        {
            try(ServerSocket ss = new ServerSocket(port))
            {
                Socket incoming = ss.accept();
                System.out.println("SERVER> Conncted: " + (outs.size()+1));
                Thread handlerThread = new Thread(new ServerHandler(incoming));
                handlerThread.start();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println("SERVER> cannot add client");
            }
        }
    }
    
    public synchronized void sendAll(Message msg)
    {
        for (ObjectOutputStream sender: outs)
        {
            try 
            {
                sender.writeObject(msg);
                sender.flush();
                //ClientPanel.messageArea.append("SERVER> sent to all clients" + "\n");
            } 
            catch (IOException ex) 
            {
                outs.remove(sender);
                System.out.println("SERVER> sender deleted");
            }
        }
    }
    
    private class ServerHandler implements Runnable
    {
        private Socket s;
        private ObjectInputStream Oin;
        private ObjectOutputStream Oout;
        
        private InputStream inStream;
        private OutputStream outStream;
        
        ServerHandler(Socket incoming)
        {
            s = incoming;
        }
        
        @Override
        public void run()
        {
            System.out.println("SERVER> handler running...");
            connect();
            start();
        }
        
        public void start()
        {
            Message msg;
            while (s.isConnected())
            {
                try 
                {
                    msg = receive();
                    System.out.println("SERVER> received: " + msg.getMessage());
                    
                    if (msg.getMessage().contains("%off"))
                        stop();
                    if (msg.getMessage().contains("%join"))
                        sendAll(new Message(msg.getName(), "joined the chat"));
                    /*if msg.getMessage().contains("%direct")
                            */
                    else
                        sendAll(msg);
                }
                catch (Exception ex) 
                {
                    stop();
                }
            } 
        }
        
        public boolean connect()
        {
            try
            {
                inStream = s.getInputStream();
                outStream = s.getOutputStream();
                Oin = new ObjectInputStream(inStream);
                Oout = new ObjectOutputStream(outStream);
                outs.add(Oout);
                System.out.println("SERVER> IO ready");
                return true;
            }
            catch(IOException e)
            {
                System.out.println("SERVER> error in connect():\n" + e.toString());
                return false;
            }
        }
        
        public Message receive() throws Exception
        {
            Message msg;
            msg = (Message)Oin.readObject();
            return msg;            
        }
        
        public boolean stop()
        {
            try 
            {
                Oout.flush();
                Oout.close();
                Oin.close();
                s.close();
                return true;
            } 
            catch (IOException ex) 
            {
                System.out.println("SERVER> error in stop():\n" + ex.toString());
                return false;
            }
        }
    }
}