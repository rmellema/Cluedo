import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameFrame extends JFrame {
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

    public GameFrame() {
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
        this.getContentPane().add(outField,BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.WEST);
        // Menu and Button creation
        this.menu = new JMenuBar();
        this.setJMenuBar(this.menu);
        JMenu cluedoMenu = new JMenu("Cluedo");
        this.menu.add(cluedoMenu);
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        cluedoMenu.add(quit);
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
        GameFrame frame = new GameFrame();
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
            System.out.println("Trying action");
            SwingWorker<Void, Void> run = null;
            Constructor<?>[] ctor = runClass.getConstructors();
            System.out.println(ctor.length);
            try {
                for (int i = 0; i < ctor.length; i++) {
                    System.out.println(ctor[i].getGenericParameterTypes().length);
                    System.out.println(ctor[i].getGenericParameterTypes()[0]);
                    if (ctor[i].getGenericParameterTypes().length == 1) {
                        ctor[i].setAccessible(true);
                        run = (SwingWorker<Void, Void>)ctor[i].newInstance(GameFrame.this);
                    }
                }
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
            run.execute();
            GameFrame.this.setBusy(true);
            System.out.println("Waiting...");
        }
    }

    private class GameWorker extends SwingWorker<Void, Void> {
        public GameWorker() {

        }
        @Override
        public Void doInBackground() {
            GameFrame.this.loop.game();
            return null;
        }
        @Override
        public void done() {
            GameFrame.this.setBusy(false);
        }
    }

    private class RoundWorker extends SwingWorker<Void, Void> {
        public RoundWorker() {

        }
        @Override
        public Void doInBackground() {
            GameFrame.this.loop.round();
            return null;
        }
        @Override
        public void done() {
            GameFrame.this.setBusy(false);
        }
    }

    private class StepWorker extends SwingWorker<Void, Void> {
        public StepWorker() {

        }
        @Override
        public Void doInBackground() {
            GameFrame.this.loop.step();
            return null;
        }
        @Override
        public void done() {
            GameFrame.this.setBusy(false);
        }
    }

    private class RestartWorker extends SwingWorker<Void, Void> {
        public RestartWorker() {

        }
        @Override
        public Void doInBackground() {
            GameFrame.this.loop = new GameLoop(loop.getDealing(),
                    loop.getPlayers().length,
                    writer);
            return null;
        }

        @Override
        public void done() {
            GameFrame.this.outField.setText("");
            GameFrame.this.setBusy(false);
        }
    }

    public class NewGameWorker extends SwingWorker<Void, Void> {
        public NewGameWorker() {

        }

        @Override
        public Void doInBackground() {
            GameFrame.this.infoPanel.removeAll();
            GameFrame.this.loop = new GameLoop(writer);
            return null;
        }

        @Override
        public void done() {
            GameFrame.this.outField.setText("");
            for (Player p : GameFrame.this.loop.getPlayers()) {
                String label = "Agent #" + p.getNumber() + "\n\t" + p.getHand();
                GameFrame.this.infoPanel.add(new JLabel(label));
            }
            GameFrame.this.writer.println("Prepared new game");
            GameFrame.this.writer.println("=================");
            GameFrame.this.setBusy(false);
        }
    }
}
