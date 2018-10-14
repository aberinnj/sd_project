import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveManager {
    private ArrayList<Move> moveList;


    MoveManager(String base){
        moveList = new ArrayList<Move>();

    }

    public void addMove(Move i){
        moveList.add(i);
    }

    // Users can undo their actions
    public Move getLastMove(){
        return moveList.get(moveList.size()-1);
    }

    // Users can replay games
    public Move returnToMove(int i)
    {
        return moveList.get(i);
    }

    public Move addToMoveManager(BoardManager bm, MoveManager MM, Player[] list, int size, int playerID){
        HashMap<String, Territory> moveMap = new HashMap<String, Territory>();
        HashMap<String, Territory> boardMap = bm.getBoardMap();
        HashMap<Integer, List<String>> playerTerritories = new HashMap<Integer, List<String>>();
        for(String key: boardMap.keySet())
        {
            List<String> neighbors = boardMap.get(key).getNeighbors();
            int occ = boardMap.get(key).getOccupantID();
            boolean isOcc = boardMap.get(key).isOccupied();
            int count = boardMap.get(key).getArmy().getInfantryCount();
            moveMap.put(key, new Territory(isOcc, occ, new Army(count), neighbors));

        }
        for(int i=0; i<size; i++)
        {
            List<String> territoryList = new ArrayList<String>(list[i].getTerritories());
            playerTerritories.put(i, territoryList);
        }
        return new Move(playerID, moveMap, playerTerritories);

    }
}
