package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Cut implements Serializable {
    private UUID id;
    private int depth;
    private List<Point> points = new ArrayList<>();
    private Tool tool;
    private boolean isValid;
    private Point reference;
    private double distance;
    private CutType type;
    protected Dimension dimension;
    private List<UUID> referencedCuts;
    protected String label;

    public Cut(int depth, List<Point> points, Tool tool, boolean isValid, Point reference) {
        this.depth = depth;
        this.points = points;
        this.tool = tool;
        this.isValid = isValid;
        this.id = UUID.randomUUID();
        this.reference = reference;
        calculateDistance();
        type = getType();
        referencedCuts = new ArrayList<>();
    }
    public abstract Cut deepCopy();

    protected void copyBaseProperties(Cut copy) {
        copy.depth = this.depth;
        copy.points = new ArrayList<>(); // Copie indépendante des points
        for (Point point : this.points) {
            copy.points.add(new Point(point)); // Copie profonde des Points
        }
        copy.tool = this.tool != null ? this.tool.deepCopy() : null; // Copie profonde de Tool
        copy.isValid = this.isValid;
        copy.reference = this.reference != null ? new Point(this.reference) : null; // Copie du Point
        copy.distance = this.distance;
        copy.dimension = this.dimension != null ? new Dimension(this.dimension) : null;
        copy.referencedCuts = this.referencedCuts != null ? new ArrayList<>(this.referencedCuts) : null;
        copy.id = this.id;
        copy.label = this.label;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public List<Point> getPoints() {
        return points;
    }

    public UUID getId() {
        return id;
    }

    public abstract void setLabel();

    public String getLabel() {
        return label;
    }

    public void setPoints(List<Point> points){
        this.points = new ArrayList<>(points);
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean getIsValid() {return isValid;}

    public void setIsValid (boolean isValid){
        this.isValid = isValid;
    }

    public void setReference(Point reference){
        this.reference = reference;
    }

    public Point getReference(){
        return reference;
    }

    public abstract void calculateDistance();

    public abstract CutType getType();

    public abstract boolean contains(Point point);

    public abstract List<Map.Entry<Point, Point>> getLines();

    // Méthode pour valider la coupe
    public void valid(){
        if (this.points != null && !this.points.isEmpty()){
            this.isValid = true;
        }else {
            this.isValid = false;
        }
    }

    public double calculateEuclideanDistance(Point p1, Point p2) {
        double deltaX = p2.x - p1.x;
        double deltaY = p2.y - p1.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public abstract List<Point> adjustPoints(double newDistance);

    public Point adjustDistance(Point reference, Point firstPoint, double newDistance) {
        var currentDistance = calculateEuclideanDistance(reference, firstPoint);
        if (currentDistance == 0 && (getType() == CutType.HORIZONTAL || getType() == CutType.VERTICAL)) {
            int newX, newY;
            if (getType() == CutType.HORIZONTAL) {
                newX = firstPoint.x;
                newY = (int) (firstPoint.y + newDistance);
            } else {
                newX = (int) (firstPoint.x + newDistance);
                newY = firstPoint.y;
            }
            return new Point(newX, newY);
        }

        var directionX = (firstPoint.x - reference.x) / currentDistance;
        var directionY = (firstPoint.y - reference.y) / currentDistance;
        var newX = (int) Math.round(reference.x + directionX * newDistance);
        var newY = (int) Math.round(reference.y +  directionY * newDistance);

        return new Point(newX, newY);
    }

    // Affichage des détails de la coupe
    @Override
    public String toString() {
        return "Coupe [profondeur=" + depth + ", outil=" + tool + ", valide=" + isValid + "]";
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Dimension getDimension() {
        return null;
    }

    public List<UUID> getReferencedCuts() {
        return referencedCuts;
    }

    public void setReferencedCuts(List<UUID> referencedCuts) {
        this.referencedCuts = referencedCuts;
    }
}

