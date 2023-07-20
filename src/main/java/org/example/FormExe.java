package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FormExe extends JPanel implements ActionListener {

    static private final String newline = "\n";
    JButton openButton;
    JButton createButton;
    JButton saveButton;
    JTextArea log;
    JFileChooser fc;
    Main main = new Main();


    public FormExe() throws IOException {
        super(new BorderLayout());

        log = new JTextArea(5, 20);
        log.setMargin(new Insets(10, 5, 5, 5));
        log.setEditable(true);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        fc.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads"));

        Icon iconOpen = new ImageIcon("Open16.gif");
        openButton = new JButton("Open a file...", iconOpen);
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Insets buttonMarigin = new Insets(5, 0, 5, 0);
        openButton.setMargin(buttonMarigin);
        openButton.addActionListener(this);

        Icon iconCreate = new ImageIcon("phone.gif");
        createButton = new JButton("Create Phone List...", iconCreate);
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setMargin(buttonMarigin);
        createButton.addActionListener(this);

        Icon iconSave = new ImageIcon("save.gif");
        saveButton = new JButton("Save...", iconSave);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setMargin(buttonMarigin);
        saveButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // BoxLayout of buttonPanel
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonPanel.add(openButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonPanel.add(createButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 10)));

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            try {
                main.writeListIntoFile(main.nowList, "previous.txt");
                if (main.nowList.isEmpty()) {
                    log.append("List is empty!" + newline);
                } else {
                    log.append("Saved to file previous.txt" + newline);

                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == createButton) {
            try {
                main.runApp();
                for (String s : main.uniquePhoneList) {
                    log.append(s + newline);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FormExe.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getPath();
                try {
                    main.initiateFile(path);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                log.append("Opening: " + file.getName() + "." + newline);
                log.append("Path to file : " + path + newline);

            } else {
                log.append("Open command cancelled by user." + newline);
            }
        }
        log.setCaretPosition(log.getDocument().getLength());

    }


    private static void createAndShowGUI() throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("MicrosoftDocumentReader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        //Add content to the window.
        frame.add(new FormExe());

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


}
