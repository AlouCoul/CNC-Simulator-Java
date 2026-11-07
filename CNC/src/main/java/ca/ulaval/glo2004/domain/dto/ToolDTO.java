package ca.ulaval.glo2004.domain.dto;
import ca.ulaval.glo2004.domain.Tool;

import java.util.Objects;
import java.util.UUID;

public class ToolDTO {

    private String name;
    private double widthCut;
    private UUID id;

    public ToolDTO(Tool tool){
        if (tool != null) {
            this.name = tool.getName();
            this.widthCut = tool.getWidthCut();
            this.id = tool.getId();
        }
    }

    public String getName(){
        return name;
    }

    public double getWidthCut(){
        return widthCut;
    }

    public UUID getId(){return id;}

    public String toString(){
        return getName() + ", " + getWidthCut();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ToolDTO toolDTO = (ToolDTO) obj;
        return Objects.equals(id, toolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}