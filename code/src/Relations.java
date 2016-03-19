import java.util.HashSet;
import java.util.Set;


public abstract class Relations {
	
    protected static byte setToByte(Set<Integer> agents) {
        byte ret = 0;
        // For every agent in the set
        for (Integer agent : agents) {
        	//Put a 1 on the agent location in the byte
            ret = (byte)(ret | (1 << (agent -1)));
        }
        return ret;
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
	public abstract void removeState(Integer state);
	
	/**
	 * returns whether the matrix contains specified relation for the specified agent.
	 */
	public boolean contains(Integer from, Integer to, Integer agent) {
		return containsAny(from, to, new HashSet<Integer>(agent));
	}

	/**
	 * returns whether the matrix contains specified relation for any of the specified agents.
	 */
	public abstract boolean containsAny(Integer from, Integer to, Set<Integer> agents);
	
	/**
	 * returns whether the matrix contains specified relation for all of the specified agents.
	 */
	public abstract boolean containsAll(Integer from, Integer to, Set<Integer> agents);
	
	/**
	 * Handles private announcement phi to agent
	 * @param states States left in the Kripke model
	 */
	public abstract void privateAnnouncement(Formula phi, Integer agent, Set<Integer> states, Set<Integer> notHolds);

}
