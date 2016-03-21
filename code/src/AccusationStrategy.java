/**
 * 
 * @author lauravandebraak
 */
class AccusationStrategy extends Strategy {
    //TODO: implement
    public AccusationStrategy() {
    }
    
    public CardSet strategy(KripkeModel model){
        return new CardSet(new Card(0,1), new Card(1,1));
    }

}
