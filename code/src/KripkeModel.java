import java.util.HashSet;
import java.util.Set;

public class KripkeModel {
	private Set<Integer> states;
	private StateDealingMap sdMap;
	private RelationMatrix relations = new RelationMatrix();
	
	/**
	 * @param point Point of the model
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
			if (!phi.evaluate(this, state))
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
			if (phi.evaluate(this, state))
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

	public Dealing getDealing(int state) {
        return sdMap.getValuation(state);
    }

	/**
     * Builds the set of states that are reachable in one step from `state` for
     * any of the agents in `agents`.
     * @param state The state to get the reachable states from
     * @param agents The agents to take into account
     * @return The set of states
	 */
	public Set<Integer> getReachableStates(Integer state, Set<Integer> agents) {
		Set<Integer> ret = new HashSet<>();
        for (Integer s : states) {
            if (relations.containsAny(state, s, agents)) {
                ret.add(s);
            }
        }
        return ret;
	}

    /**
     * Builds the set of states that `agent` can distinguish from `state`
     * @param state The state to see if they are different
     * @param agent The agent to look for differences
     * @return The set of states
     */
	public Set<Integer> getReachableStates(Integer state, Integer agent) {
		return this.getReachableStates(state, new HashSet<Integer>(agent));
	}

    /**
     * Get the set of states that are reachable in one step from `state` for
     * all agents in `agents`.
     * @param state The state to get the reachable states from
     * @param agents The agents that must all be able to reach that world
     * @return The set of states that all agents can reach from the state
     */
	public Set<Integer> getReachableStatesForall(Integer state, Set<Integer> agents) {
        Set<Integer> ret = new HashSet<>();
        for (Integer s : states) {
            if (relations.containsAll(state, s, agents)) {
                ret.add(s);
            }
        }
        return ret;
	}

}
