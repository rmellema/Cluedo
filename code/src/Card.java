/**
 * Represents a card as a category-number pair (e.g. weapon number 1).
 * @author lhboulogne
 *
 */
public class Card {

	private int category;
	private int number;

	/**
	 * Create a new card
	 * @param category The category of this card
	 * @param number The number of the card in the category
     */
	public Card(int category, int number) {
		this.category = category;
		this.number = number;
	}

    /**
     * Get the category of this card
     * @return The category
     */
	public int getCategory() {
		return category;
	}

    /**
     * Get the number of this card
     * @return The number
     */
	public int getNumber() {
		return number;
	}

    /**
     * Return the card as a string
     * @return String representation of card
     */
	public String toString() {
        return "c(" + this.category + ", " + this.number + ")";
    }

    /**
     * Check if this objects is equal to another object
     * @param o The object to compare to
     * @return `true` if the objects are equal, `false` otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Card) {
            Card other = (Card)o;
            return this.getCategory() == other.getCategory() &&
                    this.getNumber() == other.getNumber();
        }
        return false;
    }
}
