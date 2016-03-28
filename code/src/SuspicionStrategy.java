
import java.util.Random;

/**
 *
 * @author lauravandebraak
 */
class SuspicionStrategy extends Strategy {
    //TODO: implement
    
    private String strategy;
    
    public SuspicionStrategy() {
        this.strategy = "default";
    }
    
    public SuspicionStrategy(String strat){
        this.strategy = strat;
    }
    
    /**
     * the suspicion strategy to be played
     * @param model the current KripkeModel
     * @param agent the agent played for
     * @return the suspicion made
     */
    public CardSet strategy(KripkeModel model, int agent){
        if(strategy == "default"){
            return defaultStrat(model, agent);
        }
        return defaultStrat(model, agent);
    }
    
    private CardSet defaultStrat(KripkeModel model, int agent){
        int c = 0, number = 0, s = 0;
        int cat = model.point().getCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            s = 0;
            number = model.point().numberOfCards(c);

            for (int r : shuffle(super.arrayTo(number))) {
                Card card = new Card(c, r);
                System.out.println(card);
                PropVar test = new PropVar(card, 0);
                if((new Maybe(agent, test)).evaluate(model) &&
                        (new Know(agent, test)).negate().evaluate(model)) {
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
