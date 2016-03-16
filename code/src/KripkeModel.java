import java.util.HashSet;
import java.util.Set;

public class KripkeModel {
	private Set<Integer> states;
	private StateDealingMap sdMap;
	private RelationMatrix relations = new RelationMatrix();
	
	/**
	 * @param dealing Point of the model
	 * @param players Number of players
	 */
	public KripkeModel(Dealing point, int players) {
		sdMap = new StateDealingMap(point, players);
		
		//TODO: DEBUG
		System.out.println("Total nr of states: " + sdMap.size());
		
		states = new HashSet<Integer>();
		for (int stateNumber = 0; stateNumber != sdMap.size(); ++stateNumber) 
			states.add(stateNumber);
	}
	
	/**
	 * Deep copy (except for sdMap) of the KripkeModel. To save memory some sharing magic might be useful. But maybe we can do without. At least for the first version.
	 */
	public KripkeModel(KripkeModel other) {
		this.states = new HashSet<Integer>(other.states); // deep copy for now
		this.sdMap = other.sdMap; // no need for a deep copy
		this.relations = new RelationMatrix(other.relations); // deep copy for now
	}
	
	/**
	 * Removes all states from the Kripke model in which phi does not hold.
	 * @param phi Formula that is announced
	 */
	public void publicAnnouncement(Formula phi) {
		for (Integer state : states) {
			if (!phi.holds(sdMap.get(state)))
				removeState(state);
		}
	}
	
	/**
	 * Removes all relations of agent 'agent' from the Kripke model between 
	 * states where the evaluation of phi differs.
	 * @param phi Formula that is announced
	 */
	public void privateAnnouncement(Formula phi, Integer agent) {
		Set<Integer> holds = new HashSet<Integer>();
		Set<Integer> notHolds = new HashSet<Integer>();
		
		for (Integer state : states) {
			if (phi.holds(sdMap.get(state)))
				holds.add(state);
			else
				notHolds.add(state);
		}
		
		for (Integer holdState : holds)
			for (Integer notHoldState : notHolds)
				relations.removeRelation(holdState, notHoldState, agent);
	}
	
	
	private void removeState(Integer state) {
		states.remove(state);
		relations.removeState(state);
	}
	
	/**
	 * @return  Point of the model
	 */
	public final Dealing point() {
		return sdMap.point();
	}
	
	

}
