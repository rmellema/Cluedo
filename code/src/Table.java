import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

	public class Table{
		@SuppressWarnings("serial")
		private static class Initializer extends JFrame{
			public class DealPanel extends JPanel {
				private int[] cardsPerPlayer;
				
				private ArrayList<HashMap<Integer, JComboBox<Integer>>> cardLocations;
				
				public class IntsInputListener implements ActionListener {
					private JButton apply;
					
					public IntsInputListener(JButton apply) {
						this.apply = apply;
					}
					
					// Compares the contents of two int[]s with equal length
					private boolean checkEnteredCards(int[] enteredCards) {
						for (int idx = 0; idx != cardsPerPlayer.length; ++idx)
							if (enteredCards[idx] != cardsPerPlayer[idx]) {
								wrongDealingPopup(enteredCards);
								return false;
							}
						return true;
					}
					
					private void wrongDealingPopup(int[] enteredCards) {
						String playerString = "";
						for (int idx = 0; idx != cardsPerPlayer.length; ++idx)
							if (enteredCards[idx] != cardsPerPlayer[idx]) {
								playerString += "\nAgent #" + (idx+1) + ": " +enteredCards[idx]+ " \\ "+ cardsPerPlayer[idx];
							}
						final JPanel panel = new JPanel();
						JOptionPane.showMessageDialog(panel, "Some agents have been dealt a wrong number of cards:" + playerString, "Invalid input", JOptionPane.ERROR_MESSAGE);
						
					}
					// Returns null if the entered dealing is invalid, the entered dealing otherwise.
					private boolean validInputDealing() {
						int[] enteredCardsPerPlayer = new int[agents];
						dealing = dealing.envelopeDealing();
						// Read what the user has entered and meanwhile save the number of cards each player has been dealt
						for (int cat = 0; cat != categories; ++cat)
							for (int card = 0; card != categorySizes[cat]; ++card){
								if (cardLocations.get(cat).containsKey(card)) {
									int player = (Integer) cardLocations.get(cat).get(card).getSelectedItem();
									++enteredCardsPerPlayer[player-1];
									dealing.deal(player, cat, card);
								}
							}
						// Check whether the amounts of cards that the agents have been dealt are correct
						if (checkEnteredCards(enteredCardsPerPlayer))
							return true;
						return false;
					}
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(apply)) {
							if (validInputDealing()) {
								dealingReady = true;
							} else
								dealingReady = false;
					    }
					}

				}
				
				public DealPanel() {
					reOrganize();
				}
				
				private boolean showNotReady() {

					int tabsFilledIn = 2;
					String agentString = "";
					String cardString = "";
					String andString = "";
					
					if (getAgents() == 0) {
						agentString = " \"Agents\"";
						--tabsFilledIn;
					}
					if (dealing == null) {
						cardString = " \"Envelope\"";
						--tabsFilledIn;
					}
					if (tabsFilledIn == 2)
						return false;
					
					if (tabsFilledIn == 0)
						andString = " and";
					
					setLayout(new BorderLayout())	;
					add(new JLabel("Please first fill in" + agentString + andString + cardString + "."), BorderLayout.NORTH);
					return true;
				}

				private Integer[] constructAgentList() {
					Integer[] returnList = new Integer[agents];
					for (int idx = 0; idx != agents; ++idx) {
						returnList[idx] = idx+1;
					}
					return returnList;
				}
				
				public void reOrganize() {
					removeAll();
					if (showNotReady())
						return;
					
					countCardsPerAgent();
					
					Integer[] agentList = constructAgentList();
					cardLocations = new ArrayList<HashMap<Integer, JComboBox<Integer>>>();
					
					int maxCatSize = max(categorySizes);
					
					GridLayout gridLayout = new GridLayout(maxCatSize + 1, getCategories() + 1);
					JPanel gridPanel = new JPanel(gridLayout);
					
					//Empty space in the upper left corner
					gridPanel.add(new JLabel());
					
					//Add category labels and initialize the 2D array of comboboxes
					for(int cat = 0; cat != getCategories(); ++cat) {
						gridPanel.add(new JLabel("Category " + Integer.toString(cat+1) + ": "));
						cardLocations.add(cat, new HashMap<Integer, JComboBox<Integer>>());
					}
					
					for(int card = 0; card != maxCatSize; ++card) {
						// Add card labels to the table
						gridPanel.add(new JLabel("Card "+ Integer.toString(card+1) + ": "));
						
						// Add comboboxes as table entries
						for(int cat = 0; cat != getCategories(); ++cat) {
							//If the card is not in the game, place an empty panel
							if (card >= categorySizes[cat]) {
								gridPanel.add(new JLabel());
								continue;
							}
								
							//If the card is in the envelope, write an x
							if (dealing.isTrue(new PropVar(new Card(cat, card), 0))) {
								gridPanel.add(new JLabel("Envelope"));
								continue;
							}
							//Otherwise place a combobox
							cardLocations.get(cat).put(card, new JComboBox<Integer>(agentList));
							gridPanel.add(cardLocations.get(cat).get(card));
						}
					}
					setLayout(new BorderLayout());
					JPanel bigPanel = new JPanel(new BorderLayout());
					bigPanel.add(new JLabel("Specify which cards each agent gets:"));
					bigPanel.add(gridPanel, BorderLayout.SOUTH);
					
					add(bigPanel, BorderLayout.NORTH);
					
					JButton apply = new JButton("Apply");
					apply.addActionListener(new IntsInputListener(apply));
				    add(apply, BorderLayout.SOUTH);
				}

				//Stores the number of cards each agent should get
				private void countCardsPerAgent() {
					cardsPerPlayer = new int[agents];
					int total = sum(categorySizes) - categories;
					int minimum = total/agents;
					int rest = total%agents;
					for (int idx = 0; idx != agents; ++idx) {
						cardsPerPlayer[idx] = minimum;
						if (idx < rest)
							++cardsPerPlayer[idx];
					}
				}
				
				// Private function to check the sum of all ints in an int[] which is known not to be null
				private int sum(int[] vals) {
					int sum = 0;
					for (int val : vals) {
						sum += val;
					}
					return sum;
				}

				// Private function to check the size of an int[] which is known not to be null and to contain at least one int
				private int max(int[] vals) {
					int max = vals[0];
					for (int val : vals) {
						if (val > max)
							max = val;
					}
					return max;
				}
			}
			
			public class EnvelopePanel extends JPanel {

				private ArrayList<JComboBox<Integer>> cardInputs;
				
				public class IntsInputListener implements ActionListener {
					private JButton apply;
					
					public IntsInputListener(JButton apply) {
						this.apply = apply;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(apply)) {
							dealing = new Dealing(categorySizes);
							for (int cat = 0; cat != cardInputs.size(); ++cat) {
								int card = (Integer) cardInputs.get(cat).getSelectedItem() - 1;
								dealing.deal(0, cat, card);
							}
							change(Panels.DEALING);
					    }
					}

				}
				
				public EnvelopePanel() {
					reOrganize();
				}
				
				public void reOrganize() {
					removeAll();
					if (categorySizes == null) {
						setLayout(new BorderLayout())	;
						add(new JLabel("Please first fill in \"Cards\"."), BorderLayout.NORTH);
						return;
					}
					cardInputs = new ArrayList<JComboBox<Integer>>(getCategories());
					
					JPanel hugePanel = new JPanel();
					hugePanel.setLayout(new BorderLayout());
					hugePanel.add(new JLabel("Cards in the envelope:"), BorderLayout.NORTH);
					
					JPanel bigPanel = new JPanel();
					bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
					for (int idx = 0; idx != getCategories(); ++idx) {
						JPanel inputPanel = new JPanel();
						inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
						inputPanel.add(new JLabel("Category "+ (idx+1) +": "));
						
						Integer[] options = new Integer[categorySizes[idx]];
						for (int card = 0; card != categorySizes[idx]; ++card)
							options[card] = card+1;
						
						cardInputs.add(idx, new JComboBox<Integer>(options));
						inputPanel.add(cardInputs.get(idx));
						bigPanel.add(inputPanel);
					}
					hugePanel.add(bigPanel, BorderLayout.SOUTH);
					
					setLayout(new BorderLayout());
					add(hugePanel, BorderLayout.NORTH);
					
					JButton apply = new JButton("Apply");
					apply.addActionListener(new IntsInputListener(apply));
				    add(apply, BorderLayout.SOUTH);
				}
			}
			
			public class StrategiesPanel extends JPanel {
				private class StrategyPanel extends JPanel {
					int index;
					JComboBox<String> suspicionBox;
					JComboBox<String> responseBox;
					JComboBox<String> accusationBox;

					JCheckBox suspicionCheckBox = new JCheckBox();
					JCheckBox responseCheckBox = new JCheckBox();
					JCheckBox accusationCheckBox = new JCheckBox();
					
					//Index is the index 
					public StrategyPanel(int index) {
						this.index = index;
						
						
						JPanel bigPanel = new JPanel();
						
						TitledBorder title = new TitledBorder("Agent #"+ (index+1) +":");
						bigPanel.setBorder(title);
						
						bigPanel.setLayout(new GridLayout(4,3));
						
						bigPanel.add(new JLabel());
						bigPanel.add(new JLabel("Strategy:"));
						bigPanel.add(new JLabel("Manipulate:"));
						
						
						bigPanel.add(new JLabel("Suspicion: "));
						suspicionBox = new JComboBox<String>(SuspicionStrategy.getOptions());
						bigPanel.add(suspicionBox);
						bigPanel.add(suspicionCheckBox);
						
						bigPanel.add(new JLabel("Response: "));
						responseBox = new JComboBox<String>(ResponseStrategy.getOptions());
						bigPanel.add(responseBox);
						bigPanel.add(responseCheckBox);
						
						bigPanel.add(new JLabel("Accusation: "));
						accusationBox = new JComboBox<String>(AccusationStrategy.getOptions());
						bigPanel.add(accusationBox);
						bigPanel.add(accusationCheckBox);
						
						setLayout(new BorderLayout());
						add(bigPanel, BorderLayout.NORTH);
					}
					
					public void saveInput() {
						strategySets[index] = new StrategySet();
						String strategyString;
						
						strategyString = (String) suspicionBox.getSelectedItem();
						if (suspicionCheckBox.isSelected())
							strategySets[index].setSuspicion(new PlayableSuspicionStrategy(strategyString));
						else 
							strategySets[index].setSuspicion(new SuspicionStrategy(strategyString));
						
						strategyString = (String) responseBox.getSelectedItem();
						if (responseCheckBox.isSelected())
							strategySets[index].setResponse(new PlayableResponseStrategy(strategyString));
						else 
							strategySets[index].setResponse(new ResponseStrategy(strategyString));
						
						strategyString = (String) accusationBox.getSelectedItem();
						if (accusationCheckBox.isSelected())
							strategySets[index].setAccusation(new PlayableAccusationStrategy(strategyString));
						else 
							strategySets[index].setAccusation(new AccusationStrategy(strategyString));
					}
				}
				
				private StrategyPanel[] strategyPanels;
				
				public class StrategiesListener implements ActionListener {
					private JButton apply;
					
					public StrategiesListener(JButton apply) {
						this.apply = apply;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						strategySets = new StrategySet[agents];
						if(e.getSource().equals(apply)) {
							for (int idx = 0; idx != agents; ++idx) {
								strategyPanels[idx].saveInput();
							}
					    }
					}

				}
				
				public StrategiesPanel() {
					reOrganize();
				}
				
				public void reOrganize() {
					removeAll();
					if (agents == 0) {
						setLayout(new BorderLayout())	;
						add(new JLabel("Please first fill in \"Agents\"."), BorderLayout.NORTH);
						return;
					}
					
					JPanel hugePanel = new JPanel();
					hugePanel.setLayout(new BorderLayout());
					hugePanel.add(new JLabel("Strategies:"), BorderLayout.NORTH);
					
					JPanel bigPanel = new JPanel();
					bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
					
					strategyPanels = new StrategyPanel[agents];
					for (int idx = 0; idx != agents; ++idx) {
						strategyPanels[idx] = new StrategyPanel(idx);
						bigPanel.add(strategyPanels[idx]);
					}
					hugePanel.add(bigPanel, BorderLayout.SOUTH);
					
					setLayout(new BorderLayout());
					add(hugePanel, BorderLayout.NORTH);
					
					JButton apply = new JButton("Apply");
					apply.addActionListener(new StrategiesListener(apply));
				    add(apply, BorderLayout.SOUTH);
				}
			}
			
			public class CardPanel extends JPanel {

				private JTextField[] cardInputs;
				
				public class IntsInputListener implements ActionListener {
					private JButton apply;
					
					public IntsInputListener(JButton apply) {
						this.apply = apply;
					}

					@Override
					public void actionPerformed(ActionEvent e) {
						boolean correctInput = true;
						int[] tmpCatSizes = new int[cardInputs.length];
						if(e.getSource().equals(apply)) {
							// Make sure the input in every txtfield is correct
							for (int idx = 0; idx != cardInputs.length; ++idx) {
								if (cardInputs[idx].getText().matches("[0-9]+") 
										&& cardInputs[idx].getText().length() > 0) {
									tmpCatSizes[idx] = Integer.parseInt(cardInputs[idx].getText()); 
									if (tmpCatSizes[idx]  <= 0) {
										correctInput = false;
										break;
									}
								} else {
									correctInput = false;
									break;
								}
							}
							//If so, save the input
							if (correctInput) {
								categorySizes = tmpCatSizes;
								change(Panels.ENVELOPE);
								return;
							}
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please input positive integers for each category.", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}
				
				public CardPanel() {
					reOrganize();
				}
				
				public void reOrganize() {
					removeAll();
					if (getCategories() == 0) {
						setLayout(new BorderLayout())	;
						add(new JLabel("Please first fill in \"Categories\"."), BorderLayout.NORTH);
						return;
					}
					cardInputs = new JTextField[getCategories()];
					
					JPanel hugePanel = new JPanel();
					hugePanel.setLayout(new BorderLayout());
					hugePanel.add(new JLabel("Number of cards per category:"), BorderLayout.NORTH);
					
					JPanel bigPanel = new JPanel();
					bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.PAGE_AXIS));
					for (int idx = 0; idx != getCategories(); ++idx) {
						JPanel inputPanel = new JPanel();
						inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
						inputPanel.add(new JLabel("Category "+ (idx+1) +": "));
						cardInputs[idx] = new JTextField();
						inputPanel.add(cardInputs[idx]);
						bigPanel.add(inputPanel);
					}
					hugePanel.add(bigPanel, BorderLayout.SOUTH);
					
					setLayout(new BorderLayout());
					add(hugePanel, BorderLayout.NORTH);
					
					JButton apply = new JButton("Apply");
					apply.addActionListener(new IntsInputListener(apply));
				    add(apply, BorderLayout.SOUTH);
				}
			}

			public class CategoryPanel extends JPanel {
				public class CategoryListener implements ActionListener {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(apply)) {
							if (input.getText().matches("[0-9]+") 
									&& input.getText().length() > 0) {
								int tmp = Integer.parseInt(input.getText()); 
								if (tmp > 0) {
									categories = tmp;
									change(Panels.CARDS);
									return;
								}
							} 
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please enter a positive integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}

				
				private JTextField input;
				private JButton apply;
				
				public CategoryPanel() {
					
					this.setLayout(new BorderLayout());
				    JLabel label = new JLabel("Number of categories:");
				    this.apply = new JButton("Apply");
				    this.input = new JTextField();
				    this.apply.addActionListener(new CategoryListener());
				    
				    JPanel panel = new JPanel(new BorderLayout());
				    panel.add(label, BorderLayout.NORTH);
				    panel.add(this.input, BorderLayout.SOUTH);
				    add(panel, BorderLayout.NORTH);
				    add(this.apply, BorderLayout.SOUTH);
				}
				
			}

			public class AgentPanel extends JPanel {
				public class AgentListener implements ActionListener {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(apply)) {
							if (input.getText().matches("[0-9]+") 
									&& input.getText().length() > 0) {
								int tmp = Integer.parseInt(input.getText()); 
								if (tmp > 0) {
									agents = tmp;
									change(Panels.DEALING);
									change(Panels.STRATEGIES);
									return;
								}
							} 
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please enter a positive integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}
				JTextField input;
				JButton apply;
				
				public AgentPanel() {
					
					this.setLayout(new BorderLayout());
				    JLabel label = new JLabel("Number of agents:");
				    this.apply = new JButton("Apply");
				    this.input = new JTextField();
				    this.apply.addActionListener(new AgentListener());
				    
				    JPanel panel = new JPanel(new BorderLayout());
				    panel.add(label, BorderLayout.NORTH);
				    panel.add(this.input, BorderLayout.SOUTH);
				    add(panel, BorderLayout.NORTH);
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
							int tabsFilledIn = 2;
							String dealString = "";
							String stratString = "";
							String andString = "";
							
							if (!dealingReady) {
								dealString  = " \"Dealing\"";
								--tabsFilledIn;
							}
							if (strategySets == null) {
								stratString  = " \"Strategies\"";
								--tabsFilledIn;
							}
							if (tabsFilledIn == 2) {
								close();
								return;
							}
							
							if (tabsFilledIn == 0)
								andString = " and";
							 
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Please first fill in" + dealString  + andString + stratString + ".", "Invalid input", JOptionPane.ERROR_MESSAGE);

					    }

					}

				}

				public OKButton() {
					super("Done");
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
							defaultSettings();
							close();
							return;
					    }
					}

				}

				public DefaultButton() {
					super("Default settings");
					this.addActionListener(new DefaultListener(this));
				}
			}
			
			public class RandomButton extends JButton {
				Random rand = new Random();
				
				public class RandomListener implements ActionListener {
					RandomButton randomButton;
					public RandomListener(RandomButton randomButton) {
						this.randomButton = randomButton;
					}
					private void setRandomDealing() {
						categorySizes = new int[]{4, 6};
						dealing = new Dealing(categorySizes);
						dealing.randomize(agents);
						dealingReady = true;
					}
					
					private void setRandomStrategies() {
						strategySets = new StrategySet[agents];
						String[] options;
						String option;
						for (int idx = 0; idx != agents; ++idx){
							strategySets[idx] = new StrategySet();
							
							options = SuspicionStrategy.getOptions();
							option = options[rand.nextInt(options.length)];
							strategySets[idx].setSuspicion(new SuspicionStrategy(option));
							
							options = ResponseStrategy.getOptions();
							option = options[rand.nextInt(options.length)];
							strategySets[idx].setResponse(new ResponseStrategy(option));
							
							options = AccusationStrategy.getOptions();
							option = options[rand.nextInt(options.length)];
							strategySets[idx].setAccusation(new AccusationStrategy(option));
							
						}
					}
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource().equals(randomButton)) {
							agents = 4;
							setRandomDealing();
							setRandomStrategies();
							close();
							return;
					    }
					}

				}

				public RandomButton() {
					super("Random settings");
					this.addActionListener(new RandomListener(this));
				}
			}
			

			private int agents = 0;
			private int categories = 0;
			private int[] categorySizes = null;
			private Dealing dealing = null;
			private StrategySet[] strategySets = null;
			private boolean dealingReady = false;
			
			private CardPanel cardPanel;
			private EnvelopePanel envelopePanel;
			private DealPanel dealPanel;
			private StrategiesPanel strategiesPanel;
			
			private enum Panels{
				CARDS,
				ENVELOPE,
				DEALING,
				STRATEGIES;
			}
			
			public Initializer() {
		        super("New game");
		        
		        setMinimumSize(new Dimension(500, 600));
		        
		        this.addWindowListener(new WindowAdapter() {
			        @Override
			        public void windowClosing(WindowEvent arg0) {
			            synchronized (lock) {
			                setVisible(false);
			                if (!dealingReady || strategySets == null)
			                	defaultSettings();
			                lock.notify();
			            }
			        }

			    });

		        initTabbedPanel();
		        initButtonPanel();
		        
		        this.revalidate();
		        this.pack();
		        this.setVisible(true);
		        
			}
			
			private void change(Panels panel) {
				switch (panel) {
				case CARDS:
					cardPanel.reOrganize();
					categorySizes = null;
					//FALLING TROUGH
				case ENVELOPE:
					envelopePanel.reOrganize();
					dealing = null;
				//FALLING TROUGH
				case DEALING: 
					dealPanel.reOrganize();
					dealingReady = false;
					break;
				case STRATEGIES:
					strategiesPanel.reOrganize();
					strategySets = null;
					break;
				}
			}
			private void defaultSettings() {
				agents = 4;
				setDefaultDealing();
				setDefaultStrategies();
			}

			private void setDefaultDealing() {
				dealing = new Dealing(new int[][] {{0, 1, 1, 2}, {0, 2, 3, 3, 4, 4}});
			}

			private void setDefaultStrategies() {
				strategySets = new StrategySet[agents];
				for (int idx = 0; idx != agents; ++idx){
					strategySets[idx] = new StrategySet();
					strategySets[idx].setSuspicion(new SuspicionStrategy());
					strategySets[idx].setResponse(new ResponseStrategy());
					strategySets[idx].setAccusation(new AccusationStrategy());
				}
			}

			private void initButtonPanel() {
				JPanel buttonPanel = new JPanel();

				JButton ok = new OKButton();
				buttonPanel.add(ok);
				
				JButton def = new DefaultButton();
				buttonPanel.add(def);
				
				JButton rand = new RandomButton();
				buttonPanel.add(rand);

		        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			}

			private void initTabbedPanel() {
		        JTabbedPane tabbedPane = new JTabbedPane();
		        
		        AgentPanel agentPanel = new AgentPanel();
		        tabbedPane.addTab("Agents", agentPanel);

		        CategoryPanel catPanel = new CategoryPanel();
		        tabbedPane.addTab("Categories", catPanel);

		        cardPanel = new CardPanel();
		        tabbedPane.addTab("Cards", cardPanel);

		        envelopePanel = new EnvelopePanel();
		        tabbedPane.addTab("Envelope", envelopePanel);

		        dealPanel = new DealPanel();
		        tabbedPane.addTab("Dealing", dealPanel);

		        strategiesPanel = new StrategiesPanel();
		        tabbedPane.addTab("Strategies", strategiesPanel);
		        
		        add(tabbedPane);
				
			}

			private void close() {
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			
			private int getCategories() {
				return categories;
			}
			
			public Dealing getDealing() {
				return dealing;
			}
			
			public int getAgents() {
				return agents;
			}

			public StrategySet[] getStrategies() {
				return this.strategySets;
			}
		}

		private static Object lock = new Object();
		private Dealing dealing;
		private int players;
		private StrategySet[] strategySets;

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
			strategySets = init.getStrategies();
	    }

		public Dealing getDealing() {
			return this.dealing;
		}

		public int getPlayers() {
			return this.players;
		}

		public StrategySet[] getStrategies() {
			return this.strategySets;
		}
	}
	