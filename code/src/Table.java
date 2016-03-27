import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

	public class Table{
		@SuppressWarnings("serial")
		public class Initializer extends JFrame{

			public class CardPanel extends JPanel {

				private JTextField[] cardInputs;
				
				public CardPanel() {
					reOrganize();
				}
				
				public void reOrganize() {
					removeAll();
					if (getCategories() == 0) {
						add(new JLabel("Please first enter the number of categories."));
						return;
					}
					cardInputs = new JTextField[getCategories()];
					
					JPanel bigPanel = new JPanel();
					bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
					for (int idx = 0; idx != getCategories(); ++idx) {
						JPanel inputPanel = new JPanel();
						inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
						inputPanel.add(new JLabel("Category "+ (idx+1) +":"));
						cardInputs[idx] = new JTextField();
						cardInputs[idx].setPreferredSize(new Dimension(50,20));
						inputPanel.add(cardInputs[idx]);
						bigPanel.add(inputPanel);
					}
					add(bigPanel);
				}
			}

			public class IntInputPanel extends JPanel {
				public class IntInputListener implements ActionListener {
					private int[] storage;
					private JButton apply;
					private JTextField input;
					
					public IntInputListener(JButton apply, JTextField input, int[] storage) {
						this.storage = storage;
						this.apply = apply;
						this.input = input;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(apply)) {
							if (input.getText().matches("[0-9]+") 
									&& input.getText().length() > 0) {
								int tmp = Integer.parseInt(input.getText()); 
								if (tmp > 0) {
									storage[0] = tmp;
									cardPanel.reOrganize();
									return;
								}
							} 
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please enter a positive integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}

				JLabel label;
				JTextField input;
				JButton apply;
				String storeName;
				
				public IntInputPanel(int[] storage, String label) {
					
					this.setLayout(new BorderLayout());
				    this.label = new JLabel(label);
				    this.apply = new JButton("Apply");
				    this.input = new JTextField();
				    this.apply.addActionListener(new IntInputListener(apply, input, storage));
				    
				    add(this.label, BorderLayout.NORTH);
				    add(this.input, BorderLayout.CENTER);
				    add(this.apply, BorderLayout.SOUTH);
				}
				
			}

			public class OKButton extends JButton {
				public class OKListener implements ActionListener {
					OKButton okButton;
					public OKListener(OKButton okButton) {
						this.okButton = okButton;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(okButton)) {
							if (dealing != null) {
								close();
								return;
							} 
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please enter a valid dealing.", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}

				public OKButton() {
					super("Start new game");
					this.addActionListener(new OKListener(this));
				}
			}
			
			public class DefaultButton extends JButton {
				public class DefaultListener implements ActionListener {
					DefaultButton defButton;
					public DefaultListener(DefaultButton defButton) {
						this.defButton = defButton;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(defButton)) {
							setDefaultDealing();
							close();
							return;
					    }
					}

				}

				public DefaultButton() {
					super("Default dealing");
					this.addActionListener(new DefaultListener(this));
				}
			}
			
			
			private int[] nrCategories = new int[1]; //reference to an int
			private int[] categorySizes = null;
			private int[] nrAgents = new int[1]; //reference to an int
			private Dealing dealing = null;
			
			private CardPanel cardPanel;
			
			public Initializer() {
		        super("New game");
		        
		        this.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent arg0) {
			            synchronized (lock) {
			                setVisible(false);
			                lock.notify();
			                if (dealing == null)  {
			                	setDefaultDealing();
			                }
			            }
			        }

			    });

		        initTabbedPanel();
		        initButtonPanel();
		        
		        this.revalidate();
		        this.pack();
		        this.setVisible(true);
		        
			}

			private void setDefaultDealing() {
				nrAgents[0] = 4;
				dealing = new Dealing(new int[][] {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}});
			}

			private void initButtonPanel() {
				JPanel buttonPanel = new JPanel();

				JButton ok = new OKButton();
				buttonPanel.add(ok);
				
				JButton def = new DefaultButton();
				buttonPanel.add(def);

		        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			}

			private void initTabbedPanel() {
		        JTabbedPane tabbedPane = new JTabbedPane();

		        JPanel agentPanel = new IntInputPanel(nrAgents, "Number of agents:");
		        tabbedPane.addTab("Agents", agentPanel);

		        JPanel catPanel = new IntInputPanel(nrCategories, "Number of card categories:");
		        tabbedPane.addTab("Categories", catPanel);
		        
		        cardPanel = new CardPanel();
		        tabbedPane.addTab("Cards", cardPanel);
		        

		        add(tabbedPane);
				
			}

			private void close() {
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			
			private int getCategories() {
				return nrCategories[0];
			}
			
			public Dealing getDealing() {
				return dealing;
			}
			
			public int getAgents() {
				return nrAgents[0];
			}
			
		}

		private static Object lock = new Object();
		private Dealing dealing;
		private int players;

		public Table() {
			Initializer init = new Initializer();
						
			synchronized(lock) {
                while (init.isVisible())
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

			dealing = init.getDealing();
			players = init.getAgents();
	    }

		public Dealing getDealing() {
			return this.dealing;
		}

		public int getPlayers() {
			return this.players;
		}
	}
	