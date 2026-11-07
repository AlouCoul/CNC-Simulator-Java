package ca.ulaval.glo2004.domain.dto;

import ca.ulaval.glo2004.domain.*;
import ca.ulaval.glo2004.gui.CutType;

import java.awt.*;

import java.util.List;
import java.util.UUID;

public class CutDTO {
    private int depth;
    private List<Point> points;
    private Dimension dimension;
    private CutType type;
    private boolean isValid;
    private ToolDTO tool;
    private UUID id;
    private Point reference;
    private double distance;
    private String label;

    public CutDTO(Cut cut) {
        this.depth = cut.getDepth();
        this.points = cut.getPoints().stream().map(Point::new).toList();
        this.type = cut.getType();
        this.isValid = cut.isValid();
        this.tool = changeTooltoToolDTO(cut.getTool());
        this.id = cut.getId();
        var cutReference = cut.getReference() != null ? new Point(cut.getReference()) : null;
        this.reference = cutReference;
        this.distance = cut.getDistance();
        this.label = cut.getLabel();
        if (hasDimension()){
            switch (cut.getType()) {
                case L -> {
                    var lCut = (LCut) cut;
                    this.dimension = lCut.getDimension() == null ? null : new Dimension(lCut.getDimension());
                }
                case RECTANGULAR -> {
                    var rectangularCut = (RectangularCut) cut;
                    this.dimension = rectangularCut.getDimension() == null ? null : new Dimension(rectangularCut.getDimension());
                }
                case BORDER -> {
                    var borderCut = (BorderCut) cut;
                    this.dimension = borderCut.getDimension() == null ? null : new Dimension(borderCut.getDimension());
                }
            }
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getDepth() {
        return depth;
    }

    public CutType getType() {
        return type;
    }

    public UUID getId() {
        return id;
    }

    public ToolDTO changeTooltoToolDTO(Tool tool) {
        ToolDTO toolDTO = new ToolDTO(tool);
        return toolDTO;
    }

    public ToolDTO getTool() {
        return tool;
    }

    public boolean isValid() {
        return isValid;
    }

    public Point getReference() {
        return reference;
    }

    public double getDistance() {
        return distance;
    }

    public boolean hasDimension() {
        return type == CutType.L || type == CutType.BORDER || type == CutType.RECTANGULAR;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public String getLabel() {
        return label;
    }
}
