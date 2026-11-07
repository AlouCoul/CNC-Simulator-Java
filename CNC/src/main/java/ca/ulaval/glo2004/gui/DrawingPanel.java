package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ForbiddenZoneDTO;
import ca.ulaval.glo2004.domain.dto.PanelDTO;
import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.gui.drawing.CncDrawer;
import ca.ulaval.glo2004.gui.drawing.Intersection;
import ca.ulaval.glo2004.gui.drawing.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DrawingPanel extends JPanel implements MouseWheelListener, MouseListener {

    public static int GRID_SIZE = 160;
    public static final int PANEL_WIDTH = (int) Utils.convertFeetToPixels(10);
    public static final int PANEL_HEIGHT = (int) Utils.convertFeetToPixels(5);
    public static final int ORIGIN_X = 0;
    public static final double ZOOM_MULTIPLIER = 1.1;
    public static final int ORIGIN_Y = PANEL_HEIGHT;

    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;
    private List<Point> points;
    private static final int RESIZE_MARGIN = 60;
    private boolean resizingZone = false;
    private Point resizeStart = null;
    private boolean draggingZone = false;
    private Point dragStart = null;
    private ForbiddenZoneDTO activeZone = null;
    private int activeZoneIndex = -1;
    private boolean canDrag = false;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point lastMousePosition;
    private int selectedCut = -1;

    public List<Line> lines;
    public List<Intersection> intersections;
    public Point cutReference = new Point();

    Set<String> checkedPairs;
    private Line highlightedLine = null;

    private static final int LINE_DETECTION_MARGIN = 5;
    private CncDrawer cncDrawer;
    private Intersection highlightedIntersection;
    private Point initialClick;

    //for grid
    private boolean isGridEnabled;
    private int gridSquare;
    private boolean isMagnetic = false;
    private List<Point> gridIntersections;

    public DrawingPanel() {
    }

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.lines = new ArrayList<>();
        this.intersections = new ArrayList<>();
        gridIntersections = new ArrayList<>();
        this.checkedPairs = new HashSet<>();
        this.cncDrawer = new CncDrawer(mainWindow.controller, this.lines, this.intersections);
        initialDimension = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        points = new ArrayList<>();
        this.addMouseWheelListener(this);
        this.addMouseListener(this);

        var mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
                int mouseX = (int) ((e.getX() - offsetX) / zoomFactor);
                int mouseY = (int) ((e.getY() - offsetY) / zoomFactor);
                Point mousePoint = new Point(mouseX, mouseY);
                if (selectedCut != -1) {
                    canDrag = true;
                    initialClick = e.getPoint();
                } else if (mainWindow.isProhibitedAreaMode) {
                    // Ajouter une nouvelle zone interdite via le contrôleur
                    mainWindow.controller.addForbiddenZone(mousePoint, 50, 50);
                    mainWindow.isProhibitedAreaMode = false;
                    repaint();
                } else {
                    // Détection des zones existantes pour déplacer ou redimensionner
                    var zones = mainWindow.controller.getForbiddenZones();
                    for (int i = 0; i < zones.size(); i++) {
                        var zone = zones.get(i);
                        if (SwingUtilities.isRightMouseButton(e) && zone.contains(mousePoint)) {
                            mainWindow.controller.deleteForbiddenZone(i); // Remove the zone by index
                            repaint();
                            return;
                        }

                        if (zone.isOnResizeEdge(mousePoint, RESIZE_MARGIN)) {
                            activeZone = zone;
                            activeZoneIndex = i;
                            resizingZone = true;
                            resizeStart = mousePoint;
                            return;
                        }

                        if (zone.contains(mousePoint)) {
                            activeZone = zone;
                            activeZoneIndex = i;
                            draggingZone = true;
                            dragStart = mousePoint;
                            return;
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                double adjustedClickX = e.getX() - offsetX;
                double adjustedClickY = e.getY() - offsetY;
                int adjustedX = (int) (adjustedClickX / zoomFactor);
                int adjustedY = (int) (adjustedClickY / zoomFactor);

                Point newMousePosition = new Point(adjustedX, adjustedY);
                if (highlightClosestLine(newMousePosition)) {
                    System.out.println(newMousePosition);
                } else if (highlightedClosestIntersection(newMousePosition)) {
                    System.out.println("Intersection: " + newMousePosition);
                }

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (canDrag) {
                    moveCutLines(e.getPoint());
                    repaint();
                } else if (activeZone != null) {
                    int mouseX = (int) ((e.getX() - offsetX) / zoomFactor);
                    int mouseY = (int) ((e.getY() - offsetY) / zoomFactor);
                    Point mousePoint = new Point(mouseX, mouseY);
                    if (resizingZone) {
                        // Redimensionner la zone
                        int newWidth = activeZone.getBounds().width + (mousePoint.x - resizeStart.x);
                        int newHeight = activeZone.getBounds().height + (mousePoint.y - resizeStart.y);
                        activeZone.setSize(newWidth, newHeight);
                        resizeStart = mousePoint;
                    } else if (draggingZone) {
                        // Déplacer la zone
                        int deltaX = mousePoint.x - dragStart.x;
                        int deltaY = mousePoint.y - dragStart.y;
                        Point newPosition = new Point(activeZone.getBounds().x + deltaX, activeZone.getBounds().y + deltaY);
                        activeZone.setPosition(newPosition);
                        dragStart = mousePoint;
                    }
                } else {
                    var deltaX = e.getX() - lastMousePosition.x;
                    var deltaY = e.getY() - lastMousePosition.y;
                    offsetX += deltaX;
                    offsetY += deltaY;
                    lastMousePosition = e.getPoint();
                }

                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                CutType selectedType = mainWindow.cutTypePressed;
                PanelDTO panel = mainWindow.controller.getPanel();

                Dimension panelDimension = panel.getDimension();
                int panelWidth = (int) (panelDimension.width * zoomFactor);
                int panelHeight = (int) (panelDimension.height * zoomFactor);

                double adjustedClickX = e.getX() - offsetX;
                double adjustedClickY = e.getY() - offsetY;
                int adjustedX = (int) (adjustedClickX / zoomFactor);
                int adjustedY = (int) (adjustedClickY / zoomFactor);

                if ((adjustedClickX < 0 || adjustedClickX >= panelWidth) || (adjustedClickY <= (ORIGIN_Y * zoomFactor - panelHeight) || adjustedClickY > ORIGIN_Y * zoomFactor)) {
                    Point adjustedPoint = checkMouseNearAxes(new Point(adjustedX, adjustedY));
                    adjustedX = adjustedPoint.x;
                    adjustedY = adjustedPoint.y;
                    System.out.println("Votre clic est en dehors du panneau mais est redirigé à" + adjustedX + " " + adjustedY);
                }
                Point newMousePosition = new Point(adjustedX, adjustedY);
                if (isMagnetic && isGridEnabled){
                    newMousePosition = checkMouseNearGridIntersection(newMousePosition);
                    adjustedX = newMousePosition.x;
                    adjustedY = newMousePosition.y;
                }

                if (selectedType != CutType.NULL && selectedType != null) {
                    switch (selectedType) {
                        case CutType.VERTICAL:
                            verticalCutActions(adjustedX, adjustedY);
                            break;

                        case CutType.HORIZONTAL:
                            horizontalCutActions(adjustedX, adjustedY);
                            break;

                        case CutType.RECTANGULAR:
                            rectangleCutActions(adjustedX, adjustedY);
                            break;

                        case CutType.L:
                            LCutActions(adjustedX, adjustedY);
                            break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canDrag) {
                    canDrag = false;
                    double adjustedClickX = e.getX() - offsetX;
                    double adjustedClickY = e.getY() - offsetY;
                    int adjustedX = (int) (adjustedClickX / zoomFactor);
                    int adjustedY = (int) (adjustedClickY / zoomFactor);
                    Point newMousePosition = new Point(adjustedX, adjustedY);
                    if (isMagnetic && isGridEnabled){
                        newMousePosition = checkMouseNearGridIntersection(newMousePosition);
                    }
                    mainWindow.editDraggedCut(newMousePosition);
                    repaint();
                    return;
                }
                if (activeZone != null && activeZoneIndex != -1){
                    mainWindow.controller.editForbiddenZone(activeZoneIndex, activeZone.getBounds());
                    resizingZone = false;
                    draggingZone = false;
                    activeZone = null;
                    activeZoneIndex = -1;
                    repaint();
                }
            }
        };

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    private boolean isOnResizeEdge(Rectangle zone, int mouseX, int mouseY) {
        return Math.abs(mouseX - (zone.x + zone.width)) < RESIZE_MARGIN
                && Math.abs(mouseY - (zone.y + zone.height)) < RESIZE_MARGIN;
    }


    private boolean isNearCorner(Point mouse, Rectangle rect, int margin) {
        return Math.abs(mouse.x - rect.x) <= margin && Math.abs(mouse.y - rect.y) <= margin
                || Math.abs(mouse.x - (rect.x + rect.width)) <= margin && Math.abs(mouse.y - rect.y) <= margin
                || Math.abs(mouse.x - rect.x) <= margin && Math.abs(mouse.y - (rect.y + rect.height)) <= margin
                || Math.abs(mouse.x - (rect.x + rect.width)) <= margin && Math.abs(mouse.y - (rect.y + rect.height)) <= margin;
    }

    private void moveCutLines(Point point) {
        int deltaX = point.x - initialClick.x;
        int deltaY = point.y - initialClick.y;

        deltaX = (int) (deltaX / zoomFactor);
        deltaY = (int) (deltaY / zoomFactor);

        var cutLines = getCutLines(selectedCut);
        for (Line line : lines) {
            if (cutLines.contains(line)) {
                line.setX1(line.getX1() + deltaX);
                line.setY1(line.getY1() + deltaY);
                line.setX2(line.getX2() + deltaX);
                line.setY2(line.getY2() + deltaY);
            }
        }

        initialClick = point;
    }

    private List<Line> getCutLines(int selectedCut) {
        var cuts = mainWindow.controller.getCuts();
        if (cuts == null || cuts.isEmpty()) {
            return new ArrayList<>();
        }
        var cut = cuts.get(selectedCut);
        var lines = new ArrayList<Line>();
        for (var line : this.lines) {
            if (line.getCutId() != null && line.getCutId().equals(cut.getId())) {
                lines.add(line);
            }
        }

        return lines;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mainWindow != null) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            var g2d = (Graphics2D) g.create();

            g2d.scale(zoomFactor, zoomFactor);

            g2d.translate(offsetX / zoomFactor, offsetY / zoomFactor);


            drawCncBoard(g2d);
            cncDrawer.draw(g2d);
            if (selectedCut >= 0) {
                mainWindow.selectCut(selectedCut);
            }
            CutType selectedType = mainWindow.cutTypePressed;

            if (isGridEnabled) {
                drawGrid(g2d); // Dessinez la grille
            }
            cncDrawer.drawSelectebale(g2d, selectedCut, highlightedLine, highlightedIntersection, canDrag);
            if (highlightedLine != null && highlightedLine.getLabel() != null && !highlightedLine.getLabel().isEmpty()) {
                int rectWidth = 360;
                int rectHeight = 160;
                Point labelPosition = highlightedLine.getLabelPosition();
                Font baseFont = new Font("Arial", Font.BOLD, 72);
                g2d.setFont(baseFont);
                FontMetrics metrics = g2d.getFontMetrics();
                int textWidth = metrics.stringWidth(highlightedLine.getLabel());
                int textHeight = metrics.getHeight();
                float scaleFactor = Math.min((float) rectWidth / textWidth, (float) rectHeight / textHeight);
                int adjustedFontSize = (int) (baseFont.getSize() * scaleFactor);
                g2d.setFont(baseFont.deriveFont((float) adjustedFontSize));
                metrics = g2d.getFontMetrics();
                textHeight = metrics.getAscent();
                g2d.setColor(new Color(255, 255, 153));
                g2d.fillRect(labelPosition.x, labelPosition.y - rectHeight, rectWidth, rectHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(labelPosition.x, labelPosition.y - rectHeight, rectWidth, rectHeight);
                int textX = labelPosition.x + (rectWidth - metrics.stringWidth(highlightedLine.getLabel())) / 2;
                int textY = labelPosition.y - rectHeight + (rectHeight + textHeight) / 2;
                g2d.drawString(highlightedLine.getLabel(), textX, textY);
            }

            if (selectedType != CutType.NULL && selectedType != null) {
                if (points.size() == 1) {
                    drawPointClicked(points.get(0), g2d);
                } else if (points.size() >= 2) {
                    switch (selectedType) {
                        case VERTICAL, HORIZONTAL -> drawRegularCutPreview(g2d);
//                        case RECTANGULAR -> drawRectangleCutPreview(g2d);
//                        case L -> drawLCutPreview(g2d);
                        case BORDER -> drawBorderCutPreview(g2d);
                    }

                }
            }
            if (mainWindow.enableManualSelect && points.size() > 1) {
                List<CutDTO> cuts = mainWindow.controller.getCuts();
                Point clickedPoint = points.get(0);
                for (int i = 0; i < cuts.size(); i++) {
                    CutDTO cut = cuts.get(i);
                    List<Point> cutPoints = cut.getPoints();
                    Point cutPoint1 = cutPoints.get(0);
                    Point cutPoint2 = cutPoints.get(1);

                    boolean isSelectingCut = Math.abs(clickedPoint.x - cutPoint1.x) <= 1
                            && Math.abs(clickedPoint.x - cutPoint2.x) <= 1
                            && clickedPoint.y >= Math.min(cutPoint1.y, cutPoint2.y)
                            && clickedPoint.y <= Math.max(cutPoint1.y, cutPoint2.y);

                    if (isSelectingCut) {
                        selectedCut = i;
                        break;
                    }
                }
            }

            //cncDrawer.draw(g2d, selectedCut, highlightedLine, highlightedIntersection, canDrag);
            if (selectedCut >= 0) {
                mainWindow.selectCut(selectedCut);
            }

            var panel = mainWindow.controller.getPanel();
            this.gridSquare = panel.getGridSize();
            this.isGridEnabled = panel.isShowGrid();
            this.isMagnetic = panel.isEnableMagnet();
            mainWindow.gridConfigPanel.update(this.gridSquare, this.isGridEnabled, this.isMagnetic);
            for (var zone : mainWindow.controller.getForbiddenZones()) {
                Rectangle bounds = zone.getBounds();
                g2d.setColor(new Color(255, 0, 0, 100)); // Rouge semi-transparent
                g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g2d.setColor(Color.RED);
                g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            g2d.dispose();
        }
    }

    public void setAxis() {
        lines.clear();
        PanelDTO panel = mainWindow.controller.getPanel();
        int panelWidth = panel.getDimension().width;
        int panelHeight = panel.getDimension().height;

        // Ligne horizontale supérieure
        int x1 = ORIGIN_X;
        int y1 = ORIGIN_Y - panelHeight;
        int x2 = panelWidth;
        int y2 = ORIGIN_Y - panelHeight;
        lines.add(new Line(x1, y1, x2, y2, Line.LineType.HORIZONTAL, Color.BLUE, null, ""));
        System.out.println("Ligne horizontale supérieure : (" + x1 + ", " + y1 + ") -> (" + x2 + ", " + y2 + ")");

        // Ligne verticale gauche
        x1 = ORIGIN_X;
        y1 = ORIGIN_Y - panelHeight;
        x2 = ORIGIN_X;
        y2 = ORIGIN_Y;
        lines.add(new Line(x1, y1, x2, y2, Line.LineType.VERTICAL, Color.BLUE, null, ""));
        System.out.println("Ligne verticale gauche : (" + x1 + ", " + y1 + ") -> (" + x2 + ", " + y2 + ")");

        // Ligne horizontale inférieure
        x1 = ORIGIN_X;
        y1 = ORIGIN_Y;
        x2 = panelWidth;
        y2 = ORIGIN_Y;
        lines.add(new Line(x1, y1, x2, y2, Line.LineType.HORIZONTAL, Color.BLUE, null, ""));
        System.out.println("Ligne horizontale inférieure : (" + x1 + ", " + y1 + ") -> (" + x2 + ", " + y2 + ")");

        // Ligne verticale droite
        x1 = panelWidth;
        y1 = ORIGIN_Y - panelHeight;
        x2 = panelWidth;
        y2 = ORIGIN_Y;
        lines.add(new Line(x1, y1, x2, y2, Line.LineType.VERTICAL, Color.BLUE, null, ""));
        System.out.println("Ligne verticale droite : (" + x1 + ", " + y1 + ") -> (" + x2 + ", " + y2 + ")");
        cncDrawer.setIntersections();
        repaint();
    }

    private boolean areSegmentsPerpendicular(Line line1, Line line2) {
        // Calcul des vecteurs directeurs
        Point p1 = line1.getStartPoint();
        Point p2 = line1.getEndPoint();
        Point q1 = line2.getStartPoint();
        Point q2 = line2.getEndPoint();

        int v1x = p2.x - p1.x;
        int v1y = p2.y - p1.y;
        int v2x = q2.x - q1.x;
        int v2y = q2.y - q1.y;

        // Produit scalaire
        int dotProduct = v1x * v2x + v1y * v2y;

        return dotProduct == 0; // Si le produit scalaire est nul, ils sont perpendiculaires
    }

//    private boolean doSegmentsIntersect(Point p1, Point p2, Point q1, Point q2) {
//        return (ccw(p1, q1, q2) != ccw(p2, q1, q2)) && (ccw(p1, p2, q1) != ccw(p1, p2, q2));
//    }
//
//    private boolean ccw(Point a, Point b, Point c) {
//        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
//    }

    private boolean highlightClosestLine(Point mousePoint) {
        int margin = LINE_DETECTION_MARGIN;
        highlightedLine = null;
        for (Line line : lines) {
            if (line.isNear(mousePoint, margin)) {
                highlightedLine = line;
                return true;
            }
        }
        return false;
    }

    private boolean highlightedClosestIntersection(Point mousePoint) {
        highlightedIntersection = null;
        for (Intersection intersection : intersections) {
            if (intersection.isNear(mousePoint)) {
                highlightedIntersection = intersection;
                return true;
            }
        }
        return false;
    }


    public List<Point> getPoints() {
        return points;
    }

    public void setOffset(int x, int y) {
        offsetX = x;
        offsetY = y;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void setInitialDimension(Dimension newDimension) {
        this.initialDimension = newDimension;
        this.setPreferredSize(newDimension);
        revalidate();
    }


    public void setSelectedCut(int selectedCut) {
        this.selectedCut = selectedCut;
    }

    public void clearPoints() {
        points.clear();
        highlightedLine = null;
        highlightedIntersection = null;
        selectedCut = -1;
        repaint();
    }

    private Point checkMouseNearAxes(Point mousePoint) {
        Point res = new Point();
        int dist = Integer.MAX_VALUE; // Initialisation avec une valeur très élevée

        for (Line line : lines) {
            int temp = 0;

            if (Line.LineType.VERTICAL == line.getType()) {
                temp = Math.abs(mousePoint.x - line.getX1());
                if (temp < dist) {
                    dist = temp;
                    res = new Point(line.getX1(), mousePoint.y); // Redirige vers l'axe vertical
                }

            } else if (Line.LineType.HORIZONTAL == line.getType()) {
                temp = Math.abs(mousePoint.y - line.getY1());
                if (temp < dist) {
                    dist = temp;
                    res = new Point(mousePoint.x, line.getY1()); // Redirige vers l'axe horizontal
                }
            }
        }
        return res;
    }

    private Point checkMouseNearIntersection(Point mousePoint) {
        Point res = new Point();
        double dist = Integer.MAX_VALUE;

        for (Intersection intersection : intersections) {
            double x = mousePoint.x - intersection.getX();
            double y = mousePoint.y - intersection.getY();
            double temp = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            if (temp < dist) {
                dist = temp;
                res = new Point(intersection.getX(), intersection.getY());
            }
        }
        return res;
    }

    private Point checkMouseNearGridIntersection(Point mousePoint) {
        Point res = new Point();
        double dist = Integer.MAX_VALUE;

        for (Point intersection : gridIntersections) {
            double x = mousePoint.x - intersection.getX();
            double y = mousePoint.y - intersection.getY();
            double temp = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            if (temp < dist) {
                dist = temp;
                res = new Point(intersection.x, intersection.y);
            }
        }
        return res;
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(new Color(0, 0, 0)); // Couleur de la grille

        // Calculez la taille de la grille ajustée en fonction du zoom
        int adjustedGridSize = (int) (gridSquare / zoomFactor);

        gridIntersections.clear();
        // Dessinez les lignes verticales
        for (int x = 0; x <= PANEL_WIDTH; x += gridSquare) {
            g.setColor(Color.BLACK);

            g.drawLine(x, 0, x, PANEL_HEIGHT);

            for (int y = 0; y <= PANEL_HEIGHT; y += gridSquare) {
                gridIntersections.add(new Point(x, y));
            }
        }

        for (int y = 0; y <= PANEL_HEIGHT; y += gridSquare) {
            g.setColor(Color.BLACK);
            g.drawLine(0, y, PANEL_WIDTH, y);
        }
        System.out.println(gridIntersections.size());
    }

    private void drawCncBoard(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
    }

    public void drawRegularCutPreview(Graphics2D g) {
        Point startPoint = points.get(0);
        Point endPoint = points.get(1);
        g.setColor(Color.blue);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    public void drawRectangleCutPreview(Graphics2D g) {
        if (points.size() < 3) return;

        Point reference = points.get(0);
        Point second = points.get(1);
        Point third = points.get(2);

        // Calcul des coins opposés
        int x1 = reference.x;
        int y1 = reference.y;
        int x2 = third.x;
        int y2 = second.y;

        g.setColor(Color.BLUE);

        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x2, y1, x2, y2);
        g.drawLine(x2, y2, x1, y2);
        g.drawLine(x1, y2, x1, y1);
    }


    public void addCutLine(Point startPoint, Point endPoint, Line.LineType type) {
        lines.add(new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y, type, Color.BLACK, null, ""));
        repaint();
    }


    public void drawLCutPreview(Graphics2D g) {
        Point start = points.get(0);
        Point end = points.get(1);
        g.setColor(Color.blue);
        if (start.y < end.y) {
            g.drawLine(start.x, end.y, end.x, end.y);
        } else {
            g.drawLine(start.x, start.y, end.x, start.y);
        }
        if (start.x < end.x) {
            g.drawLine(end.x, end.y, end.x, start.y);
        } else {
            g.drawLine(start.x, end.y, start.x, start.y);
        }
    }

    public void drawBorderCutPreview(Graphics2D g) {
        Point startPoint = points.get(0);
        Point endPoint = points.get(1);

        int x1 = startPoint.x;
        int y1 = startPoint.y;
        int x2 = endPoint.x;
        int y2 = endPoint.y;

        g.setColor(Color.blue);

        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x2, y1, x2, y2);
        g.drawLine(x2, y2, x1, y2);
        g.drawLine(x1, y2, x1, y1);


    }

    public void verticalCutActions(int adjustedX, int adjustedY) {
        Point clickedPoint = new Point(adjustedX, adjustedY);

        if (points.size() < 2) {
            if (points.isEmpty()) {
                // Premier point - Choisir le point basé sur la ligne verticale ou un point proche
                if (highlightedLine != null && highlightedLine.getType() == Line.LineType.VERTICAL) {
                    clickedPoint = new Point(highlightedLine.getX1(), lines.get(0).getY1());
                    points.add(clickedPoint);
                }
            } else {
                clickedPoint = alignPointWithLine(clickedPoint, lines.get(2)); // Aligne avec la ligne horizontale
                points.add(clickedPoint);
            }

        } else {
            // Réinitialisation des points si plus de 2 points sont ajoutés
            System.out.println("Désélection de la coupe. Réinitialisation des points.");
            cutReference = null;
            points.clear();
        }

        if (points.size() == 2) {
            // Aligner le premier et le deuxième point sur le même axe horizontal (comme la coupe verticale)
            cutReference = new Point(points.get(0).x, points.get(0).y);
            points.get(0).x = points.get(1).x;
            var selectedPoints = new ArrayList<>(points);
            selectedPoints.add(cutReference);
            mainWindow.setCutPoints(selectedPoints); // Sécuriser avec une copie
        }

        repaint();
    }

    // Méthode pour vérifier si le clic est près d'une ligne verticale (logique personnalisée)
    private Point checkMouseNearVerticalLine(Point clickedPoint) {
        // Logique pour vérifier la proximité avec la ligne verticale (ex. vérifier les coordonnées ajustées)
        return new Point(clickedPoint.x, lines.get(0).getY1()); // Exemple: Positionne le point sur la ligne horizontale en Y
    }

    // Méthode pour aligner le point avec une ligne (ici la ligne horizontale)
    private Point alignPointWithLine(Point clickedPoint, Line referenceLine) {
        // Aligne le point avec la ligne horizontale spécifiée
        return new Point(clickedPoint.x, referenceLine.getY1());
    }

    public void horizontalCutActions(int adjustedX, int adjustedY) {
        Point clickedPoint = new Point(adjustedX, adjustedY);

        if (points.size() < 2) {
            if (points.isEmpty()) {
                // Premier point - Choisir le point basé sur la ligne horizontale ou un point proche
                if (highlightedLine != null && highlightedLine.getType() == Line.LineType.HORIZONTAL) {
                    clickedPoint = new Point(lines.get(1).getX1(), highlightedLine.getY1());
                    points.add(clickedPoint);
                }
            } else {
                // Deuxième point - Aligner avec la ligne et la position du premier point
                clickedPoint = alignPointWithHorizontalLine(clickedPoint, lines.get(3)); // Aligne avec la ligne horizontale
                points.add(clickedPoint);
            }

        } else {
            // Réinitialisation si plus de 2 points sont ajoutés
            System.out.println("Désélection de la coupe. Réinitialisation des points.");
            cutReference = null;
            points.clear();
        }

        if (points.size() == 2) {
            // Aligner les points sur le même axe vertical
            cutReference = new Point(points.get(0).x, points.get(0).y);
            points.get(0).y = points.get(1).y;
            var selectedPoints = new ArrayList<>(points);
            selectedPoints.add(cutReference);
            mainWindow.setCutPoints(selectedPoints);
        }

        repaint();
    }

    // Méthode pour vérifier si le clic est près d'une ligne horizontale (logique personnalisée)
    private Point checkMouseNearHorizontalLine(Point clickedPoint) {
        return new Point(lines.get(1).getX1(), clickedPoint.y);
    }

    // Méthode pour aligner le point avec une ligne horizontale
    private Point alignPointWithHorizontalLine(Point clickedPoint, Line referenceLine) {

        return new Point(referenceLine.getX1(), clickedPoint.y);
    }


    public void rectangleCutActions(int adjustedX, int adjustedY) {
        Point clickedPoint = new Point(adjustedX, adjustedY);

        if (points.size() < 3) {
            if (points.isEmpty()) {
                clickedPoint = highlightedIntersection != null
                        ? new Point(highlightedIntersection.getX(), highlightedIntersection.getY())
                        : checkMouseNearIntersection(clickedPoint);
            }
            points.add(clickedPoint);
        } else {
            cutReference = null;
            System.out.println("Désélection de la coupe. Réinitialisation des points.");
            points.clear();
        }

        if (points.size() == 3) {
            cutReference = new Point(points.getFirst());
            var selectedPoints = new ArrayList<Point>();
            selectedPoints.add(points.get(1));
            selectedPoints.add(points.get(2));
            selectedPoints.add(cutReference);
            mainWindow.setCutPoints(selectedPoints);
        }

        repaint();
    }

    private Point alignPointWithAxes(List<Point> points, Point temp) {
        Point reference = points.getFirst();
        return new Point(
                temp.x == reference.x ? reference.x : temp.x,
                temp.y == reference.y ? reference.y : temp.y
        );
    }

    public void LCutActions(int adjustedX, int adjustedY) {
        Point clickedPoint = new Point(adjustedX, adjustedY);
        if (points.size() < 2) {
            System.out.println("Point ajouté: " + clickedPoint);
            System.out.println("Taille de la liste des points: " + points.size());
            if (points.isEmpty()) {
                clickedPoint = highlightedIntersection != null
                        ? new Point(highlightedIntersection.getX(), highlightedIntersection.getY())
                        : checkMouseNearIntersection(clickedPoint);
            }

            points.add(clickedPoint);
        } else {
            System.out.println("Désélection de la coupe. Réinitialisation des points.");
            cutReference = null;
            points.clear();
        }
        if (points.size() == 2) {
            cutReference = new Point(points.getFirst());
            var selectedPoints = new ArrayList<Point>();
            selectedPoints.add(points.get(0));
            selectedPoints.add(points.get(1));
            selectedPoints.add(cutReference);
            mainWindow.setCutPoints(selectedPoints);
        }
        repaint();
    }

    public void setBorderCut(double widthMm, double lengthMm) {
        // Convertir les dimensions données en millimètres en pixels
        int cutWidthPx = (int) widthMm;
        int cutLengthPx = (int) lengthMm;

        // Obtenir les dimensions du panneau
        PanelDTO panel = mainWindow.controller.getPanel();
        int panelWidthPx = panel.getDimension().width;
        int panelHeightPx = panel.getDimension().height;

        // Vérifier que les dimensions de la coupe ne dépassent pas les dimensions du panneau
        if (cutWidthPx > panelWidthPx || cutLengthPx > panelHeightPx) {
            System.out.println("Erreur : Les dimensions de la coupe dépassent les limites du panneau.");
            return;
        }

        // Calculer les points du rectangle coupé, centré dans le panneau
        int x1 = ORIGIN_X + (panelWidthPx - cutWidthPx) / 2;
        int y1 = ORIGIN_Y - panelHeightPx + (panelHeightPx - cutLengthPx) / 2;
        int x2 = x1 + cutWidthPx;
        int y2 = y1 + cutLengthPx;

        // Points de la coupe (en bas à gauche et en haut à droite)
        Point bottomLeft = new Point(x1, y1);
        Point topRight = new Point(x2, y2);

        // Afficher les points pour validation
        System.out.println("Point inférieur gauche : (" + bottomLeft.x + ", " + bottomLeft.y + ")");
        System.out.println("Point supérieur droit : (" + topRight.x + ", " + topRight.y + ")");

        // Ajouter les points au tableau et mettre à jour la fenêtre principale
        points.add(bottomLeft);
        points.add(topRight);
    }

    public void addBorderCut(double widthMm, double lengthMm) {
        points.clear();
        setBorderCut(widthMm, lengthMm);
        sendPoints();
    }

    public void sendPoints() {
        mainWindow.setCutPoints(new ArrayList<>(points));
        points.clear();
    }

    public List<Point> getNewCutCoordinates(CutDTO cut, double newX, double newY) {
        var deltaX = newX - cut.getPoints().getFirst().getX();
        var deltaY = newY - cut.getPoints().getFirst().getY();
        var points = new ArrayList<Point>();
        for (var point : cut.getPoints()) {
            var x = (int) (point.getX() + deltaX);
            var y = (int) (point.getY() + deltaY);
            var newPoint = new Point(x, y);
            if (!isCutInBounds(newPoint)) {
                return null;
            }
            points.add(newPoint);
        }

        return points;
    }

    @Override
    public Dimension getPreferredSize() {
        return initialDimension;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double notches = e.getPreciseWheelRotation();
        var oldZoomFactor = zoomFactor;
        if (notches < 0) {
            zoomFactor *= ZOOM_MULTIPLIER;
        } else {
            zoomFactor /= ZOOM_MULTIPLIER;
        }

        var mousePosition = e.getPoint();
        var mouseXBeforeZoom = (mousePosition.x - offsetX) / oldZoomFactor;
        var mouseYBeforeZoom = (mousePosition.y - offsetY) / oldZoomFactor;
        offsetX = (double) (mousePosition.x - mouseXBeforeZoom * zoomFactor);
        offsetY = (double) (mousePosition.y - mouseYBeforeZoom * zoomFactor);
        revalidate();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private void drawPointClicked(Point point, Graphics2D g) {
        int width = 20;
        int height = 20;
        Color color = Color.decode("#0000FF");
        g.setColor(color);
        g.fillOval(point.x - (width / 2), point.y - (height / 2), width, height);
    }

    private boolean isCutInBounds(Point point) {
        var pointAx = point.getX();
        var pointAy = point.getY();
        var panelDimension = mainWindow.controller.getPanel().getDimension();
        var panelWidth = panelDimension.getWidth();
        var panelHeight = panelDimension.getHeight();

        return (pointAx >= 0 && pointAx <= panelWidth) &&
                (pointAy <= DrawingPanel.ORIGIN_Y && pointAy >= DrawingPanel.ORIGIN_Y - panelHeight);
    }

    //la grille
    public void setGridEnabled(boolean enabled) {
        isGridEnabled = enabled;
        mainWindow.controller.editPanel(this.gridSquare, this.isGridEnabled, isMagnetic);
        repaint();
    }


    public boolean isGridEnabled() {
        return isGridEnabled;
    }

    public void setGridSize(double gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("La taille de la grille doit être supérieure à 0.");
        }
        this.gridSquare =(int) gridSize;
        mainWindow.controller.editPanel(this.gridSquare, this.isGridEnabled, isMagnetic);
        this.repaint();
    }

    public boolean isMagnetic() {
        return isMagnetic;
    }

    public void setMagnetic(boolean magnetic) {
        this.isMagnetic = magnetic;
        mainWindow.controller.editPanel(this.gridSquare, this.isGridEnabled, isMagnetic);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getGridSize() {
        return this.gridSquare;
    }
}
