import java.util.*;

/*////////////////////////////////////////////////////////////////////////////////
Continent Class
*///////////////////////////////////////////////////////////////////////////////*/
class Continent{
    List<String> FullContinent;

    Continent(String[] k) {
        FullContinent = new ArrayList<String>();
        FullContinent.addAll(Arrays.asList(k));
    }
}
/*////////////////////////////////////////////////////////////////////////////////
BoardManager class HANDLES TERRITORY, CONTINENT and DECK

*///////////////////////////////////////////////////////////////////////////////*/
public class BoardManager {
    HashMap<String, Territory> boardMap;
    HashMap<String, Continent> continentsMap;
    Deck gameDeck;
    int completeSets;

    // Initializes the BoardManager variables and should be testable after
    BoardManager(){
        continentsMap = new HashMap<String,Continent>();
        boardMap = new HashMap<String, Territory>();

        boardMap.put("ALASKA", new Territory(new String[]{"KAMCHATKA", "NORTH WEST TERRITORY", "ALBERTA"}, "ALASKA"));
        boardMap.put("NORTH WEST TERRITORY", new Territory(new String[]{"ALASKA", "ALBERTA", "ONTARIO", "GREENLAND"}, "NORTH WEST TERRITORY"));
        boardMap.put("GREENLAND", new Territory(new String[]{"NORTH WEST TERRITORY", "ONTARIO", "QUEBEC", "ICELAND"},"GREENLAND"));
        boardMap.put("ALBERTA", new Territory(new String[]{"ALASKA", "NORTH WEST TERRITORY", "ONTARIO", "WESTERN UNITED STATES"}, "ALBERTA"));
        boardMap.put("ONTARIO", new Territory(new String[]{"ALBERTA", "NORTH WEST TERRITORY", "QUEBEC", "GREENLAND", "WESTERN UNITED STATES", "EASTERN UNITED STATES"}, "ONTARIO"));
        boardMap.put("QUEBEC", new Territory(new String[]{"ONTARIO", "EASTERN UNITED STATES", "GREENLAND"}, "QUEBEC"));
        boardMap.put("WESTERN UNITED STATES", new Territory(new String[]{"ALBERTA", "ONTARIO", "EASTERN UNITED STATES", "CENTRAL AMERICA"}, "WESTERN UNITED STATES"));
        boardMap.put("EASTERN UNITED STATES", new Territory(new String[]{"WESTERN UNITED STATES", "ONTARIO", "QUEBEC", "CENTRAL AMERICA"}, "EASTERN UNITED STATES"));
        boardMap.put("CENTRAL AMERICA", new Territory(new String[]{"WESTERN UNITED STATES", "EASTERN UNITED STATES", "VENEZUELA"}, "CENTRAL AMERICA"));
        boardMap.put("VENEZUELA", new Territory(new String[]{"CENTRAL AMERICA", "PERU", "BRAZIL"}, "VENEZUELA"));
        boardMap.put("PERU", new Territory(new String[]{"VENEZUELA", "BRAZIL", "ARGENTINA"}, "PERU"));
        boardMap.put("BRAZIL", new Territory(new String[]{"VENEZUELA", "PERU", "ARGENTINA", "NORTH AFRICA"}, "BRAZIL"));
        boardMap.put("ARGENTINA", new Territory(new String[]{"PERU", "BRAZIL"}, "ARGENTINA"));
        boardMap.put("NORTH AFRICA", new Territory(new String[]{"BRAZIL", "SOUTHERN EUROPE", "EGYPT", "EAST AFRICA", "CONGO"}, "NORTH AFRICA"));
        boardMap.put("CONGO", new Territory(new String[]{"NORTH AFRICA", "EAST AFRICA", "SOUTH AFRICA"}, "CONGO"));
        boardMap.put("EGYPT", new Territory(new String[]{"NORTH AFRICA", "SOUTHERN EUROPE", "MIDDLE EAST", "EAST AFRICA"}, "EGYPT"));
        boardMap.put("EAST AFRICA", new Territory(new String[]{"NORTH AFRICA", "EGYPT", "MIDDLE EAST", "CONGO", "SOUTH AFRICA", "MADAGASCAR"}, "EAST AFRICA"));
        boardMap.put("SOUTH AFRICA", new Territory(new String[]{"CONGO", "EAST AFRICA", "MADAGASCAR"}, "SOUTH AFRICA"));
        boardMap.put("MADAGASCAR", new Territory(new String[]{"SOUTH AFRICA", "EAST AFRICA"}, "MADAGASCAR"));
        boardMap.put("MIDDLE EAST", new Territory(new String[]{"EGYPT", "EAST AFRICA", "SOUTHERN EUROPE", "UKRAINE", "AFGHANISTAN", "INDIA"}, "MIDDLE EAST"));
        boardMap.put("INDIA", new Territory(new String[]{"MIDDLE EAST", "AFGHANISTAN", "CHINA", "SIAM"}, "INDIA"));
        boardMap.put("SIAM", new Territory(new String[]{"INDIA", "CHINA", "INDONESIA"}, "SIAM"));
        boardMap.put("INDONESIA", new Territory(new String[]{"SIAM", "NEW GUINEA", "WESTERN AUSTRALIA"}, "INDONESIA"));
        boardMap.put("WESTERN AUSTRALIA", new Territory(new String[]{"INDONESIA", "NEW GUINEA", "EASTERN AUSTRALIA"}, "WESTERN AUSTRALIA"));
        boardMap.put("EASTERN AUSTRALIA", new Territory(new String[]{"WESTERN AUSTRALIA", "NEW GUINEA"}, "EASTERN AUSTRALIA"));
        boardMap.put("NEW GUINEA", new Territory(new String[]{"INDONESIA", "WESTERN AUSTRALIA", "EASTERN AUSTRALIA"}, "NEW GUINEA"));
        boardMap.put("CHINA", new Territory(new String[]{"SIAM", "INDIA", "AFGHANISTAN", "URAL", "SIBERIA", "MONGOLIA"}, "CHINA"));
        boardMap.put("AFGHANISTAN", new Territory(new String[]{"MIDDLE EAST", "INDIA", "CHINA", "URAL", "UKRAINE"}, "AFGHANISTAN"));
        boardMap.put("URAL", new Territory(new String[]{"UKRAINE", "SIBERIA", "CHINA", "AFGHANISTAN"}, "URAL"));
        boardMap.put("SIBERIA", new Territory(new String[]{"URAL", "CHINA", "MONGOLIA", "IRKUTSK", "YAKUTSK"}, "SIBERIA"));
        boardMap.put("YAKUTSK", new Territory(new String[]{"SIBERIA", "IRKUTSK", "KAMCHATKA"}, "YAKUTSK"));
        boardMap.put("KAMCHATKA", new Territory(new String[]{"YAKUTSK", "IRKUTSK", "ALASKA", "JAPAN", "MONGOLIA"}, "KAMCHATKA"));
        boardMap.put("IRKUTSK", new Territory(new String[]{"SIBERIA", "YAKUTSK", "KAMCHATKA", "MONGOLIA"}, "IRKUTSK"));
        boardMap.put("MONGOLIA", new Territory(new String[]{"SIBERIA", "IRKUTSK", "KAMCHATKA", "JAPAN", "CHINA"}, "MONGOLIA"));
        boardMap.put("JAPAN", new Territory(new String[]{"KAMCHATKA", "MONGOLIA"}, "JAPAN"));
        boardMap.put("UKRAINE", new Territory(new String[]{"SCANDINAVIA", "URAL", "AFGHANISTAN", "MIDDLE EAST", "SOUTHERN EUROPE", "NORTHERN EUROPE"}, "UKRAINE"));
        boardMap.put("SCANDINAVIA", new Territory(new String[]{"UKRAINE", "NORTHERN EUROPE", "GREAT BRITAIN", "ICELAND"}, "SCANDINAVIA"));
        boardMap.put("ICELAND", new Territory(new String[]{"GREENLAND", "SCANDINAVIA", "GREAT BRITAIN"}, "ICELAND"));
        boardMap.put("GREAT BRITAIN", new Territory(new String[]{"ICELAND", "SCANDINAVIA", "NORTHERN EUROPE", "WESTERN EUROPE"}, "GREAT BRITAIN"));
        boardMap.put("NORTHERN EUROPE", new Territory(new String[]{"GREAT BRITAIN", "SCANDINAVIA", "UKRAINE", "SOUTHERN EUROPE", "WESTERN EUROPE"}, "NORTHERN EUROPE"));
        boardMap.put("SOUTHERN EUROPE", new Territory(new String[]{"WESTERN EUROPE", "NORTHERN EUROPE", "UKRAINE", "MIDDLE EAST", "EGYPT", "NORTH AFRICA"}, "SOUTHERN EUROPE"));
        boardMap.put("WESTERN EUROPE", new Territory(new String[]{"GREAT BRITAIN", "NORTHERN EUROPE", "SOUTHERN EUROPE", "NORTH AFRICA"}, "WESTERN EUROPE"));

        continentsMap.put("NORTH AMERICA", new Continent(new String[]{"ALASKA", "NORTH WEST TERRITORY", "ALBERTA","ONTARIO","QUEBEC","GREENLAND", "WESTERN UNITED STATES", "EASTERN UNITED STATES", "CENTRAL AMERICA"}));
        continentsMap.put("SOUTH AMERICA", new Continent(new String[]{"VENEZUELA", "BRAZIL", "PERU", "ARGENTINA"}));
        continentsMap.put("EUROPE", new Continent(new String[]{"WESTERN EUROPE", "GREAT BRITAIN", "ICELAND", "SCANDINAVIA", "NORTHERN EUROPE", "SOUTHERN EUROPE", "UKRAINE"}));
        continentsMap.put("AFRICA", new Continent(new String[]{"NORTH AFRICA", "EGYPT", "CONGO", "EAST AFRICA", "SOUTH AFRICA", "MADAGASCAR"}));
        continentsMap.put("ASIA", new Continent(new String[]{"SIAM", "INDIA", "AFGHANISTAN", "URAL", "SIBERIA", "MONGOLIA", "CHINA", "MIDDLE EAST", "JAPAN", "YAKUTSK", "IRKUTSK", "KAMCHATKA"}));
        continentsMap.put("AUSTRALIA", new Continent(new String[]{"WESTERN AUSTRALIA", "INDONESIA", "EASTERN AUSTRALIA", "NEW GUINEA"}));

        gameDeck = new Deck();

        completeSets = 0;
    }

    // re-initialize
    BoardManager(HashMap<String, Territory> k, Deck l, int m){
        boardMap = k;
        gameDeck = l;
        completeSets = m;

        continentsMap = new HashMap<String, Continent>();
        continentsMap.put("NORTH AMERICA", new Continent(new String[]{"ALASKA", "NORTH WEST TERRITORY", "ALBERTA","ONTARIO","QUEBEC","GREENLAND", "WESTERN UNITED STATES", "EASTERN UNITED STATES", "CENTRAL AMERICA"}));
        continentsMap.put("SOUTH AMERICA", new Continent(new String[]{"VENEZUELA", "BRAZIL", "PERU", "ARGENTINA"}));
        continentsMap.put("EUROPE", new Continent(new String[]{"WESTERN EUROPE", "GREAT BRITAIN", "ICELAND", "SCANDINAVIA", "NORTHERN EUROPE", "SOUTHERN EUROPE", "UKRAINE"}));
        continentsMap.put("AFRICA", new Continent(new String[]{"NORTH AFRICA", "EGYPT", "CONGO", "EAST AFRICA", "SOUTH AFRICA", "MADAGASCAR"}));
        continentsMap.put("ASIA", new Continent(new String[]{"SIAM", "INDIA", "AFGHANISTAN", "URAL", "SIBERIA", "MONGOLIA", "CHINA", "MIDDLE EAST", "JAPAN", "YAKUTSK", "IRKUTSK", "KAMCHATKA"}));
        continentsMap.put("AUSTRALIA", new Continent(new String[]{"WESTERN AUSTRALIA", "INDONESIA", "EASTERN AUSTRALIA", "NEW GUINEA"}));
    }

    // queryTerritory consults with the boardMap for territories
    public String queryTerritory(String query, String type, Player p, String origin, Game thisGame) {
        //System.out.println(query);
        String country = thisGame.messenger.getMessage();
        System.out.println("finding " + country );


        // ALL queries for a territory requires a country to exist
        if (!boardMap.containsKey(country)) {
            //System.out.println("Error: Territory not found. ");
            System.out.println("finding "+ country + " " + boardMap.containsKey(country));
            thisGame.messenger.putMessage("Error: Territory not found. ");
            return null;
        }

        // strengthening requires a country to be owned by the player
        if (type.equals("STRENGTHEN")) {
            if(boardMap.get(country).getOccupantID() != p.getId()) {
                // System.out.println("Error: Territory not owned. ");
                thisGame.messenger.putMessage("Error: Territory not owned. ");
                return null;
            }

        // initialization requires a country selected to be free
        } else if (type.equals("INITIALIZE")) {
            if(boardMap.get(country).isOccupied()){
                //System.out.println("Error: Territory already occupied. ");
                thisGame.messenger.putMessage("Error: Territory already occupied. ");
                return null;
            }

        // attacking from a country requires the country to be a player's territory, the army count be > 1 and the enemy neighbors to be > 1
        } else if (type.equals("ATTACK_FROM")) {
            if(boardMap.get(country).getOccupantID() != p.getId()) {
                //System.out.println("Error: Territory not owned. ");
                thisGame.messenger.putMessage("Error: Territory not owned. ");
                return null;
            } else if ( getOccupantCount(country) <= 1) {
                //System.out.println("Error: Territory not valid to attack from. ");
                thisGame.messenger.putMessage("Error: Territory not valid to attack from. ");
                return null;
            } else if ( getAllAdjacentEnemyTerritories(p.getId(), country).size() == 0) {
                //System.out.println("Error: Territory seems to have no neighbors that can be attacked. ");
                thisGame.messenger.putMessage("Error: Territory seems to have no neighbors that can be attacked. ");
                return null;
            }

        // selecting an enemy country requires the country to be an enemy's territory and adjacent to an origin
        } else if (type.equals("ATTACK")) {
            if(boardMap.get(country).getOccupantID() == p.getId()) {
                //System.out.println("Error: Territory is not an enemy territory. ");
                thisGame.messenger.putMessage("Error: Territory is not an enemy territory. ");
                return null;
            } else if (!getAllAdjacentEnemyTerritories(p.getId(), origin).contains(country)) {
                //System.out.println("Error: Territory is not adjacent to origin. ");
                thisGame.messenger.putMessage("Error: Territory is not adjacent to origin. ");
                return null;
            }


        // fortifying from a country requires the country to be player's territory, the army count to be more than one and the friendly neighbors to be > 0
        } else if (type.equals("FORTIFY_FROM")) {
            if(boardMap.get(country).getOccupantID() != p.getId()) {
                //System.out.println("Error: Territory not owned. ");
                thisGame.messenger.putMessage("Error: Territory not owned. ");
                return null;
            } else if ( boardMap.get(country).getArmy().getInfantryCount() <= 1) {
                //System.out.println("Error: Territory not valid to fortify from. ");
                thisGame.messenger.putMessage("Error: Territory not valid to fortify from. ");
                return null;
            } else if ( getAllAdjacentTerritories(p.getId(), country).size() == 0) {
                //System.out.println("Error: Territory seems to have no neighbors to fortify. ");
                thisGame.messenger.putMessage("Error: Territory seems to have no neighbors to fortify. ");
                return null;
            }

        // selecting a country to fortify requires the country to be the player's territory and adjacent to an origin
        } else if (type.equals("FORTIFY")) {
            if(boardMap.get(country).getOccupantID() != p.getId()) {
                //System.out.println("Error: Territory is not an territory. ");
                thisGame.messenger.putMessage("Error: Territory is not an territory. ");
                return null;
            } else if (!getNeighborsOf(origin).contains(country)) {
                //System.out.println("Error: Territory is not adjacent to origin. ");
                thisGame.messenger.putMessage("Error: Territory is not adjacent to origin. ");
                return null;
            }
        } else {
            return null;
        }
        return country;
    }

    // queryCount consults with territory-occupantCount
    public int queryCount(String query, String type, Player p, String origin, Game thisGame) throws InterruptedException {
        //System.out.println(query);
        thisGame.messenger.putMessage(query);
        int count;
        try{
            //count = Integer.parseInt(scanner.nextLine());
            count = Integer.parseInt(thisGame.messenger.getMessage());
        } catch (NumberFormatException e){
            // System.out.println("Error: input is not a number. ");
            thisGame.messenger.putMessage("Error: input is not a number. ");
            return 0;
        }

        // for attacks, the number of dice that can be rolled is 1, 2 or 3, the count of occupants must also be more than 1
        if(type.equals("ATTACK")) {
            if(getOccupantCount(origin) <= (count)) {
                //System.out.println("Error: " + origin + " does not have enough armies to roll " + count + " dice. ");
                thisGame.messenger.putMessage("Error: " + origin + " does not have enough armies to roll " + count + " dice. ");
                return 0;
            } else if (count > 3) {
                //System.out.println("Warning: The maximum is 3 dice. Defaulting dice roll to 3." );
                thisGame.messenger.putMessage("Warning: The maximum is 3 dice. Defaulting dice roll to 3.");
                return 3;
            }

        } else if (type.equals("DEFEND")) {
            if(getOccupantCount(origin) < count) {
                //System.out.println("Error: " + origin + " does not have enough armies to roll " + count + " dice. ");
                thisGame.messenger.putMessage("Error: " + origin + " does not have enough armies to roll " + count + " dice. ");
                return 0;
            } else if (count > 2) {
                //System.out.println("Warning: The maximum is 2 dice. Defaulting dice roll to 2." );
                thisGame.messenger.putMessage("Warning: The maximum is 2 dice. Defaulting dice roll to 2.");
                return 2;
            }
        } else if (type.equals("FORTIFY")) {
            if(getOccupantCount(origin) <= count) {
                //System.out.println("Error: There are not enough occupants in " + origin + " to transfer " + count + " armies. ");
                thisGame.messenger.putMessage("Error: There are not enough occupants in " + origin + " to transfer " + count + " armies. ");
                return 0;
            }

        } else {
            return 0;
        }
        return count;
    }

    // simply returns boardmap
    public HashMap<String, Territory> getBoardMap(){
        return boardMap;
    }

    // simply returns gamedeck
    public Deck getGameDeck() {return gameDeck;}

    // re-implements the deck for when loading a game
    public void newDeck(List<Card> deck) {
        gameDeck = (Deck) deck;
    }

    // used for initialization in constructor above
    public List<String> getContinentsMap(String name) {
        return continentsMap.get(name).FullContinent;
    }

    // Displays free territories
    public List<String> getFreeTerritories(){
        List<String> listing = new ArrayList<String>();

        for(HashMap.Entry<String, Territory> country: boardMap.entrySet()){
            if(!boardMap.get(country.getKey()).isOccupied()){
                listing.add(country.getKey());
            }
        }
        return listing;
    }

    // Gets a player's territories, but most useful in getting territories a player can attack from
    public List<String> getAbleTerritories(Player p, boolean attacking) {
        List<String> listing = new ArrayList<String>();
        for(String country: p.getTerritories()) {
            if(attacking) {
                if (getOccupantCount(country) > 1)
                    listing.add(country);
            } else{
                listing.add(country);
            }
        }
        return listing;
    }

    // Get All
    public List<String> getAllAdjacentTerritories(int id, String countryOfOrigin) {
        List<String> listing = new ArrayList<String>();
        for(String country: getNeighborsOf(countryOfOrigin)){
            if(boardMap.get(country).getOccupantID() == id)
                listing.add(country);
        }
        return listing;
    }

    public List<String> getAllAdjacentEnemyTerritories(int id, String countryOfOrigin) {
        List<String> listing = new ArrayList<String>();
         for(String country: getNeighborsOf(countryOfOrigin)){
            if(boardMap.get(country).getOccupantID() != id)
                listing.add(country);
        }
        return listing;
    }

    public List<String> getNeighborsOf(String country) {
        return boardMap.get(country).getNeighbors();
    }

    // Takes care of setting territories for initialization // can also be used after winning a territory
    // Note: Attacking takes an amount of armies and places them into player's placeholder Army
    public void initializeTerritory(Player p, String territory, int armyCount ){
        boardMap.get(territory).setTerritory(true, p.getId(), new Army(armyCount));
        boardMap.get(territory).addObserver(p);
        p.addTerritories(territory);
        p.loseArmies(armyCount);
    }

    public void strengthenTerritory(Player p, String territory, int armyCount) {
        addOccupantsTo(territory, armyCount);
        p.loseArmies(armyCount);
    }

    public void fortifyTerritory(String origin, String territory, int armyCount) {
        removeOccupantsFrom(origin, armyCount);
        addOccupantsTo(territory, armyCount);
    }

    /*////////////////////////////////////////////////////////////////////////////////
    Method returns occupant count of a territory as int
    *///////////////////////////////////////////////////////////////////////////////*/
    public int getOccupantCount(String country){
        return boardMap.get(country).getArmy().getInfantryCount();
    }

    /*////////////////////////////////////////////////////////////////////////////////
    *///////////////////////////////////////////////////////////////////////////////*/
    public void addOccupantsTo(String country, int count) {
        boardMap.get(country).addOccupants(count, ArmyType.INFANTRY);
    }

    public void removeOccupantsFrom(String country, int count) {
        boardMap.get(country).loseOccupants(count, ArmyType.INFANTRY);
    }
}
