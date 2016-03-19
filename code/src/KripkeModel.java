import java.util.HashSet;
import java.util.Set;

public class KripkeModel {
	private Set<Integer> states;
	private StateDealingMap sdMap;
	//private Relations relations = new RelationHashMap();
	private Relations relations = new RelationVoid(this);
	private int agents;
	
	/**
	 * @param point Point of the model
	 * @param players Number of players
	 */
	public KripkeModel(Dealing point, int players) {
        this.agents = players;
		sdMap = new StateDealingMap(point, players);
		//TODO: DEBUG
		System.out.println("Total nr of states: " + sdMap.size());

		states = new HashSet<Integer>();
		for (int stateNumber = 0; stateNumber != sdMap.size(); ++stateNumber) 
			states.add(stateNumber);
        System.out.println("Added all states: " + states.size());
        
        //this.publicAnnouncement(new Or(new PropVar(new Card(0, 2), 1) , new PropVar(new Card(0, 3), 1)));
        //System.out.println("After public announcement: " + states.size() + " states left.");
        
        for (int category = 0; category < point.categories(); category++) {
            int cards = point.numberOfCards(category);
            for (int number = 0; number < cards; number++) {
                Card card = new Card(category, number);
                int agent = point.player(category, number);
                if (agent > 0) {
                    System.out.println("Giving card "+ number +" from category "+ category +" to agent " + agent);
                    this.privateAnnouncement(new PropVar(card, agent), agent);
                }
            }
        }
        
        System.out.println("Done making model");
        
        Set<Integer> allAgents = new HashSet<Integer>();
        for (int player = 1; player <= players; ++player)
        	allAgents.add(player);
        	
        Set<Integer> reachable = getReachableStates(0, allAgents);
        Set<Integer> reachableAll = getReachableStatesForAll(0, allAgents);
        
        System.out.println("In the point:\n" +
        		reachable.size() + "/" +states.size()+ " state(s) still reachable by any agent from the point.\n" +
        		reachableAll.size() + "/" +states.size()+ " state(s) still reachable by all agents from the point.\n");
        
        System.out.println("Die " + reachable.size() +" klopt: Alle states waarin de kaarten die in \n" +
        		"s0 in de envelop zitten in de envelop zitten, maar \n" +
        		"waar de kaarten tussen de spelers voor iedere speler\n" +
        		"anders zijn verdeeld dan in s0 vallen weg. Iedere \n" +
        		"speler weet immers zijn/haar kaarten."); 
        
        
        //for (int s = 0; s < sdMap.size(); ++s)
        //	if (!reachable.contains(s))
        //		sdMap.get(s).print();
        	
        //for (Integer s : reachableAll)
        //	sdMap.get(s).print();
        
	}
	
	/**
	 * Deep copy (except for sdMap) of the KripkeModel. To save memory some sharing magic might be useful. But maybe we can do without. At least for the first version.
	 */
	public KripkeModel(KripkeModel other) {
        this.agents = other.getAgents();
		this.states = new HashSet<Integer>(other.states); // deep copy for now
		this.sdMap = other.sdMap; // no need for a deep copy
		
		if (other.relations instanceof RelationHashMap) 
			this.relations = new RelationHashMap((RelationHashMap) other.relations);
		else if (other.relations instanceof RelationVoid)
			this.relations = new RelationVoid((RelationVoid) other.relations);
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
		relations.removeStates(removeStates);
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
		relations.privateAnnouncement(phi, agent, holds, notHolds);
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
