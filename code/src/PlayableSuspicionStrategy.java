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

public class PlayableSuspicionStrategy extends SuspicionStrategy {
	@SuppressWarnings("serial")
	private class SuspicionGUI extends JFrame {

		private class SuspicionPanel extends JPanel {
			private ArrayList<JComboBox<Integer>> cardInputs;
			
			private class SuspicionListener implements ActionListener {
				private JButton done;
				
				public SuspicionListener(JButton done) {
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
				    }
					
					strategy = new CardSet(cards);
					close();
				}
	
			}
			
			public SuspicionPanel(Player agent) {
				cardInputs = new ArrayList<JComboBox<Integer>>(agent.getCardCategories());
				
				JPanel hugePanel = new JPanel();
				hugePanel.setLayout(new BorderLayout());
				hugePanel.add(new JLabel("Suspicion:"), BorderLayout.NORTH);
				
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
				
				JButton done = new JButton("Done");
				done.addActionListener(new SuspicionListener(done));
			    add(done, BorderLayout.SOUTH);
			}
		}

		private CardSet strategy;
		private Player agent;

		public SuspicionGUI(Player player) {
			super("Suspicion of agent #" + player.getNumber());
			setMinimumSize(new Dimension(300, 200));
	        
			this.agent = player;
			
			this.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent arg0) {
		            synchronized (lock) {
		            	setVisible(false);
		                if (strategy == null)
		                	strategy = computerStrategy(agent);
		                lock.notify();
		            }
		        }

		    });
			this.add(new SuspicionPanel(agent));
			
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
	
	public PlayableSuspicionStrategy(String strategy) {
		super(strategy);
	}

	@Override 
	public CardSet strategy(Player agent){
        if(manualInput(agent.getNumber())){
            return manualStrategy(agent);
        }
        return super.strategy(agent);
    }
	
	private CardSet computerStrategy(Player agent) {
		return super.strategy(agent);
	}

	private boolean manualInput(int agent) {
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                "Do you want to manipulate the suspicion of agent #"+ agent +"?", 
                "It's your turn!", 
                JOptionPane.YES_NO_OPTION); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}


	private CardSet manualStrategy(Player agent) {
		SuspicionGUI frame = new SuspicionGUI(agent);
        
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
