import java.util.Set;

/**
 * Interface for Knowledge operators over groups
 * @author rmellema
 */
public interface GroupKnow {
    /**
     * Get the agents this operator works over
     * @return The agents for this operator
     */
    public Set<Integer> getAgents();

    /**
     * Get the formula this operator works over
     * @return The formula for this operator
     */
    public Formula getFormula();
}
