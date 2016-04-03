/**
 * A class that represents a specific moment during the dealing. The deck is seen as an ordered deck that the dealer cycles through.
 * This class is used to generate all possible states.
 * @author lhboulogne
 *
 */
public class DealingState {
	private Dealing dealing;
	private Card previous;
	
	/**
	 * @param dealing The dealing so far.
	 * @param previous The previous card dealt. 
	 */
	public DealingState(Dealing dealing, Card previous) {
		this.dealing = dealing;
		this.previous = previous;
	}
	
	public Dealing getDealing() {
		return dealing;
	}
	public Card getPrevious() {
		return previous;
	}
	
}
