package org.example;

import java.awt.*;
import java.awt.image.ImageProducer;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class formExe extends JPanel implements ActionListener {

    static private final String newline = "\n";
    JButton openButton;
    JTextArea log;
    JFileChooser fc;

    public formExe() {
        super(new BorderLayout());

        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();


        Icon icon = new ImageIcon("Open16.gif");
        openButton = new JButton("Open a file...",icon);
        openButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }





    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fc.showOpenDialog(formExe.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            log.append("Opening: " + file.getName() + "." + newline);
        } else {
            log.append("Open command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = formExe.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MicrosoftDocumentReader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        //Add content to the window.
        frame.add(new formExe());

        //Display the window.
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });

    }



}
