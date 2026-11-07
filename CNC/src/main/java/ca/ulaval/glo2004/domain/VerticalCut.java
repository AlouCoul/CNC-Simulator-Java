package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VerticalCut extends Cut {

    public VerticalCut(int depth, List<Point> points, Tool tool, boolean isValid, Point reference) {
        super(depth, points, tool, isValid, reference);
    }

    @Override
    public VerticalCut deepCopy() {
        VerticalCut copy = new VerticalCut(getDepth(), new ArrayList<>(), null, isValid(), getReference());
        copyBaseProperties(copy); // Copie des propriétés de base
        return copy;
    }

    @Override
    public void setLabel() {
        var distanceInInches = Utils.convertValueToUnit(getDistance(), "po");
        this.label = distanceInInches + " po";
    }

    @Override
    public void calculateDistance() {
        var points = getPoints();
        if (points == null ||points.isEmpty()){
            return;
        }

        var reference = getReference();
        var firstPoint = points.getFirst();
        this.setDistance(calculateEuclideanDistance(reference, firstPoint));
    }

    @Override
    public CutType getType() {
        return CutType.VERTICAL;
    }

    @Override
    public boolean contains(Point point) {
        return this.getPoints().getFirst().x == point.getX();
    }

    @Override
    public List<Map.Entry<Point, Point>> getLines() {
        var lines = new ArrayList<Map.Entry<Point, Point>>();
        var firstPoint = getPoints().getFirst();
        var secondPoint = getPoints().getLast();

        lines.add(Map.entry(firstPoint, secondPoint));

        return lines;
    }

    @Override
    public List<Point> adjustPoints(double newDistance) {
        var reference = getReference();
        var firstPoint = getPoints().getFirst();
        var newPoints = new ArrayList<Point>();
        newPoints.add(adjustDistance(reference, firstPoint, newDistance));
        var secondPoint = getPoints().getLast();
        secondPoint.x = newPoints.getFirst().x;
        newPoints.add(secondPoint);
        
        return newPoints;
    }

    @Override
    public String toString() {
        return "VerticalCut{"+
                "profondeur=" + getDepth() +
                ", point="+getPoints() +
                ", outil=" + getTool() +
                ", estValide=" + isValid() +
                '}';
    }
}
