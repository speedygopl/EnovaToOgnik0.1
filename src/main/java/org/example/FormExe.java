package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class FormExe extends JPanel implements ActionListener {

    static private final String newline = "\n";
    JButton openButtonEnova;
    JButton openButtonPlan;
    JButton convertButton;
    JTextArea log;
    JFileChooser fc;
    JCheckBox checkboxByName = new JCheckBox("Sortuj po nazwisku");
    JCheckBox checkboxByNumber = new JCheckBox("Sortuj po numerze");

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    JFormattedTextField fiscalMonthField = new JFormattedTextField(numberFormat);
    JLabel fiscalMonthLabel = new JLabel("Miesiąc obrachunkowy");
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
        openButtonEnova = new JButton("Otwórz plik Enova", iconOpen);
        openButtonEnova.setPreferredSize(new Dimension(200, 50));
        openButtonEnova.setMaximumSize(new Dimension(200, 50));
        openButtonEnova.setMinimumSize(new Dimension(200, 50));
        openButtonEnova.setAlignmentX(Component.CENTER_ALIGNMENT);
        Insets buttonMarigin = new Insets(5, 0, 5, 0);
        openButtonEnova.setMargin(buttonMarigin);
        openButtonEnova.addActionListener(this);

        openButtonPlan = new JButton("Otwórz plik Plan Kont", iconOpen);
        openButtonPlan.setPreferredSize(new Dimension(200, 50));
        openButtonPlan.setMaximumSize(new Dimension(200, 50));
        openButtonPlan.setMinimumSize(new Dimension(200, 50));
        openButtonPlan.setAlignmentX(Component.CENTER_ALIGNMENT);
        openButtonPlan.setMargin(buttonMarigin);
        openButtonPlan.addActionListener(this);

        Icon iconConvert = new ImageIcon("save.gif");
        convertButton = new JButton("Konwertuj...", iconConvert);
        convertButton.setPreferredSize(new Dimension(200, 50));
        convertButton.setMaximumSize(new Dimension(200, 50));
        convertButton.setMinimumSize(new Dimension(200, 50));
        convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertButton.setMargin(buttonMarigin);
        convertButton.addActionListener(this);

        //CHECKBOXES SETTINGS
        checkboxByNumber.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkboxByName.setSelected(false);
                    System.out.println("CheckboxByNumber is selected");
                }
            }
        });

        // Add item listener to handle checkbox2 events
        checkboxByName.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkboxByNumber.setSelected(false);
                    System.out.println("CheckboxByName is selected");
                }
            }
        });
        checkboxByName.setBounds(100, 100, 200, 50);
        checkboxByNumber.setBounds(100, 100, 200, 50);

        // fiscal month input field
        fiscalMonthField.setValue(0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // BoxLayout of buttonPanel
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        buttonPanel.add(openButtonEnova);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        buttonPanel.add(openButtonPlan);
        buttonPanel.add(checkboxByName);
        buttonPanel.add(checkboxByNumber);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        buttonPanel.add(fiscalMonthLabel);
        buttonPanel.add(fiscalMonthField);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        buttonPanel.add(convertButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 10)));

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == convertButton) {

        }
        if (e.getSource() == openButtonPlan) {
            int returnVal = fc.showOpenDialog(FormExe.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getPath();
                try {
                    main.initiateFilePlan(path);
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

        if (e.getSource() == openButtonEnova) {
            int returnVal = fc.showOpenDialog(FormExe.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getPath();
                try {
                    main.initiateFileEnova(path);
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

        if (e.getSource() == convertButton) {
            main.setFiscalMonth(fiscalMonthField.getValue().toString());
            try {
                main.makeListOfMapsAndCreateResultsFile(checkboxByName.isSelected() ? "byname" : "bynumber");
                log.append("Plik Ognik.xlsx utworzony !!!");
            } catch (IOException ex) {
                log.append(ex.toString());
                throw new RuntimeException(ex);
            } catch (InvalidFormatException ex) {
                log.append(ex.toString());
                throw new RuntimeException(ex);
            }
        }
        log.setCaretPosition(log.getDocument().getLength());
    }


    private static void createAndShowGUI() throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("EnovaToOgnik converter");
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
