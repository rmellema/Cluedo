import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Just a wrapper around the set of states that are left 
 * and sets of private announcements that have been done per agent.
 */
public class RelationVoid extends Relations {

	// Exactly the same as in KripkeModel. If this implementation is used in the 
	// end version, we should consider deleting one of the two sets
	private KripkeModel parent;
	private Map<Integer, Set<Formula>> announcements = new HashMap<Integer, Set<Formula>>();
	
	public RelationVoid(KripkeModel parent) {
		this.parent = parent;
	}

	public RelationVoid(RelationVoid other) {
		this(other.parent);
		//Shallow copy of formulas is ok since they are not altered
		for (Entry<Integer, Set<Formula>> entry : other.announcements.entrySet())
			for (Formula phi : entry.getValue())
				privateAnnouncement(phi, entry.getKey(), null, null);
	}


	@Override
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

	@Override
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

	@Override
	public void removeState(Integer state) {
	}

	//Erg lelijk op deze manier. Wss moet kripkemodel en Relations samengevoegd worden ofzo
	@Override
	public void privateAnnouncement(Formula phi, Integer agent, Set<Integer> states, Set<Integer> notHolds) {
		if (!announcements.containsKey(agent))
			announcements.put(agent, new HashSet<Formula>());
		announcements.get(agent).add(phi);
	}

}
