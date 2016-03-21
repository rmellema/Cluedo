
import java.util.Random;

/**
* Class that implements strategies
* @author lauravdbraak
*/


public class Strategy {
    /**
     * strategy for accusations
     * @param model the current KripkeModel
     * @return the accusation to be made
     */
    CardSet aStrategy(KripkeModel model, int agent) {
        //TODO: veilig maken
        int c = 0, n = 0, found = 0, number = 0;
        int cat = model.point().getCategories();
        Card[] cards = new Card[cat];
    
        
        for (c=0; c < cat; c++){
            number = model.point().numberOfCards(c);
            for (n=0; n < number; n++){
                Card test = new Card(c, n);
                if(new Know(agent, new PropVar(test, 0)).evaluate(model)){
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
     * strategy for suspicions
     * @param model the current KripkeModel
     * @return the CardSet suspicion (or null if none is made)
     */
    CardSet sStrategy(KripkeModel model, int number) {
        
        return new CardSet(new Card(0,1), new Card(1,1));
    }
    
    /**
     * the response strategy
     * @param model the current KripkeModel
     * @param query the CardSet queried
     * @param hand the CardSet hand of the player
     * @return 
     */
    Card rStrategy(KripkeModel model, CardSet query, CardSet hand) {
        
        int counter = 0, i = 0, f = 0, best = 0, idx = -1;
        int found[] = new int[query.size()];
        Random rand = new Random();
        
        for (f = 0; f < found.length; f++){
            found[f] = -1;
        }
        
        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                counter++;
                found[i] = 1;
            }
        }

        // If a player has only one of the cards queried, return that one
        if (counter == 1){
            for (f = 0; f < found.length; f++){
                if(found[f] == 1){
                    return hand.getCard(f);
                }
            }
        } else if (counter == 0) {
            return null;
        }
        
        // If a player has more than one of the cards queried, return a random card
        best = rand.nextInt(counter);
        
        for (f = 0; f < found.length; f++){
            if(best == 0){
                idx = f;
                break;
            }
            if(found[f] == 1){
                best--;
            }         
        }
        
        return hand.getCard(idx);
        
    }

}