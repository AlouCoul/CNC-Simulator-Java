package ca.ulaval.glo2004.domain;

import java.awt.*;

public class ForbiddenZone {
    private Rectangle bounds;

    public ForbiddenZone(Point position, int width, int height) {
        this.bounds = new Rectangle(position.x, position.y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean contains(Point point) {
        return bounds.contains(point);
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public boolean intersectsCut(Cut cut) {
        var lines = cut.getLines();

        for (int i = 0; i < lines.size(); i++) {
            var start = lines.get(i).getKey();
            var end = lines.get(i).getValue();
            if (bounds.intersectsLine(start.x, start.y, end.x, end.y)) {
                return true;
            }
        }

        return false;
    }

}
