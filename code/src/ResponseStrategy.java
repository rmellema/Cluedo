
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lauravandebraak
 */
class ResponseStrategy extends Strategy {

    private String strategy;
    
    public ResponseStrategy() {
        this.strategy = "default";
    }
    
    public ResponseStrategy(String strat){
        this.strategy = strat;
    }
    
    public Card strategy(KripkeModel model, int agent, CardSet query, CardSet hand, int other){
        if(strategy.equals("default")){
            return defaultStrategy(query, hand);
        }
        if(strategy.equals("simple")){
            return simpleStrategy(model, agent, query, hand, other);
        }
        if(strategy.equals("optimal")){
            return optimalStrategy(model, agent, query, hand, other);
        }
        return defaultStrategy(query, hand);
    }
    
    private Card defaultStrategy(CardSet query, CardSet hand){
        int counter = 0, i, best;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        /* 
        If a player has only one of the cards queried, return that one
        else return null.
        */
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
        
        /*
        If a player has more than one of the cards queried, return a random card
        */
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }
    
    private Card simpleStrategy(KripkeModel model, int agent, CardSet query, CardSet hand, int other){
        int counter = 0, i, best;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        /* 
        If a player has only one of the cards queried, return that one
        else return null.
        */
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
        
        /*
        If a player has more than one of the cards queried, 
        and knows the other agent knows one of them is not in the envelope, 
        return that card.
        */
        for(Card c : found){
            PropVar test = new PropVar(c,0);
            if(new Know(agent, new Know(other, new Neg(test))).evaluate(model)){
                return c;
            }
        }
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }

    private Card optimalStrategy(KripkeModel model, int agent, CardSet query, CardSet hand, int other) {
        int counter = 0, i, best;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        /* 
        If a player has only one of the cards queried, return that one
        else return null.
        */
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
        
        /*
        If a player has more than one of the cards queried, 
        and knows the other agent knows one of them is not in the envelope, 
        return that card.
        */
        for(Card f : found){
            PropVar test = new PropVar(f,0);
            if(new Know(agent, new Know(other, new Neg(test))).evaluate(model)){
                return f;
            }
        }
        
        /*
        If a player does not know that the other agent knows one of them,
        try to find the card that will give the other player the least
        information.
        */
        
        /*
        if after private announcement of Card f in found, the player learns the
        least amount of information, choose that one. 
        */
        
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }
}
