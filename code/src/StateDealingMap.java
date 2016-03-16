import java.util.ArrayList;
import java.util.HashMap;
/**
 * Maps states to valuations. These valuations are represented as possible dealings. State 0 is the point of the model.
 * As of now, when constructing the map, the program does not take smart advantage of the fact that when a card already has bean dealt, it cannot be dealt again. We could implement a deck that becomes smaller, as more cards are dealt. When we do this, we don't have to check whether a card already has been dealt in a Dealing. The algorithm will maximally double in speed when this is implemented. A map construction starting at the point of the model might be a better idea.
 * @author lhboulogne
 *
 */
public class StateDealingMap {
	private HashMap<Integer, Dealing> map = new HashMap<Integer, Dealing>();
	private Integer counter = 1;
	
	public StateDealingMap(Dealing point, int players) {
		// Determine category sizes
		int[] categorySizes = new int[point.categories()];
		int totalCards = 0;
				
		for (int it = 0; it != point.categories(); ++it){
			categorySizes[it] =	point.numberOfCards(it);
			totalCards += point.numberOfCards(it);
		}

		buildMap(categorySizes, players, point, totalCards);
	}

	/**
	 * Builds the Hashmap
	 * @param categorySizes List of sizes of card categories
	 * @param players Number of players in the game
	 * @param point Point of pointed model
	 */
	public int size() {
		return map.size();
	}
	
	private void buildMap(int[] categorySizes, int players, Dealing point, int totalCards) {
		Dealing empty = new Dealing(categorySizes);
		ArrayList<Dealing> envelopeDealings = possibleEnvelopeDealings(0, empty, categorySizes);
		
		//TODO DEBUGCOMMENT
		System.out.println("nr of envelope dealings: " + envelopeDealings.size());
		
		int cardsLeft = totalCards - categorySizes.length;
		int maxHandSize = cardsLeft/players;
		if (cardsLeft % players != 0) 
			maxHandSize += 1;
		
		for (Dealing envelopeDealing:envelopeDealings) {
			mapDealings(1, envelopeDealing, players, point, maxHandSize);
		}
	}
	
	/**
	 * 
	 * @param catNr Current category number
	 * @param soFar Dealing to the envelope so far
	 * @param categorySizes List of sizes of card categories
	 * @return A list of all possible dealings to the envelope in which cards are only dealt to the envelope
	 */
	private ArrayList<Dealing> possibleEnvelopeDealings(int catNr, Dealing soFar, int[] categorySizes) {
		ArrayList<Dealing> returnDealing = new ArrayList<Dealing>();
		if (catNr == categorySizes.length) {
			returnDealing.add(soFar);
			return returnDealing;
		}
		
		for (int it = 0; it != categorySizes[catNr]; ++it) {
			Dealing newDealing = soFar.dealTo(0, catNr, it);
			returnDealing.addAll(possibleEnvelopeDealings(catNr+1, newDealing, categorySizes));
		}
		return returnDealing;
	}
		
	/**
	 * Saves all dealings to the map for a given envelope dealing
	 * @param envelopeDealing Dealing in which the envelope already contains its cards
	 * @param players Number of players in the game
	 */
	
	private void mapDealings(int playerID, Dealing soFar, int players, Dealing point, int maxHandSize) {
		if (playerID > players) {
			addToMap(soFar, point);
			return;
		}
		
		ArrayList<Dealing> newDealings = dealNCardsTo(playerID, soFar, maxHandSize);
		
		for (Dealing newDealing : newDealings) {
			mapDealings(playerID+1, newDealing, players, point, maxHandSize);
		}
	}
	
	/**
	 * Wrapper function
	 * @param playerID
	 * @param soFar
	 * @param n number of cards to be dealt
	 * @return 
	 */
	private ArrayList<Dealing> dealNCardsTo(int playerID, Dealing soFar, int n) {
		ArrayList<DealingState> states = dealNCardsTo(playerID, new DealingState(soFar, new Card(0, -1)), n);
		ArrayList<Dealing> returnDealings = new ArrayList<Dealing>();
		for (DealingState state : states) {
			returnDealings.add(state.getDealing());
		}
		return returnDealings;
	}
	
	/**
	 * 
	 * @param playerID
	 * @param soFar
	 * @param n
	 * @param previous Previous card that was dealt. New cards should be drawn from further in the deck so that there cannot be duplicate states.
	 * @return Individual ways in which player playerID can get n cards dealt
	 */
	private ArrayList<DealingState> dealNCardsTo(int playerID, DealingState state, int n) {
		if (n == 1){
			return dealCardTo(playerID, state);
		}
		ArrayList<DealingState> returnDealingStates = new ArrayList<DealingState>();
		ArrayList<DealingState> newDealingStates = dealCardTo(playerID, state);
		for (DealingState dealingState : newDealingStates) {
			returnDealingStates.addAll(dealNCardsTo(playerID, dealingState, n-1));
		}///////////////////
		return returnDealingStates;
	}

	/**
	 * Saves a dealing to the map. If the dealing is the point, it becomes state 0.
	 */
	private void addToMap(Dealing dealing, Dealing point) {
		if (point.equals(dealing)) {
			map.put(0, dealing);
			return;
		}
		map.put(counter, dealing);
		++counter;
	}
	/**
	 * @return List of all possible dealings that result from soFar when player playerID gets any new card
	 */
	private ArrayList<DealingState> dealCardTo(int playerID, DealingState state) {
		ArrayList<DealingState> returnDealingStates = new ArrayList<DealingState>();
		//For all categories including the current one
		for (int cat = state.getPrevious().getCategory(); cat != state.getDealing().categories(); ++cat) {
			int cardNr = 0;
			//For all cards further in the deck
			if (cat == state.getPrevious().getCategory())
				cardNr = state.getPrevious().getNumber() + 1;
			for (; cardNr != state.getDealing().numberOfCards(cat); ++cardNr) {
				//Give a new card to the specified player and safe this card as the previously dealt one
				Dealing newDealing = state.getDealing().dealTo(playerID, cat, cardNr);
				if (newDealing != null) {
					returnDealingStates.add(new DealingState(newDealing, new Card(cat, cardNr)));
				}
			}
		}
		return returnDealingStates;
	}
	
	public final Dealing point() {
		return map.get(0);
	}

    public final Dealing getValuation(int state) {
        return map.get(state);
    }
}
