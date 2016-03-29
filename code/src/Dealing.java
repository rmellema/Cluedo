import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
	private Random rand = new Random();
	
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
	
	public void deal(int playerID, int category, int cardnumber) {
		if (this.dealing[category][cardnumber] != -1) {
			throw new RuntimeException("Tried to deal c("+category+", "+cardnumber+") to agent #"+playerID+", while this card already belongs to agent #"+dealing[category][cardnumber]+".");
		}
		this.dealing[category][cardnumber] = playerID;
	}
	
	public void deal(int agent, Card card) {
		deal(agent, card.getCategory(), card.getNumber());
	}
	
	/**
	 * Returns a random dealing to the specified number of agents
	 * @param agents Number of agents to be dealt to.
	 */
	public void randomize(int agents) {
		// Make this dealing empty
		int[] categorySizes = new int[dealing.length];
		for (int cat = 0; cat != dealing.length; ++cat)
			categorySizes[cat] = dealing[cat].length;
		Dealing empty = new Dealing(categorySizes);
		dealing = empty.dealing;
		
		// Random envelope dealing
		for (int cat = 0; cat != dealing.length; ++cat)
			dealing[cat][rand.nextInt(categorySizes[cat])] = 0;
		
		// Random card dealing to players;
			// first construct a list of all cards in the game
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (int cat = 0; cat != dealing.length; ++cat)
			for (int number = 0; number != categorySizes[cat]; ++number) {
				Card card = new Card(cat, number);
				if (!isTrue(new PropVar(card, 0))) //that are not in the envelope
					cardList.add(card);
			}
		
			// then deal these cards randomly;
		randomDealing(1, agents, cardList);
	}
	
	// agent is the agent that is currently being dealt to
	private void randomDealing(int agent, int agents, ArrayList<Card> cardList) {
		if (cardList.isEmpty())
			return;
		if (agent > agents)
			agent = 1;
		
		int cardIdx = rand.nextInt(cardList.size());
		deal(agent, cardList.get(cardIdx));
		cardList.remove(cardIdx);
		randomDealing(agent+1, agents, cardList);
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
	
	/**
	 * @return Current dealing with only cards dealt to the envelope. Other cards have not been dealt yet.
	 */
	public Dealing envelopeDealing() {
		Dealing envelopeDealing = new Dealing(this);
		for (int category = 0; category != envelopeDealing.dealing.length; ++category) {
			for (int cardNumber = 0; cardNumber != envelopeDealing.dealing[category].length; ++cardNumber) {
				if (envelopeDealing.dealing[category][cardNumber] != 0)
					envelopeDealing.dealing[category][cardNumber] = -1;
			}
		}
		return envelopeDealing;
	}
}
