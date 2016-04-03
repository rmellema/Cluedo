import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class Relations {
	
	private KripkeModel parent;
	private Map<Integer, Set<Formula>> announcements = new HashMap<Integer, Set<Formula>>();
	
	/**
	 * Initialises a relation matrix in which all possible relations are present.
	 * @param parent KripkeModel instance that this Relations belongs to.
	 */
	public Relations(KripkeModel parent) {
		this.parent = parent;
	}

	/**
	 * Makes a deep copy
	 * @param other The to be deep copied Relations instance
	 */
	public Relations(Relations other) {
		this(other.parent);
		//Shallow copy of formulas is ok since they are not altered
		for (Entry<Integer, Set<Formula>> entry : other.announcements.entrySet())
			for (Formula phi : entry.getValue())
				privateAnnouncement(phi, entry.getKey());
	}
	
	/**
	 * @param from One of the states involved in the relation. This can be switched with the to parameter without consequences since the Kripke model is in S5.
	 * @param to One of the states involved in the relation. This can be switched with the from parameter without consequences since the Kripke model is in S5.
	 * @param agent The agent for which the function checks whether the queried relation holds.
	 * @return Whether the matrix contains specified relation for the specified agent.
	 */
	public boolean contains(Integer from, Integer to, Integer agent) {
		return containsAny(from, to, new HashSet<Integer>(agent));
	}

	/**
	 * @param from One of the states involved in the relation. This can be switched with the to parameter without consequences since the Kripke model is in S5.
	 * @param to One of the states involved in the relation. This can be switched with the from parameter without consequences since the Kripke model is in S5.
	 * @param agents The agents for which the function checks whether the queried relation holds.
	 * @return Whether the matrix contains specified relation for any of the specified agents.
	 */
	public boolean containsAny(Integer from, Integer to, Set<Integer> agents) {
		// for any agent
		for (Integer agent : agents) {
			
			//If there are no private announcements done to the agent, the relation is still there for that agent
			if (!announcements.containsKey(agent))
				return true;
			
			boolean relationStillThere = true;
			// otherwise, if no private announcement crossed away the specified relation
			for (Formula phi : announcements.get(agent))
				if (phi.evaluate(parent, from) != phi.evaluate(parent, to))
					relationStillThere = false;
			if (relationStillThere)
				// Then there is an agent for which this relation is still there
				return true;
		}		
		return false;
	}
	
	/**
	 * @param from One of the states involved in the relation. This can be switched with the to parameter without consequences since the Kripke model is in S5.
	 * @param to One of the states involved in the relation. This can be switched with the from parameter without consequences since the Kripke model is in S5.
	 * @param agents The agents for which the function checks whether the queried relation holds.
	 * @return Whether the matrix contains specified relation for all of the specified agents.
	 */
	public boolean containsAll(Integer from, Integer to, Set<Integer> agents) {
		// if for any agent
		for (Integer agent : agents)
			// a private announcement crossed away the specified relation
			if (announcements.containsKey(agent))
				for (Formula phi : announcements.get(agent))
					if (phi.evaluate(parent, from) != phi.evaluate(parent, to))
						// Then the relation does not hold for all agents.
						return false;
		
		return true;
	}
	/**
	 * Updates the relations according to the specified private announcement
	 * @param phi Formula that is announced
	 * @param agent Agent to which the private announcement is done
	 */
	public void privateAnnouncement(Formula phi, Integer agent) {
			if (!announcements.containsKey(agent))
				announcements.put(agent, new HashSet<Formula>());
			announcements.get(agent).add(phi);
		}

}
