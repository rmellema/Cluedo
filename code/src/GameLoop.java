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
    private Agent[]     players;

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
        Agent agent = this.players[this.current];
    }

    public void run() {
    }
}
