
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lauravandebraak
 */
class ResponseStrategy extends Strategy {
    //TODO: implement
    private String strategy;
    
    public ResponseStrategy() {
        this.strategy = "default";
    }
    
    public ResponseStrategy(String strat){
        this.strategy = strat;
    }
    
    public Card strategy(KripkeModel model, int agent, CardSet query, CardSet hand){
        if(strategy == "default"){
            return defaultStrat(model, agent, query, hand);
        }
        return defaultStrat(model, agent, query, hand);
    }
    
    private Card defaultStrat(KripkeModel model, int agent, CardSet query, CardSet hand){
        int counter = 0, i = 0, best = 0, idx = -1;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        // If a player has only one of the cards queried, return that one
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
        
        // If a player has more than one of the cards queried, return a random card
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }
            
}
