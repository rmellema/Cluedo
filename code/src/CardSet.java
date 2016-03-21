/**
 *
 * @author lauravandebraak
 */
class CardSet {
    private Card one;
    private Card two;
    
    
    //TODO: make this variable!
    
    /**
     * the cardset constructor
     * @param one the first card
     * @param two the second card
     */
    public CardSet(Card one, Card two){
        this.one = one;
        this.two = two;
    }

    public String toString() {
        return "[" + one.toString() + ", " + two.toString() + "]";
    }
}
