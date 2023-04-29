package frame;

import entity.*;
import main.Sync;
import ai.Network;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{
	Entity[] etts = new Entity[0];
	
	Network hiders = new Network();
	Network seekers = new Network();
	
	int sizeX = 500;
	int sizeY = 500;
	
	InfoPanel infoPanel = new InfoPanel();
	
	public MainFrame(){
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
	
		JButton createEntity = new JButton("Create Entity");
		JButton runSimulation = new JButton("Run Simulation");
		JButton runPack = new JButton("Run Pack Simulation");
		JButton runSelection = new JButton("Run Selection");
		JButton runAutoEvolution = new JButton("Run Auto Evolution");
		JButton refreshInformation = new JButton("Refresh Information");
		JButton exit = new JButton("Exit");
		
		add(new JPanel(){{
			setLayout(new BorderLayout());
			add(new JPanel(){{
				add(createEntity);
				add(runSelection);
				add(refreshInformation);
			}}, BorderLayout.NORTH);
			add(new JPanel(){{
				add(runSimulation);
				add(runPack);
				add(runAutoEvolution);
			}}, BorderLayout.CENTER);
			add(new JPanel(){{
				add(exit);
			}}, BorderLayout.SOUTH);
		}}, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		
		createEntity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new createEntity();
			}
		});
		createEntity.setPreferredSize(new Dimension(200,80));
		runSimulation.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new runSimulation();
			}
		});
		runSimulation.setPreferredSize(new Dimension(200,80));
		runPack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new runPack();
			}
		});
		runPack.setPreferredSize(new Dimension(200,80));
		runSelection.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				hiders.runSelection();
				seekers.runSelection();
			}
		});
		runSelection.setPreferredSize(new Dimension(200,80));
		runAutoEvolution.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new AutoEvolution();
			}
		});
		runAutoEvolution.setPreferredSize(new Dimension(200,80));
		refreshInformation.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				MainFrame.this.infoPanel.refresh();
			}
		});
		refreshInformation.setPreferredSize(new Dimension(200,80));
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				exit();
			}
		});
		exit.setPreferredSize(new Dimension(200,80));
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		
		pack();
		setVisible(true);
	}
	
	private class InfoPanel extends JPanel{
		int[] idsList = new int[0];
		JComboBox<String> comboBox = new JComboBox<String>(){{
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(getSelectedIndex() < 0) return;
					InfoPanel.this.textArea.setText(etts[idsList[getSelectedIndex()]].getInfo());
				}
			});
		}};
		JTextArea textArea = new JTextArea(){{
			setEditable(false);
		}};
		public InfoPanel(){
			setLayout(new BorderLayout());
			add(comboBox, BorderLayout.NORTH);
			add(new JScrollPane(textArea){{
				setPreferredSize(new Dimension(500,500));
			}}, BorderLayout.CENTER);
		}
		public void refresh(){
			idsList = new int[0];
			for(int i = 0; i < etts.length; i++){
				if(etts[i] instanceof HiderNPC || etts[i] instanceof SeekerNPC){
					int[] temp = new int[idsList.length + 1];
					for(int j = 0; j < idsList.length; j++)
						temp[j] = idsList[j];
					temp[idsList.length] = i;
					idsList = temp;
				}
			}
			String[] items = new String[idsList.length];
			for(int i = 0; i < idsList.length; i++)
				items[i] = etts[idsList[i]].name;
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
			comboBox.setModel(model);
		}
	}
	
	private void exit(){
		dispose();
	}
	
	private void setOccupied(){
		setVisible(false);
	}
	
	private void setUnoccupied(){
		setVisible(true);
	}
	
	private class runSimulation extends JFrame{
		int[] inList = new int[0];
		int[] offList;
		
		JList listIn;
		JList listOff;
		
		JScrollPane listInScroll;
		JScrollPane listOffScroll;
		
		public runSimulation(){
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			
			offList = new int[etts.length];
			for(int i = 0; i < offList.length; i++)
				offList[i] = i;
			
			String[] list = new String[offList.length];
			for(int i = 0; i < offList.length; i++)
				list[i] = Integer.toString(offList[i]);
			
			listOff = new JList<>(list);
			listOffScroll = new JScrollPane(listOff);
			listOffScroll.setPreferredSize(new Dimension(80,400));
			add(listOffScroll);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("add All"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							for(int id:offList)
								addToInList(id);
							
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("add"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(listOff.getSelectedIndex() == -1) return;
							
							int n = 0;
							for(int id:listOff.getSelectedIndices())
								addToInList(offList[id-(n++)]);
							
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.CENTER);
				add(new JButton("remove"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(listIn.getSelectedIndex() == -1) return;
							
							int n = 0;
							for(int id:listIn.getSelectedIndices())
								removeFromInList(inList[id-(n++)]);
						
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			
			listIn = new JList<>();
			listInScroll = new JScrollPane(listIn);
			listInScroll.setPreferredSize(new Dimension(80,400));
			add(listInScroll);
			
			add(new JButton("Start"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						start();
					}
				});
			}});
			
			pack();
			setVisible(true);
		}
		
		private void addToInList(int id){
			for(int i:inList)
				if(i == id) return;
			
			int[] temp = new int[inList.length+1];
			for(int i = 0; i < inList.length; i++)
				temp[i] = inList[i];
			temp[inList.length] = id;
			inList = temp;
			
			temp = new int[offList.length-1];
			int n = 0;
			for(int i = 0; i < offList.length; i++){
				if(offList[i] == id) continue;
				temp[n++] = offList[i];
			}
			offList = temp;
		}
		
		private void removeFromInList(int id){
			exist:{
				for(int i:inList)
					if(i == id) break exist;
				return;
			}
			
			int[] temp = new int[inList.length-1];
			int n = 0;
			for(int i = 0; i < inList.length; i++){
				if(inList[i] == id) continue;
				temp[n++] = inList[i];
			}
			inList = temp;
			
			temp = new int[offList.length+1];
			for(int i = 0; i < offList.length; i++)
				temp[i] = offList[i];
			temp[offList.length] = id;
			offList = temp;
		}
		
		private void start(){
			setOccupied();
			(new Thread(new Runnable(){
				@Override
				public void run(){
					Sync room = new Sync();
					for(int i = 0; i < 500; i++){
						Border border = new Border();
						border.start(room);
						border.spawn(0.0f,(float)i);
						border = new Border();
						border.start(room);
						border.spawn(500.0f,(float)i);
					}
					for(int i = 1; i < 499; i++){
						Border border = new Border();
						border.start(room);
						border.spawn((float)i,0.0f);
						border = new Border();
						border.start(room);
						border.spawn((float)i,500.0f);
					}
					for(int i:inList){
						etts[i].start(room);
					}
					dispose();
					room.run(new GameFrame());
					MainFrame.this.setUnoccupied();
				}
			})).start();
		}
	}
	
	private class runPack extends JFrame{
		int sizeOfPack = 100;
		int[] inList = new int[0];
		int[] offList;
		
		JList listIn;
		JList listOff;
		
		JScrollPane listInScroll;
		JScrollPane listOffScroll;
		
		JSlider slider = new JSlider(0, 1000, 100){{
			addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e){
					sizeOfPack = slider.getValue();
					label0.setText(" " + sizeOfPack);
					runPack.this.pack();
				}
			});
		}};
		JLabel label0 = new JLabel(" 100");
		
		JTextField textField = new JTextField("300", 5);
		JLabel label1 = new JLabel("Timer: ");
		
		public runPack(){
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			
			offList = new int[etts.length];
			for(int i = 0; i < offList.length; i++)
				offList[i] = i;
			
			String[] list = new String[offList.length];
			for(int i = 0; i < offList.length; i++)
				list[i] = Integer.toString(offList[i]);
			
			listOff = new JList<>(list);
			listOffScroll = new JScrollPane(listOff);
			listOffScroll.setPreferredSize(new Dimension(80,400));
			add(listOffScroll);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("add All"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							for(int id:offList)
								addToInList(id);
							
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("add"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(listOff.getSelectedIndex() == -1) return;
							
							int n = 0;
							for(int id:listOff.getSelectedIndices())
								addToInList(offList[id-(n++)]);
							
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.CENTER);
				add(new JButton("remove"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(listIn.getSelectedIndex() == -1) return;
							
							int n = 0;
							for(int id:listIn.getSelectedIndices())
								removeFromInList(inList[id-(n++)]);
						
							String[] list = new String[offList.length];
							for(int i = 0; i < offList.length; i++)
								list[i] = Integer.toString(offList[i]);
							
							listOff = new JList<>(list);
							listOffScroll.setViewportView(listOff);
							
							list = new String[inList.length];
							for(int i = 0; i < inList.length; i++)
								list[i] = Integer.toString(inList[i]);
							
							listIn = new JList<>(list);
							listInScroll.setViewportView(listIn);
						}
					});
				}}, BorderLayout.SOUTH);
			}});
			listIn = new JList<>();
			listInScroll = new JScrollPane(listIn);
			listInScroll.setPreferredSize(new Dimension(80,400));
			add(listInScroll);
			
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JPanel(){{
					add(new JLabel("Size of pack: "));
					add(slider);
					add(label0);
				}}, BorderLayout.NORTH);
				add(new JPanel(){{
					add(label1);
					add(textField);
				}}, BorderLayout.SOUTH);
			}});
			add(new JButton("Start"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						start();
					}
				});
			}});
			
			pack();
			setVisible(true);
		}
		
		private void addToInList(int id){
			for(int i:inList)
				if(i == id) return;
			
			int[] temp = new int[inList.length+1];
			for(int i = 0; i < inList.length; i++)
				temp[i] = inList[i];
			temp[inList.length] = id;
			inList = temp;
			
			temp = new int[offList.length-1];
			int n = 0;
			for(int i = 0; i < offList.length; i++){
				if(offList[i] == id) continue;
				temp[n++] = offList[i];
			}
			offList = temp;
		}
		
		private void removeFromInList(int id){
			exist:{
				for(int i:inList)
					if(i == id) break exist;
				return;
			}
			
			int[] temp = new int[inList.length-1];
			int n = 0;
			for(int i = 0; i < inList.length; i++){
				if(inList[i] == id) continue;
				temp[n++] = inList[i];
			}
			inList = temp;
			
			temp = new int[offList.length+1];
			for(int i = 0; i < offList.length; i++)
				temp[i] = offList[i];
			temp[offList.length] = id;
			offList = temp;
		}
		
		private void start(){
			setOccupied();
			(new Thread(new Runnable(){
				@Override
				public void run(){
					try{
						Integer timer = Integer.valueOf(runPack.this.textField.getText());
						if(timer < 50){
						label1.setText("Invalid timer");
								MainFrame.this.setUnoccupied();
								return;
						}
						setVisible(false);
						for(int i = 0; i < sizeOfPack; i++){
							Sync room = new Sync();
							for(int j = 0; j < 500; j++){
								Border border = new Border();
								border.start(room);
								border.spawn(0.0f,(float)j);
								border = new Border();
								border.start(room);
								border.spawn(500.0f,(float)j);
							}
							for(int j = 1; j < 499; j++){
								Border border = new Border();
								border.start(room);
								border.spawn((float)j,0.0f);
								border = new Border();
								border.start(room);
								border.spawn((float)j,500.0f);
							}
							for(int id:inList){
								if(!(etts[id] instanceof HiderPlayer || etts[id] instanceof SeekerPlayer))
									etts[id].start(room);
							}
							room.run((long)timer);
							System.out.println("game "+ i + " out of " + sizeOfPack + " completed");
						}
						dispose();
					} catch(NumberFormatException e){
						label1.setText("Invalid timer");
						MainFrame.this.setUnoccupied();
						return;
					}
					MainFrame.this.setUnoccupied();
				}
			})).start();
		}
	}
	
	private class AutoEvolution{
		public AutoEvolution(){
			setOccupied();
			(new Thread(new Runnable(){
				@Override
				public void run(){
					seekers.setBackPropagate(false);
					hiders.setBackPropagate(false);
					int h = 0;
					int s = 0;
					for(Entity e:etts){
						if(e instanceof HiderNPC)
							h++;
						else if(e instanceof SeekerNPC)
							s++;
					}
					Entity[] p = new Entity[h+s];
					int n = 0;
					int m = 0;
					for(int i = 0; i < etts.length; i++){
						if(etts[i] instanceof HiderNPC)
							p[n++] = etts[i];
						else if(etts[i] instanceof SeekerNPC)
							p[p.length-1-(m++)] = etts[i];
					}
					for(int j = 0; j < 1; j++){
						for(int k = 0; k < 3; k++){
							m = 0;
							for(int i = 0; i+1 < h && m < s; i++){
								Sync room = new Sync();
								p[i++].start(room);
								p[i].start(room);
								p[p.length-1-(m++)].start(room);
								room.run(50);
							}
						}
						hiders.runSelection();
						seekers.runSelection();
					}
					MainFrame.this.setUnoccupied();
				}
			})).start();
		}
	}
	
	private class createEntity extends JFrame{
		int type = 0;
		int qtt = 1;
		String[] typeOptions = new String[]{"Hider", "Seeker", "Wall"};
		boolean userControlled = true;
		JLabel label0 = new JLabel("User Controlled Hider");
		JLabel label1 = new JLabel("Quantity: 1");
		
		private createEntity(){
			setName("creating entity");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());
			
			JComboBox typeBox = new JComboBox<>(typeOptions);
			typeBox.setSize(new Dimension(80,40));
			typeBox.setLocation(0,40);
			typeBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					type = typeBox.getSelectedIndex();
					if(type == 2) label0.setText("Wall");
					else if(userControlled)
						label0.setText("User Controlled " + typeOptions[type]);
					else label0.setText(typeOptions[type] + " AI");
					createEntity.this.pack();
				}
			});
			add(typeBox);
			add(new JButton("User Controlled"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						userControlled = !userControlled;
						if(userControlled) setText("User Controlled");
						else setText("AI");
						if(type == 2) label0.setText("Wall");
						else if(userControlled)
							label0.setText("User Controlled " + typeOptions[type]);
						else label0.setText(typeOptions[type] + " AI");
						createEntity.this.pack();
					}
				});
			}});
			add(label1);
			add(new JPanel(){{
				setLayout(new BorderLayout());
				add(new JButton("+"){{
						addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								counter = 0;
								createEntity.this.qtt++;
								createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
								createEntity.this.pack();
							}
						});
						getModel().addChangeListener(new ChangeListener(){
							@Override
							public void stateChanged(ChangeEvent e){
								if(getModel().isPressed())
									trigger.start();
								else
									trigger.stop();
							}
						});
					}
					int counter;
					Timer trigger = new Timer(125, new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(counter == 10)
								trigger.setDelay(75);
							else if(counter == 20)
								trigger.setDelay(25);
							counter++;
							createEntity.this.qtt++;
							createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
							createEntity.this.pack();
						}
					}){{
       						setCoalesce(true);
						setRepeats(true);
					}};
				}, BorderLayout.NORTH);
				add(new JButton("-"){{
						addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								if(createEntity.this.qtt == 1) return;
								counter = 0;
								createEntity.this.qtt--;
								createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
								createEntity.this.pack();
							}
						});
						addChangeListener(new ChangeListener(){
							@Override
							public void stateChanged(ChangeEvent e){
								if(getModel().isPressed())
									trigger.start();
								else
									trigger.stop();
							}
						});
					}
					int counter;
					Timer trigger = new Timer(125, new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createEntity.this.qtt == 1) return;
							if(counter == 10)
								trigger.setDelay(75);
							else if(counter == 20)
								trigger.setDelay(25);
							counter++;
							createEntity.this.qtt--;
							createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
							createEntity.this.pack();
						}
					}){{
       						setCoalesce(true);
						setRepeats(true);
					}};
				}, BorderLayout.SOUTH);
			}});
			add(new JButton("done"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						Entity[] temp = new Entity[etts.length+qtt];
						for(int i = 0; i < etts.length; i++)
							temp[i] = etts[i];
						switch(typeBox.getSelectedIndex()){
							case 0:
								if(userControlled){
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new HiderPlayer();
								} else{
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new HiderNPC(hiders);
									new AIEditorFrame(etts, temp, hiders);
								}
								break;
							case 1:
								if(userControlled){
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new SeekerPlayer();
								} else{
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new SeekerNPC(seekers);
									new AIEditorFrame(etts, temp, seekers);
								}
								break;
							case 2:
								for(int i = etts.length; i < temp.length; i++)
									temp[i] = new Wall();
								break;
						}
						etts = temp;
						dispose();
					}
				});
			}});
			add(label0);
			
			pack();
			setVisible(true);
		}
	}
}