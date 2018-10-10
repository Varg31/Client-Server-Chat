/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console_chat.gui;

import console_chat.consoleClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author varg
 */
public class ClientPanel extends JPanel {
    
    public static JTextArea write;
    public static JTextArea messageArea;
  
    public static final int WIDHT = 300;
    public static final int HEIGTH = 500;
    
    private Graphics2D graphics;
    private BufferedImage image;
      
    private consoleClient client;
       
    public ClientPanel() {
        
        client = new consoleClient(StartFrame.name.getText(), StartFrame.ip.getText());
        client.connect();
        
        setPreferredSize(new Dimension(WIDHT, HEIGTH));
        setFocusable(true);
        requestFocus();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        write = new JTextArea();
        messageArea = new JTextArea();
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        createMessagePanel();
        createWritePanel();
    }
    
    private void createMessagePanel() {
       
        JPanel mes = new JPanel();
        mes.setPreferredSize(new Dimension(ClientPanel.WIDTH, ClientPanel.HEIGTH - 100));
        
        messageArea.setFont(new Font("verdana", Font.PLAIN, 18));
        messageArea.setPreferredSize(new Dimension(270, 430));
        messageArea.setEditable(false);
        
        JScrollPane mesArea = new JScrollPane(messageArea);
        mesArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mesArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        mes.add(mesArea);
        add(BorderLayout.CENTER, mes);
    }
    
    private void createWritePanel() {
        
        JPanel show = new JPanel();
        show.setFocusable(true);
        //show.addKeyListener(new SendListener());
        show.requestFocus();
        show.setBackground(Color.LIGHT_GRAY);
        
        //pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        write.setFont(new Font("Consolas", Font.PLAIN, 15));
        write.setWrapStyleWord(true);
        write.setLineWrap(true);
        write.addKeyListener(new SendListener());
                
        JScrollPane textArea = new JScrollPane(write);
        textArea.setPreferredSize(new Dimension(ClientPanel.WIDHT - 80 , 30));
        textArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JButton sendButton = new JButton(new ImageIcon("res/deadbeef_logo.png"));
        sendButton.addActionListener(event ->  { 
            client.send(write.getText());
            write.setText("");
                });
        
        show.add(textArea);
        show.add(sendButton);
        
        add(BorderLayout.SOUTH, show);
    
}
        
    private class SendListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) { // пресс зе баттон
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_ENTER) {
                 client.send(write.getText());
                 write.setText("");
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_ENTER) {
                System.out.println("dyakuju");
            }    
        }
    }
}
 
