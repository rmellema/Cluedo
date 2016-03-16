
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class RelationMatrix {
	
	//Like a matrix that contains sets of agent numbers
	//All relations that are in the map are NOT in the Kripke Model
	private Map<Integer, Map<Integer, Set<Integer>>> jaggedMatrix 
		= new HashMap<Integer, Map<Integer, Set<Integer>>>();
			
	
	public RelationMatrix() {	}

	/**
	 * Deep copy (at least for now)
	 */
	public RelationMatrix(RelationMatrix other) {
		for (Map.Entry<Integer, Map<Integer, Set<Integer>>> entry : other.jaggedMatrix.entrySet())
			addDeepCopyOf(entry);
	}

	private void addDeepCopyOf(Entry<Integer, Map<Integer, Set<Integer>>> entry) {
		jaggedMatrix.put(entry.getKey(), deepCopyMap(entry.getValue()));
	}

	private Map<Integer, Set<Integer>> deepCopyMap(Map<Integer, Set<Integer>> map) {
		Map<Integer, Set<Integer>> m = new HashMap<Integer, Set<Integer>>();
		for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
			m.put(entry.getKey() , new HashSet<Integer>(entry.getValue()));
		}
		return m;
	}

	/**Removes the specified relation (both (from, to) and (to, from))
	 * 
	 * @param from State that the relation comes from
	 * @param to State that the relation goes to
	 * @param agent For which the relation has to be removed.
	 */
	public void removeRelation(Integer from, Integer to, Integer agent) {
		if (from > to) 
			removeRelation(to, from, agent);

		if (!jaggedMatrix.containsKey(from))
			jaggedMatrix.put(from, new HashMap<Integer, Set<Integer>>());

		if (!jaggedMatrix.get(from).containsKey(to))
			jaggedMatrix.get(from).put(to, new HashSet<Integer>());
		
		jaggedMatrix.get(from).get(to).add(agent);
	}

	/**
	 * Specified state is removed from the matrix
	 */
	public void removeState(Integer state) {
		jaggedMatrix.remove(state);
		for (Map<Integer, Set<Integer>> to : jaggedMatrix.values()) {
			to.remove(state);
		}
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
		if (from > to) 
			containsAny(to, from, agents);

		if (jaggedMatrix.containsKey(from) && 
		    jaggedMatrix.get(from).containsKey(to) &&
			jaggedMatrix.get(from).get(to).containsAll(agents))
			return false;
		
		return true;

	}
	

	/**
	 * returns whether the matrix contains specified relation for all of the specified agents.
	 */
	public boolean containsAll(Integer from, Integer to, Set<Integer> agents) {
		if (from > to) 
			containsAny(to, from, agents);
		
		//If there is no overlap between agents and the specified entry, return true
		if (jaggedMatrix.containsKey(from) && 
		    jaggedMatrix.get(from).containsKey(to))
		    for (Integer agent : jaggedMatrix.get(from).get(to))
		    	if (agents.contains(agent))
		    		return false;
		return true;
	}
	
	/** @return Set of states that are related for any agent from 'agents' to 'state' 
	 */
	public Set<Integer> related(Integer state, Set<Integer> agents) {
		Set<Integer> returnSet = new HashSet<Integer>();
		if (jaggedMatrix.containsKey(state)) {
			Set<Entry<Integer, Set<Integer>>> rels = jaggedMatrix.get(state).entrySet();
			for (Entry<Integer, Set<Integer>> rel : rels)
				if (!rel.getValue().containsAll(agents))
					returnSet.add(rel.getKey());
		}
		
		return returnSet;
	}
	
	/** @return Set of states that are related for all 'agents' to 'state' 
	 */
	public Set<Integer> allRelated(Integer state, Set<Integer> agents) {
		Set<Integer> returnSet = new HashSet<Integer>();
		if (jaggedMatrix.containsKey(state)) {
			Set<Entry<Integer, Set<Integer>>> rels = jaggedMatrix.get(state).entrySet();
			for (Entry<Integer, Set<Integer>> rel : rels) {
				boolean add = true;
				for (Integer agent : agents)
					if (rel.getValue().contains(agent)) {
						add = false;
						break;
					}
				if (add)
					returnSet.add(rel.getKey());
			}
		}
		return returnSet;
	}
	
}
