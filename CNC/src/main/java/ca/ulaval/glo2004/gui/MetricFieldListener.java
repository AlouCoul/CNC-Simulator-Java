package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.Utils.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MetricFieldListener implements DocumentListener {
    private final JTextField textField;

    public MetricFieldListener(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validateInput();
    }

    private void validateInput() {
        try {
            String text = textField.getText();
            double value = Utils.parseMetricValue(text);
            textField.setBackground(Color.WHITE); // Entrée valide
            System.out.println("Valeur métrique décimale : " + value);
        } catch (IllegalArgumentException ex) {
            textField.setBackground(Color.PINK); // Entrée invalide
            System.out.println("Erreur : " + ex.getMessage());
        }
    }
}
