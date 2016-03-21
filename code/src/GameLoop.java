import java.io.PrintStream;
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

    /**
     * Create a new loop using the given model and PrintStream.
     * @param model The model that the players of this game will use
     * @param out PrintStream that this object uses to print towards.
     */
    public GameLoop(KripkeModel model, PrintStream out) {
        this.model = model;
        this.numPlayers = model.getAgents();
        this.current = 1;
        this.out = out;
        this.initPlayers(model.point(), model.getAgents());
    }

    /**
     * Create a new loop using the given model
     * @param model The model that the players of this game will use
     */
    public GameLoop(KripkeModel model) {
        this(model, System.out);
    }

    /**
     * Create a new loop using the given dealing of cards, number of players
     * and PrintStream
     * @param deal The dealing of the cards
     * @param players the number of players in this game.
     * @param out PrintStream this object uses for printing
     */
    public GameLoop(Dealing deal, int players, PrintStream out) {
        this(new KripkeModel(deal, players), out);
    }

    /**
     * Create a new loop using the given dealing of cards, number of players
     * @param deal The dealing of the cards
     * @param players the number of players in this game.
     */
    public GameLoop(Dealing deal, int players) {
        this(deal, players, System.out);
    }

    /**
     * Create a new loop using a random dealing of cards and PrintStream
     * @param out The PrintStream used for printing
     */
    public GameLoop(PrintStream out) {
        this(new Dealing(new int[][] {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}}), 4, out);
    }

    /**
     * Create a new loop using a random dealing of cards. Resulting object
     * prints to `System.out`
     */
    public GameLoop() {
        this(System.out);
    }

    /**
     * Given a dealing and number of players, adds `players` number of players
     * into the game and deals them their cards
     * @param deal The deal for this game
     * @param players The number of players in this game
     */
    private void initPlayers(Dealing deal, int players) {
        this.players = new Player[players];
        ArrayList<ArrayList<Card>> dealing = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < players; i++) {
            dealing.add(i, new ArrayList<>());
        }
        for (int i = 0; i < deal.getCategories(); i ++) {
            for (int j = 0; j < deal.numberOfCards(i); j++) {
                if (deal.player(i, j) > 0)
                    dealing.get(deal.player(i, j) - 1).add(new Card(i, j));
            }
        }
        for (int i = 0; i < players; i++) {
            this.players[i] = new Player(new
                    CardSet(dealing.get(i).toArray(new
                    Card[dealing.get(i).size()])),i);
        }
    }

    /**
     * Create a set of agents
     * @param agents The agents that should be in the set
     * @return The set with `agents` in it
     */
    public Set<Integer> set(int... agents) {
        HashSet<Integer> ret = new HashSet<>(agents.length);
        for (int agent : agents) {
            ret.add(agent);
        }
        return ret;
    }

    /**
     * Given an accusation, check the other players and see if they have one of
     * the cards in the accusation. If they do, handle that situation
     * @param accusation The accusation to check
     */
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
                    eors[i] = new CommonKnow(set(current, next), ors[i]);
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

    /**
     * Check if a suspicion is correct, and if so, end the game
     * @param suspicion The suspicion to check
     */
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

    /**
     * Play one turn of the game for the current player
     */
    public void step() {
        Player agent = this.players[this.current];
        CardSet accusation = agent.accuse(this.model);
        if (accusation != null){
            checkAccusation(accusation);
        }
        checkSuspicion(agent.suspect(model));
        if (++this.current == this.numPlayers) {
            this.current = 0;
            this.round++;
        }
    }

    /**
     * End this round of the game
     */
    public void round() {
        int oldRound = this.round;
        while (this.round == oldRound && !this.done) {
            step();
        }
    }

    /**
     * Play the game until someone wins
     */
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
