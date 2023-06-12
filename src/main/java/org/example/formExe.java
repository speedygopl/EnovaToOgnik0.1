package org.example;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class formExe extends JFrame implements ActionListener {

    static JLabel l;

    formExe() {
    }

    ;

    public static void main(String[] args) {
        JFrame f = new JFrame("Microsoft Documents Reader");
        f.setLayout(null);
        f.setSize(400, 400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Otwórz");
        button.setLayout(null);
        formExe fe = new formExe();
        button.addActionListener(fe);
        JPanel p = new JPanel();
        p.setLayout(null);
        button.setLocation(20,20);
        p.add(button);
        button.setVisible(true);

        l = new JLabel("wybierz plik xlsx");
        l.setHorizontalTextPosition(SwingConstants.LEFT);

        p.add(l);
        f.add(p);
        f.show();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        if (com.equals("Otwórz")) {
            JFileChooser j = new JFileChooser();
            j.showSaveDialog(null);
        }

    }
}
