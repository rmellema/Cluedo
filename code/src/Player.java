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
         * @param model the model played in
         * @param suspicion the suspicion strategy
         * @param response the response strategy
         * @param accusation the accusation strategy
         */
	public Player(Card one, Card two, int number, KripkeModel model, String suspicion, String response, String accusation){
        this(new CardSet(one, two), number, model, suspicion, response, accusation);
	}

        /**
         * Constructor for Player class
         * @param hand a CardSet cards that the player has
         * @param number the player's index number
         * @param model the model played in
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
         * @param model the model played in
         * @param suspicion the suspicion strategy
         * @param response the response strategy
         * @param accusation the accusation strategy
         */
        public Player(CardSet hand, int number, KripkeModel model, String suspicion, String response, String accusation) {
            this(hand, number, model, new SuspicionStrategy(suspicion), 
            		new ResponseStrategy(response),  
            		new AccusationStrategy(accusation));
        }

        /**
         * Constructor for Player class
         * @param hand a CardSet cards that the player has
         * @param number the player's index number
         * @param model the model played in
         * @param suspicion the suspicion strategy
         * @param response the response strategy
         * @param accusation the accusation strategy
         */
        public Player(CardSet hand, int number, KripkeModel model,
				SuspicionStrategy suspicion, ResponseStrategy response,
				AccusationStrategy accusation) {
            super(model, number);
            System.out.println("CONSTRUCTING PLAYER"); //TODO 
        	this.hand = hand;
            this.number = number;
            this.suspicion = suspicion;
            this.response = response;
            this.accusation = accusation;
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
        
        /**
         * Get the Hand of the agent
         * @return the hand of the agent
         */
        public CardSet getHand() {
            return this.hand;
        }

        /**
         * Get the number of the agent in the model
         * @return the number of the agent
         */
        public int getNumber() {
            return this.number;
        }

        /**
         * Get the strategies played by this agent
         * @return a StrategySet of Strategies played
         */
        public StrategySet getStrategies() {
            return new StrategySet(this.suspicion, this.response, this.accusation);
        }

        /**
         * Get the AccusationStrategy played
         * @return the AccusationStrategy played
         */
        public String getAccusationStrategy() {
            return this.accusation.getStrategy();
        }

        /**
         * Get the ResponseStrategy played
         * @return the ResponseStrategy played
         */
        public String getResponseStrategy() {
            return this.response.getStrategy();
        }

        /**
         * Get the SuspicionStrategy played
         * @return the SuspicionStrategy played
         */
        public String getSuspicionStrategy() {
            return this.suspicion.getStrategy();
        }

}