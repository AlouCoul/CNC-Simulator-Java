/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ca.ulaval.glo2004.gui;


import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.domain.CNC;
import ca.ulaval.glo2004.domain.CncController;
import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ToolDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author zakar
 */
public class MainWindow extends javax.swing.JFrame {

    public CncController controller;
    public CutType cutTypePressed;
    public boolean enableManualSelect;
    public GridConfigPanel gridConfigPanel;
    boolean isProhibitedAreaMode = false;
    private Rectangle prohibitedArea = null;
    private final int DEFAULT_AREA_WIDTH = (int) Utils.convertPoToPixels(6);
    private final int DEFAULT_AREA_HEIGHT = (int) Utils.convertPoToPixels(6);

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        controller = new CncController();
        initComponents();
        initCutPanelComponent();
        gridConfigPanel = new GridConfigPanel(drawingPanel);
        addgridConfigPanel();
        addMetricFieldListeners();
        clearCutComboBox();
        drawingPanel.setZoomFactor(0.076);
        drawingPanel.setOffset(102, 188);
        drawingPanel.setAxis();
        toolComboBox.removeAllItems();
        fillToolComboBox();
        ToolpositionTextField.setText(String.valueOf(1));

        boolean isImperial = systemMetric.isSelected();
        String texte = isImperial ? "imperial" : "universal";
        systemMetric.setText(texte);
        systemMetricActionPerformed(new ActionEvent(systemMetric, 0, texte));
        //setTextTool();
    }

    public void setCutPoints(ArrayList<Point> points) {
        Point origin = (Point) points.get(0).getLocation();
        Point dest = (Point) points.get(1).getLocation();

        String origineText = String.format("(%.2f,%.2f)", origin.getX(), origin.getY());
        String destText = String.format("(%.2f,%.2f)", dest.getX(), dest.getY());

        originTextField.setText(origineText);
        destinationTextField.setText(destText);
    }

    public void selectCut(int selectedCut) {
        cutComboBox.setSelectedIndex(selectedCut + 1);
    }

    public void addgridConfigPanel() {
        // Définir une position personnalisée
        gridConfigPanel.setBounds(375, 650, 500, 100); // Position et taille
        gridConfigPanel.setBackground(new Color(240, 240, 240)); // Couleur de fond
        gridConfigPanel.setOpaque(true);

        // Ajouter à la fenêtre
        drawingPanel.setLayout(null); // Nécessaire pour positionner manuellement
        drawingPanel.add(gridConfigPanel);
    }

    private Point parsePointFromString(String pointText) {
        pointText = pointText.replaceAll("[()]", ""); // Retire les parenthèses
        String[] coords = pointText.split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        return new Point((int)x, (int)y);
    }

    private void clearCutComboBox(){
        cutComboBox.removeAllItems();
        cutComboBox.addItem("Aucune selection");
        referenceTextField.setText("");
        distanceTextField.setText("");
        editLTextField.setText("");
        editWTextField.setText("");
    }

    private void addMetricFieldListeners() {
        widthTextField.getDocument().addDocumentListener(new MetricFieldListener(widthTextField));
        depthTextField.getDocument().addDocumentListener(new MetricFieldListener(depthTextField));
        lengthTextField.getDocument().addDocumentListener(new MetricFieldListener(lengthTextField));
        cutDepthTextField.getDocument().addDocumentListener(new MetricFieldListener(cutDepthTextField));
        widthCutTextField.getDocument().addDocumentListener(new MetricFieldListener(widthCutTextField));
        lengthCutTextField.getDocument().addDocumentListener(new MetricFieldListener(lengthCutTextField));

        distanceTextField.getDocument().addDocumentListener(new MetricFieldListener(distanceTextField));
        editWTextField.getDocument().addDocumentListener(new MetricFieldListener(editWTextField));
        editLTextField.getDocument().addDocumentListener(new MetricFieldListener(editLTextField));
        ToolWidthTextField.getDocument().addDocumentListener(new MetricFieldListener(ToolWidthTextField));
        borderCutDepthTextField.getDocument().addDocumentListener(new MetricFieldListener(borderCutDepthTextField));

    }

    private void validateAllMetricFields() {
        try {
            double width = Utils.parseMetricValue(widthTextField.getText());
            double length = Utils.parseMetricValue(lengthTextField.getText());
            double depth = Utils.parseMetricValue(depthTextField.getText());
            System.out.println("Width: " + width + ", Length: " + length + ", Depth: " + depth);


        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Erreur dans les champs métriques : " + ex.getMessage(),
                    "Erreur de validation", JOptionPane.ERROR_MESSAGE);
        }
    }


    public File selectFileFromFileManager(String text){
        File choosedFile = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(text);
        boolean act = chooser.showOpenDialog(this) !=0;
        if (act){
            System.out.println("Choosed file: " + chooser.getSelectedFile());
            choosedFile = chooser.getSelectedFile();
        }
        return choosedFile;
    }

    public void editDraggedCut(Point newPoint){
        var cutIndex = cutComboBox.getSelectedIndex() - 1;
        if (cutIndex >= 0){
            controller.editCut(cutIndex, newPoint);
        }
    }

    private void initCutPanelComponent(){
        RegularPanel.setVisible(true);
        BorderCutPanel.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        setupPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        widthTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lengthTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        depthTextField = new javax.swing.JTextField();
        airSupportRadio = new javax.swing.JRadioButton();
        clampSupportRadio = new javax.swing.JRadioButton();
        prohibutedAreaButton = new javax.swing.JButton();
        resizePanelButton = new javax.swing.JButton();
        createPanelButton = new javax.swing.JButton();
        widthPanelComboBox = new javax.swing.JComboBox<>();
        lengthPanelComboBox = new javax.swing.JComboBox<>();
        depthPanelComboBox = new javax.swing.JComboBox<>();
        systemMetric = new javax.swing.JToggleButton();
        cutPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        toolComboBox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        ToolNameTextField = new javax.swing.JTextField();
        addToolButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        ToolWidthTextField = new javax.swing.JTextField();
        deleteToolButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        ToolpositionTextField = new javax.swing.JTextField();
        editToolButton = new javax.swing.JButton();
        toolLengthComboBox = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        SwitchablePanel = new javax.swing.JPanel();
        RegularPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cutDepthTextField = new javax.swing.JTextField();
        depthMarginTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        originTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        destinationTextField = new javax.swing.JTextField();
        marginComboBox = new javax.swing.JComboBox<>();
        depthComboBox = new javax.swing.JComboBox<>();
        BorderCutPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lengthCutTextField = new javax.swing.JTextField();
        widthCutTextField = new javax.swing.JTextField();
        previewButton = new javax.swing.JButton();
        borderCutWidthComboBox = new javax.swing.JComboBox<>();
        borderCutLengthComboBox = new javax.swing.JComboBox<>();
        borderCutDepthTextField = new javax.swing.JTextField();
        borderCutDepthComboBox = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        drawCutButton = new javax.swing.JButton();
        coupeOptionsComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cutComboBox = new javax.swing.JComboBox<>();
        editCutButton = new javax.swing.JButton();
        deleteCutButton = new javax.swing.JButton();
        enableSelection = new javax.swing.JCheckBox();
        referenceTextField = new javax.swing.JTextField();
        distanceTextField = new javax.swing.JTextField();
        resetCutParams = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        editLTextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        editWTextField = new javax.swing.JTextField();
        larguerMetricComboBox = new javax.swing.JComboBox<>();
        longueurMetricComboBox = new javax.swing.JComboBox<>();
        toolOfCutComboBox = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        distanceFromRefMetricComboBox = new javax.swing.JComboBox<>();
        editReferenceToggle = new javax.swing.JToggleButton();
        rightPanel = new javax.swing.JPanel();
        drawingPanel = new ca.ulaval.glo2004.gui.DrawingPanel(this);
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveButton = new javax.swing.JMenuItem();
        saveAsButton = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        exportGcodeButton = new javax.swing.JMenuItem();
        exportPanButton = new javax.swing.JMenuItem();
        exportCncButton = new javax.swing.JMenuItem();
        importFileButton = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        undoButton = new javax.swing.JMenuItem();
        redoButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel.setPreferredSize(new java.awt.Dimension(1457, 917));

        splitPane.setDividerLocation(300);
        splitPane.setPreferredSize(new java.awt.Dimension(1457, 917));

        leftPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jLabel4.setText("Configuration du panneau");

        jLabel1.setText("Largeur");

        jLabel2.setText("Longueur");

        lengthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lengthTextFieldActionPerformed(evt);
            }
        });

        editToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editToolButtonActionPerformed(evt);
            }
        });

        addToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToolButtonActionPerformed(evt);
            }
        });


        jLabel3.setText("Depth");

        airSupportRadio.setText("Support à air");
        airSupportRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        airSupportRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                airSupportRadioActionPerformed(evt);
            }
        });

        clampSupportRadio.setText("Serre Joint");
        clampSupportRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        prohibutedAreaButton.setText("Définir une zone interdite");
        prohibutedAreaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prohibutedAreaButtonActionPerformed(evt);
            }
        });

        resizePanelButton.setText("Rédimensionner le panneau");
        resizePanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resizePanelButtonActionPerformed(evt);
            }
        });

        createPanelButton.setText("Créer un nouveau panneau");
        createPanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPanelButtonActionPerformed(evt);
            }
        });

        widthPanelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        lengthPanelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        depthPanelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        systemMetric.setText("imperial");
        systemMetric.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                systemMetricStateChanged(evt);
            }
        });
        systemMetric.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemMetricActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout setupPanelLayout = new javax.swing.GroupLayout(setupPanel);
        setupPanel.setLayout(setupPanelLayout);
        setupPanelLayout.setHorizontalGroup(
            setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prohibutedAreaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(resizePanelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(createPanelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(systemMetric)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(setupPanelLayout.createSequentialGroup()
                            .addComponent(airSupportRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(clampSupportRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(setupPanelLayout.createSequentialGroup()
                            .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(widthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(setupPanelLayout.createSequentialGroup()
                            .addComponent(lengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lengthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(setupPanelLayout.createSequentialGroup()
                            .addComponent(depthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(depthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 51, Short.MAX_VALUE))
        );
        setupPanelLayout.setVerticalGroup(
            setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(systemMetric)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(widthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lengthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depthPanelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(airSupportRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clampSupportRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(prohibutedAreaButton)
                .addGap(1, 1, 1)
                .addComponent(resizePanelButton)
                .addGap(1, 1, 1)
                .addComponent(createPanelButton))
        );

        leftPanel.add(setupPanel);

        cutPanel.setPreferredSize(new java.awt.Dimension(337, 300));

        toolComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        toolComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolComboBoxActionPerformed(evt);
            }
        });

        jLabel15.setText("nom");

        addToolButton.setText("Ajouter");

        jLabel16.setText("largeur");

        deleteToolButton.setText("Supprimer");
        deleteToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteToolButtonActionPerformed(evt);
            }
        });

        jLabel17.setText("Position");

        editToolButton.setText("Modifier");

        toolLengthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(addToolButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editToolButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteToolButton))
                            .addComponent(jLabel15)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ToolpositionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addComponent(ToolWidthTextField)
                                    .addComponent(ToolNameTextField))
                                .addGap(6, 6, 6)
                                .addComponent(toolLengthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(toolComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(toolComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(ToolNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(ToolpositionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ToolWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(toolLengthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addToolButton)
                    .addComponent(editToolButton)
                    .addComponent(deleteToolButton))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Outils", jPanel2);

        SwitchablePanel.setLayout(new java.awt.CardLayout());

        jLabel6.setText("profondeur");

        jLabel7.setText("marge");

        depthMarginTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depthMarginTextFieldActionPerformed(evt);
            }
        });

        jLabel8.setText("Origine");

        jLabel9.setText("Destination");

        marginComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));
        marginComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marginComboBoxActionPerformed(evt);
            }
        });

        depthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        javax.swing.GroupLayout RegularPanelLayout = new javax.swing.GroupLayout(RegularPanel);
        RegularPanel.setLayout(RegularPanelLayout);
        RegularPanelLayout.setHorizontalGroup(
            RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegularPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(originTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(depthMarginTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cutDepthTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(destinationTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(marginComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        RegularPanelLayout.setVerticalGroup(
            RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegularPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cutDepthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(depthMarginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(marginComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(originTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegularPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        SwitchablePanel.add(RegularPanel, "card2");

        jLabel21.setText("Width");

        jLabel22.setText("Length");

        widthCutTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                widthCutTextFieldActionPerformed(evt);
            }
        });

        previewButton.setText("Preview");
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });

        borderCutWidthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        borderCutLengthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));
        borderCutLengthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borderCutLengthComboBoxActionPerformed(evt);
            }
        });

        borderCutDepthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));
        borderCutDepthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borderCutDepthComboBoxActionPerformed(evt);
            }
        });

        jLabel23.setText("Depth");

        javax.swing.GroupLayout BorderCutPanelLayout = new javax.swing.GroupLayout(BorderCutPanel);
        BorderCutPanel.setLayout(BorderCutPanelLayout);
        BorderCutPanelLayout.setHorizontalGroup(
            BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorderCutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BorderCutPanelLayout.createSequentialGroup()
                        .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BorderCutPanelLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(widthCutTextField))
                            .addGroup(BorderCutPanelLayout.createSequentialGroup()
                                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(BorderCutPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(8, 8, 8))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BorderCutPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(borderCutDepthTextField)
                                    .addComponent(lengthCutTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(2, 2, 2)
                        .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(borderCutLengthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(borderCutWidthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(borderCutDepthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BorderCutPanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(previewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        BorderCutPanelLayout.setVerticalGroup(
            BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorderCutPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthCutTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(borderCutWidthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lengthCutTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borderCutLengthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BorderCutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borderCutDepthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borderCutDepthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addComponent(previewButton)
                .addGap(60, 60, 60))
        );

        SwitchablePanel.add(BorderCutPanel, "card3");

        drawCutButton.setText("Dessiner la coupe");
        drawCutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawCutButtonActionPerformed(evt);
            }
        });

        coupeOptionsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {" ", "coupe horizontale", "coupe verticale", "coupe en L", "coupe rectangulaire", "coupe en bordure" }));
        coupeOptionsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coupeOptionsComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setText("Coupes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(drawCutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel5))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(coupeOptionsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(122, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(SwitchablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(5, 5, 5)
                .addComponent(coupeOptionsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157)
                .addComponent(drawCutButton)
                .addGap(186, 186, 186))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(SwitchablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(241, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Coupes", jPanel1);

        jLabel10.setText("Gestion des coupes");

        cutComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cutComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cutComboBoxItemStateChanged(evt);
            }
        });
        cutComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutComboBoxActionPerformed(evt);
            }
        });

        editCutButton.setText("Modifier");
        editCutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCutButtonActionPerformed(evt);
            }
        });

        deleteCutButton.setText("Supprimer");
        deleteCutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCutButtonActionPerformed(evt);
            }
        });

        enableSelection.setText("Activer la sélection manuelle");
        enableSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableSelectionActionPerformed(evt);
            }
        });

        resetCutParams.setText("Réinitialiser");
        resetCutParams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetCutParamsActionPerformed(evt);
            }
        });

        jLabel13.setText("Reference");

        jLabel14.setText("Distance");

        jLabel18.setText("Longueur");

        jLabel19.setText("Largeur");

        editWTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editWTextFieldActionPerformed(evt);
            }
        });

        larguerMetricComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        longueurMetricComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));
        longueurMetricComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                longueurMetricComboBoxActionPerformed(evt);
            }
        });

        toolOfCutComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        toolOfCutComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolOfCutComboBoxActionPerformed(evt);
            }
        });

        jLabel20.setText("Outil");

        distanceFromRefMetricComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mm", "cm", "m", "po", "pi" }));

        editReferenceToggle.setText("edit");
        editReferenceToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editReferenceToggleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(editCutButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetCutParams)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteCutButton))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(referenceTextField)
                            .addComponent(distanceTextField)
                            .addComponent(toolOfCutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(distanceFromRefMetricComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editReferenceToggle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(enableSelection)
                    .addComponent(jLabel10)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(editLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(longueurMetricComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(editWTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(larguerMetricComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(cutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(enableSelection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toolOfCutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(referenceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editReferenceToggle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(distanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(distanceFromRefMetricComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel19))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(longueurMetricComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editWTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(larguerMetricComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editCutButton)
                    .addComponent(deleteCutButton)
                    .addComponent(resetCutParams))
                .addGap(183, 183, 183))
        );

        jTabbedPane1.addTab("Gestion", jPanel3);

        javax.swing.GroupLayout cutPanelLayout = new javax.swing.GroupLayout(cutPanel);
        cutPanel.setLayout(cutPanelLayout);
        cutPanelLayout.setHorizontalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cutPanelLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        cutPanelLayout.setVerticalGroup(
            cutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        leftPanel.add(cutPanel);

        splitPane.setLeftComponent(leftPanel);

        javax.swing.GroupLayout drawingPanelLayout = new javax.swing.GroupLayout(drawingPanel);
        drawingPanel.setLayout(drawingPanelLayout);
        drawingPanelLayout.setHorizontalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11520, Short.MAX_VALUE)
        );
        drawingPanelLayout.setVerticalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 905, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(drawingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        splitPane.setRightComponent(rightPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1451, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileMenu.setText("File");

        saveButton.setText("Save");
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveButtonMousePressed(evt);
            }
        });
        fileMenu.add(saveButton);

        saveAsButton.setText("Save as");
        saveAsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveAsButtonMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveAsButtonMouseClicked(evt);
            }
        });
        fileMenu.add(saveAsButton);

        jMenu1.setText("Export");

        exportGcodeButton.setText("GCODE");
        exportGcodeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exportGcodeButtonMousePressed(evt);
            }
        });
        exportGcodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportGcodeButtonActionPerformed(evt);
            }
        });
        jMenu1.add(exportGcodeButton);

        exportPanButton.setText("PAN");
        exportPanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPanButtonActionPerformed(evt);
            }
        });
        jMenu1.add(exportPanButton);

        exportCncButton.setText("CNC");
        jMenu1.add(exportCncButton);

        fileMenu.add(jMenu1);

        importFileButton.setText("Import");
        importFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                importFileButtonMousePressed(evt);
            }
        });
        fileMenu.add(importFileButton);

        jMenuBar1.add(fileMenu);

        jMenu2.setText("Edit");

        undoButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        undoButton.setText("undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });
        jMenu2.add(undoButton);

        redoButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        redoButton.setText("redo");
        redoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoButtonActionPerformed(evt);
            }
        });
        jMenu2.add(redoButton);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshDrawingPanel() {
        drawingPanel.intersections.clear();
        fillComboBox();
        fillToolComboBox();
        drawingPanel.setAxis();
        drawingPanel.repaint();
    }

    private void lengthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lengthTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lengthTextFieldActionPerformed

    private void airSupportRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_airSupportRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_airSupportRadioActionPerformed

    private void prohibutedAreaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        isProhibitedAreaMode = true;
    }

    private void resizeNewPanel() {
        try {
            String depthMetric = depthPanelComboBox.getSelectedItem().toString();
            String widthMetric = widthPanelComboBox.getSelectedItem().toString();
            String lengthMetric = lengthPanelComboBox.getSelectedItem().toString();

            double width = Utils.parseMetricValue(widthTextField.getText());
            double length = Utils.parseMetricValue(lengthTextField.getText());
            double depthTempo = Utils.parseMetricValue(depthTextField.getText());

            int widthPixels = (int) Utils.convertValueToPixel(width, widthMetric);
            int lengthPixels = (int) Utils.convertValueToPixel(length, lengthMetric);
            double depthPixel = Utils.convertValueToPixel(depthTempo, depthMetric);

            drawingPanel.clearPoints();
            drawingPanel.lines.clear();
            drawingPanel.intersections.clear();

            Dimension dimension = new Dimension(
                    (int) Utils.convertMetersToPixels(widthPixels),
                    (int) Utils.convertMetersToPixels(lengthPixels)
            );

            SupportType supportType = airSupportRadio.isSelected() ? SupportType.AIR : SupportType.CLAMP;
            controller.addPanel(supportType, dimension, depthPixel);

            drawingPanel.setAxis();
            drawingPanel.repaint();

            JOptionPane.showMessageDialog(this, "Le panneau a été redimensionné avec succès.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des dimensions valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void createNewPanel() {
        try {
            drawingPanel.getPoints().clear();
            drawingPanel.lines.clear();
            drawingPanel.intersections.clear();

            String depthMetric = depthPanelComboBox.getSelectedItem().toString();
            String widthMetric = widthPanelComboBox.getSelectedItem().toString();
            String lengthMetric = lengthPanelComboBox.getSelectedItem().toString();


            double width = Utils.parseMetricValue(widthTextField.getText());
            double length = Utils.parseMetricValue(lengthTextField.getText());
            double depthTempo = Utils.parseMetricValue(depthTextField.getText());

            int widthPixels = (int) Utils.convertValueToPixel(width, widthMetric);
            int lengthPixels = (int) Utils.convertValueToPixel(length, lengthMetric);
            double depthPixel = Utils.convertValueToPixel(depthTempo, depthMetric);


            Dimension dimension = new Dimension(widthPixels, lengthPixels);
            SupportType supportType = airSupportRadio.isSelected() ? SupportType.AIR : SupportType.CLAMP;

            controller.addPanel(supportType, dimension, depthPixel);
            drawingPanel.setAxis();

            drawingPanel.setInitialDimension(dimension);
            drawingPanel.repaint();

            JOptionPane.showMessageDialog(this, "Un nouveau panneau a été créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : Les dimensions fournies sont invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void coupeOptionsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coupeOptionsComboBoxActionPerformed
        drawingPanel.getPoints().clear();
        originTextField.setText("");
        destinationTextField.setText("");
        widthTextField.setText("");
        lengthCutTextField.setText("");
        widthCutTextField.setText("");
        borderCutDepthTextField.setText("");
        cutDepthTextField.setText("0");

        int variable = (int) coupeOptionsComboBox.getSelectedIndex();

        CutType type = CutType.values()[variable];
        System.out.println(type);
        this.cutTypePressed = type;

        if(type == CutType.BORDER){
            RegularPanel.setVisible(false);
            BorderCutPanel.setVisible(true);
        }
        else{
            initCutPanelComponent();
        }

    }//GEN-LAST:event_coupeOptionsComboBoxActionPerformed



    private void toolComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toolComboBoxActionPerformed


    private void drawCutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawCutButtonActionPerformed
        try {
            double depth =  Utils.parseMetricValue(cutDepthTextField.getText());
            String borderWidthComboBox = borderCutWidthComboBox.getSelectedItem().toString();
            String borderLengthComboBox = borderCutLengthComboBox.getSelectedItem().toString();
            String borderDepthComboBox = borderCutDepthComboBox.getSelectedItem().toString();
            String cutDepthComboBox = depthComboBox.getSelectedItem().toString();
            String cutMarginComboBox = marginComboBox.getSelectedItem().toString();

            if(cutTypePressed != null && cutTypePressed == CutType.BORDER){
                double width = Utils.parseMetricValue(widthCutTextField.getText());
                double length = Utils.parseMetricValue(lengthCutTextField.getText());
                double depthB = Utils.parseMetricValue(borderCutDepthTextField.getText());
                if (width <= 0 || length <= 0 || depthB <= 0) {
                    JOptionPane.showMessageDialog(this, "Erreur : Les dimensions fournies sont invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                width = Utils.convertValueToPixel(width, borderWidthComboBox);
                length = Utils.convertValueToPixel(length, borderLengthComboBox);
                depth = Utils.convertValueToPixel(depthB, borderDepthComboBox);
                drawingPanel.addBorderCut(width, length);
            }
            String originText = originTextField.getText();
            String destText = destinationTextField.getText();
            Point origin = parsePointFromString(originText);
            Point dest = parsePointFromString(destText);
            ToolDTO tool_cut = controller.getTools().get(toolComboBox.getSelectedIndex());
            ArrayList<Point> points = new ArrayList<Point>();
            if (cutTypePressed == CutType.RECTANGULAR){
                points.add(drawingPanel.cutReference);
            }
            points.add(origin);
            points.add(dest);

            controller.addCut((int)depth, points, cutTypePressed, tool_cut.getId(), drawingPanel.cutReference);
            drawingPanel.cutReference = null;
            System.out.println("Coupe créée avec succès : " + cutTypePressed);
            System.out.println("LE TYPE D'OUTIL EST: " + tool_cut.getName());

            coupeOptionsComboBox.setSelectedIndex(0);

            drawingPanel.repaint();
            fillComboBox();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : Problème de conversion des valeurs de profondeur ou de marge.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());

        }
    }//GEN-LAST:event_drawCutButtonActionPerformed

    private void fillComboBox() {
        clearCutComboBox();
        var cuts = controller.getCuts();
        for (int i = 0; i < cuts.size(); i++) {
            var cutPoints = cuts.get(i).getPoints();
            var pointA = cutPoints.get(0);
            var pointB = cutPoints.get(1);
            var pointAString = String.format("(%.2f,%.2f)", pointA.getX(), pointA.getY());
            var pointBString = String.format("(%.2f,%.2f)", pointB.getX(), pointB.getY());
            cutComboBox.addItem("Coupe #" + (i + 1) + " de " + pointAString + " à " + pointBString);
        }
    }

    private void fillToolComboBox() {
        toolComboBox.removeAllItems();
        toolOfCutComboBox.removeAllItems();
        toolOfCutComboBox.addItem("");
        var tools = controller.getTools();
        for(int i = 0; i < tools.size(); i++){
            var tool = tools.get(i);
            var width = tool.getWidthCut();
            var name = tool.getName();
            var widhtString = String.format(name);
            var nameString = String.format(width + " mm");
            var item = widhtString + " ayant une largeur de " + nameString;
            toolComboBox.addItem(item);
            toolOfCutComboBox.addItem(item);
        }
        //setTextTool();
    }



    private void destinationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_destinationTextFieldActionPerformed

    private void depthMarginTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depthMarginTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depthMarginTextFieldActionPerformed

    private void lengthCutTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lengthCutTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lengthCutTextFieldActionPerformed

    private String getPointAsString(Point point){
        if (point == null){
            return "";
        }

        var x =  Utils.convertValueToUnit(point.getX(), "px");
        var y =  Utils.convertValueToUnit(point.getY(), "px");
        return String.format("(%.2f,%.2f)", x, y);
    }

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed

        String borderWidthComboBox = Objects.requireNonNull(borderCutWidthComboBox.getSelectedItem()).toString();
        String borderLengthComboBox = Objects.requireNonNull(borderCutLengthComboBox.getSelectedItem()).toString();
        double width = Double.parseDouble(widthCutTextField.getText());
        double length = Double.parseDouble(lengthCutTextField.getText());

        width = Utils.convertValueToPixel(width, borderWidthComboBox);
        length = Utils.convertValueToPixel(length, borderLengthComboBox);

        drawingPanel.setBorderCut(width, length);
        drawingPanel.repaint();
    }//GEN-LAST:event_previewButtonActionPerformed

    private void marginComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marginComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_marginComboBoxActionPerformed

    private void widthCutTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_widthCutTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_widthCutTextFieldActionPerformed

    private void borderCutLengthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borderCutLengthComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_borderCutLengthComboBoxActionPerformed

    private void exportPanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPanButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportPanButtonActionPerformed

    private void exportGcodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportGcodeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportGcodeButtonActionPerformed

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_saveButtonMouseClicked

    public void saveAsButtonMouseClicked(java.awt.event.MouseEvent evt) {

    }

    public void setProhibitedArea(Point point) {
        prohibitedArea = new Rectangle(point.x, point.y, DEFAULT_AREA_WIDTH, DEFAULT_AREA_HEIGHT);
        isProhibitedAreaMode = false;
        drawingPanel.repaint();
    }

    public Rectangle getProhibitedArea() {
        return prohibitedArea;
    }

    private void saveAsButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveAsButtonMousePressed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Spécifier un endroit pour enregistrer le fichier");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFolder = fileChooser.getSelectedFile();
            String directoryPath = selectedFolder.getAbsolutePath();

            String fileName = JOptionPane.showInputDialog(this, "Entrez le nom du fichier:", "Save As", JOptionPane.PLAIN_MESSAGE);
            if (fileName != null && !fileName.trim().isEmpty()) {
                try {
                    controller.saveAs(directoryPath, fileName);
                    JOptionPane.showMessageDialog(this,
                            "Le fichier a été sauvegardé : " + directoryPath + java.io.File.separator + fileName + ".json",
                            "Save As", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(this,
                            "Erreur: " + e.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {

                JOptionPane.showMessageDialog(this,
                        "Spécifier un nom de fichier.",
                        "Erreur", JOptionPane.WARNING_MESSAGE);


            }

        }


    }//GEN-LAST:event_saveAsButtonMousePressed

    private void importFileButtonMousePressed(java.awt.event.MouseEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to import");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("FileCNC selected: " + filePath);

            controller.importFile(filePath);
            drawingPanel.repaint();
            drawingPanel.setAxis();
            fillComboBox();
            fillToolComboBox();

        } else {
            System.out.println("FileCNC selection canceled.");
        }
    }

    private void saveButtonMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if( controller.getfilePath() != null){
            controller.save();
        }
        else{
            saveAsButtonMousePressed(evt);
        }
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        controller.undo();
        refreshDrawingPanel();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void redoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoButtonActionPerformed
        controller.redo();
        refreshDrawingPanel();
    }//GEN-LAST:event_redoButtonActionPerformed

    private void toolOfCutComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolOfCutComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toolOfCutComboBoxActionPerformed

    private void longueurMetricComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_longueurMetricComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_longueurMetricComboBoxActionPerformed

    private void editWTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editWTextFieldActionPerformed

    private void resetCutParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetCutParamsActionPerformed
        cutComboBox.setSelectedIndex(0);
        toolOfCutComboBox.setSelectedIndex(0);
        enableSelection.setSelected(false);
        referenceTextField.setText("");
        distanceTextField.setText("");
    }//GEN-LAST:event_resetCutParamsActionPerformed

    private void enableSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableSelectionActionPerformed
        enableManualSelect = enableSelection.isSelected();
    }//GEN-LAST:event_enableSelectionActionPerformed

    private void deleteCutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCutButtonActionPerformed
        var cuts = controller.getCuts();
        if (cuts != null && !cuts.isEmpty()){
            var selectedCut = cutComboBox.getSelectedIndex() - 1;
            if (selectedCut >= 0){
                controller.deleteCut(selectedCut);
                referenceTextField.setText("");
                distanceTextField.setText("");
                fillComboBox();
                fillToolComboBox();
                drawingPanel.repaint();
                drawingPanel.clearPoints();
            }
        }
    }//GEN-LAST:event_deleteCutButtonActionPerformed

    private void editCutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCutButtonActionPerformed
        var cuts = controller.getCuts();
        String distanceMetric = distanceFromRefMetricComboBox.getSelectedItem().toString();
        String editWMetric = longueurMetricComboBox.getSelectedItem().toString();
        String editLMetric = larguerMetricComboBox.getSelectedItem().toString();

        double distance = Utils.parseMetricValue(distanceTextField.getText());
        double height = Utils.parseMetricValue(editLTextField.getText());
        double width = Utils.parseMetricValue(editWTextField.getText());

        if (cuts != null && !cuts.isEmpty()) {
            var selectedCut = cutComboBox.getSelectedIndex() - 1;
            if (selectedCut >= 0) {
            //                var pointAx = Utils.convertMmToPixels(Double.parseDouble(editReferenceTextField.getText()));
            //                var pointAy = Utils.convertMmToPixels(Double.parseDouble(editDistanceTextField.getText()));
            var cut = cuts.get(selectedCut);
            //                var points = drawingPanel.getNewCutCoordinates(cut, pointAx, pointAy);
            //                if (points != null) {
            var reference = referenceTextField.getText();
            int distancePixels = (int) Utils.convertValueToPixel(distance, distanceMetric);
            var selectedTool = toolOfCutComboBox.getSelectedIndex() - 1;
            Dimension dimension = null;
            if (cut.hasDimension()){
                double heightPixels = Utils.convertValueToPixel(height, editWMetric);
                double widthPixels = Utils.convertValueToPixel(width, editWMetric);
                dimension = new Dimension((int)widthPixels, (int)heightPixels);
            }
            controller.editCut(selectedCut, cut.getDepth(), cut.getReference(), distancePixels, dimension, selectedTool);
            fillComboBox();
            fillToolComboBox();
            drawingPanel.repaint();
                //drawingPanel.clearPoints();
                //}
            }
        }
    }//GEN-LAST:event_editCutButtonActionPerformed

    private void cutComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutComboBoxActionPerformed

    }//GEN-LAST:event_cutComboBoxActionPerformed

    private void cutComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cutComboBoxItemStateChanged
        // TODO add your handling code here:
        var selectedCut = cutComboBox.getSelectedIndex() - 1;
        drawingPanel.setSelectedCut(selectedCut);
        editLTextField.setText("");
        editWTextField.setText("");
        referenceTextField.setText("");
        distanceTextField.setText("");
        if (selectedCut >= 0){
            var cut = controller.getCuts().get(selectedCut);
            //            var point = cuts.getPoints().getFirst();
            //            var x = point.getX();
            //            var y = point.getY();

            var reference = cut.getReference();
            var referencePosition = getPointAsString(reference);
            referenceTextField.setText(referencePosition);
            distanceTextField.setText(Double.toString(Utils.convertValueToUnit(cut.getDistance(), distanceFromRefMetricComboBox.getSelectedItem().toString())));
            var toolPosition = cut.getTool() == null ? 0 : controller.getToolPosition(cut.getTool().getId()) + 1;
            toolOfCutComboBox.setSelectedIndex(toolPosition);
            if (cut.hasDimension()){
                var dimension = cut.getDimension();
                var unit = larguerMetricComboBox.getSelectedItem().toString();

                editWTextField.setText(Utils.convertValueToUnit(dimension.width, longueurMetricComboBox.getSelectedItem().toString()) + "");
                editLTextField.setText(Utils.convertValueToUnit(dimension.height, larguerMetricComboBox.getSelectedItem().toString()) + "");
            }
        }
        drawingPanel.repaint();
    }//GEN-LAST:event_cutComboBoxItemStateChanged

    private void borderCutDepthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borderCutDepthComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_borderCutDepthComboBoxActionPerformed

    private void systemMetricActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemMetricActionPerformed
        // TODO add your handling code here:
        // Liste des unités pour chaque système
        String[] imperialUnits = {"po", "pi"};
        String[] universalUnits = {"mm", "cm", "m"};

        // Vérifie si le bouton est sélectionné (impérial ou non)
        boolean isImperial = systemMetric.isSelected();
        String texte = isImperial ? "imperial" : "universal";
        systemMetric.setText(texte);


        // Liste des JComboBox à gérer (ajustez si elles sont déclarées autrement)
        JComboBox[] comboBoxes = {
                borderCutDepthComboBox, borderCutLengthComboBox, borderCutWidthComboBox,
                distanceFromRefMetricComboBox, longueurMetricComboBox, larguerMetricComboBox,
                depthComboBox, marginComboBox, lengthPanelComboBox, widthPanelComboBox, depthPanelComboBox, toolLengthComboBox,
        };

        // Parcourir chaque ComboBox et ajuster les unités visibles
        for (JComboBox<String> comboBox : comboBoxes) {
            comboBox.removeAllItems(); // Effacer les anciens items
            String[] unitsToAdd = isImperial ? imperialUnits : universalUnits;

            for (String unit : unitsToAdd) {
                comboBox.addItem(unit);
            }

            comboBox.setSelectedIndex(0); // Sélectionner la première unité par défaut
        }

        // Mettre à jour les champs texte associés (si nécessaire, selon votre logique)
        System.out.println("Système de mesure mis à jour : " + (isImperial ? "Impérial" : "Universel"));

    }//GEN-LAST:event_systemMetricActionPerformed

    private void systemMetricStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_systemMetricStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_systemMetricStateChanged

    private void editReferenceToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editReferenceToggleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editReferenceToggleActionPerformed

    private void exportGcodeButtonMousePressed(java.awt.event.MouseEvent evt) {
        ExportGcodeDialog dialog = new ExportGcodeDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        // Vérifier si l'utilisateur a confirmé
        if (dialog.isConfirmed()) {
            String feedRate = dialog.getFeedRate();
            String units = dialog.getUnits();
            String pause = dialog.getPause();
            String fileType = dialog.getFileType();

            // Ouvrir JFileChooser pour spécifier l'emplacement du fichier
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose export location");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    fileType + " Files", fileType.substring(1))); // Add filter for file type

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // Vérifier et ajouter l'extension au fichier si nécessaire
                if (!filePath.endsWith(fileType)) {
                    //Peut-être permettre une rééicriture des lignes en spécifiant l'autre type de fichier (.c ou .gcode)
                    filePath += fileType;
                }

                // Appeler la méthode d'exportation avec les paramètres saisis
                controller.generateGCODE(units, pause, feedRate);
                try{
                    controller.exporterFichierGCODE(filePath);
                }
                catch (IOException e){
                    JOptionPane.showMessageDialog(this, "The file could not be exported");
                }

            }
        }
    }

    private void deleteToolButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //setTextTool();
        int tool_selected_index = toolComboBox.getSelectedIndex();
        UUID selectedToolUUID = controller.getToolId(tool_selected_index);
        controller.deleteTool(tool_selected_index);
        toolComboBox.removeAllItems();
        var list_cuts = controller.getCuts();
        int position_cut = 0;

        for (CutDTO cut : list_cuts) {
            ToolDTO tool = cut.getTool();
            if (tool.getId() != null && tool.getId().equals(selectedToolUUID)) {
                controller.setIsvalid_False(position_cut);
            }
            position_cut++;
        }
        fillComboBox();
        fillToolComboBox();
    }

    private void addToolButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String toolBox = toolLengthComboBox.getSelectedItem().toString();

        String tool_name = ToolNameTextField.getText();
        double toolW = Utils.parseMetricValue(ToolWidthTextField.getText());
        double tool_width = Utils.convertValueToUnit(Utils.convertValueToPixel(toolW, toolBox), "mm");
        int positionCut = Integer.parseInt(ToolpositionTextField.getText());
        controller.addTool(tool_name, tool_width);
        toolComboBox.removeAllItems();
        fillToolComboBox();
    }

    private void editToolButtonActionPerformed(java.awt.event.ActionEvent evt) {

        int position_selected_tool = toolComboBox.getSelectedIndex();
        String toolBox = toolLengthComboBox.getSelectedItem().toString();

        String tool_name = ToolNameTextField.getText();
        double toolW = Utils.parseMetricValue(ToolWidthTextField.getText());
        double tool_width = Utils.convertValueToUnit(Utils.convertValueToPixel(toolW, toolBox), "mm");

        ToolDTO tool_edit = controller.getTools().get(position_selected_tool);
        controller.editTool(tool_edit.getId(), tool_name, tool_width);
        fillToolComboBox();
    }

    public void setTextTool(){
        int position_selected_tool = toolComboBox.getSelectedIndex();
        ToolDTO tool_selected = controller.getTools().get(position_selected_tool);
        ToolNameTextField.setText(tool_selected.getName());
        ToolWidthTextField.setText(String.valueOf(tool_selected.getWidthCut()));
        ToolpositionTextField.setText(String.valueOf(position_selected_tool));
    }



    private void resizePanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        resizeNewPanel();
    }

    private void createPanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createNewPanel();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
//        coupeOptionsComboBox.addActionListener(e -> {
//            cutTypePressed =
//
//        }
//
//        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BorderCutPanel;
    private javax.swing.JPanel RegularPanel;
    private javax.swing.JPanel SwitchablePanel;
    private javax.swing.JTextField ToolNameTextField;
    private javax.swing.JTextField ToolWidthTextField;
    private javax.swing.JTextField ToolpositionTextField;
    private javax.swing.JButton addToolButton;
    private javax.swing.JRadioButton airSupportRadio;
    private javax.swing.JComboBox<String> borderCutDepthComboBox;
    private javax.swing.JTextField borderCutDepthTextField;
    private javax.swing.JComboBox<String> borderCutLengthComboBox;
    private javax.swing.JComboBox<String> borderCutWidthComboBox;
    private javax.swing.JRadioButton clampSupportRadio;
    private javax.swing.JComboBox<String> coupeOptionsComboBox;
    private javax.swing.JButton createPanelButton;
    private javax.swing.JComboBox<String> cutComboBox;
    private javax.swing.JTextField cutDepthTextField;
    private javax.swing.JPanel cutPanel;
    private javax.swing.JButton deleteCutButton;
    private javax.swing.JButton deleteToolButton;
    private javax.swing.JComboBox<String> depthComboBox;
    private javax.swing.JTextField depthMarginTextField;
    private javax.swing.JComboBox<String> depthPanelComboBox;
    private javax.swing.JTextField depthTextField;
    private javax.swing.JTextField destinationTextField;
    private javax.swing.JComboBox<String> distanceFromRefMetricComboBox;
    private javax.swing.JTextField distanceTextField;
    private javax.swing.JButton drawCutButton;
    private ca.ulaval.glo2004.gui.DrawingPanel drawingPanel;
    private javax.swing.JButton editCutButton;
    private javax.swing.JTextField editLTextField;
    private javax.swing.JToggleButton editReferenceToggle;
    private javax.swing.JButton editToolButton;
    private javax.swing.JTextField editWTextField;
    private javax.swing.JCheckBox enableSelection;
    private javax.swing.JMenuItem exportCncButton;
    private javax.swing.JMenuItem exportGcodeButton;
    private javax.swing.JMenuItem exportPanButton;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem importFileButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> larguerMetricComboBox;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JTextField lengthCutTextField;
    private javax.swing.JComboBox<String> lengthPanelComboBox;
    private javax.swing.JTextField lengthTextField;
    private javax.swing.JComboBox<String> longueurMetricComboBox;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox<String> marginComboBox;
    private javax.swing.JTextField originTextField;
    private javax.swing.JButton previewButton;
    private javax.swing.JButton prohibutedAreaButton;
    private javax.swing.JMenuItem redoButton;
    private javax.swing.JTextField referenceTextField;
    private javax.swing.JButton resetCutParams;
    private javax.swing.JButton resizePanelButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JMenuItem saveAsButton;
    private javax.swing.JMenuItem saveButton;
    private javax.swing.JPanel setupPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JToggleButton systemMetric;
    private javax.swing.JComboBox<String> toolComboBox;
    private javax.swing.JComboBox<String> toolLengthComboBox;
    private javax.swing.JComboBox<String> toolOfCutComboBox;
    private javax.swing.JMenuItem undoButton;
    private javax.swing.JTextField widthCutTextField;
    private javax.swing.JComboBox<String> widthPanelComboBox;
    private javax.swing.JTextField widthTextField;

    public CncController getController() {
        return controller;
    }

    // End of variables declaration//GEN-END:variables
}
