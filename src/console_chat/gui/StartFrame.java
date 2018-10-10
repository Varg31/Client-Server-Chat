/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console_chat.gui;

import console_chat.consoleClient;
import console_chat.consoleServer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
/**
 *
 * @author varg
 */
public class StartFrame extends JFrame{
    
    private JRadioButton client;
    private JRadioButton server;
    
    public static JTextField ip = new JTextField(15);
    public static JTextField name = new JTextField(15);
    
    private consoleClient newCLient;
    private ClientFrame clientWindow;
    
    public String getIPAdr() {
        return ip.getText();
    }
    
    public String getClientName() {
        return name.getText();
    }
    
    public StartFrame () {
        setTitle("Start");
        setBounds(100, 100, 250, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        buildPanel(this);
        setResizable(false);
        setVisible(true);
    }
    
    public void buildPanel(JFrame c) {
        JPanel backPan = new JPanel();
        backPan.setLayout(new GridLayout(4, 2));
        c.setContentPane(backPan);
        
        ButtonGroup check = new ButtonGroup();
        client = new JRadioButton("Client", true);
        check.add(client);
        server = new JRadioButton("Server", false);
        check.add(server);
        
        try {
            
        ip.setText(consoleServer.getIP());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can`t get IP. Try again");
                this.setFocusable(true);
        }
        
        JLabel ipNumber = new JLabel("IP  ");
        JLabel nameString = new JLabel("Name");
        
        name.setFocusable(true);
        name.requestFocus();
        name.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (client.isSelected() && !(name.getText().equals("")))) {
                    clientWindow = new ClientFrame();
                } else if (server.isSelected()) { 
                    Runnable r = (() -> new consoleServer().addClient() ); 
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        });
        
        JButton start = new JButton("Start");
        start.addActionListener(event -> { if (client.isSelected() && !(name.getText().equals(""))) {
          
            //new consoleClient(name.getText(), ip.getText()).connect();
            clientWindow = new ClientFrame();
            
        } else if (server.isSelected()) { 
            Runnable r = (() -> new consoleServer().addClient() ); 
            Thread t = new Thread(r);
            t.start();
        }
        });
        
        JButton quit = new JButton("Quit?");
        quit.addActionListener(event -> System.exit(0));
        
        backPan.add(client);
        backPan.add(server);
        backPan.add(ipNumber);
        backPan.add(ip);
        backPan.add(nameString);
        backPan.add(name);
        backPan.add(start);
        backPan.add(quit);
    }    
}
