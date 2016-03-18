/**
* Class that implements strategies
* @author lauravdbraak
*/


public abstract class Strategy {

    CardSet aStrategy(KripkeModel model) {
        
        return new CardSet(new Card(1,1), new Card(2,1));
    }
    
    CardSet sStrategy(KripkeModel model) {
        
        return new CardSet(new Card(1,1), new Card(2,1));
    }
    
    Card rStrategy(KripkeModel model) {
        
        return new Card(1,1);
    }

}