
public class StrategySet {
	private SuspicionStrategy suspicion;
	private ResponseStrategy response;
	private AccusationStrategy accusation;
	
	public SuspicionStrategy getSuspicion() {
		return suspicion;
	}
	public void setSuspicion(SuspicionStrategy suspicion) {
		this.suspicion = suspicion;
	}
	public ResponseStrategy getResponse() {
		return response;
	}
	public void setResponse(ResponseStrategy response) {
		this.response = response;
	}
	public AccusationStrategy getAccusation() {
		return accusation;
	}
	public void setAccusation(AccusationStrategy accusation) {
		this.accusation = accusation;
	}

}
