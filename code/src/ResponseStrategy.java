
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
    
    /**
     * The strategy function for responses
     * @param model the current model
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
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
    
    /**
     * The default strategy: if the agent has multiple cards, show a random one
     * @param query the query made
     * @param hand the hand of the current agent
     * @return the card to show, null if they have none
     */
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
    
    /**
     * The simple strategy: if the agent knows the other agent knows one of the cards, show that one, else, choose a random card
     * @param model the current model
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
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
            if(new Know(agent, new Know(other, test.negate())).evaluate(model)){
                return c;
            }
        }
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }
    
    /**
     * The optimal strategy: if the agent knows the other agent knows one of the cards, show that one. Else, show the card that gives the least information.
     * @param model the current model
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
    private Card optimalStrategy(KripkeModel model, int agent, CardSet query, CardSet hand, int other) {
        int counter = 0, i, number, cat;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();
        double knowCopy, bestCount=0;
        Card bestCard;
 
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
            if(new Know(agent, new Know(other, test.negate())).evaluate(model)){
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
        
        bestCard = found.get(rand.nextInt(found.size()));
        
        for(Card f : found){
            knowCopy = 0;
            KripkeModel copy = new KripkeModel(model);
            copy.privateAnnouncement(new PropVar(f,agent), other);
            cat = f.getCategory();
            number = model.point().numberOfCards(f.getCategory());
            for(int n=0; n < number; n++){
                if(new Know(agent, new And(new Maybe(other,new PropVar(new Card(cat,n),0)), new Know(other, new PropVar(new Card(cat,n),0)).negate())).evaluate(copy)){
                    knowCopy++;
                }
            }
            if(knowCopy/cat > bestCount){
                bestCard = f;
            }
        }
        
        return bestCard;
    }
}
