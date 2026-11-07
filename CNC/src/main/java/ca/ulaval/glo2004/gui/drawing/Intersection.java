package ca.ulaval.glo2004.gui.drawing;

import java.awt.*;

public class Intersection {
    private int x, y, size;
    private Color color;

    public Intersection(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.size = 60;
        this.color = Color.MAGENTA;
    }

    ;

    public Point getPoint() {
        return new Point(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public boolean isNear(Point p) {
        int margin = size / 2;
        return Math.abs(p.x - x) <= margin && Math.abs(p.y - y) <= margin;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - (size / 2), y - (size / 2), size, size);

        g.setColor(Color.BLACK);
        g.drawOval(x - (size / 2), y - (size / 2), size, size);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Intersection intersection) {
            return intersection.getX() == x && intersection.getY() == y;
        }

        return false;
    }
}
