/**
* Class that implements strategies
* @author lauravdbraak
*/


public abstract class Strategy {
    /**
     * strategy for accusations
     * @param model the current KripkeModel
     * @return the accusation to be made
     */
    CardSet aStrategy(KripkeModel model) {
        
        return new CardSet(new Card(1,1), new Card(2,1));
    }
    /**
     * strategy for suspicions
     * @param model the current KripkeModel
     * @return the CardSet suspicion (or null if none is made)
     */
    CardSet sStrategy(KripkeModel model) {
        
        return new CardSet(new Card(1,1), new Card(2,1));
    }
    
    /**
     * the response strategy
     * @param model the current KripkeModel
     * @param query the CardSet queried
     * @param hand the CardSet hand of the player
     * @return 
     */
    Card rStrategy(KripkeModel model, CardSet query, CardSet hand) {
        
        return new Card(1,1);
    }

}