package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RectangularCut extends Cut {
    public RectangularCut(int depth, List<Point> points, Tool tool, boolean isValid, Point reference) {
        super(depth, points, tool, isValid, reference);
    }

    @Override
    public RectangularCut deepCopy() {
        RectangularCut copy = new RectangularCut(getDepth(), new ArrayList<>(), null, isValid(), getReference());
        copyBaseProperties(copy); // Copie des propriétés de base
        return copy;
    }

    @Override
    public void calculateDistance() {
        var points = getPoints();
        if (points == null || points.isEmpty()) {
            return;
        }

        var reference = getReference();
        var firstPoint = points.get(1);
        this.setDistance(calculateEuclideanDistance(reference, firstPoint));
        this.setDimension();
    }

    @Override
    public CutType getType() {
        return CutType.RECTANGULAR;
    }

    @Override
    public boolean contains(Point point) {
        var firstPoint = getPoints().get(1);
        var secondPoint = getPoints().get(2);
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
    public void setLabel() {
        var height = Utils.convertValueToUnit(dimension.height, "po");
        var width = Utils.convertValueToUnit(dimension.width, "po");
        this.label = "w: " + width + " po" + " h: " + height + " po";
    }

    @Override
    public List<Map.Entry<Point, Point>> getLines() {
        var lines = new ArrayList<Map.Entry<Point, Point>>();
        var firstPoint = getPoints().get(1);
        var secondPoint = getPoints().get(2);
        var thirdPoint = new Point(firstPoint.x, secondPoint.y);
        var fourthPoint = new Point(secondPoint.x, firstPoint.y);

        lines.add(Map.entry(thirdPoint, secondPoint));
        lines.add(Map.entry(fourthPoint, secondPoint));
        lines.add(Map.entry(thirdPoint, firstPoint));
        lines.add(Map.entry(fourthPoint, firstPoint));

        return lines;
    }

    @Override
    public List<Point> adjustPoints(double newDistance) {
        var reference = getReference();
        var adjustedSecondPoint = new Point();
        var adjustedThirdPoint = new Point();
        var secondPoint = getPoints().get(1);
        var thirdPoint = getPoints().get(2);
        var oldDistance = getDistance();
        if (newDistance != oldDistance) {
            adjustedSecondPoint = adjustDistance(reference, secondPoint, newDistance);
            adjustedThirdPoint.x = thirdPoint.x + adjustedSecondPoint.x - secondPoint.x;
            adjustedThirdPoint.y = thirdPoint.y + adjustedSecondPoint.y - secondPoint.y;
        } else {
            var newSecondX = secondPoint.x + reference.x - getPoints().getFirst().x;
            var newSecondY = secondPoint.y + reference.y - getPoints().getFirst().y;
            adjustedSecondPoint = new Point(newSecondX, newSecondY);
            var newThirdX = thirdPoint.x + reference.x - getPoints().getFirst().x;
            var newThirdY = thirdPoint.y + reference.y - getPoints().getFirst().y;
            adjustedThirdPoint = new Point(newThirdX, newThirdY);
        }

        var newPoints = new ArrayList<Point>();
        newPoints.add(reference);
        newPoints.add(adjustedSecondPoint);
        newPoints.add(adjustedThirdPoint);

        return newPoints;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension() {
        var firstPoint = this.getPoints().get(1);
        var secondPoint = this.getPoints().getLast();
        var deltaX = Math.abs(secondPoint.x - firstPoint.x);
        var deltaY = Math.abs(secondPoint.y - firstPoint.y);
        this.dimension = new Dimension(deltaX, deltaY);
    }
}
