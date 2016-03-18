/**
 * 
 * @author lauravandebraak
 */
class AccusationStrategy extends Strategy {

    public AccusationStrategy() {
    }
    
    public CardSet strategy(KripkeModel model){
        
        return new CardSet(new Card(1,1), new Card(2,1));
    }

}
