import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Cluedo extends JFrame {
    private GameLoop loop = null;
    private boolean busy = false;
    private JTextArea outField;
    private PrintStream writer;
    private JProgressBar bar;
    private JMenuBar menu;
    private JPanel buttons;
    private JPanel infoPanel;
    private GameAction gameAction;
    private GameAction roundAction;
    private GameAction stepAction;
    private GameAction restartAction;
    private GameAction newGameAction;

    public Cluedo() {
        super("Cluedo");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Panel Creation
        this.outField = new JTextArea();
        this.outField.setPreferredSize(new Dimension(600, 800));
        this.outField.setMargin(new Insets(0, 5, 0, 0));
        this.outField.setEditable(false);
        this.outField.setTabSize(8);
        this.writer = new PrintStream(new JTextStream(this.outField));
        this.bar = new JProgressBar(SwingConstants.VERTICAL);
        this.bar.setVisible(false);
        this.bar.setIndeterminate(true);
        this.infoPanel = new JPanel();
        this.infoPanel.setPreferredSize(new Dimension(200, 800));
        this.infoPanel.setLayout(new BoxLayout(this.infoPanel, BoxLayout.Y_AXIS));
        this.add(this.infoPanel, BorderLayout.EAST);
        JScrollPane scrollPane = new JScrollPane(this.outField);
        scrollPane.setPreferredSize(new Dimension(640, 800));
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.WEST);
        // Menu and Button creation
        this.menu = new JMenuBar();
        this.setJMenuBar(this.menu);
        JMenu game = new JMenu("Game");
        this.menu.add(game);
        this.buttons = new JPanel();
        this.getContentPane().add(this.buttons, BorderLayout.SOUTH);
        this.gameAction = new GameAction("Game",
                "Play until the end of the game", GameWorker.class);
        this.buttons.add(new JButton(gameAction));
        game.add(new JMenuItem(gameAction));
        this.roundAction = new GameAction("Round",
                "Play until the end of this round", RoundWorker.class);
        this.buttons.add(new JButton(roundAction));
        game.add(new JMenuItem(roundAction));
        this.stepAction = new GameAction("Step",
                "Play one turn for the current player", StepWorker.class);
        this.buttons.add(new JButton(stepAction));
        game.add(new JMenuItem(stepAction));
        this.restartAction = new GameAction("Restart",
                "Start a new game with the same dealing", RestartWorker.class);
        this.buttons.add(new JButton(restartAction));
        game.add(new JMenuItem(restartAction));
        this.newGameAction = new GameAction("New game", "Start a new game", NewGameWorker.class);
        this.buttons.add(new JButton(newGameAction));
        game.add(new JMenuItem(newGameAction));
        this.setEnabledButtons(false);
        this.newGameAction.setEnabled(true);
        this.revalidate();
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        Cluedo frame = new Cluedo();
    }

    /**
     * Set whether or not the buttons should be enabled
     *
     * @param b `true` if the user should be able to press the buttons,
     *          `false` otherwise
     */
    public void setEnabledButtons(boolean b) {
        this.gameAction.setEnabled(b);
        this.roundAction.setEnabled(b);
        this.stepAction.setEnabled(b);
        this.restartAction.setEnabled(b);
        this.newGameAction.setEnabled(b);
    }

    public void setBusy(boolean busy) {
        this.setEnabledButtons(!busy);
        this.busy = busy;
        if (this.busy) {
            this.bar.setVisible(true);
        } else {
            this.bar.setVisible(false);
        }
    }

    private class GameAction extends AbstractAction {
        private Class<? extends SwingWorker<Void, Void>> runClass;

        public GameAction(String name, String description, Class<? extends SwingWorker<Void, Void>> run) {
            super(name);
            this.putValue(SHORT_DESCRIPTION, description);
            this.runClass = run;
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingWorker<Void, Void> run = null;
            Constructor<?>[] ctor = runClass.getConstructors();
            try {
                for (int i = 0; i < ctor.length; i++) {
                    if (ctor[i].getGenericParameterTypes().length == 1) {
                        ctor[i].setAccessible(true);
                        run = (SwingWorker<Void, Void>)ctor[i].newInstance(Cluedo.this);
                    }
                }
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
            if (run == null) {
                System.err.println("No constructor for action found:");
                System.err.print("\t");
                System.err.println(e.getActionCommand());
                return;
            }
            Cluedo.this.setBusy(true);
            run.execute();
        }
    }

    private class GameWorker extends SwingWorker<Void, Void> {
        public GameWorker() {

        }
        @Override
        public Void doInBackground() {
            Cluedo.this.loop.game();
            return null;
        }
        @Override
        public void done() {
            Cluedo.this.setBusy(false);
            if (Cluedo.this.loop.isDone()) {
                Cluedo.this.setEnabledButtons(false);
                Cluedo.this.restartAction.setEnabled(true);
                Cluedo.this.newGameAction.setEnabled(true);
            }
        }
    }

    private class RoundWorker extends SwingWorker<Void, Void> {
        public RoundWorker() {

        }
        @Override
        public Void doInBackground() {
            Cluedo.this.loop.round();
            return null;
        }
        @Override
        public void done() {
            Cluedo.this.setBusy(false);
            if (Cluedo.this.loop.isDone()) {
                Cluedo.this.setEnabledButtons(false);
                Cluedo.this.restartAction.setEnabled(true);
                Cluedo.this.newGameAction.setEnabled(true);
            }
        }
    }

    private class StepWorker extends SwingWorker<Void, Void> {
        public StepWorker() {

        }
        @Override
        public Void doInBackground() {
            Cluedo.this.loop.step();
            return null;
        }
        @Override
        public void done() {
            Cluedo.this.setBusy(false);
            if (Cluedo.this.loop.isDone()) {
                Cluedo.this.setEnabledButtons(false);
                Cluedo.this.restartAction.setEnabled(true);
                Cluedo.this.newGameAction.setEnabled(true);
            }
        }
    }

    private class RestartWorker extends SwingWorker<Void, Void> {
        public RestartWorker() {

        }
        @Override
        public Void doInBackground() {
            Cluedo.this.loop = new GameLoop(loop.getDealing(),
                    loop.getPlayers().length,
                    writer);
            return null;
        }

        @Override
        public void done() {
            Cluedo.this.outField.setText("");
            Cluedo.this.writer.println("Restarted game");
            Cluedo.this.writer.println("=================");
            Cluedo.this.writer.println("# Round 0");
            Cluedo.this.setBusy(false);
        }
    }

    public class NewGameWorker extends SwingWorker<Void, Void> {
        public NewGameWorker() {

        }

        @Override
        public Void doInBackground() {
            Cluedo.this.infoPanel.removeAll();
            Cluedo.this.loop = new GameLoop(writer);
            return null;
        }

        @Override
        public void done() {
            Cluedo.this.outField.setText("");
            for (Player p : Cluedo.this.loop.getPlayers()) {
                String label = "Agent #" + p.getNumber() + "\n\t" + p.getHand();
                Cluedo.this.infoPanel.add(new JLabel(label));
            }
            Cluedo.this.writer.println("Prepared new game");
            Cluedo.this.writer.println("=================");
            Cluedo.this.writer.println("# Round 0");
            Cluedo.this.setBusy(false);
        }
    }
}
