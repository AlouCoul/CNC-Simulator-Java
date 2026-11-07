package ca.ulaval.glo2004.domain.dto;

import ca.ulaval.glo2004.domain.Panel;
import ca.ulaval.glo2004.gui.SupportType;

import java.awt.*;

public class PanelDTO {
    private SupportType support;
    private Dimension dimension;
    private double depth;
    private int gridSize;
    private boolean showGrid;
    private boolean enableMagnet;

    public PanelDTO(Panel panel) {
        this.support = panel.getSupport();
        this.dimension = panel.getDimension();
        this.depth = panel.getDepth();
        this.gridSize = panel.getGridSize();
        this.enableMagnet = panel.getEnableMagnet();
        this.showGrid = panel.getShowGrid();
    }

    public SupportType getSupport() {
        return support;
    }

    public void setSupport(SupportType support) {
        this.support = support;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public boolean isEnableMagnet() {
        return enableMagnet;
    }
}
