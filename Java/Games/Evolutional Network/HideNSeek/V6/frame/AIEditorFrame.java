package frame;

import ai.Network;
import ai.AIConstructor;
import entity.Entity;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AIEditorFrame extends JFrame{
	Network net;
	AIConstructor[] base;
	Entity[] etts;
	Entity[] temp;
	int qtt;
	NetworkIlustration networkIlustration;
	
	public AIEditorFrame(Entity[] etts, Entity[] temp, Network net){
		this.etts = etts;
		this.temp = temp;
		this.net = net;
		
		qtt = this.temp.length - this.etts.length;
		base = new AIConstructor[qtt];
		for(int i = 0; i < qtt; i++){
			base[i] = new AIConstructor(new int[]{8*5,8});
		}
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				for(int i = etts.length; i < temp.length; i++){
					AIConstructor tempAI = new AIConstructor(new int[]{8*5,8});
					tempAI.setDivisor(1.0f);
					temp[i].setNetId( net.add(tempAI));
				}
				dispose();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setName("AI Editor");
		setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		add(buttons, BorderLayout.NORTH);
		buttons.add(new JButton("Add Layer"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new createLayer();
				}
			});
		}});
		buttons.add(new JButton("Remove Layer"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new removeLayer();
				}
			});
		}});
		buttons.add(new JButton("Edit Layer Connection"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new editLayerConnection();
				}
			});
		}});
		buttons.add(new JButton("Add Neuron"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new createNeuron();
				}
			});
		}});
		buttons.add(new JButton("Remove Neuron"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new removeNeuron();
				}
			});
		}});
		buttons.add(new JButton("Edit Connection"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new editConnection();
				}
			});
		}});
		buttons.add(new JButton("Create"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					AIEditorFrame.this.create();
				}
			});
		}});
		networkIlustration = new NetworkIlustration();
		add(networkIlustration, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	private void create(){
		int n = 0;
		for(int i = etts.length; i < temp.length; i++)
			temp[i].setNetId( net.add( base[n++]));
		dispose();
	}
	
	private class NetworkIlustration extends JPanel{
		JTextArea textArea = new JTextArea();
		public NetworkIlustration(){
			textArea.setEditable(false);
			add(new JScrollPane(textArea){{
				setPreferredSize(new Dimension(500,500));
			}});
			refresh();
		}
		public void refresh(){
			textArea.setText("Information about the layers in Neural Network:\n\n" + base[0].getLayersData() + "General information about the Neural Network:\n\n" + base[0].getData());
		}
	}
	
	private class createLayer extends JFrame{
		int size = 1;
		JLabel label = new JLabel("Size of the layer: 1");
		public createLayer(){
			setName("Create Layer");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("+"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							createLayer.this.size++;
							createLayer.this.label.setText("Size of the layer: " + createLayer.this.size);
							createLayer.this.pack();
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("-"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createLayer.this.size == 1) return;
							createLayer.this.size--;
							createLayer.this.label.setText("Size of the layer: " + createLayer.this.size);
							createLayer.this.pack();
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			add(new JButton("Create"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						for(int i = 0; i < AIEditorFrame.this.qtt; i++)
							AIEditorFrame.this.base[i].addLayer(createLayer.this.size);
						AIEditorFrame.this.networkIlustration.refresh();
					}
				});
			}});
			add(new JButton("Create straight recursive layers"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						new createStraightRecursiveLayers();
						createLayer.this.dispose();
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	
	private class createStraightRecursiveLayers extends JFrame{
		int size = 1;
		int qtt = 2;
		JLabel label0 = new JLabel("Size of the layers: 1");
		JLabel label1 = new JLabel("Quantity of layers: 2");
		public createStraightRecursiveLayers(){
			setName("Create Layer");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label0);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("+"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							createStraightRecursiveLayers.this.size++;
							label0.setText("Size of the layers: " + createStraightRecursiveLayers.this.size);
							createStraightRecursiveLayers.this.pack();
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("-"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createStraightRecursiveLayers.this.size == 1) return;
							createStraightRecursiveLayers.this.size--;
							label0.setText("Size of the layers: " + createStraightRecursiveLayers.this.size);
							createStraightRecursiveLayers.this.pack();
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			add(label1);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("+"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							createStraightRecursiveLayers.this.qtt++;
							label1.setText("Quantity of layers: " + createStraightRecursiveLayers.this.qtt);
							createStraightRecursiveLayers.this.pack();
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("-"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createStraightRecursiveLayers.this.qtt == 2) return;
							createStraightRecursiveLayers.this.qtt--;
							label1.setText("Quantity of layers: " + createStraightRecursiveLayers.this.qtt);
							createStraightRecursiveLayers.this.pack();
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			add(new JButton("Create"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						for(int i = 0; i < AIEditorFrame.this.qtt; i++)
							base[i].addStraightRecursiveLayers(createStraightRecursiveLayers.this.qtt, createStraightRecursiveLayers.this.size);
						AIEditorFrame.this.networkIlustration.refresh();
					}
				});
			}});
			add(new JButton("Create simple layer"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						new createLayer();
						createStraightRecursiveLayers.this.dispose();
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	private class removeLayer extends JFrame{
		int id = -1;
		JLabel label = new JLabel("Index of layer to be removed");
		JTextField textField = new JTextField(5);
		public removeLayer(){
			setName("Remove Layer");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label);
			add(textField);
			add(new JButton("Remove"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						try{
							Integer value = Integer.valueOf(removeLayer.this.textField.getText());
							if(value < 0){
								removeLayer.this.label.setText("Please type a valid index.");
								removeLayer.this.pack();
								return;
							}
							for(int i = 0; i < base.length; i++)
								AIEditorFrame.this.base[i].removeLayer((int)value);
							AIEditorFrame.this.networkIlustration.refresh();
						} catch(NumberFormatException except){
							removeLayer.this.label.setText("Please type a valid index.");
							removeLayer.this.pack();
							return;
						}
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	private class editLayerConnection extends JFrame{
		int receiver = -1;
		int target = -1;
		JLabel label0 = new JLabel("Receiver Layer: ");
		JTextField textField0 = new JTextField(5);
		JLabel label1 = new JLabel("Target Layer: ");
		JTextField textField1 = new JTextField(5);
		JLabel label3 = new JLabel();
		JButton button = new JButton("New Layer Connection"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(getText().equals("New Layer Connection")) setText("New Layer Recursive Connection");
					else if(getText().equals("New Layer Recursive Connection")) setText("Remove Connection");
					else setText("New Layer Connection");
					editLayerConnection.this.pack();
				}
			});
		}};
		
		public editLayerConnection(){
			setName("Edit Layer Connection");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label0);
			add(textField0);
			add(label1);
			add(textField1);
			add(button);
			add(label3);
			add(new JButton("Done"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						try{
							Integer value0 = Integer.valueOf(editLayerConnection.this.textField0.getText());
							Integer value1 = Integer.valueOf(editLayerConnection.this.textField1.getText());
							if(value0 < 0 || value1 < 0){
								editLayerConnection.this.label3.setText("Please type a valid index.");
								editLayerConnection.this.pack();
								return;
							}
							if(button.getText().equals("New Layer Connection")){
								for(int i = 0; i < base.length; i++)
									AIEditorFrame.this.base[i].addLayerConnection(value0, value1);
							} else if(editLayerConnection.this.button.getText().equals("New Layer Recursive Connection")){
								for(int i = 0; i < base.length; i++)
									AIEditorFrame.this.base[i].addLayerRecursiveConnection(value0, value1);
							} else{
								for(int i = 0; i < base.length; i++)
									AIEditorFrame.this.base[i].removeLayerConnection(value0, value1);
							}
							AIEditorFrame.this.networkIlustration.refresh();
						} catch(NumberFormatException except){
							editLayerConnection.this.label3.setText("Please type a valid index.");
							editLayerConnection.this.pack();
							return;
						}
						AIEditorFrame.this.networkIlustration.refresh();
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	private class createNeuron extends JFrame{
		int qtt = 1;
		JLabel label = new JLabel("Quantity add: 1");
		
		public createNeuron(){
			setName("Create Neuron");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("+"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							createNeuron.this.qtt++;
							createNeuron.this.label.setText("Quantity added: " + createNeuron.this.qtt);
							createNeuron.this.pack();
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("-"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createNeuron.this.qtt == 1) return;
							createNeuron.this.qtt--;
							createNeuron.this.label.setText("Quantity added: " + createNeuron.this.qtt);
							createNeuron.this.pack();
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			add(new JButton("Create"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						for(int i = 0; i < AIEditorFrame.this.qtt; i++)
							AIEditorFrame.this.base[i].add(createNeuron.this.qtt);
						AIEditorFrame.this.networkIlustration.refresh();
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	private class removeNeuron extends JFrame{
		JLabel label = new JLabel("Neuron to be removed: ");
		JTextField textField = new JTextField(5);
		public removeNeuron(){
			setName("Remove Neuron");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label);
			add(textField);
			add(new JButton("Remove"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						try{
							Integer value = Integer.valueOf(textField.getText());
							if(value < 0){
								removeNeuron.this.label.setText("Please type a valid index.");
								removeNeuron.this.pack();
								return;
							}
							for(int i = 0; i < base.length; i++)
								AIEditorFrame.this.base[i].remove((int)value);
							AIEditorFrame.this.networkIlustration.refresh();
						} catch(NumberFormatException except){
							removeNeuron.this.label.setText("Please type a valid index.");
							removeNeuron.this.pack();
							return;
						}
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
	private class editConnection extends JFrame{
		JLabel label0 = new JLabel("Receiver Neuron: ");
		JTextField textField0 = new JTextField(5);
		JLabel label1 = new JLabel("Target Neuron: ");
		JTextField textField1 = new JTextField(5);
		JLabel label3 = new JLabel();
		JButton button = new JButton("New Connection"){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(getText().equals("New Connection")) setText("New Recursive Connection");
					else if(getText().equals("New Recursive Connection")) setText("Remove Connection");
					else setText("New Connection");
					editConnection.this.pack();
				}
			});
		}};
		public editConnection(){
			setName("Edit Connection");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			add(label0);
			add(textField0);
			add(label1);
			add(textField1);
			add(button);
			add(label3);
			add(new JButton("Done"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						try{
							Integer value0 = Integer.valueOf(editConnection.this.textField0.getText());
							Integer value1 = Integer.valueOf(editConnection.this.textField1.getText());
							if(value0 < 0 || value1 < 0){
								editConnection.this.label3.setText("Please type a valid index.");
								editConnection.this.pack();
								return;
							}
							if(editConnection.this.button.getText().equals("New Connection")){
								for(int i = 0; i < AIEditorFrame.this.base.length; i++)
									AIEditorFrame.this.base[i].addConnection(value0, value1);
							} else if(editConnection.this.button.getText().equals("New Recursive Connection")){
								for(int i = 0; i < AIEditorFrame.this.base.length; i++)
									AIEditorFrame.this.base[i].addRecursiveConnection(value0, value1);
							} else{
								for(int i = 0; i < AIEditorFrame.this.base.length; i++)
									AIEditorFrame.this.base[i].removeConnection(value0, value1);
							}
							AIEditorFrame.this.networkIlustration.refresh();
						} catch(NumberFormatException except){
							editConnection.this.label3.setText("Please type a valid index.");
							editConnection.this.pack();
							return;
						}
						AIEditorFrame.this.networkIlustration.refresh();
					}
				});
			}});
			pack();
			setVisible(true);
		}
	}
}