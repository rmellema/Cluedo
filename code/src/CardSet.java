/**
 *
 * @author lauravandebraak
 */
class CardSet {
    private Card[] cards;

    //TODO: make this variable!
    
    /**
     * the cardset constructor
     * @param cards the cards in this CardSet
     */
    public CardSet(Card... cards){
        this.cards = cards;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(this.cards[0]);
        for (int i = 1; i < this.cards.length; i++) {
            sb.append(", ");
            sb.append(this.cards[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Get the number of cards in this CardSet
     * @return Number of cards in CardSet
     */
    public int size() {
        return this.cards.length;
    }

    /**
     * Get the cards in this CardSet
     * @return Cards in the CardSet
     */
    public Card[] getCards() {
        return this.cards;
    }
}
