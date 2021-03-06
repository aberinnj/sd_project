/*////////////////////////////////////////////////////////////////////////////////

*///////////////////////////////////////////////////////////////////////////////*/

import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Responses {

    public static String onStart(){
        return "Welcome! To play risk, you can either do the following:" +
                "\n/create your own session for this chat, if the chat is not playing" +
                "\n/join if the chat is playing and there's enough players." +
                "\n/help to view all the available commands.";

    }

    public static String onListAllGames(){
        if (_GameMaster.gamesListing.isEmpty()){
            return "There are no current sessions. /create to create your own session.";
        }
        else {
            String res = "The following game sessions have been found.\n";
            for(String game_id: _GameMaster.gamesListing.keySet()){

                res += _GameMaster.gamesListing.get(game_id).playerDirectory.size();
                res += " player(s) \t";
                res += game_id;

                res += "\n";
            }
            return res;
        }
    }

    public static String onListMyGames(int user_id){
        if (_GameMaster.gamesListing.isEmpty()){
            return "There are no current sessions. /create to create your own session.";
        }
        else {
            String res = "";
            boolean gamesFound = false;
            for(String id: _GameMaster.gamesListing.keySet()){
                if (_GameMaster.gamesListing.get(id).playerDirectory.containsKey(user_id)) {
                    res += _GameMaster.gamesListing.get(id).playerDirectory.size();
                    res += " player(s) \t";
                    res += id;
                    res += "\n";
                    gamesFound = true;
                }
            }
            if(gamesFound)
                return "The following are your games: \n" + res;
            else {
                return "No games are found.";
            }
        }
    }

    public static String onJoin(ChatInput in, int user_id, String username, Long chat_id ) {
        String context;
        if(in.getArgs().size() > 0) {
            context = in.getArgs().get(0);

            if (!_GameMaster.gamesListing.containsKey(context)) {
                return "The game does not exist.";
            } else if (_GameMaster.gamesListing.get(context).playerDirectory.containsKey(user_id)) {
                return "You are already in this game.";
            }
            //else if (_GameMaster.gamesListing.get(context).state != GameState.QUEUE) {
            //    return "This game is no longer accepting players.";
            //}
            else {
                _GameMaster.allPlayersAndTheirGames.put(user_id, context);
                _GameMaster.gamesListing.get(context).addUser(user_id, username, chat_id);

                return "You have successfully joined.";
            }
        }
        else {
            return "You did not provide a gameID";
        }
    }

    public static String onCreate(int user_id, String gameID, String username, long chat_id){
        if(_GameMaster.allPlayersAndTheirGames.containsKey(user_id)){
            return "@"+username + " sorry! You are already playing a game.";
        }
        else {
            Game game = new Game();// create new game
            game.setGameID(gameID);// give it the ID
            _GameMaster.gamesListing.put(gameID, game);
            _GameMaster.gamesListing.get(gameID).addUser(user_id, username, chat_id);

            _GameMaster.allPlayersAndTheirGames.put(user_id, gameID);

            //_GameMaster.gamesListing.get(gameID).addObserver(_GameMaster.kineticEntity);
            return "Creating a new game session. \nGameID: " + gameID;
        }
    }

    public static String onHelp(){
        return "Risk has the following commands available: \n" +
                "/create to create a new instance of the game\n" +
                "/join <gameID> to select an available session\n" +
                "/listAllGames to list all available game sessions\n" +
                "/listMyGames to list all your game sessions\n" +
                "/getStatus to get the status on the chat's current game\n" +
                "/load to replay a game.";
    }

    public static String onSkipReinforce(Game game){
        String msg = "";
        while(!CommandUtils.isReinforcingOver(game))
        {
            Player nextPlayer = CommandUtils.getPlayer(game);
            if(nextPlayer.getNumberOfArmies() > 0)
            {
                String terr = nextPlayer.getTerritories().get(nextPlayer.getNumberOfArmies() % nextPlayer.getTerritories().size());
                msg +=  nextPlayer.username + " reinforces " + terr + "\n";
                game.BM.strengthenTerritory(nextPlayer, terr, 1);
                game.turn++;

            }
        }

        game.nextTurnUserID = CommandUtils.getFirstPlayer(game.playerDirectory).id;
        return msg;
    }

    public static String onSkipClaim(Game game)
    {
        String msg = "";
        ArrayList<Integer> tempListing = new ArrayList<>();
        tempListing.addAll(game.playerDirectory.keySet());
        for(String terr: game.BM.getFreeTerritories())
        {
            Player player = game.playerDirectory.get(tempListing.get(game.turn % game.playerDirectory.size()));
            game.BM.initializeTerritory(player, terr, 1);
            msg += (player.username + " chose " + terr + "\n");
            game.turn += 1;

            Player nextNextPlayer = CommandUtils.getPlayer(game);
            game.nextTurnUserID = nextNextPlayer.id;
        }

        game.nextTurnUserID = CommandUtils.getFirstPlayer(game.playerDirectory).id;
        return msg;
    }

    public static String onPick(ChatInput in, Integer user_id, Game game ){
        if(user_id == game.nextTurnUserID)
        {
            Player player = CommandUtils.getPlayer(game);

            String tempTerritory = String.join(" ", in.getArgs());
            if(tempTerritory.equals(""))
                return "You did not put a country to claim.";
            else if(!game.BM.getFreeTerritories().contains(tempTerritory))
            {
                return "This territory has already been claimed.";
            }
            else {
                // SUCCESS
                game.BM.initializeTerritory(player, tempTerritory, 1);
                String out = "@" + player.username + " chose " + tempTerritory + ".\n";
                game.turn += 1;


                if(game.BM.getFreeTerritories().size() != 0)
                {
                    Player nextPlayer = CommandUtils.getPlayer(game);
                    out += "\nIt is now player @" + nextPlayer.username + "'s turn\n";
                    out += "The following territories are still available\n";
                    List<String> territories = game.BM.getFreeTerritories();
                    for (String territory : territories) {
                        out += (territory + "\n");
                    }
                    game.nextTurnUserID = nextPlayer.id;
                } else {
                    ArrayList<Integer> users = new ArrayList<Integer>();
                    users.addAll(game.playerDirectory.keySet());
                    game.nextTurnUserID = game.playerDirectory.get(users.get(0)).id;
                }
                return out;
            }


        } else {
            return "Uh Oh! It is not your turn player#" + user_id + ", it is player#"+game.nextTurnUserID+ "'s turn.";
        }
    }

    public static String onReinforce(ChatInput in, Integer user_id, Game game){
        String out;
        Player player = CommandUtils.getPlayer(game);
        if(player.getNumberOfArmies() == 0)
        {
            return "You already have dispatched all available armies";
        }
        else if(user_id == game.nextTurnUserID)
        {
            String tempTerritory = String.join(" ", in.getArgs());
            if (tempTerritory.equals(""))
            {
                return "You did not put a country to reinforce.";
            }
            else if (!player.getTerritories().contains(tempTerritory))
            {
                return "You do not own this territory.";
            }
            else
            {
                out = "@"+player.username + " reinforces " + tempTerritory;
                game.BM.strengthenTerritory(player, tempTerritory, 1);
                out += "\n@"+player.username + " you have " + player.getNumberOfArmies() + " armies left\n";

                return out;
            }
        } else {
            return "Uh Oh! It is not your turn player #" + user_id + ", it is player #"+ game.nextTurnUserID+ "'s turn.";
        }
    }

    public static String onFollowUpJoin(String context){
        _GameMaster.gamesListing.get(context).start();
        String res = "Order:\n";
        for (int user_id : _GameMaster.gamesListing.get(context).playerDirectory.keySet()) {
            res += "@";
            res += _GameMaster.gamesListing.get(context).playerDirectory.get(user_id).username;
            res += "\n";
        }

        res += "\n\n";
        res += "To begin claiming your initial territories, enter /listFreeTerritories to get the list of available territories again." +
                " The list is automatically shown below. \n\n" +
                "__AVAILABLE TERRITORIES__";
        for(String each: _GameMaster.gamesListing.get(context).BM.getFreeTerritories())
        {
            res += "\n"+each;
        }

        ArrayList<Integer> tempListing = new ArrayList<>();
        tempListing.addAll(_GameMaster.gamesListing.get(context).playerDirectory.keySet());
        Game game = _GameMaster.gamesListing.get(context);
        res += "\n\nIt is now player @" +
                game.playerDirectory.get(tempListing.get(game.turn % game.playerDirectory.size())).username + "'s turn";
        res += "\nEnter /pick <country> to select and capture your initial territories. ";

        return "Your game " + _GameMaster.gamesListing.get(context).gameID + " is now starting. " + res;
    }

    public static String onFollowUpInitPick(Game thisGame){
        String out = "Initial territory claiming is complete." +
                "\nPlayers have remaining armies to dispatch to their territories. Please select a territory to dispatch your remaining armies to and fortify. \n" +
                "\n/reinforce <country> to select a country to dispatch one(1) army to." +
                "\n/listMyTerritories to view your territories and their status. (not implemented yet)" +
                "\n\nIt is now player @" + thisGame.playerDirectory.get(0).username + "'s turn:" +
                "\nYour territories you can reinforce";


        for(String k: thisGame.playerDirectory.get(0).getTerritories())
        {
            out += "\n"+k;
        }

        thisGame.state = GameState.CLAIMING;
        return out;
    }

    public static String onFollowUpReinforce(Game game){
        String out = "";
        if(CommandUtils.isReinforcingOver(game) && game.state == GameState.CLAIMING)
        {
            game.state = GameState.ON_TURN;
            out = "Initial territory reinforcing is complete." +
                    "\nPlayers can now begin making turns\n" +
                    "\n/beginTurn to begin your turn" +
                    "\n/endTurn to end your turn" +
                    "\n\nIt is now player @" + game.playerDirectory.get(0).username + "'s turn:";

        } else if(game.state == GameState.CLAIMING){
            game.turn += 1;
            Player nextPlayer = CommandUtils.getPlayer(game);
            out = "\nIt is now player @" + nextPlayer.username + "'s turn";
            out += "\nYour territories are:";
            for(String i: nextPlayer.getTerritories())
            {
                out += "\n"+i;
            }

            game.nextTurnUserID = nextPlayer.id;
        }
        else if (game.state == GameState.ON_TURN )
        {
            Player nextPlayer = CommandUtils.getPlayer(game);

            if(nextPlayer.getNumberOfArmies() == 0)
            {
                out += "\n/attack <invading> <defending> <number of armies to attack with MAX.3> <number of armies to defend with MAX.2>" +
                        "\n/fortify <fortify from> <fortify neighbor> <number of armies to transfer>" +
                        "\n/buycredit <amount> to buy credit" +
                        "\n/buystuff <# of undos> <# of cards> to buy stuff with your credits" +
                        "\n/endturn to finally end your turn";
            }
            else {
                out += "\nCurrently, your territories are:";
                for (String i : nextPlayer.getTerritories()) {
                    out += "\n" + game.BM.getBoardMap().get(i).getArmy().getInfantryCount() + " armies -- " + i;
                }
            }
        }
        else {
            out = "/reinforce is done.";
        }
        return out;


    }

    public static String onBeginTurn(Game game)
    {
        Player player = CommandUtils.getPlayer(game);
        game.currentTurn = new Turn(game.BM, player, game.turn);
        String out = ("Player @" +player.getUsername()+ " has begin their turn. You may: \n" +
                "\n/tradecards to trade your cards if you have pairs" +
                "\n/reinforce <country> to reinforce new free armies to your territory" +
                "\n/attack to commence an attack and learn more about related attack commands" +
                "\n/fortify <fortify from> <fortify neighbor> <number of armies to transfer>" +
                "\n/buycredit <amount> to buy credit" +
                "\n/buystuff <# of undos> <# of cards> to buy stuff with your credits" +
                "\n/endturn to finally end your turn");
        Turn turn = new Turn(game.BM, player, game.turn);
        game.setCurrentTurn(turn);

        System.out.println("Breakdown of free armies: ");
            int freebies = turn.getArmiesFromCards() + turn.getFreeArmiesFromTerritories();
            player.addArmies(freebies);

            out += "\n\nYou have " + player.getNumberOfArmies() + " available armies to reinforce";
        out += ("\nYou have " + player.getUndos() + " undo");
        out += ("\nYou have " + player.getWallet() + " credits");
        ArrayList<Card> cards = player.getHandListing();

        out += "\nYou have " + cards.size() + " card(s)";
        if (!cards.isEmpty())
        {
            for (Card c : cards) {
                out += ("\n\t"+c.getOrigin() + ": " + c.getUnit());
            }
        }


        return out;
    }

    public static String onAttack(Game game, ChatInput in) {
        String tempTerritory = String.join(" ", in.getArgs());
            if(game.state == GameState.ON_TURN || game.state == GameState.ATTACKING || game.state == GameState.DEFENDING)
            {

                Player player = CommandUtils.getPlayer(game);
                Turn turn = new Turn(game.BM, player, game.turn);

                if (tempTerritory.equals("")) {
                    game.context = new Context();
                    return "To commence an attack," +
                            "\n/attack for help, or to reset"+
                            "\n/attack <from>" +
                            "\n/attack <enemy>" +
                            "\n/attackWith <count max(3)>" +
                            "\n\nYour territories that are able to attack:\n" + (turn.getAttackableTerritories());

                } else
                {
                    if (player.getTerritories().contains(tempTerritory)
                            && game.state == GameState.ON_TURN) {
                        // the player owns the territory
                        // the context is attacking from this territory
                        game.context = new Context();
                        game.context.countryFrom = tempTerritory;
                        game.state = GameState.ATTACKING;
                        return "You have commenced attacking from " + tempTerritory;
                    } else if (player.getTerritories().contains(tempTerritory)
                        && game.state == GameState.ATTACKING) {
                        // the player owns the territory
                        // the context is attacking an enemy territory
                        return "You own this territory and cannot attack it.";
                    }else if (!player.getTerritories().contains(tempTerritory)
                                && game.BM.getBoardMap().keySet().contains(tempTerritory)
                                && game.BM.getNeighborsOf(game.context.countryFrom).contains(tempTerritory)
                                && game.state == GameState.ATTACKING) {
                        // the player does not own the enemy's territory
                        // the territory exists
                        // the territory is a neighbor of
                        // the context is attacking an enemy territory
                        game.context.countryTo = tempTerritory;
                        return "You have commenced an attack on " + tempTerritory;
                    } else if (!player.getTerritories().contains(tempTerritory)
                            && game.BM.getBoardMap().keySet().contains(tempTerritory)
                            && !game.BM.getNeighborsOf(game.context.countryFrom).contains(tempTerritory)
                            && game.state == GameState.ATTACKING) {
                        // the player does not own the enemy's territory
                        // the territory exists
                        // the territory is NOT a neighbor of
                        // the context is attacking an enemy territory
                        return "Territory is unreachable from " + game.context.countryFrom;
                    } else if (tempTerritory.toLowerCase().equals("cancel")) {
                        game.context = null;
                        game.state = GameState.ON_TURN;
                        return "You cancelled attacking.";
                    } else {
                        return "You do not own " + tempTerritory;
                    }
                }
            }
            else {
                return "You cannot attack right now.";
            }
    }

    public static String onAttackWith(Game game, ChatInput in){
        if(game.state == GameState.ATTACKING && game.context != null)
        {
            // assuming value is a number for now
            int k = Integer.parseInt(in.getArgs().get(0));
            if(k <= 0 || k > 3)
            {
                return "You can only attack with 1-3 armies.";
            }
            else {
                game.context.count1 = k;
                game.state = GameState.DEFENDING;
                return "You have decided to attack " + game.context.countryTo + " from " + game.context.countryFrom + " with " + game.context.count1 + " armies.";
            }
        } else {
            return "You have not specified where to attack from and which enemy to attack yet.";
        }
    }

    public static String onDefendWith(Game game, ChatInput in){

        if(game.state == GameState.DEFENDING && game.context != null)
        {
            // assuming value is a number for now
            int k = Integer.parseInt(in.getArgs().get(0));
            if(k <= 0 || k > 2)
            {
                return "You can only defend with 1-2 armies.";
            }
            else {
                game.state = GameState.RESULT;
                game.context.count2 = k;
                return "You have decided to defend " + game.context.countryTo + " with " + k +" armies.";
            }
        }else {
            return "You are not under attack.";
        }
    }

    public static String onFollowUpAttack(Game game) {
        // shows up for when after a player finishes declaring an attack and a follow-up message is needed for
        // giving instructions to the person who needs to defend

        int id = game.BM.getBoardMap().get(game.context.countryTo).getOccupantID();
        String username = "null";
        for (int i: game.playerDirectory.keySet())
        {
            if(game.playerDirectory.get(i).id == id)
            {
                username = game.playerDirectory.get(i).username;
            }
        }
        if (!username.equals("null"))
        {
            return "@" + username + " Your territory " + game.context.countryTo + " is under attack!\n/defendWith <amount MAX.2> to defend it.";
        } else {
            return "Uh Oh! Somehow, no one owns the territory! This is unexpected.\n" +
                    "Either the game is being tested and some objects are not yet initialized" +
                    "\nOR this has been called before players get to pick a territory or a country to attack/fortify.";
        }
    }

    public static String onFollowUpResult(Game game) {
            Player p = CommandUtils.getPlayer(game);
            Turn k = new Turn(game.BM, p, game.turn);
            String s = k.battle(game.context.countryFrom, game.context.countryTo, game.context.count1, game.context.count2);
            game.state = GameState.ON_TURN;
            game.context = null;
            return s;
    }

    public static String onBuyCredit(Game game, ChatInput in){
        if(in.getArgs().get(0).equals("") || in.getArgs().isEmpty())
        {
            return "You did not provide the amount of credits you want to buy." +
                    "\n/buycredits <amount>";
        } else{
            Player player = CommandUtils.getPlayer(game);
            player.addMoney(Double.parseDouble(in.getArgs().get(0)));
            return "You bought "+ in.getArgs().get(0) + " credits and now have a total of " + player.getWallet() + " credits";
        }

    }

    public static String onBuyStuff(Game game, ChatInput in)
    {
        String response = "";
        if(in.getArgs().size() < 2)
        {
            return "Uh Oh! You either did not provide the amount of cards or the amount of undos.\n/buystuff <undos> <cards>";
        }
        else {
            int turnNo = game.turn % game.playerDirectory.size();
            Player player = game.playerDirectory.get(turnNo);

            Double cash = player.getWallet();

            int undos = Integer.parseInt(in.getArgs().get(0));
            if (undos * 1000 <= cash) {
                player.addUndos(undos);
                player.addMoney( undos * 1000 * -1);
                response += "You successfully bought " + undos + " undos\n";
            } else {
                return "You do not have enough credits to buy " + undos + " undos (1000 each). \nYou currently have " + cash + " credits.";
            }

            cash = player.getWallet();
            int cards = Integer.parseInt(in.getArgs().get(1));
            if (cards * 100 <= cash) {
                for (int i = 0; i < cards; i++) {
                    Card c = game.BM.getGameDeck().draw();
                    if(c != null) player.getHand().get(c.getUnit()).push(c);
                }
                player.addMoney( cards * 100 * -1);
                return response + "You successfully bought " + cards + " cards.";
            }
            else {
                return response + "You do not have enough credits to buy " + cards + " cards (100 each). \nYou currently have " + cash + " credits.";
            }
        }

    }


    public static String onFortify(Game game, ChatInput in){

        String tempTerritory = String.join(" ", in.getArgs());
        if(game.state == GameState.ON_TURN)
        {

            Player player = CommandUtils.getPlayer(game);

            if (tempTerritory.equals("")) {
                game.context = new Context();
                String res = "";
                for(String terr: player.getTerritories())
                {
                    res += "\n"+terr;
                }
                return "To fortify," +
                        "\n/fortify for help, or to reset"+
                        "\n/fortify <from>" +
                        "\n/fortify <neighbor territory>" +
                        "\n/fortify <transferCount>" +
                        "\n\nYour territories:" + res;

            } else
            {
                if (player.getTerritories().contains(tempTerritory)
                        && game.context != null
                        && !game.context.countryFrom.equals("")
                        && game.BM.getNeighborsOf(game.context.countryFrom).contains(tempTerritory)) {
                    game.context.countryTo = tempTerritory;
                    return "You have selected to fortify " + tempTerritory;
                }else if (player.getTerritories().contains(tempTerritory)
                        && game.context != null
                        && !game.context.countryFrom.equals("")
                        && !game.context.countryTo.equals("")
                        && !game.BM.getNeighborsOf(game.context.countryFrom).contains(tempTerritory)) {
                    return "Territory is unreachable from " + game.context.countryFrom;
                } else if(player.getTerritories().contains(tempTerritory)) {
                game.context = new Context();
                game.context.countryFrom = tempTerritory;
                return "You have selected to fortify from " + tempTerritory;
            } else if (!game.context.countryFrom.equals("") && !game.context.countryTo.equals("") && !game.BM.getBoardMap().keySet().contains(tempTerritory)) {
                    int k = -1;
                    try {
                        k = Integer.parseInt(tempTerritory);
                    } catch(NumberFormatException e){
                        return "Territory " + tempTerritory + " not found.";
                    }
                    if(k < game.BM.getOccupantCount(game.context.countryFrom) && k >= 0) {
                        game.BM.fortifyTerritory(game.context.countryFrom, game.context.countryTo, k);
                        return "You have fortified successfully with " + tempTerritory + " armies.";
                    } else {
                        return "You cannot transfer " + k + " armies.\nYou only have " + game.BM.getOccupantCount(game.context.countryFrom) + " armies in " + game.context.countryFrom;
                    }
                } else {
                    return "You do not own " + tempTerritory;
                }
            }
        }
        else {
            return "You cannot fortify right now.";
        }

    }


    public static String onEndTurn(Game game, Twitter tw)
    {
        Turn turn = game.currentTurn;
        turn.earnCards();

        String result = "";

        // Write game to save game file
        try {
            JSONhandler JSONhandler = new JSONhandler(game);
            JSONhandler.JSONwriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            result += Twitter.broadcastToTwitter(game.currentTurn, game.currentTurn.player);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        // move to next turn
        game.turn += 1;
        int turnNo = game.turn % game.playerDirectory.size();
        Player player = game.playerDirectory.get(turnNo);
        game.nextTurnUserID = player.id;
        return result+"\nPlayer @" +player.getUsername()+ " it is now your turn, type /beginTurn to begin your turn";

    }


    public static String onLoad(Game game, AWS aws, ChatInput in)
    {
        String response;
        if(in.getArgs().isEmpty() || in.getArgs().get(0).equals(""))
        {
            ArrayList<String> games = aws.listObjects();
            response = "You have opted to load a game but did not provide a gameID:\n/load <gameID> to load a game\nAvailable games to load:\n";
            for (String g: games) {
                response += g + "\n";
            }
            return response;
        }
        else {

            try {
                if(!aws.download(in.getArgs().get(0)))
                {
                    return "Game could not be downloaded from AWS.";
                } else {
                    game.gameLoader = new Loader(in.getArgs().get(0));
                    _GameMaster.gamesListing.get("game").gameLoader.JH.fileName = aws.getFileName();
                    _GameMaster.gamesListing.put(in.getArgs().get(0), game.gameLoader.loadGame());

                    int turn = _GameMaster.gamesListing.get(in.getArgs().get(0)).turn;
                    return "Game loaded, it is now the " + turn + " turn";
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                return "Game could not be found";
            }
            // create new loader & game using the input gameID
        }
    }

    public static String onUndo(Game game, AWS aws)
    {
        // compared to load, undo is used during gameplay, hence there are players
        try {
            if(!aws.download(game.gameID))
            {
                return "Game could not be downloaded from AWS.";
            }else {
                aws.download(game.gameID);
                // create new loader & game using the input gameID
                game.gameLoader = new Loader(game.gameID);
                _GameMaster.gamesListing.get("game").gameLoader.JH.fileName = aws.getFileName();
                _GameMaster.gamesListing.put(game.gameID, game.gameLoader.loadGame());
                return "Undo successful";
            }
        } catch(IOException e)
        {
            e.printStackTrace();
            return "Undo unsuccessful";
        }

    }
}
