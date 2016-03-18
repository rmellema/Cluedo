
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class RelationMatrix {
	
	//Like a matrix that contains sets of agent numbers
	//All relations that are in the map are NOT in the Kripke Model
	private Map<Integer, Map<Integer, Byte>> jaggedMatrix
		= new HashMap<Integer, Map<Integer, Byte>>();
			
	
	public RelationMatrix() {	}

    /**
     * Deep copy (at least for now)
     */
    public RelationMatrix(RelationMatrix other) {
        for (Map.Entry<Integer, Map<Integer, Byte>> entry : other.jaggedMatrix.entrySet())
            addDeepCopyOf(entry);
    }

    public static byte setToByte(Set<Integer> agents) {
        byte ret = 0;
        for (Integer agent : agents) {
            ret = (byte)(ret | (1 << (agent -1)));
        }
        return ret;
    }

	private void addDeepCopyOf(Entry<Integer, Map<Integer, Byte>> entry) {
		jaggedMatrix.put(entry.getKey(), deepCopyMap(entry.getValue()));
	}

	private Map<Integer, Byte> deepCopyMap(Map<Integer, Byte> map) {
		Map<Integer, Byte> m = new HashMap<Integer, Byte>();
		for (Map.Entry<Integer, Byte> entry : map.entrySet()) {
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
		if (from > to) 
			removeRelation(to, from, agent);

		if (!jaggedMatrix.containsKey(from))
			jaggedMatrix.put(from, new HashMap<Integer, Byte>());

		if (!jaggedMatrix.get(from).containsKey(to))
			jaggedMatrix.get(from).put(to, new Byte((byte)0));

        byte curVal = jaggedMatrix.get(from).get(to);
		jaggedMatrix.get(from).put(to, (byte)(curVal | (2 ^ agent)));
	}

	/**
	 * Specified state is removed from the matrix
	 */
	public void removeState(Integer state) {
		jaggedMatrix.remove(state);
		for (Map<Integer, Byte> to : jaggedMatrix.values()) {
			to.remove(state);
		}
        jaggedMatrix.remove(state);
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
        byte mask = RelationMatrix.setToByte(agents);

		if (jaggedMatrix.containsKey(from) &&
		    jaggedMatrix.get(from).containsKey(to) &&
                ((jaggedMatrix.get(from).get(to) & mask) == mask)) {
            // If the bitvecotr & mask is equal to mask, all the agents'
            // relations from this state are removed
            return false;
        }
		return true;
	}
	
	/**
	 * returns whether the matrix contains specified relation for all of the specified agents.
	 */
	public boolean containsAll(Integer from, Integer to, Set<Integer> agents) {
		if (from > to)
			containsAll(to, from, agents);
        byte mask = RelationMatrix.setToByte(agents);

		//If there is no overlap between agents and the specified entry, return true
		if (jaggedMatrix.containsKey(from) && 
		    jaggedMatrix.get(from).containsKey(to) &&
                ((jaggedMatrix.get(from).get(to) & mask) != 0)) {
            return false;
        }
		return true;
	}
}
