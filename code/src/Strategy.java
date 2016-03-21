
import java.util.ArrayList;
import java.util.Random;

/**
* Class that implements strategies
* @author lauravdbraak
*/


public class Strategy {
    private int[] arrayTo(int num) {
        int[] ret = new int[num];
        for (int i = 0; i< num; i++) {
            ret[i] = i;
        }
        return ret;
    }

    public int[] shuffle(int[] array) {
        Random rn = new Random();
        for (int i = array.length - 1; i> 0; i--) {
            int idx = rn.nextInt(i + 1);
            int a = array[idx];
            array[idx] = array[i];
            array[i] = a;
        }
        return array;
    }
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
                if((new Know(agent, new PropVar(test, 0))).evaluate(model)) {
                    System.out.println("Agent knows card " + test);
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
    CardSet sStrategy(KripkeModel model, int agent) {
        
        int c = 0, number = 0, s = 0;
        int cat = model.point().getCategories();
        Card[] cards = new Card[cat];
        Random rand = new Random();
        
        for (c=0; c < cat; c++){
            s = 0;
            number = model.point().numberOfCards(c);

            for (int r : shuffle(arrayTo(number))) {
                Card card = new Card(c, r);
                System.out.println(card);
                PropVar test = new PropVar(card, 0);
                if((new Maybe(agent, test)).evaluate(model) &&
                        (new Know(agent, test)).negate().evaluate(model)) {
                    cards[c] = card;
                    break;
                }
            }
            if (cards[c] == null) {
                cards[c] = new Card(c, rand.nextInt(number));
            }
        }
        return new CardSet(cards);
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
        ArrayList<Card> found = new ArrayList<Card>();
        Random rand = new Random();

        for(i = 0; i < query.size(); i++){
            if (hand.contains(query.getCard(i))){
                System.out.println("Have card: " + query.getCard(i).toString());
                counter++;
                found.add(query.getCard(i));
            }
        }

        // If a player has only one of the cards queried, return that one
        if (counter == 1){
            return found.get(0);
        } else if (counter == 0) {
            return null;
        }
        
        // If a player has more than one of the cards queried, return a random card
        best = rand.nextInt(found.size());
        
        return found.get(best);
        
    }

}