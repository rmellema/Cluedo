
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class for Response Strategies
 * @author lauravandebraak
 */
class ResponseStrategy extends Strategy {

    private String strategy;
    
    /**
     * The default constructor. If no strategy is given, it will be "default"
     */
    public ResponseStrategy() {
        this.strategy = "default";
    }
    
    
    /**
     * The constructor. Creates a ResponseStrategy with a certain strategy.
     * @param strat the strategy to be played
     */
    public ResponseStrategy(String strat){
        this.strategy = strat;
    }
    
    /**
     * The strategy function for responses
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
    public Card strategy(Player agent, CardSet query, CardSet hand, int other){
        if(strategy.equals("default")){
            return defaultStrategy(query, hand);
        }
        if(strategy.equals("simple")){
            return simpleStrategy(agent, query, hand, other);
        }
        if(strategy.equals("optimal")){
            return optimalStrategy(agent, query, hand, other);
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
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
    private Card simpleStrategy(Player agent, CardSet query, CardSet hand, int other){
        int counter = 0, i, best;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
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
            if (agent.doesKnow(new Know(other, test.negate()))) {
                return c;
            }
        }
        best = rand.nextInt(found.size());
        
        return found.get(best);
    }
    
    /**
     * The optimal strategy: if the agent knows the other agent knows one of the cards, show that one. Else, show the card that gives the least information.
     * @param agent the agent asked for response
     * @param query the query made
     * @param hand the hand of the current agent
     * @param other the agent querying
     * @return the card to show (null if none are in the hand)
     */
    private Card optimalStrategy(Player agent, CardSet query, CardSet hand, int other) {
        int counter = 0, i, number, cat;
        ArrayList<Card> found = new ArrayList<>();
        Random rand = new Random();
        double knowCopy, bestCount=0;
        Card bestCard;
 
        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
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
            if (agent.doesKnow(new Know(other, test.negate()))) {
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
            Set<Integer> others = new HashSet<>();
            others.add(other);
            cat = f.getCategory();
            number = agent.getCardsForCategory(cat);
            for(int n=0; n < number; n++){
                PropVar propVar = new PropVar(new Card(cat, n), 0);
                if (agent.doesKnow(new PrivateAnnouncement(others,
                        new PropVar(f, agent.getNumber()),
                        new And(new Maybe(other, propVar),
                                new Know(other, propVar).negate())))) {
                    knowCopy++;
                }
            }
            if(knowCopy/cat > bestCount){
                bestCard = f;
            }
        }
        return bestCard;
    }

    /**
     * Get the strategy played
     * @return the strategy played by this agent
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Get the different types of ResponseStrategy
     * @return a String[] of the possible types of ResponseStrategy
     */
    public static String[] getOptions() {
    	return new String[]{"default", "simple", "optimal"};
    }
}
