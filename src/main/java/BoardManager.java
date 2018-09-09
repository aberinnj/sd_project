import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**/
class Territory {
    static boolean occupied;
    static int occupiedByID;
    static List<String> neighbors;

    /**/
    Territory(boolean i, int j, List<String> k)
    {
        occupied = i;
        occupiedByID = j;
        neighbors = k;
    }

    public void setTerritory(boolean i, int j){
        occupied = i;
        occupiedByID = j;
    }
}

/**/
public class BoardManager {

    private static HashMap<String, Territory> thisMap;

    /**/
    BoardManager(String path){
        thisMap = new HashMap<String, Territory>();
        try {
            FileReader json = new FileReader(path);
            JsonParser parser = new JsonParser();

            Gson gson = new Gson();
            JsonObject rootObj = parser.parse(json).getAsJsonObject();
            JsonArray mapArray = rootObj.getAsJsonArray("map");

            for(JsonElement mapItem: mapArray){
                JsonObject territoryObject = mapItem.getAsJsonObject();
                String territoryName = territoryObject.get("name").getAsString();
                JsonArray neighborsObject = territoryObject.get("neighbors").getAsJsonArray();

                Type listType= new TypeToken<List<String>>() {}.getType();
                List<String> territoryNeighbors = gson.fromJson(neighborsObject, listType);
                thisMap.put(territoryName, new Territory(false, -1, territoryNeighbors));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }




}
