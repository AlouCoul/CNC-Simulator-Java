package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.Cut;
import com.google.gson.*;

import ca.ulaval.glo2004.gui.CutType;
import com.google.gson.*;
import java.lang.reflect.Type;

public class CutTypeAdapter implements JsonDeserializer<Cut>, JsonSerializer<Cut> {

    @Override
    public JsonElement serialize(Cut cut, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(cut).getAsJsonObject();
        jsonObject.addProperty("type", cut.getType().toString());
        return jsonObject;
    }

    @Override
    public Cut deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString(); // Récupère le type de coupe


        return switch (typeName) {
            case "HORIZONTAL" -> context.deserialize(jsonObject, HorizontalCut.class);
            case "VERTICAL" -> context.deserialize(jsonObject, VerticalCut.class);
            case "L" -> context.deserialize(jsonObject, LCut.class);
            case "RECTANGULAR" -> context.deserialize(jsonObject, RectangularCut.class);
            case "BORDER" -> context.deserialize(jsonObject, BorderCut.class);
            default -> throw new JsonParseException("Type de coupe inconnu : " + typeName);
        };
    }
}
