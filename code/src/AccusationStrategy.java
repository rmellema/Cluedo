
import java.util.Random;

/**
 * 
 * @author lauravandebraak
 */
class AccusationStrategy extends Strategy {
    //TODO: implement
    private String strategy;
    
    public AccusationStrategy() {
        this.strategy = "default";
    }
    
    public AccusationStrategy(String strat){
        this.strategy = strat;
    }
    
    public CardSet strategy(KripkeModel model, int agent){
        if(strategy.equals("default")){
            return defaultStrat(model, agent);
        }
        if(strategy.equals("risk")){
            return riskStrat(model, agent);
        }
        return null;
    }
    
    /**
     * Default Strategy: if an agent knows all cards in the envelope, they will make an accusation
     * @param model the current model
     * @param agent the current agent
     * @return a suspicion if one is made, otherwise null
     */
    private CardSet defaultStrat(KripkeModel model, int agent){
        int c, n, found = 0, number;
        int cat = model.point().getCategories();
        Card[] cards = new Card[cat];
    
        
        for (c=0; c < cat; c++){
            number = model.point().numberOfCards(c);
            for (n=0; n < number; n++){
                Card test = new Card(c, n);
                if((new Know(agent, new PropVar(test, 0))).evaluate(model)) {
                    System.out.println("Agent knows card " + test);
                    found++;
                    cards[c] = test;
                }
            }
        }
        if(found == cat){
            return new CardSet(cards);
        }
        
        return null;
    }
    
    /**
     * A risky strategy: if an agent holds only two cards as possible, 
     * and knows all the rest, make a guess so someone can not win before you
     * @param model the current model
     * @param agent the current agent
     * @return a suspicion if one is made, otherwise null
     */
    private CardSet riskStrat(KripkeModel model, int agent){
        int c, n, found = 0, number, notC = -1, not1 = -1, not2 = -1;
        int cat = model.point().getCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        
        for (c=0; c < cat; c++){
            number = model.point().numberOfCards(c);
            for (n=0; n < number; n++){
                Card test = new Card(c, n);
                if((new Know(agent, new PropVar(test, 0))).evaluate(model)) {
                    System.out.println("Agent knows card " + test);
                    found++;
                    cards[c] = test;
                    break;
                }
                if(not1 == -1){
                    not1 = n;
                }else{
                    not2 = n;
                }
                if(n == number-1){
                    notC = c;
                }
            }
        }
        if(found == cat){
            return new CardSet(cards);
        }
        if(found == cat-1){
            if(rand.nextBoolean()){
                cards[notC] = new Card(notC, not1);
            }else{
                cards[notC] = new Card(notC, not2);
            }
            return new CardSet(cards);
        }
        
        return null;
    }
}
