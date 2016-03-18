/**
* Class that implements players (agents).  
* @author lauravdbraak
*/


public class Player {
	private Strategy suspision;
	private Strategy response;
	private Strategy accusastion;

	private KripkeModel model;


	public Player(){
		suspision = new SuspisionStrategy();
		response = new ResponseStrategy();
		accusastion = new AccusationStrategy();
	}
}