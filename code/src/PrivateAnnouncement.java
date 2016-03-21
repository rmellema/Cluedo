import java.util.Iterator;
import java.util.Set;

/**
 * Created by rene on 20/03/16.
 */
public class PrivateAnnouncement extends Formula {
    private Set<Integer> agents;
    private Formula announcement;
    private Formula formula;

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