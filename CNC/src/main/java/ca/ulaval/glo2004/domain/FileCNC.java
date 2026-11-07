package ca.ulaval.glo2004.domain;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileCNC {
    private String filePath;
    private CNC cnc;



    public FileCNC(CNC cnc) {
        this.cnc = cnc;
    }

    public void save() {
        saveToFile(filePath);
    }

    public void saveAs(String path, String fileName) {
        String filePath = path + java.io.File.separator + fileName + ".json";
        setFilePath(filePath);
        saveToFile(filePath);
    }

    public CNC importFile(String filePath) {
        CNC cnc = null;
        try (FileReader fileReader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CNC.class, new CNCTypeAdapter())
                    .registerTypeAdapter(Cut.class, new CutTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            cnc = gson.fromJson(fileReader, CNC.class);
            this.cnc = cnc;
            setFilePath(filePath);
            System.out.println("Les données de ce path ont été lues: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cnc;
    }

    private void saveToFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, false);) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CNC.class, new CNCTypeAdapter())
                    .registerTypeAdapter(Cut.class, new CutTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(cnc);
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public CNC getCnc() {
        return cnc;
    }


    public void setCnc(CNC cnc) {
        this.cnc = cnc;
    }
}
