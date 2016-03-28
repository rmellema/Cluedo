/**
* Class that implements players (agents).  
* @author lauravdbraak
*/


public class Player {
	private SuspicionStrategy suspicion;
	private ResponseStrategy response;
	private AccusationStrategy accusation;

        //TODO: make this variable
        private CardSet hand;
        private int number;
        

        /**
         * Constructor for Player class
         * @param one the first card in the player's hand
         * @param two the first card in the player's hand
         * @param number the player's index number
         * @param suspicion the suspicion strategy
         * @param response the response strategy
         * @param accusation the accusation strategy
         */
	public Player(Card one, Card two, int number, String suspicion, String response, String accusation){
            this.hand = new CardSet(one, two);
            this.number = number;
            this.suspicion = new SuspicionStrategy(suspicion);
            this.response = new ResponseStrategy(response);
            this.accusation = new AccusationStrategy(accusation);
	}
        
        /**
         * Constructor for Player class
         * @param hand a CardSet cards that the player has
         * @param number the player's index number
         */
        public Player(CardSet hand, int number) {
            this.hand = hand;
            this.number = number;
            this.suspicion = new SuspicionStrategy("default");
            this.response = new ResponseStrategy("default");
            this.accusation = new AccusationStrategy("default");
        }
        
        /**
         * Constructor for Player class
         * @param hand a CardSet cards that the player has
         * @param number the player's index number
         * @param suspicion the suspicion strategy
         * @param response the response strategy
         * @param accusation the accusation strategy
         */
        public Player(CardSet hand, int number, String suspicion, String response, String accusation) {
            this.hand = hand;
            this.number = number;
            this.suspicion = new SuspicionStrategy(suspicion);
            this.response = new ResponseStrategy(response);
            this.accusation = new AccusationStrategy(accusation);
        }
        
        /**
         * the accusation function, determines an accusation
         * @param model the current KripkeModel
         * @return the accusation made
         */
        public CardSet accuse(KripkeModel model){
            return accusation.strategy(model, number);
        }
        
        /**
         * the response function, determines if a response is to be made
         * @param model the current KripkeModel
         * @param query the query made
         * @return the card response (if any, otherwise null)
         */
        public Card response(KripkeModel model, CardSet query){
            return response.strategy(model, number, query, hand);
        }
        
        /**
         * the suspect function, determines if a suspicion is pronounced
         * @param model the current KripkeModel
         * @return the suspicion to be made (if any, otherwise null)
         */
        public CardSet suspect(KripkeModel model){
            return suspicion.strategy(model, number);
        }

    public CardSet getHand() {
        return this.hand;
    }

    public int getNumber() {
        return this.number;
    }
}