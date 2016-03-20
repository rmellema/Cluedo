import java.io.PrintStream;
import java.io.Writer;

/**
 * Created by rene on 19/03/16.
 */
public class GameLoop {
    private KripkeModel model;
    private int         numPlayers;
    private int         current;
    private int         round = 0;
    private PrintStream out;
    private Player[]    players;

    public GameLoop(KripkeModel model, PrintStream out) {
        this.model = model;
        this.numPlayers = model.getAgents();
        this.current = 1;
        this.out = out;
    }

    public GameLoop(KripkeModel model) {
        this(model, System.out);
    }

    public GameLoop(Dealing deal, int players, PrintStream out) {
        this(new KripkeModel(deal, players), out);
    }

    public GameLoop(Dealing deal, int players) {
        this(deal, players, System.out);
    }

    public GameLoop(PrintStream out) {
        this(new Dealing(new int[][] {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}}), 4, out);
    }

    public GameLoop() {
        this(System.out);
    }

    public void step() {
        boolean counterGiven = false;
        Player agent = this.players[this.current];
        CardSet accusation = agent.accuse(this.model);
        this.out.println("Agent #" + this.current +
                " speaks accusation " + accusation);
        for (int next = (current + 1) % this.numPlayers;
             next != current;
             next = (next + 1) % this.numPlayers) {
            Player a = this.players[next];
            Card resp = a.response(this.model, accusation);
            if (resp != null) {
                counterGiven = true;
                this.out.println("\tAgent #" + next +
                        " has some of these cards");
                //Do stuff
                break;
            } else {
                this.out.println("\tAgent #" + next +
                        " has none of these cards");
            }
        }
        if (!counterGiven) {
            this.out.println("None of the agents disprove the accusation");
            //Do public announcement
        }
        CardSet suspicion = agent.suspect(model);
        if (suspicion != null) {
            //Do check if true
        }
        if (++this.current == this.numPlayers) {
            this.current = 0;
            this.round++;
        }
    }

    public void run() {
    }
}
