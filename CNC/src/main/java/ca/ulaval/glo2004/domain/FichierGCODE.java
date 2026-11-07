package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.domain.dto.CutDTO;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class FichierGCODE {
    private StringBuilder gcode;
    private String units;
    private final double pauseTime;
    private final double feedRate;
    private double panelHeight;
    public static final int cncHeight = (int) Utils.convertFeetToPixels(5);
    public static final int ORIGIN_Y = cncHeight;


    FichierGCODE(){
        this.gcode = new StringBuilder();
        this.units = "mm";
        this.pauseTime = 4;
        this.feedRate = 500; //Changer pour mettre une valeur par défaut
        this.panelHeight = 0;
        initializeGCODE();
    };

    FichierGCODE(String units, String pauseTime, String feedRate){
        this.gcode = new StringBuilder();
        this.units = units;
        this.pauseTime = Double.parseDouble(pauseTime);
        this.feedRate = Double.parseDouble(feedRate);
        this.panelHeight = 0;
        initializeGCODE();
    }

    private void initializeGCODE(){
        this.gcode.append("; GCODE for CNC\n");
        setUpUnits(units);
        setPostionAbsolute();
    }

    private void setUpUnits(String units) {
        switch (units){
            case "mm":
                this.gcode.append("G21 ; Metric set up\n");
                break;
            case "in":
                this.gcode.append("G20 ; Imperial set up\n");
                break;
        }
    }

    public String getGcode(){
        return this.gcode.toString();
    }

    private void changeDepth(double depth){
        depth = Utils.convertPixelsToMm(depth);
        gcode.append(String.format("G0 Z%.2f\n", depth));
    }

    private void moveTo(double x, double y) {
        x = Utils.convertPixelsToMm(x);
        y = Utils.convertPixelsToMm(invertY(y)); // Inversion de Y
        gcode.append(String.format("G0 X%.2f Y%.2f\n", x, y));
    }

    private void linearMoveTo(double x, double y) {
        x = Utils.convertPixelsToMm(x);
        y = Utils.convertPixelsToMm(invertY(y)); // Inversion de Y
        gcode.append(String.format("G1 X%.2f Y%.2f F%.2f\n", x, y, feedRate));
    }

    private void addLine(double x, double y) {
        x = Utils.convertPixelsToMm(x);
        y = Utils.convertPixelsToMm(invertY(y)); // Inversion de Y
        gcode.append(String.format("X%.2f Y%.2f\n", x, y));
    }
    private void changeFeedRate(double rate){
        gcode.append(String.format("G1 F%.2f\n", rate));
    }



    public void pauseFor(){
        gcode.append(String.format("G4 P%.2f\n", pauseTime));
    }

    public void setPostionAbsolute(){
        gcode.append("G90 ; Absolute Position\n\n");
    }

    //tool
    public void selectTool(String tool) {
        gcode.append("\nT").append(tool).append(" ; Select tool\n");
    }

    //regularCut avec peut-etre la liste de point instead
    private void regularCut(CutDTO cut, String type, double panelDepth){
        addComment(type + " cut start");
        Point first = cut.getPoints().getFirst();
        Point last = cut.getPoints().getLast();
        moveTo(first.x, first.y);
        changeDepth(adjustDepth(cut.getDepth(), panelDepth));
        linearMoveTo(last.x, last.y);
        addComment(type + " cut end\n");
    }

    private void LCut(CutDTO cut, double panelDepth){
        addComment("L cut start");
        Point first = cut.getPoints().getFirst();
        Point last = cut.getPoints().getLast();
        moveTo(first.x, last.y);
        changeDepth(adjustDepth(cut.getDepth(), panelDepth));
        linearMoveTo(last.x, last.y);
        addLine(last.x, first.y);
        addComment("L cut end\n");

    }

    private void rectangularCut(CutDTO cut, double panelDepth){
        addComment("Rectangular cut start");
        Point first = cut.getPoints().get(1);
        Point last = cut.getPoints().getLast();
        moveTo(first.x, first.y);
        changeDepth(adjustDepth(cut.getDepth(), panelDepth));
        linearMoveTo(last.x, first.y);
        addLine(last.x, last.y);
        addLine(first.x, last.y);
        addLine(first.x, first.y);
        addComment("Rectangular cut end\n");

    }

    private void borderCut(CutDTO cut, double panelDepth){
        addComment("Border cut start");
        Point first = cut.getPoints().getFirst();
        Point last = cut.getPoints().getLast();
        moveTo(first.x, first.y);
        changeDepth(adjustDepth(cut.getDepth(), panelDepth));
        linearMoveTo(last.x, first.y);
        addLine(last.x, last.y);
        addLine(first.x, last.y);
        addLine(first.x, first.y);
        addComment("Border cut end\n");
    }
    //Type of cut
    public void writeCut(CutDTO cut, double panelDepth, int toolPosition){
        //S'Assurer que l'outil est bien à un certain en haut du panneau
        changeDepth(panelDepth+50);
        //Prendre le bon outil

        String tool = Integer.toString(toolPosition);
        selectTool(tool);
        //un switchCase based on CutType
        switch(cut.getType()){
            case VERTICAL, HORIZONTAL -> regularCut(cut, cut.getType().toString(), panelDepth);
            case L -> LCut(cut, panelDepth);
            case RECTANGULAR -> rectangularCut(cut, panelDepth);
            case BORDER -> borderCut(cut, panelDepth);
        }
        //Remettre l'outil en haut du panneau
        changeDepth(panelDepth+50);
        //Déais avant de commencer l'autre coupe
        pauseFor();
    }

    //Méthode d'exportation d'un file
    public File exportFile(String path, String content) throws IOException {
        File file = new File(path);
        try (FileWriter writer = new FileWriter(file)){
            writer.write(content);
            writer.flush();
        }
        return file;
    }
    private double adjustDepth(double depth, double panelDepth) {
        if ((panelDepth-depth)<= 0){
            return 0;
        }
        return panelDepth - depth;
    }
    private double invertY(double y) {
        return ORIGIN_Y - y;
    }

    public double getPanelHeight() {
        return panelHeight;
    }

    public void setPanelHeight(double panelHeight) {
        this.panelHeight = panelHeight;
    }

    private void addComment(String comment){
        gcode.append("; ").append(comment).append("\n");
    }

}
