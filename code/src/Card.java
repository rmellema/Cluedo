/**
 * Represents a card as a category-number pair (e.g. weapon number 1).
 * @author lhboulogne
 *
 */
public class Card {

	private int category;
	private int number;
	
	public Card(int category, int number) {
		this.category = category;
		this.number = number;
	}

	public int getCategory() {
		return category;
	}

	public int getNumber() {
		return number;
	}

	public String toString() {
        return "c(" + this.category + ", " + this.number + ")";
    }
}
