package ca.ulaval.glo2004.gui.drawing;

import ca.ulaval.glo2004.Utils.Utils;

import java.awt.*;
import java.util.UUID;

public class Line {
    private int id;
    public static int counter;
    private UUID cutId;
    private double toolWidth;
    private double defaultWidth;
    private String label;

    public Line(){

    }

    public Line(int x1, int y1, int x2, int y2, LineType type, Color color, UUID cutId, double toolWidth, String label) {
        this.id = ++counter;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = type;
        this.color = color;
        this.cutId = cutId;
        this.toolWidth = Utils.convertMmToPixels(toolWidth);
        this.defaultWidth = Utils.convertMmToPixels(1.5);
        this.label = label;
    }

    public Line(int x1, int y1, int x2, int y2, LineType type, Color color, UUID cutId, String label) {
        this.id = ++counter;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = type;
        this.color = color;
        this.cutId = cutId;
        this.toolWidth = 0;
        this.label = label;
    }

    public int getId() {
        return id;
    }


    public boolean containsPoint(Point point) {
        // Vérifie si le point se trouve entre les extrémités de la ligne
        boolean withinBounds = point.x >= Math.min(x1, x2) && point.x <= Math.max(x1, x2)
                && point.y >= Math.min(y1, y2) && point.y <= Math.max(y1, y2);

        if (!withinBounds) {
            return false;
        }

        // Vérifie si le point respecte l'équation de la ligne
        // Calcul de la pente (évite la division pour éviter les erreurs de précision)
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;

        // Si la ligne est verticale, on vérifie uniquement la coordonnée X
        if (deltaX == 0) {
            return point.x == x1;
        }

        // Si la ligne est horizontale, on vérifie uniquement la coordonnée Y
        if (deltaY == 0) {
            return point.y == y1;
        }

        // Sinon, vérifie que le rapport des différences est égal
        return (point.y - y1) * deltaX == (point.x - x1) * deltaY;
    }

    public Point getStartPoint() {
        return new Point(x1, y1);
    }

    public Point getEndPoint() {
        return new Point(x2, y2);
    }

    public UUID getCutId() {
        return cutId;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public boolean contains(Line line) {
        if (line == null){
            return false;
        }

        return containsPoint(line.getStartPoint()) && containsPoint(line.getEndPoint());
    }

    public String getLabel() {
        return label;
    }

    public enum LineType { VERTICAL, HORIZONTAL }

    private int x1, y1, x2, y2;
    private Color color;
    private LineType type;

    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public LineType getType() { return type; }
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }

    public void draw(Graphics2D g) {
        g.setColor(color);

        g.setStroke(new BasicStroke((float) toolWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        g.drawLine(x1, y1, x2, y2);

        g.setStroke(new BasicStroke((float) defaultWidth));
    }

    public boolean isNear(Point p, int value) {
        if(this.toolWidth == 0){
            value += (int) defaultWidth;
        }
        else {
            value += (int) toolWidth;
        }
        if (type == LineType.VERTICAL) {
            return Math.abs(p.x - x1) <= value;
        } else {
            return Math.abs(p.y - y1) <= value;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        if (obj instanceof Line line) {
            if (line.cutId == null && cutId == null) {
                return line.toolWidth == toolWidth
                        && line.type == type
                        && line.getX1() == getX1()
                        && line.getY1() == getY1()
                        && line.getX2() == getX2()
                        && line.getY2() == getY2();
            }

            return line.cutId != null && line.cutId.equals(cutId)
                    && line.toolWidth == toolWidth
                    && line.type == type
                    && line.getX1() == getX1()
                    && line.getY1() == getY1()
                    && line.getX2() == getX2()
                    && line.getY2() == getY2();
        }

        return false;
    }

    public Point getLabelPosition() {
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        int offsetY = -10;
        int offsetX = -20;

        return new Point(midX + offsetX, midY + offsetY);
    }

}
