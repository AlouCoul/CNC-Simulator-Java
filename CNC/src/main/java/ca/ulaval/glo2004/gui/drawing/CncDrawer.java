package ca.ulaval.glo2004.gui.drawing;

import ca.ulaval.glo2004.domain.CncController;
import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ToolDTO;
import ca.ulaval.glo2004.gui.DrawingPanel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CncDrawer {
    private final CncController controller;
    private final List<Line> lines;
    private final List<Intersection> intersections;
    private final List<CutDTO> currentCuts = new ArrayList<>();
    Set<String> checkedPairs;

    public CncDrawer(CncController controller, List<Line> lines, List<Intersection> intersections) {
        this.controller = controller;
        this.lines = lines;
        this.intersections = intersections;
        this.checkedPairs = new HashSet<>();
    }

    public void draw(Graphics2D g) {
        drawPanel(g);
    }
    public void drawSelectebale(Graphics2D g, int selectedCut, Line highlightedLine, Intersection highlightedIntersection, boolean isDragging){
        drawCuts(g, selectedCut, highlightedLine, isDragging);
        drawIntersections(g, highlightedIntersection);
    }

    private void drawIntersections(Graphics2D g, Intersection highlightedIntersection) {
        for (Intersection intersection : intersections) {
            if (intersection.equals(highlightedIntersection)) {
                intersection.setColor(Color.GREEN);
            } else {
                intersection.setColor(Color.MAGENTA);
            }
            intersection.draw(g);
        }
    }

    private void drawPanel(Graphics g) {
        var dimension = controller.getPanel().getDimension();
        var width = (int) dimension.getWidth();
        var height = (int) dimension.getHeight();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(DrawingPanel.ORIGIN_X, DrawingPanel.ORIGIN_Y - height, width, height);
    }

    private void drawCuts(Graphics2D g, int selectedCut, Line highlightedLine, boolean isDragging) {
        var newCuts = controller.getCuts();

        // Détecter et gérer les suppressions
        if (newCuts.size() < currentCuts.size()) {
            handleDeletions(newCuts);
        }

        // Ajouter uniquement les nouvelles coupes

        if (!isDragging) {
            lines.removeIf(line -> line.getCutId() != null);
            for (CutDTO newCut : newCuts) {
                addCutLine(newCut);
            }
        }

        var invalidCuts = newCuts.stream().filter(nc -> !nc.isValid()).map(CutDTO::getId).toList();
        var cut = selectedCut != -1 && newCuts.size() >= selectedCut + 1 ? newCuts.get(selectedCut) : null;
        // Dessiner toutes les lignes
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            var lineCutId = line.getCutId();
            // En faisant ceci nous ignorant les bordure du panneau qui sont considéré comme des lignes.
            if (cut != null && lineCutId != null && lineCutId.equals(cut.getId())) {
                line.setColor(Color.CYAN); // Met en évidence la ligne sélectionnée
            } else if (line.getCutId() != null && invalidCuts.contains(lineCutId)) {
                line.setColor(Color.RED);
            } else if (line.equals(highlightedLine)) {
                line.setColor(Color.GREEN);
            } else {
                line.setColor(Color.BLUE);
            }

            line.draw(g); // Utilise la méthode de la classe `Line` pour dessiner
        }

        for (int i = 0; i < intersections.size(); i++) {
            Intersection intersection = intersections.get(i);
            intersection.draw(g); // Utilise la méthode de la classe `Line` pour dessiner
        }

        // Mettre à jour la liste des coupes actuelles
        currentCuts.clear();
        currentCuts.addAll(newCuts);
    }

    public void updateIntersections() {
        intersections.clear();
        checkedPairs.clear();

        for (int i = 0; i < lines.size(); i++) {
            Line lineI = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Line lineJ = lines.get(j);

                String pairKey = lineI.getId() + "-" + lineJ.getId();
                if (!checkedPairs.contains(pairKey) && lineI.getType() != lineJ.getType()
                        && isIntersections(lineI, lineJ)) {

                    Point intersectionPoint = calculateIntersection(lineI, lineJ);
                    if (intersectionPoint != null) {
                        checkedPairs.add(pairKey);

                        boolean exists = intersections.stream()
                                .anyMatch(intersection -> intersection.isNear(intersectionPoint));

                        if (!exists) {
                            intersections.add(new Intersection(new Point(intersectionPoint)));
                        }
                    }
                }
            }
        }
    }

    private void handleDeletions(List<CutDTO> newCuts) {
        // Trouver les coupes supprimées
        var removedCuts = new ArrayList<>(currentCuts);
        var newCutsIds = newCuts.stream().map(CutDTO::getId).toList();
        removedCuts.removeIf(cut -> newCutsIds.contains(cut.getId()));
        for (CutDTO removedCut : removedCuts) {
            removeCutLines(removedCut);
        }

        updateIntersections();
    }

    private void removeCutLines(CutDTO cut) {
        // Supprimer toutes les lignes associées à cette coupe
        lines.removeIf(line -> line.getCutId() != null && line.getCutId().equals(cut.getId()));
    }

    public void addCutLine(CutDTO cut) {
        ToolDTO toolDTO = cut.getTool();
        ca.ulaval.glo2004.domain.Tool tool = controller.ToolDTOtoTool(toolDTO);
        double stroke = tool.getWidthCut();

        switch (cut.getType()) {
            case VERTICAL -> addVerticalCut(cut.getPoints(), cut.getId(), stroke, cut.getLabel());
            case HORIZONTAL -> addHorizontalCut(cut.getPoints(), cut.getId(), stroke, cut.getLabel());
            case RECTANGULAR -> addRectangularCut(cut.getPoints(), cut.getId(), stroke, cut.getLabel());
            case L -> addLCut(cut.getPoints(), cut.getId(), stroke, cut.getLabel());
            case BORDER -> borderCut(cut.getPoints(), cut.getId(), stroke, cut.getLabel());
        }
    }

    private void addVerticalCut(List<Point> points, UUID id, double stroke, String label) {
        Point start = points.get(0);
        Point end = points.get(1);
        lines.add(new Line(start.x, start.y, end.x, end.y, Line.LineType.VERTICAL, Color.BLUE, id, stroke, label));
        updateIntersections();
    }

    private void addHorizontalCut(List<Point> points, UUID id, double stroke, String label) {
        Point start = points.get(0);
        Point end = points.get(1);
        lines.add(new Line(start.x, start.y, end.x, end.y, Line.LineType.HORIZONTAL, Color.BLUE, id, stroke, label));
        updateIntersections();
    }

    private void addRectangularCut(List<Point> points, UUID id, double stroke, String label) {
        Point start = points.get(1);
        Point end = points.get(2);

        lines.addAll(getRectangleLines(start, end, id, stroke, label));
        updateIntersections();
    }

    private void addLCut(List<Point> points, UUID id, double stroke, String label) {
        Point start = points.get(0);
        Point end = points.get(1);

        var rectangleLines = getRectangleLines(start, end, id, stroke, label);
        var topLine = rectangleLines.get(0);
        var bottomLine = rectangleLines.get(1);
        var leftLine = rectangleLines.get(2);
        var rightLine = rectangleLines.get(3);

        // Add lines to the list if they do not already exist
        var foundHorizontalLine = false;
        var foundVerticalLine = false;
        if (!containsLine(topLine)) {
            lines.add(topLine);
            foundHorizontalLine = true;
        }
        if (!containsLine(bottomLine) && !foundHorizontalLine) {
            lines.add(bottomLine);
        }
        if (!containsLine(leftLine)) {
            lines.add(leftLine);
            foundVerticalLine = true;
        }
        if (!containsLine(rightLine) && !foundVerticalLine) {
            lines.add(rightLine);
        }

        // Update intersections after adding the lines
        updateIntersections();
    }

    private void borderCut(List<Point> points, UUID id, double stroke, String label) {
        Point start = points.get(0);
        Point end = points.get(1);

        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = end.y;

        lines.add(new Line(x1, y1, x2, y1, Line.LineType.HORIZONTAL, Color.BLUE, id, stroke, label)); // Ligne supérieure
        lines.add(new Line(x2, y1, x2, y2, Line.LineType.VERTICAL, Color.BLUE, id, stroke, label));   // Ligne droite
        lines.add(new Line(x2, y2, x1, y2, Line.LineType.HORIZONTAL, Color.BLUE, id, stroke, label)); // Ligne inférieure
        lines.add(new Line(x1, y2, x1, y1, Line.LineType.VERTICAL, Color.BLUE, id, stroke, label));   // Ligne gauche

        updateIntersections();
    }

    private List<Line> getRectangleLines(Point start, Point end, UUID id, double stroke, String label) {
        // Calculate the 4 corners of the rectangle
        Point topLeft = new Point(start.x, start.y);
        Point topRight = new Point(end.x, start.y);
        Point bottomLeft = new Point(start.x, end.y);
        Point bottomRight = new Point(end.x, end.y);

        var result = new ArrayList<Line>();
        // Horizontal lines
        result.add(new Line(topLeft.x, topLeft.y, topRight.x, topRight.y, Line.LineType.HORIZONTAL, Color.BLUE, id, stroke, label));
        result.add(new Line(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, Line.LineType.HORIZONTAL, Color.BLUE, id, stroke, label));

        // Vertical lines
        result.add(new Line(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, Line.LineType.VERTICAL, Color.BLUE, id, stroke, label));
        result.add(new Line(topRight.x, topRight.y, bottomRight.x, bottomRight.y, Line.LineType.VERTICAL, Color.BLUE, id, stroke, label));

        return result;
    }


    private boolean containsLine(Line other) {
        for (var line : lines) {
            if (line.contains(other)) {
                return true;
            }
        }
        return false;
    }

    private Point calculateIntersection(Line line1, Line line2) {
        if (line1.getType() == Line.LineType.VERTICAL && line2.getType() == Line.LineType.HORIZONTAL) {
            // line1 est verticale, line2 est horizontale
            int x = line1.getX1(); // x constant pour une ligne verticale
            int y = line2.getY1(); // y constant pour une ligne horizontale
            return new Point(x, y);
        } else if (line1.getType() == Line.LineType.HORIZONTAL && line2.getType() == Line.LineType.VERTICAL) {
            // line1 est horizontale, line2 est verticale
            int x = line2.getX1(); // x constant pour une ligne verticale
            int y = line1.getY1(); // y constant pour une ligne horizontale
            return new Point(x, y);
        }

        // Si les types ne correspondent pas, retourner null (devrait être impossible ici)
        return null;
    }

    public void setIntersections() {
        for (int i = 0; i < lines.size(); i++) {
            Line lineI = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Line lineJ = lines.get(j);

                String pairKey = lineI.getId() + "-" + lineJ.getId();
                if (!checkedPairs.contains(pairKey) && lineI.getType() != lineJ.getType()
                        && isIntersections(lineI, lineJ)) {
                    Point intersection = calculateIntersection(lineI, lineJ);
                    checkedPairs.add(pairKey);
                    intersections.add(new Intersection(intersection));
                    System.out.println("Added intersection: " + pairKey);
                } else {
                    System.out.println("Intersection non perpendiculaire entre : " + pairKey);
                }
            }
        }
    }

    public boolean isIntersections(Line line1, Line line2) {
        // Vérifier si les deux segments se croisent
        return areSegmentsPerpendicular(line1, line2);
    }

    private boolean areSegmentsPerpendicular(Line line1, Line line2) {
        // Calcul des vecteurs directeurs
        Point p1 = line1.getStartPoint();
        Point p2 = line1.getEndPoint();
        Point q1 = line2.getStartPoint();
        Point q2 = line2.getEndPoint();

        // Vérifier si les segments se croisent
        if (!doSegmentsIntersect(p1, p2, q1, q2)) {
            return false; // Si les segments ne se croisent pas, ils ne peuvent pas être perpendiculaires
        }

        // Calcul des vecteurs pour les segments
        int v1x = p2.x - p1.x;
        int v1y = p2.y - p1.y;
        int v2x = q2.x - q1.x;
        int v2y = q2.y - q1.y;

        // Produit scalaire
        int dotProduct = v1x * v2x + v1y * v2y;

        return dotProduct == 0; // Si le produit scalaire est nul, ils sont perpendiculaires
    }

    private boolean doSegmentsIntersect(Point p1, Point p2, Point q1, Point q2) {
        // Calculer les orientations
        int o1 = orientation(p1, p2, q1);
        int o2 = orientation(p1, p2, q2);
        int o3 = orientation(q1, q2, p1);
        int o4 = orientation(q1, q2, p2);

        // Cas général : les segments se croisent
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Cas particuliers : vérifier si les points sont colinéaires
        return (o1 == 0 && onSegment(p1, q1, p2)) ||
                (o2 == 0 && onSegment(p1, q2, p2)) ||
                (o3 == 0 && onSegment(q1, p1, q2)) ||
                (o4 == 0 && onSegment(q1, p2, q2));
    }

    // Orientation d'un triplet (p, q, r)
    private int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Colinéaire
        return (val > 0) ? 1 : 2; // Horaire ou anti-horaire
    }

    // Vérifie si le point r est sur le segment [p, q]
    private boolean onSegment(Point p, Point r, Point q) {
        return r.x <= Math.max(p.x, q.x) && r.x >= Math.min(p.x, q.x) &&
                r.y <= Math.max(p.y, q.y) && r.y >= Math.min(p.y, q.y);
    }
}


