
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class RelationHashMap extends Relations{
	
	//Like a matrix that contains sets of agent numbers
	//All relations that are in the map are NOT in the Kripke Model
	private Map<Integer, Map<Integer, Byte>> jaggedMatrix
		= new HashMap<Integer, Map<Integer, Byte>>();

	public RelationHashMap(){};
    /**
     * Deep copy (at least for now)
     */
    public RelationHashMap(RelationHashMap other) {
    	//Deep copy each entry of the map
        for (Map.Entry<Integer, Map<Integer, Byte>> entry : other.jaggedMatrix.entrySet())
            addDeepCopyOf(entry);
    }
    
	private void addDeepCopyOf(Entry<Integer, Map<Integer, Byte>> entry) {
		// Add this entry by its key (integer doesn't need deep copying) and a deep copy of the map that is its value
		jaggedMatrix.put(entry.getKey(), deepCopyMap(entry.getValue()));
	}

	private Map<Integer, Byte> deepCopyMap(Map<Integer, Byte> map) {
		// A deep copy of a map is made by:
		Map<Integer, Byte> m = new HashMap<Integer, Byte>();
		// For each entry
		for (Map.Entry<Integer, Byte> entry : map.entrySet()) {
			// add a new entry, that is a copy of this entry (integer and byte don't need deep copying)
			m.put(entry.getKey() , entry.getValue());
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
		// Since relations go both ways, only relations that where from < to are 'removed'
		if (from > to) 
			removeRelation(to, from, agent);

		// If no relation from state 'from' has yet been removed, create the HashMap for this.
		if (!jaggedMatrix.containsKey(from))
			jaggedMatrix.put(from, new HashMap<Integer, Byte>());

		// If no relation from 'from 'to 'to' has been removed, create the Byte for this.
		if (!jaggedMatrix.get(from).containsKey(to))
			jaggedMatrix.get(from).put(to, new Byte((byte)0));

		// Remove the relation by putting a 1 in the byte on the location of the agent for which the relation must be removed
        byte curVal = jaggedMatrix.get(from).get(to);
		jaggedMatrix.get(from).put(to, (byte)(curVal | (2 ^ agent)));
	}

	

	/**
	 * Specified states are removed from the matrix
	 */
	public void removeStates(Set<Integer> states) {
		for (Integer state : states)
			removeState(state);
	}
	
	/**
	 * Specified state is removed from the matrix
	 */
	public void removeState(Integer state) {
		// Remove all relations that originate from the state
		jaggedMatrix.remove(state);
		// Remove all relations that go to the state
		for (Map<Integer, Byte> to : jaggedMatrix.values()) {
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
		// Since relations go both ways, only relations that where from < to can have been 'removed'
		if (from > to) 
			containsAny(to, from, agents);
		
        byte mask = Relations.setToByte(agents);
        
        // If none of the relations have been removed, return false
		if (jaggedMatrix.containsKey(from) &&
		    jaggedMatrix.get(from).containsKey(to) &&
            (jaggedMatrix.get(from).get(to) & mask) == mask) {
            // If the bitvector & mask is equal to mask, all the agents'
            // relations from this state are removed
            return false;
        }
		return true;
	}
	
	/**
	 * returns whether the matrix contains specified relation for all of the specified agents.
	 */
	public boolean containsAll(Integer from, Integer to, Set<Integer> agents) {
		// Since relations go both ways, only relations that where from < to can have been 'removed'
		if (from > to)
			containsAll(to, from, agents);
        byte mask = Relations.setToByte(agents);

		//If there is no overlap between agents and the specified entry, return true
		if (jaggedMatrix.containsKey(from) && 
		    jaggedMatrix.get(from).containsKey(to) &&
                ((jaggedMatrix.get(from).get(to) & mask) != 0)) {
            return false;
        }
		return true;
	}

	//Erg lelijk op deze manier. Wss moet kripkemodel en Relations samengevoegd worden ofzo
	@Override
	public void privateAnnouncement(Formula phi, Integer agent, Set<Integer> holds, Set<Integer> notHolds) {
		//int it = 0;
		for (Integer holdState : holds){
			//TODO debug
			//System.out.println("Relations from " + ++it + "/" + holds.size() + " states in which phi holds have been removed.");
			for (Integer notHoldState : notHolds)
				removeRelation(holdState, notHoldState, agent);
		}
		
	}

}
