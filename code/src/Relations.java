import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class Relations {
	
	private KripkeModel parent;
	private Map<Integer, Set<Formula>> announcements = new HashMap<Integer, Set<Formula>>();
	
	public Relations(KripkeModel parent) {
		this.parent = parent;
	}

	public Relations(Relations other) {
		this(other.parent);
		//Shallow copy of formulas is ok since they are not altered
		for (Entry<Integer, Set<Formula>> entry : other.announcements.entrySet())
			for (Formula phi : entry.getValue())
				privateAnnouncement(phi, entry.getKey());
	}
    
	/**
	 * returns whether the matrix contains specified relation for the specified agent.
	 */
	public boolean contains(Integer from, Integer to, Integer agent) {
		return containsAny(from, to, new HashSet<Integer>(agent));
	}

	/**
	 * returns whether the matrix contains specified relation for any of the specified agents.
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
	 * returns whether the matrix contains specified relation for all of the specified agents.
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
	 * Handles private announcement phi to agent
	 * @param states States left in the Kripke model
	 */
	public void privateAnnouncement(Formula phi, Integer agent) {
			if (!announcements.containsKey(agent))
				announcements.put(agent, new HashSet<Formula>());
			announcements.get(agent).add(phi);
		}

}
