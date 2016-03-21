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
}
