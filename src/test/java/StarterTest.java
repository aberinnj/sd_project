import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


/*////////////////////////////////////////////////////////////////////////////////
_Starter Class starts a _Starter by initializing GameManager and runs a game
*///////////////////////////////////////////////////////////////////////////////*/
public class StarterTest extends TestCase{

    private _Starter ng;

    @Test
    public void testNewGame() throws Exception {
        ng = new _Starter();
        assertNotNull(ng.scanner);
        assertNotNull(ng.base);
        assertEquals(0, ng.getPlayerCount());
        ng = null;
    }

    @Test
    public void testDefaultStart() {
        ByteArrayInputStream in = new ByteArrayInputStream(("1\n2\n" +
                "ALASKA\n" +
                "NORTH WEST TERRITORY\n" +
                "GREENLAND\n" +
                "ALBERTA\n" +
                "ONTARIO\n" +
                "QUEBEC\n" +
                "WESTERN UNITED STATES\n" +
                "EASTERN UNITED STATES\n" +
                "CENTRAL AMERICA\n" +
                "VENEZUELA\n" +
                "PERU\n" +
                "BRAZIL\n" +
                "ARGENTINA\n" +
                "NORTH AFRICA\n" +
                "CONGO\n" +
                "EGYPT\n" +
                "EAST AFRICA\n" +
                "SOUTH AFRICA\n" +
                "MADAGASCAR\n" +
                "MIDDLE EAST\n" +
                "INDIA\n" +
                "SIAM\n" +
                "INDONESIA\n" +
                "WESTERN AUSTRALIA\n" +
                "EASTERN AUSTRALIA\n" +
                "NEW GUINEA\n" +
                "CHINA\n" +
                "AFGHANISTAN\n" +
                "URAL\n" +
                "SIBERIA\n" +
                "YAKUTSK\n" +
                "KAMCHATKA\n" +
                "IRKUTSK\n" +
                "MONGOLIA\n" +
                "JAPAN\n" +
                "UKRAINE\n" +
                "SCANDINAVIA\n" +
                "ICELAND\n" +
                "GREAT BRITAIN\n" +
                "NORTHERN EUROPE\n" +
                "SOUTHERN EUROPE\n" +
                "WESTERN EUROPE\n"+

                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +

                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +

                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +

                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +
                "ALASKA\n" +

                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +

                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +

                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +

                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n" +
                "ALBERTA\n").getBytes());
        System.setIn(in);
        GameManager GM = new GameManager();
        ng = new _Starter();

        ng.defaultStart(GM);


        assertNotNull(GM.getPlayer(0));
        assertNotNull(GM.getPlayer(1));
        assertEquals(2, GM.playerTurnPattern.length);
        assertEquals(0, GM.getPlayer(1).getNumberOfArmies());

        assertEquals(GM.playerTurnPattern[0], GM.getBM().getBoardMap().get("ALASKA").getOccupantID());
    }

    /*
    // This test requires a complete stream of inputs for a complete game
    @Test
    public void testMain() throws Exception {
        ng = new _Starter();
        final InputStream old = System.in;
        final FileInputStream fis = new FileInputStream(new File(ng.base + "/src/main/java/input.txt"));
        System.setIn(fis);

        ng.main(null);

        System.setIn(old);
        ng = null;
    }
    */


    @Test
    public void testPass()
    {
        ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
        System.setIn(in);
        ng = new _Starter();
        ng.setNumberOfPlayers();
        assertEquals(2, ng.getPlayerCount());
        System.setIn(System.in);
        ng = null;
    }

    @Test
    public void testFail()
    {
        ByteArrayInputStream in = new ByteArrayInputStream("9".getBytes());
        System.setIn(in);
        ng = new _Starter();
        ng.setNumberOfPlayers();
        assertEquals(0, ng.getPlayerCount());

        ByteArrayInputStream in2 = new ByteArrayInputStream("1".getBytes());
        System.setIn(in2);
        ng = new _Starter();
        ng.setNumberOfPlayers();
        assertEquals(0, ng.getPlayerCount());

        System.setIn(System.in);
        ng = null;
    }

    @Test
    public void testInvalid()
    {
        ByteArrayInputStream in = new ByteArrayInputStream("INVALID ".getBytes());
        System.setIn(in);
        ng = new _Starter();
        assertFalse(ng.setNumberOfPlayers());
        assertEquals(0, ng.getPlayerCount());
        System.setIn(System.in);
        ng = null;
    }

    @Test
    public void testQueryGameChecker(){
        ByteArrayInputStream in = new ByteArrayInputStream("CANCEL\ngamma-not-even-a-game-key-1\ngame-2".getBytes());
        System.setIn(in);
        ArrayList<String> games1 = new ArrayList<>();

        _Starter st = new _Starter();
        assertFalse(st.queryGameChecker(games1));

        games1.add("game-1");
        games1.add("game-2");

        assertFalse(st.queryGameChecker(games1));
        assertTrue(st.queryGameChecker(games1));
        System.setIn(System.in);


    }

}