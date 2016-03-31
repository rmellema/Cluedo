import java.util.Set;

/**
 * Created by rene on 28/03/16.
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
