package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class GridConfigPanel extends JPanel {

    private final ImageIcon finalIcon;
    private final ImageIcon finalIcon1;
    private JToggleButton gridToggle;
    private JButton magnetismButton;
    private JTextField gridSizeField;
    private DrawingPanel drawingPanel;
    private JComboBox<String> unitComboBox;

    public GridConfigPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Grid"));

        // Initialisation des composants
        //toggle
        gridToggle = new JToggleButton();
        gridToggle.setText("OFF");
        gridToggle.setFont(new Font("Arial", Font.BOLD, 12));
        gridToggle.setFocusPainted(false);
        gridToggle.setBackground(Color.LIGHT_GRAY);

        //Button for magnetic grid
        magnetismButton = new JButton();
        gridSizeField = new JTextField(10);
        unitComboBox = new JComboBox<>(new String[]{"mm", "cm", "m", "po", "pi"});
        ImageIcon icon = new ImageIcon();
        ImageIcon icon2 = new ImageIcon();
        try{
            URL activeMagnet = new URL("https://cdn-icons-png.flaticon.com/512/3989/3989601.png");
            URL inactiveMagnet = new URL("https://static.vecteezy.com/system/resources/thumbnails/014/494/432/small_2x/horseshoe-magnet-icon-with-lightning-shaped-attraction-png.png");

            icon = new ImageIcon(activeMagnet);
            icon2 = new ImageIcon(inactiveMagnet);

            // Redimensionner si nécessaire
            Image resizedImage = icon.getImage().getScaledInstance(35, 25, Image.SCALE_SMOOTH);
            Image resizedImage2 = icon2.getImage().getScaledInstance(35, 25, Image.SCALE_SMOOTH);
            icon = new ImageIcon(resizedImage);
            icon2 = new ImageIcon(resizedImage2);
            magnetismButton.setIcon(drawingPanel.isMagnetic() ? icon : icon2);  magnetismButton.setIcon(drawingPanel.isMagnetic() ? icon : icon2);
            if (drawingPanel.isGridEnabled()) {
                gridToggle.setText("ON");
                gridToggle.setBackground(Color.BLUE);
            } else {
                gridToggle.setText("OFF");
                gridToggle.setBackground(Color.RED);
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            magnetismButton.setText("Erreur : image");
            magnetismButton.setForeground(Color.RED);
        }

        JLabel gridSizeLabel = new JLabel("Taille des carreaux :");

        // Configuration du layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(gridToggle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(gridSizeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(gridSizeField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(unitComboBox, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(magnetismButton, gbc);

        // Ajout des actions
        gridToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean activated = gridToggle.isSelected();
                drawingPanel.setGridEnabled(activated);

                if (activated) {
                    gridToggle.setText("ON");
                    gridToggle.setBackground(Color.BLUE);
                } else {
                    gridToggle.setText("OFF");
                    gridToggle.setBackground(Color.RED);
                }
            }
        });

        finalIcon = icon2;
        finalIcon1 = icon;
        magnetismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isMagnetic = drawingPanel.isMagnetic();
                drawingPanel.setMagnetic(!isMagnetic); // Inverser l'état du magnétisme
                System.out.println(isMagnetic ? "Magnétisme désactivé" : "Magnétisme activé");

                if (!isMagnetic) {
                    magnetismButton.setIcon(finalIcon1);
                } else {
                    magnetismButton.setIcon(finalIcon);
                }
            }
        });

        gridSizeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String unitUsed = (String) unitComboBox.getSelectedItem();
                String sizeText = gridSizeField.getText();

                try {
                    double size = Double.parseDouble(sizeText);

                    if (size > 0 && unitUsed != null) {
                        size = Utils.convertValueToPixel(size, unitUsed);
                        drawingPanel.setGridSize(size);
                    } else {
                        JOptionPane.showMessageDialog(GridConfigPanel.this, "Veuillez entrer un nombre supérieur à 0.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GridConfigPanel.this, "Veuillez entrer un nombre valide.");
                }

            }
        });
    }

    public void update(int gridSquare, boolean isGridEnabled, boolean isMagnetic) {
        if (isGridEnabled) {
            gridToggle.setText("ON");
            gridToggle.setBackground(Color.BLUE);
        } else {
            gridToggle.setText("OFF");
            gridToggle.setBackground(Color.RED);
        }

        magnetismButton.setIcon(isMagnetic ? finalIcon1 : finalIcon);
    }
}