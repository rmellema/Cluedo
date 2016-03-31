
import java.util.Random;

/**
 * Class for Suspicion Strategies
 * @author lauravandebraak
 */
class SuspicionStrategy extends Strategy {
    
    protected String strategy;
    
    public SuspicionStrategy() {
        this.strategy = "default";
    }
    
    public SuspicionStrategy(String strat){
        this.strategy = strat;
    }
    
    /**
     * the suspicion strategy to be played
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
    
    protected CardSet defaultStrat(Player agent){
        int c, number;
        int cat = agent.getCardCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            number = agent.getCardsForCategory(c);

            for (int r : shuffle(super.arrayTo(number))) {
                Card card = new Card(c, r);
                System.out.println(card);
                PropVar test = new PropVar(card, 0);
                System.out.println(agent.doesConsider(test));
                System.out.println(!agent.doesKnow(test));
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
            ind = 0;
            best = -1;
            
            for (int r : shuffle(super.arrayTo(number))) {
                Card card = new Card(c, r);
                System.out.println(card);
                PropVar test = new PropVar(card, 0);
                System.out.println(agent.doesConsider(test));
                System.out.println(!agent.doesKnow(test));
                if (agent.doesConsider(test) &&
                        !agent.doesKnow(test)) {
                    options[c][ind] = card;
                    ind++;
                }
            }
            for(i = 0; i < ind; i++){
                count = 0;
                for(a = 0; a < ag; a++){
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

    
    public String getStrategy() {
        return strategy;
    }

    public static String[] getOptions() {
    	return new String[]{"default", "complex"};
    }
}
