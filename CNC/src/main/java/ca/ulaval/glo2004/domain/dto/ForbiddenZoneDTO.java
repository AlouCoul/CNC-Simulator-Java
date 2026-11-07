package ca.ulaval.glo2004.domain.dto;

import ca.ulaval.glo2004.domain.ForbiddenZone;

import java.awt.*;

public class ForbiddenZoneDTO {
    private Rectangle bounds;

    public ForbiddenZoneDTO(ForbiddenZone zone) {
        this.bounds = new Rectangle(zone.getBounds());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPosition(Point position) {
        bounds.setLocation(position);
    }

    public void setSize(int width, int height) {
        bounds.setSize(Math.max(10, width), Math.max(10, height));
    }

    public boolean contains(Point point) {
        return bounds.contains(point);
    }

    public boolean isOnResizeEdge(Point point, int margin) {
        int edgeX = bounds.x + bounds.width;
        int edgeY = bounds.y + bounds.height;
        return Math.abs(point.x - edgeX) <= margin && Math.abs(point.y - edgeY) <= margin;
    }
}
