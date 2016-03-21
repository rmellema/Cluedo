/**
* Class that implements players (agents).  
* @author lauravdbraak
*/


public class Player {
	private Strategy suspicion = new SuspicionStrategy();
	private Strategy response = new ResponseStrategy();
	private Strategy accusation = new AccusationStrategy();

        //TODO: make this variable
        private CardSet hand;

        /**
         * Constructor for Player class
         * @param one the first card in the player's hand
         * @param two the first card in the player's hand
         */
	public Player(Card one, Card two){
            this.hand = new CardSet(one, two);
	}

    public Player(CardSet hand) {
        this.hand = hand;
    }
        
        /**
         * the accusation function, determines an accusation
         * @param model the current KripkeModel
         * @return the accusation made
         */
        public CardSet accuse(KripkeModel model){
            return accusation.aStrategy(model);
        }
        
        /**
         * the response function, determines if a response is to be made
         * @param model the current KripkeModel
         * @param query the query made
         * @return the card response (if any, otherwise null)
         */
        public Card response(KripkeModel model, CardSet query){
            return response.rStrategy(model, query, hand);
        }
        
        /**
         * the suspect function, determines if a suspicion is pronounced
         * @param model the current KripkeModel
         * @return the suspicion to be made (if any, otherwise null)
         */
        public CardSet suspect(KripkeModel model){
            return suspicion.sStrategy(model);
        }
        
}