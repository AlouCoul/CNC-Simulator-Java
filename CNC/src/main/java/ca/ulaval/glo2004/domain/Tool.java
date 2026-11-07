package ca.ulaval.glo2004.domain;
import java.io.Serializable;
import java.util.UUID;


public class Tool implements Serializable {
    private String name;
    private double widthCut;
    private UUID id;

    public Tool(String name, double widthCut) {
        this.name = name;
        this.widthCut = widthCut;
        this.id = UUID.randomUUID();
    }

    public Tool deepCopy() {
        var tool = new Tool(this.name, this.widthCut);
        tool.setId(this.id);
        return tool;
    }

    public String getName(){
        return name;
    }

    public double getWidthCut(){
        return widthCut;
    }

    public UUID getId(){
        return id;
    }

    public void setWidthCut(double widthCut){
        this.widthCut = widthCut;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setId(UUID id){
        this.id = id;
    }

}

