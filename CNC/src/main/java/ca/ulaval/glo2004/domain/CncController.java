package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ForbiddenZoneDTO;
import ca.ulaval.glo2004.domain.dto.PanelDTO;
import ca.ulaval.glo2004.domain.dto.ToolDTO;
import ca.ulaval.glo2004.gui.CutType;
import ca.ulaval.glo2004.gui.SupportType;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.Point;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;


public class CncController {


    private FileCNC fileCNC;
    private CNC cnc;
    private FichierGCODE fichierGCODE; //Trouver peut-être autre chose que de l'appeler ici..
    private HistoryManager historyManager;

    public CncController() {
        cnc = new CNC();
        this.fileCNC = new FileCNC(cnc);
        historyManager = new HistoryManager(cnc);
    }

    public CncController(CNC cnc) {
        this.cnc = cnc;
    }

    public void addCut(int depth, List<Point> points, CutType type, UUID toolId, Point reference) {
        var newReference = reference != null ? reference : null;
        cnc.addCut(depth, points.stream().map(Point::new).toList(), type, toolId, newReference);
        historyManager.saveState(cnc);
    }

    public void selectCut(Point point) {
        cnc.selectCut(point);
    }

    public void editCut(int id, int depth, Point reference, double distance, Dimension dimension, int selectedTool) {
        if (dimension == null) {
            cnc.editCut(id, depth, reference == null ? null : new Point(reference), distance, new Dimension(), selectedTool);
        } else {
            cnc.editCut(id, depth, reference == null ? null : new Point(reference), distance, new Dimension(dimension), selectedTool);
        }
        historyManager.saveState(cnc);
    }

    public void editCut(int id, Point newPoint) {
        if (newPoint == null) {
            return;
        }

        cnc.editCut(id, new Point(newPoint));
        historyManager.saveState(cnc);
    }

    public void deleteCut(int id) {
        cnc.deleteCut(id);
        historyManager.saveState(cnc);
    }

    public PanelDTO getPanel() {
        Panel panel = cnc.getPanel();

        return new PanelDTO(panel);
    }

    public void addPanel(SupportType supportType, Dimension dimension, double depth) {
        cnc.addPanel(supportType, new Dimension(dimension), depth);
        historyManager.saveState(cnc);
    }

    public List<CutDTO> getCuts() {
        return cnc.getCuts();
    }


    public List<ToolDTO> getTools() {
        return cnc.getTools();
    }

    public void addTool(String name, double widthCut) {
        cnc.addTool(name, widthCut);
        historyManager.saveState(cnc);
    }

    public void deleteTool(int position) {
        cnc.deleteTool(position);
        historyManager.saveState(cnc);
    }

    public void editTool(UUID id, String name, double width) {
        cnc.editTool(id, name, width);
        historyManager.saveState(cnc);
    }

    public void undo() {
        CNC previousState = historyManager.undo();
        if (previousState != null) {
            cnc = previousState;
            this.fileCNC.setCnc(cnc);
        } else {
            System.out.println("Undo non disponible.");
        }
    }

    public void redo() {
        CNC nextState = historyManager.redo();
        if (nextState != null) {
            cnc = nextState;
            this.fileCNC.setCnc(cnc);
        } else {
            System.out.println("Redo non disponible.");
        }
    }

    public CNC getCnc() {
        return cnc;
    }

    public ToolDTO ToolTotoolDTO(Tool tool) {
        ToolDTO toolDTO = new ToolDTO(tool);
        return toolDTO;
    }

    public Tool ToolDTOtoTool(ToolDTO tool) {
        String toolName = tool.getName();
        double width_tool = tool.getWidthCut();
        Tool tool_1 = new Tool(toolName, width_tool);
        return tool_1;
    }

    public UUID getToolId(int position) {
        var toolsDTO_list = getTools();
        ToolDTO ToolDTO_ = toolsDTO_list.get(position);
        UUID id_toolDTO = ToolDTO_.getId();
        return id_toolDTO;
    }

    public int getToolPosition(UUID toolId) {
        return cnc.getToolPosition(toolId);
    }

    public void setIsvalid_False(int position) {
        cnc.setIsvalid_False(position);
    }

    public void saveAs(String path, String fileName) {
        fileCNC.saveAs(path, fileName);
    }

    public void save() {
        fileCNC.save();
    }

    public void importFile(String path) {
        cnc = fileCNC.importFile(path);
        historyManager.saveState(cnc);
    }

    public String generateGCODE(String units, String pauseTime, String feedRate) {
        FichierGCODE fichierGCODE = new FichierGCODE();
        if (!Objects.equals(units, "mm") || !Objects.equals(pauseTime, "4") || !Objects.equals(feedRate, "500")) {
            fichierGCODE = new FichierGCODE(units, pauseTime, feedRate);
        }
        List<CutDTO> cuts = cnc.getCuts();
        List<ToolDTO> tools = cnc.getTools();
        PanelDTO panel = getPanel();
        double panelHeight = panel.getDimension().height;
        fichierGCODE.setPanelHeight(panelHeight);
        for (CutDTO cut : cuts) {
            int position = IntStream.range(0, tools.size())
                    .filter(i -> tools.get(i).getId().toString().equals(cut.getTool().getId().toString()))
                    .findFirst()
                    .orElse(-1);
            if (position == -1) {
                throw new IllegalArgumentException("Tool not found");
            }
            System.out.println("Position de l'outil: " + position);
            fichierGCODE.writeCut(cut, panel.getDepth(), position);
        }
        this.fichierGCODE = fichierGCODE;
        return fichierGCODE.getGcode();
    }

    public void exporterFichierGCODE(String path) throws IOException {
        File res = fichierGCODE.exportFile(path, fichierGCODE.getGcode());
    }


    public FileCNC getFile() {
        return fileCNC;
    }

    public String getfilePath() {
        return fileCNC.getFilePath();
    }

    public void setFile(FileCNC fileCNC) {
        this.fileCNC = fileCNC;
    }

    // Ajouter une zone interdite
    public void addForbiddenZone(Point position, int width, int height) {
        cnc.addForbiddenZone(position, width, height);
        historyManager.saveState(cnc);
    }

    // Obtenir toutes les zones interdites
    public List<ForbiddenZoneDTO> getForbiddenZones() {
        return cnc.getForbiddenZones();
    }

    public void editForbiddenZone(int index, Rectangle bounds) {
        cnc.editForbiddenZone(index, bounds);
        historyManager.saveState(cnc);
    }

    // Supprimer une zone interdite
    public void deleteForbiddenZone(int index) {
        cnc.deleteForbiddenZone(index);
        historyManager.saveState(cnc);
    }

    public void saveState() {
        historyManager.saveState(cnc); // Sauvegarder l'état courant
    }

    public void editPanel(int gridSize, boolean showGrid, boolean enableMagnet) {
        cnc.editPanel(gridSize, showGrid, enableMagnet);
        historyManager.saveState(cnc);
    }
}
