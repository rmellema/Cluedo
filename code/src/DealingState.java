
public class DealingState {
	private Dealing dealing;
	private Card previous;
	
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
