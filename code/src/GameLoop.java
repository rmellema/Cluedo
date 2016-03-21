import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private boolean     done = false;

    public GameLoop(KripkeModel model, PrintStream out) {
        this.model = model;
        this.numPlayers = model.getAgents();
        this.current = 1;
        this.out = out;
        this.initPlayers(model.point(), model.getAgents());
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

    private void initPlayers(Dealing deal, int players) {
        this.players = new Player[players];
        ArrayList<ArrayList<Card>> dealing = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < players; i++) {
            dealing.add(i, new ArrayList<>());
        }
        for (int i = 0; i < deal.categories(); i ++) {
            for (int j = 0; j < deal.numberOfCards(i); j++) {
                if (deal.player(i, j) > 0)
                    dealing.get(deal.player(i, j) - 1).add(new Card(i, j));
            }
        }
        for (int i = 0; i < players; i++) {
            this.players[i] = new Player(new
                    CardSet(dealing.get(i).toArray(new
                    Card[dealing.get(i).size()])));
        }
    }

    public Set<Integer> set(int... agents) {
        HashSet<Integer> ret = new HashSet<>(agents.length);
        for (int agent : agents) {
            ret.add(agent);
        }
        return ret;
    }

    private void checkAccusation(CardSet accusation) {
        this.out.println("Agent #" + this.current +
                " speaks accusation " + accusation);
        ArrayList<Formula> ands = new ArrayList<>();
        Card[] cards = accusation.getCards();
        boolean counterGiven = false;
        for (int next = (current + 1) % this.numPlayers;
             next != current;
             next = (next + 1) % this.numPlayers) {
            Player a = this.players[next];
            Card resp = a.response(this.model, accusation);
            if (resp != null) {
                counterGiven = true;
                this.out.println("\tAgent #" + next +
                        " has some of these cards");
                Formula[] ors = new Formula[accusation.size()];
                Formula[] eors = new Formula[accusation.size()];
                for (int i = 0; i < cards.length; i++) {
                    ors[i]  = new PropVar(cards[i], next);
                    eors[i] = new EveryKnows(set(current, next), ors[i]);
                }
                model.publicAnnouncement(new Or(ors));
                model.privateAnnouncement(new PropVar(resp, next), current);
                model.publicAnnouncement(new Or(eors));
                //Do stuff
                break;
            } else {
                this.out.println("\tAgent #" + next +
                        " has none of these cards");
                for (Card card : cards) {
                    ands.add((new PropVar(card, next).negate()));
                }
            }
        }
        if (!counterGiven) {
            this.out.println("None of the agents disprove the accusation");
            model.publicAnnouncement(new And((Formula[])ands.toArray()));
        }
    }

    private void checkSuspicion(CardSet suspicion) {
        if (suspicion != null) {
            this.out.println("Agent #" + this.current + " speaks suspicion "
                    + suspicion.toString());
            Formula[] props = new Formula[suspicion.size()];
            for (int i = 0; i < props.length; i++) {
                props[i] = new PropVar(suspicion.getCards()[i], 0);
            }
            if ((new And(props)).evaluate(model)) {
                this.out.println("The suspicion is correct\nThe game is over");
                this.done = true;
            } else {
                this.out.println("The suspicion is incorrect\nAgent #" +
                        this.current + " will be removed from the game");
                for (int i = this.current + 1; i < this.numPlayers; i++) {
                    this.players[i - 1] = this.players[i];
                }
                this.numPlayers--;
            }
        }
    }

    public void step() {
        Player agent = this.players[this.current];
        CardSet accusation = agent.accuse(this.model);
        checkAccusation(accusation);
        checkSuspicion(agent.suspect(model));
        if (++this.current == this.numPlayers) {
            this.current = 0;
            this.round++;
        }
    }

    public void round() {
        int oldRound = this.round;
        while (this.round == oldRound) {
            step();
        }
    }

    public void game() {
        while (!this.done) {
            step();
        }
    }

    public static void main(String[] args) {
        GameLoop loop = new GameLoop(System.out);
        loop.game();
    }
}
