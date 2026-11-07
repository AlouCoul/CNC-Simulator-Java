package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LCut extends Cut {
    public LCut(int depth, List<Point> points, Tool tool, boolean isValid, Point reference) {
        super(depth, points, tool, isValid, reference);
    }

    public void calculateDistance() {
        var points = getPoints();
        if (points == null || points.isEmpty()){
            return;
        }

        var reference = getReference();
        var firstPoint = points.getLast();
        this.setDistance(calculateEuclideanDistance(reference, firstPoint));
        this.setDimension();
    }

    @Override
    public LCut deepCopy() {
        LCut copy = new LCut(getDepth(), new ArrayList<>(), null, isValid(), getReference());
        copyBaseProperties(copy); // Copie des propriétés de base
        return copy;
    }

    @Override
    public CutType getType() {
        return CutType.L;
    }

    @Override
    public void setLabel() {
        var height = Utils.convertValueToUnit(dimension.height, "po");
        var width = Utils.convertValueToUnit(dimension.width, "po");
        this.label = "w: " + width + " po" + " h: " + height + " po";
    }

    @Override
    public boolean contains(Point point) {
        var reference = getReference();
        var diagonalPoint = getPoints().getLast();
        Point corner1 = new Point(reference.x, diagonalPoint.y);
        Point corner2 = new Point(diagonalPoint.x, reference.y);
        Point line1Start, line1End, line2Start, line2End;

        if (reference.x == corner1.x) {
            line1Start = corner1;
            line1End = diagonalPoint;
        } else {
            line1Start = corner2;
            line1End = diagonalPoint;
        }

        if (reference.y == corner1.y) {
            line2Start = corner1;
            line2End = diagonalPoint;
        } else {
            line2Start = corner2;
            line2End = diagonalPoint;
        }

        return isPointOnLineSegment(line1Start, line1End, point) || isPointOnLineSegment(line2Start, line2End, point);
    }

    @Override
    public List<Map.Entry<Point, Point>> getLines() {
        var lines = new ArrayList<Map.Entry<Point, Point>>();
        var firstPoint = getPoints().getFirst();
        var secondPoint = getPoints().getLast();
        var topPoint = new Point(secondPoint.x, firstPoint.y);
        var bottomPoint = new Point(firstPoint.x, secondPoint.y);
        lines.add(Map.entry(topPoint, secondPoint));
        lines.add(Map.entry(bottomPoint, secondPoint));

        return lines;
    }

    private boolean isPointOnLineSegment(Point start, Point end, Point point) {
        if ((point.y - start.y) * (end.x - start.x) != (point.x - start.x) * (end.y - start.y)) {
            return false;
        }

        return (point.x >= Math.min(start.x, end.x) && point.x <= Math.max(start.x, end.x) &&
                point.y >= Math.min(start.y, end.y) && point.y <= Math.max(start.y, end.y));
    }


    @Override
    public List<Point> adjustPoints(double newDistance) {
        var reference = getReference();
        var adjustedSecondPoint = new Point();
        var secondPoint = getPoints().getLast();
        if (newDistance != getDistance()){
            adjustedSecondPoint = adjustDistance(reference, secondPoint, newDistance);
        } else {
            var newX = secondPoint.x + reference.x - getPoints().getFirst().x;
            var newY = secondPoint.y + reference.y - getPoints().getFirst().y;
            adjustedSecondPoint = new Point(newX, newY);
        }

        var newPoints = new ArrayList<Point>();
        newPoints.add(reference);
        newPoints.add(adjustedSecondPoint);

        return newPoints;
    }


    @Override
    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension() {
        var reference = this.getReference();
        var firstPoint = this.getPoints().getLast();
        var deltaX = Math.abs(firstPoint.x - reference.x);
        var deltaY = Math.abs(firstPoint.y - reference.y);
        this.dimension = new Dimension(deltaX, deltaY);
    }
}
