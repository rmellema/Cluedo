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
        if(strategy == "default"){
            return defaultStrat(model, agent);
        }
        return null;
    }
    
    private CardSet defaultStrat(KripkeModel model, int agent){
        int c = 0, n = 0, found = 0, number = 0;
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
}
