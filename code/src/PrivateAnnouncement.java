import java.util.Iterator;
import java.util.Set;

/**
 * Formula to evaluate our Private Announcements
 * @author rmellema
 */
public class PrivateAnnouncement extends Formula {
    private Set<Integer> agents;
    private Formula announcement;
    private Formula formula;

    /**
     * Create a new Private Announcement formula
     * @param agents The agents to which the announcement is made
     * @param announcement The announcement that is made
     * @param formula The formula which we want to evaluate in the new model
     */
    public PrivateAnnouncement(Set<Integer> agents, Formula announcement, Formula formula) {
        if (agents.size() < 1) {
            throw new IllegalArgumentException("No agents in Private Announcement");
        }
        this.agents = agents;
        this.announcement = announcement;
        this.formula = formula;
    }

    /**
     * Evaluate a Formula in a state using the given model
     *
     * @param model The model used for evaluation
     * @param state The state to evaluate the Formula in
     * @return `true` if the Formula evaluate in state `state`, `false` otherwise
     */
    @Override
    public boolean evaluate(KripkeModel model, int state) {
        KripkeModel copy = new KripkeModel(model);
        for (Integer agent : this.agents)
            copy.privateAnnouncement(this.announcement, agent);
        return this.formula.evaluate(copy, state);
    }

    /**
     * Return a string representation of this Formula
     *
     * @return String representation
     */
    @Override
    public String toString() {
        if (this.agents.size() > 1) {
            return "<" + this.agents.toString() + ", " +
                    this.announcement.toString() + "> " + this.formula.toString();
        } else {
            Iterator<Integer> it = this.agents.iterator();
            return "<" + it.next() + ", " + this.announcement.toString() + "> "
                    + this.formula.toString();
        }

    }
}
