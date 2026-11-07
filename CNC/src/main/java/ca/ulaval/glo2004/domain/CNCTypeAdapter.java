package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.gui.CutType;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CNCTypeAdapter implements JsonDeserializer<CNC>, JsonSerializer<CNC> {

    @Override
    public JsonElement serialize(CNC cnc, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();


        jsonObject.add("panel", context.serialize(cnc.getPanel()));


        jsonObject.add("tools", context.serialize(cnc.getTools()));

        if (cnc.getPanel() != null) {
            JsonArray cutsArray = new JsonArray();
            for (Cut cut : cnc.getPanel().getCuts()) {
                cutsArray.add(context.serialize(cut));
            }
            jsonObject.add("cuts", cutsArray);
        }

        return jsonObject;
    }

    @Override
    public CNC deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        CNC cnc = new CNC();
        cnc.setTools(new ArrayList<>());

        if (jsonObject.has("panel") && !jsonObject.get("panel").isJsonNull()) {
            Panel panel = context.deserialize(jsonObject.get("panel"), Panel.class);
            cnc.setPanel(panel);
        }


        if (jsonObject.has("tools") && jsonObject.get("tools").isJsonArray()) {
            JsonArray toolsArray = jsonObject.getAsJsonArray("tools");
            for (JsonElement toolElement : toolsArray) {
                Tool tool = context.deserialize(toolElement, Tool.class);
                cnc.addTool(tool);
            }
        }
    /*
        // Désérialisation des coupes
        if (jsonObject.has("cuts") && jsonObject.get("cuts").isJsonArray()) {
            JsonArray cutsArray = jsonObject.getAsJsonArray("cuts");
            if (cnc.getPanel() != null) {
                for (JsonElement cutElement : cutsArray) {
                    Cut cut = context.deserialize(cutElement, Cut.class);
                    cnc.getPanel().addCut(
                            cut.getDepth(),
                            cut.getPoints(),
                            cut.getType(),
                            cut.getTool(),
                            cut.getReference()
                    );
                }
            }
        }
    */
        return cnc;
    }
}

