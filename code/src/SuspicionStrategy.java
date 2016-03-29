
import java.util.Random;

/**
 * Class for Suspicion Strategies
 * @author lauravandebraak
 */
class SuspicionStrategy extends Strategy {
    
    private String strategy;
    
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
        return defaultStrat(agent);
    }
    
    private CardSet defaultStrat(Player agent){
        int c = 0, number = 0, s = 0;
        int cat = agent.getCardCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            s = 0;
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
    
}
