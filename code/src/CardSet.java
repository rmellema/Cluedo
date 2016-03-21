/**
 *
 * @author lauravandebraak
 */
class CardSet {
    private Card[] cards;

    /**
     * Create a new CardSet
     * @param cards The cards in this CardSet
     */
    public CardSet(Card... cards){
        this.cards = cards;
    }

    /**
     * Create a string representation of this CardSet
     * @return String representation
     */
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
