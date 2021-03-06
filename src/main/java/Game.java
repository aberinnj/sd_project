import java.util.*;


enum GameState {
    NULL,
    CLAIMING,
    ON_TURN,
    ATTACKING,
    DEFENDING,
    RESULT

}

class Context {
    public String countryFrom;
    public String countryTo;
    public int count1;
    public int count2;

    Context(){
        countryFrom = "";
        countryTo = "";
        count1 = 0;
        count2 = 0;
    }
}

/*////////////////////////////////////////////////////////////////////////////////
Game holds all each game's information and the list of players in the game
todo: check if static is causing problems
*///////////////////////////////////////////////////////////////////////////////*/
class Game extends Observable {
    HashMap<Integer, Player> playerDirectory;
    BoardManager BM;
    ArrayList<Integer> users;
    //ArrayList<Integer> turnPattern;
    int nextTurnUserID;
    int turn;
    String gameID;
    GameState state;
    Context context;
    Messenger messenger;
    Turn currentTurn;
    Loader gameLoader;


    Game() {
        messenger = new Messenger();
        playerDirectory = new HashMap<>();
        BM = new BoardManager();
        users = new ArrayList<>();
        state = GameState.NULL;
        turn = 0;
        nextTurnUserID = 0;
        //state = GameState.QUEUE;
    }

    //funciton to reset the game ID if reloading
    public void setGameID(String id) {
        gameID = id;
    }

    // function to reset the turn if reloading
    public void setTurn(int turn) {
        this.turn = turn;
    }

    // telegram style, gives each players the appropriate number of armies on init
    public void setPlayerList(){
        int size = playerDirectory.size();
        int default_infantry = 0;
        switch(size){
            case 2:
                default_infantry=40;
                break;
            case 3:
                default_infantry=35;
                break;
            case 4:
                default_infantry=30;
                break;
            case 5:
                default_infantry=25;
                break;
            case 6:
                default_infantry=20;
                break;
        }
        for(int a=0; a<size; a++){
            playerDirectory.get(a).addArmies(default_infantry);
        }
    }

    // shuffle players to create random turn order
    public void shufflePlayers() {
        List<Player> valueList = new ArrayList<Player>(playerDirectory.values());
        Collections.shuffle(valueList);
        Iterator<Player> valueIt = valueList.iterator();
        messenger.putMessage("Turn-Order:");
        for(Map.Entry<Integer,Player> e : playerDirectory.entrySet()) {
            Player thisPlayer = valueIt.next();
            messenger.putMessage("@"+thisPlayer.username);
            e.setValue(thisPlayer);
        }
    }

    public void start() {
        shufflePlayers();
        setPlayerList();

        nextTurnUserID = CommandUtils.getFirstPlayer(playerDirectory).id;
    }

    public void addUser(Integer user_id, String username, long chat_id){

        playerDirectory.put(playerDirectory.size(), new Player(user_id, username, chat_id, 0));
        users.add(user_id);

    }


    public void setCurrentTurn(Turn turn) { this.currentTurn = turn;}

    //function to determine it all players have distributed their armies
    public boolean checkArmies() {
        for (int key: playerDirectory.keySet()) {
            if (playerDirectory.get(key).getNumberOfArmies() > 0) return false;
        }
        return true;
    }
}
