import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PlayableAccusationStrategy extends AccusationStrategy {
	@SuppressWarnings("serial")
	private class AccusationGUI extends JFrame {

		private class AccusationPanel extends JPanel {
			private ArrayList<JComboBox<Integer>> cardInputs;
			
			private class AccusationDoneListener implements ActionListener {
				private JButton done;
				
				public AccusationDoneListener(JButton done) {
					this.done = done;
				}
	
				@Override
				public void actionPerformed(ActionEvent e) {
					Card[] cards = new Card[agent.getCardCategories()];
					
					if(e.getSource().equals(done)) {
						for (int cat = 0; cat != cardInputs.size(); ++cat) {
							int card = (Integer) cardInputs.get(cat).getSelectedItem() -1;
							cards[cat] = new Card(cat, card);
						}
						strategy = new CardSet(cards);
						strategyReady = true;
						close();
				    }
					
				}
	
			}
			
			private class AccusationNoneListener implements ActionListener {
				private JButton none;
				
				public AccusationNoneListener(JButton none) {
					this.none = none;
				}
	
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(e.getSource().equals(none)) {
						strategy = null;
						strategyReady = true;
						close();
				    }
					
				}
	
			}
			
			public AccusationPanel() {
				cardInputs = new ArrayList<JComboBox<Integer>>(agent.getCardCategories());
				
				JPanel hugePanel = new JPanel();
				hugePanel.setLayout(new BorderLayout());
				hugePanel.add(new JLabel("Accusation:"), BorderLayout.NORTH);
				
				JPanel bigPanel = new JPanel();
				bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
				for (int idx = 0; idx != agent.getCardCategories(); ++idx) {
					JPanel inputPanel = new JPanel();
					inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
					inputPanel.add(new JLabel("Category "+ (idx+1) +": "));
					
					Integer[] options = new Integer[agent.getCardsForCategory(idx)];
					for (int card = 0; card != agent.getCardsForCategory(idx); ++card)
						options[card] = card+1;
					
					cardInputs.add(idx, new JComboBox<Integer>(options));
					inputPanel.add(cardInputs.get(idx));
					bigPanel.add(inputPanel);
				}
				hugePanel.add(bigPanel, BorderLayout.SOUTH);
				
				setLayout(new BorderLayout());
				add(hugePanel, BorderLayout.NORTH);
			
				JPanel buttonPanel = new JPanel();
				
				JButton done = new JButton("Done");
				done.addActionListener(new AccusationDoneListener(done));
				buttonPanel.add(done);
				
				JButton none = new JButton("Do nothing");
				none.addActionListener(new AccusationNoneListener(none));
				buttonPanel.add(none);
				
				add(buttonPanel, BorderLayout.SOUTH);
			}
		}

		private CardSet strategy;
		private boolean strategyReady;
		private Player agent;

		public AccusationGUI(Player player) {
			super("Accusation of agent #" + player.getNumber());
			setMinimumSize(new Dimension(300, 200));
	        
			this.agent = player;
			
			this.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent arg0) {
		            synchronized (lock) {
		            	setVisible(false);
		                if (!strategyReady)
		                	strategy = computerStrategy(agent);
		                lock.notify();
		            }
		        }

		    });
			this.add(new AccusationPanel());
			
			this.revalidate();
	        this.pack();
	        this.setVisible(true);
		}
		
		private void close() {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}

		public CardSet getStrategy() {
			return strategy;
		}
	}
		
	private static Object lock = new Object();
	
	public PlayableAccusationStrategy(String strategy) {
		super(strategy);
	}

	/**
	 * The user is asked whether he/she wants to play this strategy manually. If so, a GUI pops up in which the user can specify an accusation. Otherwise, the accusation is obtained by calling the strategy function of the AccusationStrategy class.
	 * @param agent The agent that plays this strategy
	 * @return A set of cards that represents the accusation if an accusation is done. Null if no accusation is done. 
	 */
	@Override 
	public CardSet strategy(Player agent){
        if(manualInput(agent.getNumber())){
            return manualStrategy(agent);
        }
        return super.strategy(agent);
    }

	private boolean manualInput(int agent) {
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                "Do you want to manipulate the accusation of agent #"+ agent +"?", 
                "It's your turn!", 
                JOptionPane.YES_NO_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}
	
	private CardSet computerStrategy(Player agent) {
		return super.strategy(agent);
	}

	private CardSet manualStrategy(Player agent) {
		AccusationGUI frame = new AccusationGUI(agent);
        
		synchronized(lock) {
            while (frame.isVisible())
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
		
		return frame.getStrategy();
	}
	
}
