/**
* Class that implements players (agents).  
* @author lauravdbraak
*/


public class Player extends AbstractPlayer {
	private SuspicionStrategy suspicion;
	private ResponseStrategy response;
	private AccusationStrategy accusation;

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
	public Player(Card one, Card two, int number, KripkeModel model, String suspicion, String response, String accusation){
        super(model, number);
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
        public Player(CardSet hand, int number, KripkeModel model) {
            super(model, number);
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
        public Player(CardSet hand, int number, KripkeModel model, String suspicion, String response, String accusation) {
            super(model, number);
            this.hand = hand;
            this.number = number;
            this.suspicion = new SuspicionStrategy(suspicion);
            this.response = new ResponseStrategy(response);
            this.accusation = new AccusationStrategy(accusation);
        }
        
        /**
         * the accusation function, determines an accusation
         * @return the accusation made
         */
        public CardSet accuse(){
            return accusation.strategy(this);
        }
        
        /**
         * the response function, determines if a response is to be made
         * @param query the query made
         * @param other the agent requesting response
         * @return the card response (if any, otherwise null)
         */
        public Card response(CardSet query, int other){
            return response.strategy(this, query, hand, other);
        }
        
        /**
         * the suspect function, determines if a suspicion is pronounced
         * @return the suspicion to be made (if any, otherwise null)
         */
        public CardSet suspect(){
            return suspicion.strategy(this);
        }

    public CardSet getHand() {
        return this.hand;
    }

    public int getNumber() {
        return this.number;
    }
}