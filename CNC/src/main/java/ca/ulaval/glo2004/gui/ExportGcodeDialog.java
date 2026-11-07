package ca.ulaval.glo2004.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExportGcodeDialog extends JDialog {
    private JComboBox<String> unitComboBox;
    private JComboBox<String> fileTypeComboBox;
    private JTextField feedRateField;
    private JTextField pauseField;
    private boolean confirmed = false;

    public ExportGcodeDialog(Frame parent) {
        super(parent, "Export G-Code Settings", true);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Units (in/mm):"));
        unitComboBox = new JComboBox<>(new String[]{"mm", "in"});
        add(unitComboBox);

        add(new JLabel("Feed Rate (mm/min):"));
        feedRateField = new JTextField("500");
        add(feedRateField);

        add(new JLabel("Pause Time:"));
        pauseField = new JTextField("4");
        add(pauseField);


        add(new JLabel("File Type:"));
        fileTypeComboBox = new JComboBox<>(new String[]{".gcode", ".nc"});
        add(fileTypeComboBox);

        // Buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose(); // Close the dialog
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getFeedRate() {
        return feedRateField.getText();
    }

    public String getUnits() {
        return (String) unitComboBox.getSelectedItem();
    }

    public String getPause(){
        return pauseField.getText();
    }

    public String getFileType() {
        return (String) fileTypeComboBox.getSelectedItem();
    }


    public boolean isConfirmed() {
        return confirmed;
    }
}
