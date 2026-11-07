package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BorderCut extends RectangularCut {

    public BorderCut(int depth, List<Point> points, Tool tool, boolean isValid) {
        super(depth, points, tool, isValid, null);
    }

    @Override
    public BorderCut deepCopy() {
        BorderCut copy = new BorderCut(getDepth(), new ArrayList<>(), null, isValid());
        copyBaseProperties(copy); // Copie des propriétés de base
        return copy;
    }

    @Override
    public CutType getType() {
        return CutType.BORDER;
    }

    @Override
    public boolean contains(Point point) {
        var firstPoint = getPoints().getFirst();
        var secondPoint = getPoints().getLast();
        var corner1 = new Point(firstPoint.x, secondPoint.y);
        var corner2 = new Point(secondPoint.x, firstPoint.y);
        var corner3 = firstPoint;
        var corner4 = secondPoint;

        return (point.x == corner1.x && point.y == corner1.y)
                || (point.x == corner2.x && point.y == corner2.y)
                || (point.x == corner3.x && point.y == corner3.y)
                || (point.x == corner4.x && point.y == corner4.y);
    }

    @Override
    public void calculateDistance() {
        var points = getPoints();
        if (points == null || points.isEmpty()) {
            return;
        }

        var firstPoint = points.getFirst();
        var secondPoint = points.getLast();
        this.setDistance(calculateEuclideanDistance(firstPoint, secondPoint));
        this.setDimension();
    }

    @Override
    public List<Point> adjustPoints(double newDistance) {
        return getPoints();
    }

    @Override
    public List<Map.Entry<Point, Point>> getLines() {
        var lines = new ArrayList<Map.Entry<Point, Point>>();
        var firstPoint = getPoints().get(0);
        var secondPoint = getPoints().get(1);
        var thirdPoint = new Point(firstPoint.x, secondPoint.y);
        var fourthPoint = new Point(secondPoint.x, firstPoint.y);

        lines.add(Map.entry(thirdPoint, secondPoint));
        lines.add(Map.entry(fourthPoint, secondPoint));
        lines.add(Map.entry(thirdPoint, firstPoint));
        lines.add(Map.entry(fourthPoint, firstPoint));

        return lines;
    }

    public void setDimension() {
        var firstPoint = this.getPoints().getFirst();
        var secondPoint = this.getPoints().getLast();
        var deltaX = Math.abs(secondPoint.x - firstPoint.x);
        var deltaY = Math.abs(secondPoint.y - firstPoint.y);
        this.dimension = new Dimension(deltaX, deltaY);
    }
}
