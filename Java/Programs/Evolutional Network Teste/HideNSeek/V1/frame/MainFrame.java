package frame;

import entity.Entity;
import entity.Hider;
import entity.HiderAI;
import entity.HiderUser;
import entity.Seeker;
import entity.SeekerAI;
import entity.SeekerUser;
import entity.Wall;
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
	Sync room = new Sync();
	
	int sizeX = 500;
	int sizeY = 500;
	
	JButton createEntity = new JButton("Create Entity");
	JButton runSimulation = new JButton("Run Simulation");
	JButton runPack = new JButton("Run Pack Simulation");
	JButton runSelection = new JButton("Run Selection");
	JButton exit = new JButton("Exit");
	InfoPanel infoPanel = new InfoPanel();
	
	public MainFrame(){
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		setMethods();
		add(new JPanel(){{
			add(createEntity);
			add(runSimulation);
			add(runPack);
			add(runSelection);
			add(new JButton("Refresh Information"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						MainFrame.this.infoPanel.refresh();
					}
				});
				setPreferredSize(new Dimension(200,80));
			}});
			add(exit);
		}}, BorderLayout.NORTH);
		
		add(infoPanel, BorderLayout.CENTER);
		
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
				if(etts[i] instanceof HiderAI || etts[i] instanceof SeekerAI){
					int[] temp = new int[idsList.length + 1];
					for(int j = 0; j < idsList.length; j++)
						temp[j] = idsList[j];
					temp[idsList.length] = i;
					idsList = temp;
				}
			}
			String[] items = new String[idsList.length];
			for(int i = 0; i < idsList.length; i++)
				items[i] = etts[idsList[i]].getName();
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
			comboBox.setModel(model);
		}
	}
	
	private void exit(){
		room.close();
		dispose();
	}
	
	private void setMethods(){
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
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				exit();
			}
		});
		exit.setPreferredSize(new Dimension(200,80));
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
			
			add(new JButton("add"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if(listOff.getSelectedIndex() == -1) return;
						
						int n = 0;
						for(int id:listOff.getSelectedIndices()){
							addToInList(offList[id-n]);
							n++;
						}
						
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
			}});
			add(new JButton("remove"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if(listIn.getSelectedIndex() == -1) return;
						
						int n = 0;
						for(int id:listIn.getSelectedIndices()){
							removeFromInList(inList[id-n]);
							n++;
						}
						
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
			room.open();
			for(int i:inList){
				etts[i].start(room);
			}
			new GameFrame(room);
			room.setTimer(-1);
			Thread thread = new Thread(room); 
			thread.start();
			dispose();
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
		
		JSlider slider = new JSlider(0,100, 100){{
			addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e){
					sizeOfPack = slider.getValue();
					label.setText(" " + sizeOfPack);
					runPack.this.pack();
				}
			});
		}};
		JLabel label = new JLabel(" 100");
		
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
			
			add(new JButton("add"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if(listOff.getSelectedIndex() == -1) return;
						
						int n = 0;
						for(int id:listOff.getSelectedIndices()){
							addToInList(offList[id-n]);
							n++;
						}
						
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
			}});
			add(new JButton("remove"){{
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if(listIn.getSelectedIndex() == -1) return;
						
						int n = 0;
						for(int id:listIn.getSelectedIndices()){
							removeFromInList(inList[id-n]);
							n++;
						}
						
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
			}});
			
			listIn = new JList<>();
			listInScroll = new JScrollPane(listIn);
			listInScroll.setPreferredSize(new Dimension(80,400));
			add(listInScroll);
			
			add(new JLabel("Size of pack: "));
			add(slider);
			add(label);
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
			setVisible(false);
			room.open();
			for(int i = 0; i < sizeOfPack; i++){
				for(int id:inList){
					if(!(etts[id] instanceof HiderUser || etts[id] instanceof SeekerUser))
						etts[id].start(room);
				}
				room.setTimer(3000);
				Thread thread = new Thread(room);
				thread.start();
				while(!room.isClosed()){
					try{
						Thread.sleep(100);
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
			dispose();
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
							createEntity.this.qtt++;
							createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
							createEntity.this.pack();
						}
					});
				}}, BorderLayout.NORTH);
				add(new JButton("-"){{
					addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(createEntity.this.qtt == 1) return;
							createEntity.this.qtt--;
							createEntity.this.label1.setText("Quantity: " + createEntity.this.qtt);
							createEntity.this.pack();
						}
					});
				}}, BorderLayout.SOUTH);
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
										temp[i] = new HiderUser(0.0f, (float)sizeX, 0.0f, (float)sizeY);
								} else{
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new HiderAI(0.0f, (float)sizeX, 0.0f, (float)sizeY, hiders);
									new AIEditorFrame(etts, temp, hiders);
								}
								break;
							case 1:
								if(userControlled){
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new SeekerUser(0.0f, (float)sizeX, 0.0f, (float)sizeY);
								} else{
									for(int i = etts.length; i < temp.length; i++)
										temp[i] = new SeekerAI(0.0f, (float)sizeX, 0.0f, (float)sizeY, seekers);
									new AIEditorFrame(etts, temp, seekers);
								}
								break;
							case 2:
								for(int i = etts.length; i < temp.length; i++)
									temp[i] = new Wall(0.0f, (float)sizeX, 0.0f, (float)sizeY);
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