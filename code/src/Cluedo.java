import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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
        this.outField.setPreferredSize(new Dimension(600, 600));
        this.outField.setMargin(new Insets(0, 5, 0, 0));
        this.outField.setEditable(false);
        this.outField.setTabSize(8);
        this.writer = new PrintStream(new JTextStream(this.outField));
        this.bar = new JProgressBar(SwingConstants.VERTICAL);
        this.bar.setVisible(false);
        this.bar.setIndeterminate(true);
        this.infoPanel = new JPanel();
        this.infoPanel.setPreferredSize(new Dimension(200, 600));
        this.infoPanel.setLayout(new BoxLayout(this.infoPanel, BoxLayout.Y_AXIS));
        this.add(this.infoPanel, BorderLayout.EAST);
        JScrollPane scrollPane = new JScrollPane(this.outField);
        scrollPane.setPreferredSize(new Dimension(640, 600));
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
            KripkeModel model = new KripkeModel(loop.getDealing(),
                    loop.getPlayers().length);
            Player[] oldPlayers = Cluedo.this.loop.getPlayers();
            Player[] players = new Player[oldPlayers.length];
            for (int i = 0; i < players.length; i++) {
                Player oldPlayer = oldPlayers[i];
                StrategySet set = oldPlayer.getStrategies();
                players[i] = new Player(oldPlayer.getHand(),
                        oldPlayer.getNumber(), model,
                        set.getSuspicion(),
                        set.getResponse(),
                        set.getAccusation());
            }
            Cluedo.this.loop = new GameLoop(model,
                    writer, players);
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
        
        private Player[] initPlayers(KripkeModel model, StrategySet... strategySets) {
            Dealing deal = model.point();
        	Player[] ret = new Player[strategySets.length];
            // Initialize an array of cards per player
            ArrayList<ArrayList<Card>> dealing = new ArrayList<ArrayList<Card>>();
            for (int i = 0; i < strategySets.length; i++) {
                dealing.add(i, new ArrayList<Card>());
            }
            
            // Save all cards in the list of the correct player
            for (int i = 0; i < deal.getCategories(); i ++) {
                for (int j = 0; j < deal.numberOfCards(i); j++) {
                    if (deal.player(i, j) > 0)
                        dealing.get(deal.player(i, j) - 1).add(new Card(i, j));
                }
            }
            
            // Initialize the players
            for (int i = 0; i < strategySets.length; i++) {
				Card[] dummyArray = new Card[dealing.get(i).size()];
				CardSet cardSet = new CardSet(dealing.get(i).toArray(dummyArray));
				
                ret[i] = new Player(
                			cardSet,
                			i + 1, 
                			model,
                			strategySets[i].getSuspicion(),
                			strategySets[i].getResponse(),
                			strategySets[i].getAccusation()
                			);
            }
            return ret;
        }

        @Override
        public Void doInBackground() {
            Cluedo.this.infoPanel.removeAll();
            Table table = new Table();
            KripkeModel model = new KripkeModel(table.getDealing(), table.getPlayers());
            Cluedo.this.loop = new GameLoop(
            		model,
                    writer,
                    initPlayers(model, table.getStrategies()));
            
            return null;
        }

        @Override
        public void done() {
            for (Player p : Cluedo.this.loop.getPlayers()) {
                JPanel       panel  = new JPanel();
                TitledBorder border = new TitledBorder("Agent #" + p.getNumber());
                panel.setBorder(border);
                panel.add(new JLabel("Hand:\t" + p.getHand()));
                panel.add(new JLabel("Suspicion strategy:\t" + p.getSuspicionStrategy()));
                panel.add(new JLabel("Response strategy:\t" + p.getResponseStrategy()));
                panel.add(new JLabel("Accusation strategy:\t" + p.getAccusationStrategy()));
                Cluedo.this.infoPanel.add(panel, p.getNumber() - 1);
            }
            Cluedo.this.outField.setText("");
            Cluedo.this.writer.println("Prepared new game");
            Cluedo.this.writer.println("=================");
            Cluedo.this.writer.println("# Round 0");
            Cluedo.this.setBusy(false);
        }
    }
}
