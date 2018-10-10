/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console_chat.gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author varg
 */
public class ClientFrame extends JFrame {
    
    private String name = "Local Chat";
    JPanel panel;
            
    public ClientFrame () {
         panel = new ClientPanel();
        
        setTitle(name);
        setContentPane(panel);
        setUpMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setResizable(false);
    }
    
    public void setUpMenu() {
        JMenuBar menu = new JMenuBar();
        
        JMenu menuButton = new JMenu("File");
        
        JMenuItem findNew = new JMenuItem("Find someone");
        findNew.addActionListener(event -> System.out.println("Without this option..."));
        menuButton.add(findNew);
        
        menu.add(menuButton);
        
        setJMenuBar(menu);
    }
    
}
