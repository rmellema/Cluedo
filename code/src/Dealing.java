import java.util.Arrays;

/**
 * A Dealing contains the information about which cards are held by whom and which 
 * cards are in the envelope. This information is represented as a 2D ragged array. 
 * The first index specifies the category of the card (e.g. 0th category means 
 * weapon) and the second index specifies the card number (e.g. 4th weapon means 
 * lead pipe). The entries in the array specify the number of the agent that is 
 * holding the card. An entry of 0 means that the card is in the envelope. An 
 * entry of -1 means that the specified card has not yet been dealt.
 * @author lhboulogne
 *
 */
public class Dealing {
	
	private int[][] dealing; 
	
	/**
	 * @param dealing List of getCategories with in each category a number of cards (indices) that are in the hands of a specific player (value) e.g. [1][2] = 3 means  player 3 has the second card of category 1.
	 */
	public Dealing(int[][] dealing) {
		//Make a deep copy of the dealing
		this.dealing = new int[dealing.length][];
		for (int it = 0; it < dealing.length; ++it){
			this.dealing[it] = Arrays.copyOf(dealing[it], dealing[it].length);
		}
	}
	/**
	 * Empty valuation with a specified size (empty means every card belongs to no one (-1)).
	 * @param sizes List of category sizes.
	 */
	public Dealing(int[] sizes) {
		dealing = new int[sizes.length][];
		for (int it = 0; it != sizes.length; ++it){
			dealing[it] = new int[sizes[it]];
			Arrays.fill(dealing[it], -1);
		}
	}
	
	/**
	 * @param other Valuation to be deep copied.
	 */
	public Dealing(Dealing other) {
		this(other.dealing);
	}
	
	/**
	 * @return number of categories (e.g. weapon) in the valuation
	 */
	public int getCategories() {
		return dealing.length;
	}
	
	/** 
	 * 
	 * @param category
	 * @return number of cards in the specified category
	 */
	public int numberOfCards(int category) {
		return dealing[category].length;
	}

        public int player(int category, int card) {
            return dealing[category][card];
        }
	
	/**
	 * @return Whether the propositional variable is true in this valuation
	 */
	public boolean isTrue(PropVar var){
		int category = var.getCard().getCategory();
		int cardnumber = var.getCard().getNumber();
		int player = var.getPlayer();
		return dealing[category][cardnumber] == player;
	}
	
	public void print() {
		for (int[] ar : dealing) {
			for (int num : ar) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * @return null if the card has already been dealt to a player. A new dealing with the card dealt to the specified player otherwise.
	 */
	public Dealing dealTo(int playerID, int category, int cardnumber) {
		Dealing returnDealing = new Dealing(this);
		if (returnDealing.dealing[category][cardnumber] != -1) {
			return null;
		}
		returnDealing.dealing[category][cardnumber] = playerID;
		return returnDealing;
	}
	
	public boolean equals(Dealing other) {
		if (dealing.length != other.dealing.length)
			return false;
		
		for (int category = 0; category != dealing.length; ++category) {
			if (dealing[category].length != other.dealing[category].length)
				return false;
			
			for (int cardNumber = 0; cardNumber != dealing[category].length; ++cardNumber) {
				if (dealing[category][cardNumber] != other.dealing[category][cardNumber])
					return false;
			}
		}
		return true;
	}
}
