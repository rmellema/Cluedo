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
        if (this.cards.length != 0)
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
     * Check if this CardSet contains a specific card
     * @param card The card to look for
     * @return `true` if `card` is in this set, `false` otherwise
     */
    public boolean contains(Card card) {
        for (Card c : this.cards) {
            if (c.equals(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the cards in this CardSet
     * @return Cards in the CardSet
     */
    public Card[] getCards() {
        return this.cards;
    }
    
    /**
     * Get a specific card in this CardSet
     * @param i The index of the card to get
     * @return Card at index `i`
     */
    public Card getCard(int i) {
        return this.cards[i];
    }
}
