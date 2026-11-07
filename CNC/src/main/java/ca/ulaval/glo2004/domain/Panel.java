package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ForbiddenZoneDTO;
import ca.ulaval.glo2004.gui.CutType;
import ca.ulaval.glo2004.gui.DrawingPanel;
import ca.ulaval.glo2004.gui.SupportType;

import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Panel implements Serializable {
    private SupportType support;
    private Dimension dimension;
    private List<Cut> cuts;
    private double depth;
    private int gridSize;
    private boolean showGrid;
    private boolean enableMagnet;
    private List<ForbiddenZone> forbiddenZones;
    public static final int TABLE_WIDTH = (int) Utils.convertFeetToPixels(10);
    public static final int TABLE_HEIGHT = (int) Utils.convertFeetToPixels(5);

    public Panel(SupportType support, Dimension dimension, List<Cut> cuts, double depth) {
        this.support = support;
        this.dimension = dimension;
        this.cuts = cuts;
        this.depth = depth;
        gridSize = 160;
        forbiddenZones = new ArrayList<>();
    }

    public Panel() {
        this.support = null;
        this.dimension = new Dimension();
        this.cuts = new ArrayList<>();
        this.depth = 0.0;
        forbiddenZones = new ArrayList<>();
    }

    public Panel deepCopy() {
        Panel copy = new Panel();
        copy.support = this.support;
        copy.dimension = new Dimension(this.dimension);
        copy.cuts = new ArrayList<>();
        for (Cut cut : this.cuts) {
            copy.cuts.add(cut.deepCopy());
        }

        // Copie profonde des forbiddenZones
        copy.forbiddenZones = new ArrayList<>();
        for (ForbiddenZone zone : this.forbiddenZones) {
            Point position = new Point(zone.getBounds().x, zone.getBounds().y);
            int width = zone.getBounds().width;
            int height = zone.getBounds().height;
            copy.forbiddenZones.add(new ForbiddenZone(position, width, height));
        }

        copy.depth = this.depth;
        copy.gridSize = this.gridSize;
        copy.showGrid = this.showGrid;
        copy.enableMagnet = this.enableMagnet;
        return copy;
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

    public void removeCut() {

    }

    public void editCut(int id, Point newPoint){
        if (cuts != null && cuts.size() > id) {
            var cut = cuts.get(id);
            var reference = cut.getReference();
            var distance = cut.getDistance();
            switch (cut.getType()) {
                case HORIZONTAL -> {
                    distance =  newPoint.y - reference.y;
                }
                case VERTICAL -> {
                    distance =  newPoint.x - reference.x;
                }
                case L-> {
                    var oldCut = cuts.get(id).deepCopy();
                    cut.getPoints().getLast().x = newPoint.x;
                    cut.getPoints().getLast().y = newPoint.y;
                    double deltaX = newPoint.x - reference.x;
                    double deltaY = newPoint.y - reference.y;
                    distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    var newPoints = cuts.get(id).adjustPoints(distance);
                    moveReferencedCuts(id, oldCut, newPoints, reference);
                }
                case RECTANGULAR -> {
                    var oldCut = cuts.get(id).deepCopy();
                    cut.getPoints().getLast().x = cut.getPoints().getLast().x + newPoint.x - cut.getPoints().get(1).x;
                    cut.getPoints().getLast().y = cut.getPoints().getLast().y + newPoint.y - cut.getPoints().get(1).y;
                    cut.getPoints().get(1).x = newPoint.x;
                    cut.getPoints().get(1).y = newPoint.y;
                    double deltaX = newPoint.x - reference.x;
                    double deltaY = newPoint.y - reference.y;
                    distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    var newPoints = cuts.get(id).adjustPoints(distance);
                    moveReferencedCuts(id, oldCut, newPoints, reference);
                }
            }

            editCut(id, cut.getDepth(), cut.getReference(), distance, cut.getDimension(), cut.getTool());
        }
    }

    public void editCut(int id, int depth, Point reference, double distance, Dimension dimension, Tool tool) {
        if (cuts != null && cuts.size() > id) {
            cuts.get(id).setDepth(depth);
            var oldReference = reference == null ? null : new Point(cuts.get(id).getReference());
            cuts.get(id).setReference(reference);
            cuts.get(id).setIsValid(true);
            cuts.get(id).setTool(tool);
            var oldCut = cuts.get(id).deepCopy();
            if (dimension != null
                    && cuts.get(id).getDimension() != null
                    && (cuts.get(id).getDimension().width != dimension.width
                    || cuts.get(id).getDimension().height != dimension.height)) {
                if (cuts.get(id) instanceof LCut) {
                    var heightFactor = cuts.get(id).getPoints().getLast().y > cuts.get(id).getPoints().get(0).y ? 1 : -1;
                    var widthFactor = cuts.get(id).getPoints().getLast().x > cuts.get(id).getPoints().get(0).x ? 1 : -1;
                    cuts.get(id).getPoints().getLast().y = cuts.get(id).getPoints().get(0).y + dimension.height * heightFactor;
                    cuts.get(id).getPoints().getLast().x = cuts.get(id).getPoints().get(0).x + dimension.width * widthFactor;
                    distance = Math.sqrt(Math.pow(dimension.getWidth(), 2) + Math.pow(dimension.getHeight(), 2));
                }
                else if (cuts.get(id).getType() == CutType.BORDER) {
                    int x1 = (this.dimension.width - dimension.width) / 2;
                    int y1 = TABLE_HEIGHT - this.dimension.height + (this.dimension.height - dimension.height) / 2;
                    int x2 = x1 + dimension.width;
                    int y2 = y1 + dimension.width;
                    Point bottomLeft = new Point(x1, y1);
                    Point topRight = new Point(x2, y2);
                    cuts.get(id).getPoints().get(0).x = bottomLeft.x;
                    cuts.get(id).getPoints().get(0).y = bottomLeft.y;
                    cuts.get(id).getPoints().get(1).x = topRight.x;
                    cuts.get(id).getPoints().get(1).y = topRight.y;
                }
                else {
                    cuts.get(id).getPoints().getLast().y = cuts.get(id).getPoints().get(1).y + dimension.height;
                    cuts.get(id).getPoints().getLast().x = cuts.get(id).getPoints().get(1).x + dimension.width;
                }
            }

            var newPoints = cuts.get(id).adjustPoints(distance);
            moveReferencedCuts(id, oldCut, newPoints, oldReference);
            cuts.get(id).setPoints(newPoints);
            cuts.get(id).calculateDistance();
            cuts.get(id).setLabel();
            validateCuts();
        }
    }

    private void moveReferencedCuts(int id, Cut oldCut, List<Point> newPoints, Point oldReference) {
        for (Cut cut : cuts) {
            if (cut.getId() == cuts.get(id).getId()) {
                continue;
            }

            var childReference = cut.getReference();
            if (childReference != null && oldCut.contains(childReference) && newPoints != null && cut.getReferencedCuts().contains(oldCut.getId())) {
                Point oldBasePoint, newBasePoint;
                if (oldReference == null || oldCut.getPoints().getFirst().x != oldReference.x || oldCut.getPoints().getFirst().y != oldReference.y) {
                    oldBasePoint = oldCut.getPoints().getFirst();
                    newBasePoint = newPoints.getFirst();
                } else {
                    oldBasePoint = oldCut.getPoints().get(1);
                    newBasePoint = newPoints.get(1);
                }

                var deltaX = newBasePoint.getX() - oldBasePoint.getX();
                var deltaY = newBasePoint.getY() - oldBasePoint.getY();
                var newReference = new Point();
                switch (cut.getType()) {
                    case HORIZONTAL -> {
                        newReference.x = 0;
                        newReference.y = (int) (childReference.getY() + deltaY);
                    }
                    case VERTICAL -> {
                        newReference.x = (int) (childReference.getX() + deltaX);
                        newReference.y = (int) childReference.getY();
                    }
                    case L, RECTANGULAR -> {
                        newReference.x = (int) (childReference.getX() + deltaX);
                        newReference.y = (int) (childReference.getY() + deltaY);
                    }
                    case BORDER -> {
                        newReference = null;
                    }
                }

                editCut(cuts.indexOf(cut), cut.getDepth(), newReference, cut.getDistance(), null, cut.getTool());
            }
        }
    }


    public void deleteCut(int id) {
        var cutId = cuts.get(id).getId();
        cuts.remove(id);
        for (Cut cut : cuts) {
            validateCut(cut, cut.getTool());
            if(cut.getReferencedCuts().contains(cutId)) {
                cut.setIsValid(false);
            }
        }
    }

    public List<CutDTO> getCutsDTO() {
        var cutsDTO = new ArrayList<CutDTO>();
        for (var cut : cuts) {
            cutsDTO.add(new CutDTO(cut));
        }

        return cutsDTO;
    }

    public void setCuts(List<Cut> cuts) {
        this.cuts = cuts;
    }

    public void addCut(int depth, List<Point> points, CutType type, Tool tool, Point reference) {
        switch (type) {
            case HORIZONTAL -> {
                cuts.add(new HorizontalCut(depth, points, tool, true, reference));
            }
            case VERTICAL -> {
                cuts.add(new VerticalCut(depth, points, tool, true, reference));
            }
            case L -> {
                cuts.add(new LCut(depth, points, tool, true, reference));
            }
            case RECTANGULAR -> {
                cuts.add(new RectangularCut(depth, points, tool, true, reference));
            }
            case BORDER -> {
                cuts.add(new BorderCut(depth, points, tool, true));
            }
        }

        var newCutId = cuts.size() - 1;
        var referencedCuts = new ArrayList<UUID>();
        for (var cut: cuts) {
            if (cut.getId() == cuts.get(newCutId).getId()) {
                continue;
            }

            if (reference != null && cut.contains(reference)) {
                referencedCuts.add(cut.getId());
            }
        }

        cuts.get(newCutId).setReferencedCuts(referencedCuts);
        cuts.get(newCutId).setLabel();
        validateCuts();
    }

    public void setIsvalid_False(int position) {
        Cut cut_isValidFalse = cuts.get(position);
        cut_isValidFalse.setIsValid(false);
        System.out.println("INVALID EST ÉGAL À : " + cut_isValidFalse.getIsValid());
    }

    public List<Cut> getCuts() {
        return cuts;
    }

    // Ajouter une nouvelle zone interdite
    public void addForbiddenZone(Point position, int width, int height) {
        forbiddenZones.add(new ForbiddenZone(position, width, height));
        validateCuts();
    }

    // Récupérer toutes les zones interdites
    public List<ForbiddenZoneDTO> getForbiddenZones() {
        var zones = new ArrayList<ForbiddenZoneDTO>();
        for (var zone: forbiddenZones) {
            zones.add(new ForbiddenZoneDTO(zone));
        }

        return zones;
    }

    // Supprimer une zone interdite
    public void deleteForbiddenZone(int index) {
        if (index >= 0 && index < forbiddenZones.size()) {
            forbiddenZones.remove(index);
            validateCuts();
        }
    }

    public void editForbiddenZone(int index, Rectangle bounds) {
        if (index >= 0 && index < forbiddenZones.size()) {
            forbiddenZones.get(index).setBounds(bounds);
            validateCuts();
        }
    }

    public void removeToolFromCut(UUID toolId) {
        for (var cut: cuts) {
            if (cut.getTool() != null && cut.getTool().getId() == toolId) {
                cut.setTool(null);
                validateCut(cut, cut.getTool());
            }
        }
    }

    private void validateCuts(){
        for (Cut cut : cuts) {
            validateCut(cut, cut.getTool());
        }
    }

    private void validateCut(Cut cut, Tool tool) {
        if (tool == null) {
            cut.setIsValid(false);
            return;
        }

        for (var line: cut.getLines()) {
            var start = line.getKey();
            var end = line.getValue();
            if (isOutsidePanel(start) || isOutsidePanel(end)) {
                cut.setIsValid(false);
                return;
            }
        }


        for (var zone: forbiddenZones) {
            if (zone.intersectsCut(cut)){
                cut.setIsValid(false);
                return;
            }
        }

        Set<UUID> existingCutIds = cuts.stream()
                .map(Cut::getId)
                .collect(Collectors.toSet());
        var allReferencesExist = existingCutIds.containsAll(cut.getReferencedCuts());
        cut.setIsValid(allReferencesExist);
    }

    private boolean isOutsidePanel(Point point) {;
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();

        return point.x < 0 || point.x > width ||
                point.y > TABLE_HEIGHT  || point.y < TABLE_HEIGHT - height;
    }

    public void setGridSize(int gridSize, boolean showGrid, boolean enableMagnet) {
        this.gridSize = gridSize;
        this.showGrid = showGrid;
        this.enableMagnet = enableMagnet;
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean getEnableMagnet() {
        return enableMagnet;
    }

    public boolean getShowGrid() {
        return showGrid;
    }
}
