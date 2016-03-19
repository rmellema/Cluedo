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
	
	public StateDealingMap(Dealing point, int players) {
		// Determine category sizes
		int[] categorySizes = new int[point.categories()];
		int totalCards = 0;
				
		for (int it = 0; it != point.categories(); ++it){
			categorySizes[it] =	point.numberOfCards(it);
			totalCards += point.numberOfCards(it);
		}
		// Add point to the map
        map.put(0, point);
        // Add all other dealings to the map
		buildMap(categorySizes, players, point, totalCards);
	}

	public Dealing get(Integer state) {
		return map.get(state);
	}

	public int size() {
		return map.size();
	}

	/**
	 * Builds the Hashmap
	 * @param categorySizes List of sizes of card categories
	 * @param players Number of players in the game
	 * @param point Point of pointed model
	 */
	private void buildMap(int[] categorySizes, int players, Dealing point, int totalCards) {
		Dealing empty = new Dealing(categorySizes);
		//Determine possible envelope contents
		ArrayList<Dealing> envelopeDealings = possibleEnvelopeDealings(0, empty, categorySizes);
		
		//TODO DEBUG
		System.out.println("nr of envelope dealings: " + envelopeDealings.size());
		
		//Determine how many cards a player can maximally get
		int cardsLeft = totalCards - categorySizes.length;
		int maxHandSize = cardsLeft/players;
		if (cardsLeft % players != 0) 
			maxHandSize += 1;
		
		//Deal cards to each player for all possible envelope contents
		for (Dealing envelopeDealing:envelopeDealings) {
			mapDealings(1, envelopeDealing, players, point, maxHandSize, cardsLeft);
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
		// If all a card of every category has been added
		if (catNr == categorySizes.length) {
			//Return the full envelope 
			returnDealing.add(soFar);
			return returnDealing;
		}
		
		//Otherwise, for every card of the current category
		for (int it = 0; it != categorySizes[catNr]; ++it) {
			//Put the card in the envelope
			Dealing newDealing = soFar.dealTo(0, catNr, it);
			//Make all combinations of cards from the next categories with this given card in the envelope
			returnDealing.addAll(possibleEnvelopeDealings(catNr+1, newDealing, categorySizes));
		}
		//Return all these combinations for each card of the current category
		return returnDealing;
	}
		
	/**
	 * Saves all dealings to the map for a given envelope dealing
	 * @param envelopeDealing Dealing in which the envelope already contains its cards
	 * @param players Number of players in the game
	 */
	private void mapDealings(int playerID, Dealing soFar, int players, Dealing point, int maxHandSize, int cardsLeft) {
		//If all cards have been dealt
		if (cardsLeft == 0) {
			//Safe the dealing to the map
			addToMap(soFar, point);
			// and return
			return;
		}
		
		//Determine the number of cards that should be dealt to the current player
		int cardsDealt = Math.min(maxHandSize,cardsLeft);
		//Otherwise, deal as many cards as should to the next player. 
		// Save all possible ways in which this can be done in a list
		ArrayList<Dealing> newDealings = dealNCardsTo(playerID, soFar, cardsDealt);

		// And for each possible way it can be done,
		for (Dealing newDealing : newDealings) {
			//continue dealing cards to the next player
			mapDealings(playerID+1, newDealing, players, point, maxHandSize, cardsLeft-cardsDealt);
		}
	}
	
	/**
	 * Wrapper function
	 * @param playerID
	 * @param soFar
	 * @param n number of cards to be dealt
	 */
	private ArrayList<Dealing> dealNCardsTo(int playerID, Dealing soFar, int n) {
		// This is actually a wrapper function. The real function keeps track of dealing states instead of dealings.
		// During the dealing the dealer goes through the sorted deck. 
		// A dealing state is specified by a dealing, and the card in the deck that the dealer is at. 
		// The dealer starts at the -1th card (since counting starts at 0) of the 0th category.
		ArrayList<DealingState> states = dealNCardsTo(playerID, new DealingState(soFar, new Card(0, -1)), n);
		
		// The dealings are retrieved from the returned dealing states 
		ArrayList<Dealing> returnDealings = new ArrayList<Dealing>();
		for (DealingState state : states) {
			returnDealings.add(state.getDealing());
		}
		// and returned
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
			//If only one card has to be dealt, deal the last card to the specified player
			return dealCardTo(playerID, state);
		}
		//Otherwise, deal a card to the specified player
		ArrayList<DealingState> returnDealingStates = new ArrayList<DealingState>();
		ArrayList<DealingState> newDealingStates = dealCardTo(playerID, state);
		//And for each way that this can be done.
		for (DealingState dealingState : newDealingStates) {
			// Deal the rest of the cards that need to be dealt in every possible way.
			returnDealingStates.addAll(dealNCardsTo(playerID, dealingState, n-1));
		}
		return returnDealingStates;
	}

	/**
	 * Saves a dealing to the map. If the dealing is the point, it is ignored, since it already has been added.
	 */
	private void addToMap(Dealing dealing, Dealing point) {
		// We don't want a duplicate of the point in the map. It has already been added.
		if (point.equals(dealing)) {
			return;
		}
		map.put(map.size(), dealing);
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
