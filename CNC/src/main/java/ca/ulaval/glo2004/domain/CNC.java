package ca.ulaval.glo2004.domain;
import ca.ulaval.glo2004.Utils.Utils;
import ca.ulaval.glo2004.domain.dto.CutDTO;
import ca.ulaval.glo2004.domain.dto.ForbiddenZoneDTO;
import ca.ulaval.glo2004.domain.dto.ToolDTO;
import ca.ulaval.glo2004.gui.CutType;
import ca.ulaval.glo2004.gui.SupportType;
import java.io.*;


import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
public class CNC implements Serializable {

    private Panel panel;
    private List<Tool> tools;
    private Tool toolSelected;

    //Constructor CNC qui crée un outil de base.
    public CNC() {
        tools = new ArrayList<>();
        var x = (int) Utils.convertMetersToPixels(1.2192);
        var y = (int)Utils.convertMetersToPixels(0.9144);
        addPanel(SupportType.AIR, new Dimension(x, y), 1);
        Tool tool_base = new Tool("Fraises", 1.5);
        tools.add(tool_base);
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public CNC deepCopy() {
        CNC copy = new CNC();
        copy.panel = this.panel != null ? this.panel.deepCopy() : null;
        copy.tools = new ArrayList<>();
        for (Tool tool : this.tools) {
            copy.tools.add(tool.deepCopy());
        }
        copy.toolSelected = this.toolSelected != null ? this.toolSelected.deepCopy() : null;

        return copy;
    }

    public void addTool(String name, double widthCut) {
        if(tools.size() < 12)
        {
            Tool tool_1 = new Tool(name, widthCut);
            tools.add(tool_1);
        }

    }

    public void addTool(Tool tool) {
        tools.add(tool);
    }

    public void deleteTool(int position) {
        if (tools.size() >= 2) {
            var toolId = tools.get(position).getId();
            tools.remove(position);
            panel.removeToolFromCut(toolId);
        }
    }

    public int getSizeToolsList(){
        return tools.size();
    }

    public Tool getToolSelected(String name) {
        for (Tool tool : tools) {
            if (tool.getName().equals(name)) {
                toolSelected = tool;
                return toolSelected;
            }

        }
        return null;
    }

    public void addCut(int depth, List<Point> points, CutType type, UUID toolId, Point reference){
        var tool = getToolById(toolId);
        panel.addCut(depth, points, type, tool, reference);

    }

    public void selectCut(Point point){

    }

    public List<ToolDTO> getTools() {
        var toolsDTO = new ArrayList<ToolDTO>();
        for (var tool : tools) {
            if(tool != null){
                toolsDTO.add(new ToolDTO(tool));}
        }
        return toolsDTO;
    }

    public Tool getToolById(UUID toolId) {
        for (Tool tool : tools) {
            if (tool != null && tool.getId().equals(toolId)) {
                return tool;
            }
        }

        return null;
    }

    public void editCut(int id, Point newPoint){
        panel.editCut(id, newPoint);
    }

    public void editCut(int id, int depth, Point point, double distance, Dimension dimension, int selectedTool) {
        var tool = selectedTool == -1 ? null : tools.get(selectedTool);
        panel.editCut(id, depth, point, distance, dimension, tool);
    }

    public void deleteCut(int id) {
        panel.deleteCut(id);
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public void addPanel(SupportType supportType, Dimension dimension, double depth) {
        this.panel = new Panel(supportType, dimension, new ArrayList<>(), depth);
        setPanel(panel);
    }

    public List<CutDTO> getCuts() {
        return panel.getCutsDTO();
    }

    public void setIsvalid_False(int position) {
        panel.setIsvalid_False(position);
    }

    public void editTool(UUID id, String name, double width){

        for (Tool tool : tools) {
            if (tool != null && tool.getId().equals(id)) {
                tool.setName(name);
                tool.setWidthCut(width);

            }
        }
    }


    public int getToolPosition(UUID toolId) {
        for (int i = 0; i < tools.size(); i++) {
            if (tools.get(i).getId().equals(toolId)) {
                return i;
            }
        }

        return -1;
    }
    // Ajouter une nouvelle zone interdite
    public void addForbiddenZone(Point position, int width, int height) {
       panel.addForbiddenZone(position, width, height);
    }

    // Récupérer toutes les zones interdites
    public List<ForbiddenZoneDTO> getForbiddenZones() {
        return panel.getForbiddenZones();
    }

    // Supprimer une zone interdite
    public void deleteForbiddenZone(int index) {
       panel.deleteForbiddenZone(index);
    }

    public void editForbiddenZone(int index, Rectangle bounds) {
        panel.editForbiddenZone(index, bounds);
    }

    public void editPanel(int gridSize, boolean showGrid, boolean enableMagnet) {
        panel.setGridSize(gridSize, showGrid, enableMagnet);
    }
}
