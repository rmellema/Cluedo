/**
* Class that implements players (agents).  
* @author lauravdbraak
*/


public class Player {
	private Strategy suspision = new SuspisionStrategy();
	private Strategy response = new ResponseStrategy();
	private Strategy accusation = new AccusationStrategy();

        //TODO: make this variable
        private CardSet hand;


	public Player(Card one, Card two){
            this.hand = new CardSet(one, two);
	}
        
        
        public CardSet accuse(KripkeModel model){
            return accusation.aStrategy(model);
        }
        
        public Card response(KripkeModel model, CardSet query){
            return response.rStrategy(model, query, hand);
        }
        
        public CardSet suspect(KripkeModel model){
            return suspision.sStrategy(model);
        }
        
}