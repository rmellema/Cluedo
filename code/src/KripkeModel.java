import java.util.HashSet;
import java.util.Set;

public class KripkeModel {
	private Set<Integer> states;
	private StateDealingMap sdMap;
	//private Relations relations = new RelationHashMap();
	private Relations relations = new Relations(this);
	private int agents;
	
	/**
	 * @param point Point of the model
	 * @param players Number of players
	 */
	public KripkeModel(Dealing point, int players) {
        this.agents = players;
		
        sdMap = new StateDealingMap(point, players);
		
		states = new HashSet<Integer>();
		for (int stateNumber = 0; stateNumber != sdMap.size(); ++stateNumber) 
			states.add(stateNumber);
		
        dealCards(point);    
	}
	
	private void dealCards(Dealing point) {
		for (int category = 0; category < point.getCategories(); category++) {
            int cards = point.numberOfCards(category);
            for (int number = 0; number < cards; number++) {
                Card card = new Card(category, number);
                int agent = point.player(category, number);
                if (agent > 0) {
                    this.privateAnnouncement(new PropVar(card, agent), agent);
                }
            }
        }
	}
	

    private void queryCheck(Integer state, Set<Integer> agents) {
    	if (!hasState(state)) 
    		throw new RuntimeException("Queried relations from a non-existing state.");
		for (int agent : agents) 
			agentCheck(agent);
	}

	private void agentCheck(int agent) {
		if (agent < 1 || agent > this.agents) 
    		throw new RuntimeException("Queried relations of a non-existing agent.");
	}
	
	/**
	 * Deep copy (except for sdMap) of the KripkeModel. To save memory some sharing magic might be useful. But maybe we can do without. At least for the first version.
	 */
	public KripkeModel(KripkeModel other) {
        this.agents = other.getAgents();
		this.states = new HashSet<Integer>(other.states); // deep copy for now
		this.sdMap = other.sdMap; // no need for a deep copy
		
		this.relations = new Relations(other.relations);
	}
	
	/**
	 * Removes all states from the Kripke model in which phi does not hold.
	 * @param phi Formula that is announced
	 */
	public void publicAnnouncement(Formula phi) {
		Set<Integer> removeStates = new HashSet<Integer>();
		for (Integer state : states) {
			if (!phi.evaluate(this, state))
				removeStates.add(state);
		}
		states.removeAll(removeStates);
	}
	
	/**
	 * Removes all relations of agent 'agent' from the Kripke model between 
	 * states where the evaluation of phi differs.
	 * @param phi Formula that is announced
	 */
	public void privateAnnouncement(Formula phi, Integer agent) {
		relations.privateAnnouncement(phi, agent);
	}
	
	/**
	 * @return  Point of the model
	 */
	public final Dealing point() {
		return sdMap.point();
	}

	public Dealing getDealing(int state) {
		if (this.states.contains(state))
            return sdMap.getValuation(state);
        else
            return null;
    }

    public boolean hasState(int state) {
        return this.states.contains(state);
    }

	/**
     * Builds the set of states that are reachable in one step from `state` for
     * any of the agents in `agents`.
     * @param state The state to get the reachable states from
     * @param agents The agents to take into account
     * @return The set of states
	 */
	public Set<Integer> getReachableStates(Integer state, Set<Integer> agents) {
		queryCheck(state, agents);
		
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
		Set<Integer> agents = new HashSet<Integer>();
		agents.add(agent);
		return this.getReachableStates(state, agents);
	}

    /**
     * Get the set of states that are reachable in one step from `state` for
     * all agents in `agents`.
     * @param state The state to get the reachable states from
     * @param agents The agents that must all be able to reach that world
     * @return The set of states that all agents can reach from the state
     */
	public Set<Integer> getReachableStatesForAll(Integer state, Set<Integer> agents) {
		queryCheck(state, agents);
		
        Set<Integer> ret = new HashSet<>();
        for (Integer s : states) {
            if (relations.containsAll(state, s, agents)) {
                ret.add(s);
            }
        }
        return ret;
	}

    /**
     * Get the number of agents in this model
     * @return The number of agents
     */
    public int getAgents() {
        return agents;
    }
}
