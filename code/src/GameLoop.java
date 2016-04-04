import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class to handle all the game logic in Cluedo.
 * @author rmellema
 */
public class GameLoop {

	private KripkeModel model;
    private int         numPlayers;
    private int         current;
    private int         round = 0;
    private PrintStream out;
    private Player[]    players;
    private boolean     done = false;
    private Set<Integer> skip = new HashSet<>();

    /**
     * Create a new loop using the given model and PrintStream.
     * @param model The model that the players of this game will use
     * @param out PrintStream that this object uses to print towards.
     * @param players The players that are going to play this game
     */
    public GameLoop(KripkeModel model, PrintStream out, Player... players) {
        this.model = model;
        this.numPlayers = model.getAgents();
        this.current = 0;
        this.out = out;
        if (players.length != this.numPlayers) {
            throw new IllegalArgumentException("Wrong number of players given");
        }
        this.players = players;
    }

    /**
     * Create a new loop using the given model
     * @param model The model that the players of this game will use
     * @param players The players that will play this game
     */
    public GameLoop(KripkeModel model, Player... players) {
        this(model, System.out, players);
    }

    /**
     * Create a new loop for the given model and PrintStream
     * @param model The model that the players of this game will use
     * @param out PrintStream this object uses for printing
     */
    public GameLoop(KripkeModel model, PrintStream out) {
        this(model, out, initPlayers(model.point(), model.getAgents(), model));
    }

    /**
     * Create a new loop for the given dealing, players and PrintStream
     * @param deal The dealing of cards for this game
     * @param out PrintStream this object uses for printing
     * @param players The players that will play this game
     */
    public GameLoop(Dealing deal, PrintStream out, Player... players) {
        this(new KripkeModel(deal, players.length), out, players);
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
        this(new Dealing(new int[]{4, 6}).randomize(4), 4, out);
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
     * @param model The model that these agents will use to reason about the world
     * @return An array of players with the default strategy
     */
    private static Player[] initPlayers(Dealing deal, int players, KripkeModel model) {
        Player[] ret = new Player[players];
        ArrayList<ArrayList<Card>> dealing = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < players; i++) {
            dealing.add(i, new ArrayList<Card>());
        }
        for (int i = 0; i < deal.getCategories(); i ++) {
            for (int j = 0; j < deal.numberOfCards(i); j++) {
                if (deal.player(i, j) > 0)
                    dealing.get(deal.player(i, j) - 1).add(new Card(i, j));
            }
        }
        for (int i = 0; i < players; i++) {
            ret[i] = new Player(new
                    CardSet(dealing.get(i).toArray(new
                    Card[dealing.get(i).size()])),i + 1, model);
        }
        return ret;
    }

    /**
     * Get the dealing in state 0 for this GameLoop
     * @return The true dealing of cards
     */
    public Dealing getDealing() {
        return this.model.point();
    }

    /**
     * Get the players in this game
     * @return The playing agents
     */
    public Player[] getPlayers() {
        return this.players;
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
     * Given an suspicion, check the other players and see if they have one of
     * the cards in the suspicion. If they do, handle that situation
     * @param suspicion The suspicion to check
     */
    private void checkSuspicion(CardSet suspicion) {
        Player agent = this.players[this.current];
        this.out.println("Agent #" + agent.getNumber() +
                " speaks suspicion " + suspicion);
        Card[] cards = suspicion.getCards();
        boolean counterGiven = false;
        for (int next = (current + 1) % this.numPlayers;
             next != current;
             next = (next + 1) % this.numPlayers) {
            Player a = this.players[next];
            Card resp = a.response(suspicion, agent.getNumber());
            if (resp != null) {
                counterGiven = true;
                this.out.println("\tAgent #" + a.getNumber() +
                        " has some of these cards: " + resp.toString());
                Formula[] ors = new Formula[suspicion.size()];
                Formula[] eors = new Formula[suspicion.size()];
                for (int i = 0; i < cards.length; i++) {
                    ors[i]  = new PropVar(cards[i], a.getNumber());
                    eors[i] = new EveryKnows(set(agent.getNumber(), a.getNumber()), ors[i]);
                }
                model.publicAnnouncement(new Or(ors));
                model.privateAnnouncement(new PropVar(resp, a.getNumber()), agent.getNumber());
                long startTime = System.nanoTime();
                model.publicAnnouncement(new Or(eors).simplify());
                long endTime = System.nanoTime();
                //System.out.println("Time taken: " + (endTime - startTime)/(60000000000.0));
                break;
            } else {
                this.out.println("\tAgent #" + a.getNumber() +
                        " has none of these cards");
                Formula[] ands = new Formula[cards.length];
                for (int i = 0; i < cards.length; i++) {
                    ands[i] = (new PropVar(cards[i], a.getNumber())).negate();
                }
                model.publicAnnouncement(new And(ands));
            }
        }
        if (!counterGiven) {
            this.out.println("None of the agents disprove the suspicion");
        }
    }

    /**
     * Check if a accusation is correct, and if so, end the game
     * @param accusation The accusation to check
     */
    private void checkAccusation(CardSet accusation) {
        Player agent = this.players[this.current];
        if (accusation != null) {
            this.out.println("Agent #" + agent.getNumber() + " speaks accusation "
                    + accusation.toString());
            Formula[] props = new Formula[accusation.size()];
            for (int i = 0; i < props.length; i++) {
                props[i] = new PropVar(accusation.getCards()[i], 0);
            }
            if ((new And(props)).evaluate(model)) {
                this.out.println("The accusation is correct\nThe game is over");
                this.done = true;
            } else {
                this.out.println("The accusation is incorrect\nAgent #" +
                        agent.getNumber() + " will be removed from the game");
                skip.add(this.current);
            }
        } else {
            this.out.println("Agent #" + agent.getNumber() + " makes no accusation");
        }
    }

    /**
     * Play one turn of the game for the current player
     */
    public void step() {
        if (skip.size() == this.numPlayers - 1) {
            this.done = true;
            this.out.println("Only one agent left, so the game is over.");
            return;
        }
        Player agent = this.players[this.current];
        CardSet suspicion = agent.suspect();
        checkSuspicion(suspicion);
        checkAccusation(agent.accuse());
        do {
            if (++this.current == this.numPlayers) {
                this.current = 0;
                this.round++;
                this.out.println("# Round " + this.round);
            }
        } while (skip.contains(this.current));
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

    /**
     * Check if the current game is finished
     * @return `true` if the game is done, `false` otherwise
     */
    public boolean isDone() {
        return this.done;
    }

    public Player[] getEliminatedPlayers() {
        if (skip.size() == 0) {
            return null;
        }
        ArrayList<Player> ret = new ArrayList<>(skip.size());
        for (int num : skip) {
            ret.add(this.players[num]);
        }
        return ret.toArray(new Player[skip.size()]);
    }

    /**
     * Get the number of the current round
     * @return number of the current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Play one random game of Cluedo with `System.out` as output
     * @param args unused
     */
    public static void main(String[] args) {
        GameLoop loop = new GameLoop(System.out);
        for (Player agent : loop.players) {
            System.out.println("Agent #" + agent.getNumber() + " " +
                    agent.getHand());
        }
        loop.game();
    }
}
