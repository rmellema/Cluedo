
import java.util.Random;

/**
 * Class for Suspicion Strategies
 * @author lauravandebraak
 */
class SuspicionStrategy extends Strategy {
    
    protected String strategy;
    
    /**
     * The default constructor. If no strategy is given, it will be "default"
     */
    public SuspicionStrategy() {
        this.strategy = "default";
    }
    
    /**
     * The constructor. Creates a SuspicionStrategy with a certain strategy.
     * @param strat the strategy to be played
     */
    public SuspicionStrategy(String strat){
        this.strategy = strat;
    }
    
    /**
     * The suspicion strategy to be played
     * @param agent the agent played for
     * @return the suspicion made
     */
    public CardSet strategy(Player agent){
        if(strategy.equals("default")){
            return defaultStrat(agent);
        }
        if(strategy.equals("complex")){
            return complexStrat(agent);
        }
        return defaultStrat(agent);
    }
    
    /**
     * The default strategy: suspect cards you do not yet know
     * @param agent the playing agent
     * @return the CardSet to be suspected
     */
    protected CardSet defaultStrat(Player agent){
        int c, number;
        int cat = agent.getCardCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            number = agent.getCardsForCategory(c);

            for (int r : shuffle(super.arrayTo(number))) {
                Card card = new Card(c, r);
                PropVar test = new PropVar(card, 0);
                if (agent.doesConsider(test) &&
                        !agent.doesKnow(test)) {
                    cards[c] = card;
                    break;
                }
            }
            if (cards[c] == null) {
                cards[c] = new Card(c, rand.nextInt(number));
            }
        }
        return new CardSet(cards);
    }

    
    /**
     * A complex strategy for suspicion: ask the cards that give the opponents the least information
     * @param agent the playing agent
     * @return the CardSet suspicion
     */
    protected CardSet complexStrat(Player agent){
        int c, number, ind, i, bestCount = 100, a, count, best = -1;
        int cat = agent.getCardCategories();
        int ag = agent.getAgents();
        Card[] cards = new Card[cat];
        Card[][] options = new Card[cat][];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            number = agent.getCardsForCategory(c);
            options[c] = new Card[number];
            ind = 0;
            best = -1;
            
            for (int r : shuffle(super.arrayTo(number))) {
                Card card = new Card(c, r);
                PropVar test = new PropVar(card, 0);
                if (agent.doesConsider(test) &&
                        !agent.doesKnow(test)) {
                    options[c][ind] = card;
                    ind++;
                }
            }
            for(i = 0; i < ind; i++){
                count = 0;
                for(a = 1; a <= ag; a++){
                    PropVar test = new PropVar(options[c][i],0);
                    PropVar testSelf = new PropVar(options[c][i],agent.getNumber());
                    if(agent.doesKnow(new Or(new Know(a,test), new Know(a,testSelf)))){
                        count++;
                    }
                }
                if(count < bestCount){
                    bestCount = count;
                    best = i;
                }
            }
            
            if(best != -1){
                cards[c] = options[c][best];
            }

            if (cards[c] == null) {
                cards[c] = new Card(c, rand.nextInt(number));
            }
        }
        return new CardSet(cards);
    }

    /**
     * Get the strategy played
     * @return the strategy played by this agent
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Get the different types of SuspicionStrategy
     * @return a String[] of the possible types of SuspicionStrategy
     */
    public static String[] getOptions() {
    	return new String[]{"default", "complex"};
    }
}
