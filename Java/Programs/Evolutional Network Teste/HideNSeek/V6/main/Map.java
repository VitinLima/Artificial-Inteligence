package main;

import entity.Border;
import entity.Wall;
import java.awt.*;

public class Map{
	public final Dimension wallSpawnArea;
	public final Dimension hiderSpawnArea;
	public final Dimension seekerSpawnArea;
	public final Point wallSpawnPoint;
	public final Point hiderSpawnPoint;
	public final Point seekerSpawnPoint;
	private Border[] borders = new Border[0];
	private Wall[] walls = new Wall[0];
	private Sync sync;
	public Map(int opt, Sync sync){
		this.sync = sync;
		loadBorder();
		switch(opt){
			case 1:
				openMapConstructor();
				wallSpawnArea = new Dimension(460,460);
				hiderSpawnArea = new Dimension(50,50);
				seekerSpawnArea = new Dimension(50,50);
				wallSpawnPoint = new Point(20,20);
				hiderSpawnPoint = new Point(100,150);
				seekerSpawnPoint = new Point(300,300);
				break;
			case 2:
				roomMapConstructor1();
				wallSpawnArea = new Dimension(460,460);
				hiderSpawnArea = new Dimension(50,50);
				seekerSpawnArea = new Dimension(50,50);
				wallSpawnPoint = new Point(20,20);
				hiderSpawnPoint = new Point(20,300);
				seekerSpawnPoint = new Point(300,300);
				break;
			case 3:
				roomMapConstructor2();
				wallSpawnArea = new Dimension(460,460);
				hiderSpawnArea = new Dimension(50,50);
				seekerSpawnArea = new Dimension(50,50);
				wallSpawnPoint = new Point(20,20);
				hiderSpawnPoint = new Point(20,300);
				seekerSpawnPoint = new Point(300,300);
				break;
			default:
				wallSpawnArea = new Dimension(460,460);
				hiderSpawnArea = new Dimension(50,50);
				seekerSpawnArea = new Dimension(50,50);
				wallSpawnPoint = new Point(20,20);
				hiderSpawnPoint = new Point(20,300);
				seekerSpawnPoint = new Point(300,300);
				break;
		}
	}
	private final void openMapConstructor(){
		for(int i = 0; i < 20; i += 1)
			(new Wall()).start(sync);
		for(int i = 100; i <= 300; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn((float)i,250.0f, true);
			border = new Border();
		}
	}
	private final void roomMapConstructor1(){
		for(int i = 0; i < 3; i += 1)
			(new Wall()).start(sync);
		for(int i = 0; i <= 250; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn((float)i,250.0f, true);
			border = new Border();
		}
		for(int i = 0; i <= 190; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn(250.0f,(float)i, true);
			border = new Border();
		}
		for(int i = 230; i <= 250; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn(250.0f,(float)i, true);
			border = new Border();
		}
	}
	private final void roomMapConstructor2(){
		for(int i = 0; i < 6; i += 1)
			(new Wall()).start(sync);
		for(int i = 0; i <= 100; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn((float)i,250.0f, true);
			border = new Border();
		}
		for(int i = 140; i <= 250; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn((float)i,250.0f, true);
			border = new Border();
		}
		for(int i = 0; i <= 190; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn(250.0f,(float)i, true);
			border = new Border();
		}
		for(int i = 230; i <= 250; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn(250.0f,(float)i, true);
			border = new Border();
		}
	}
	private final void loadBorder(){
		for(int i = 0; i <= 500; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn((float)i,0.0f, true);
			border = new Border();
			border.start(sync);
			border.spawn((float)i,500.0f, true);
		}
		for(int i = 10; i < 500; i += 10){
			Border border = new Border();
			border.start(sync);
			border.spawn(500.0f,(float)i, true);
			border = new Border();
			border.start(sync);
			border.spawn(0.0f,(float)i, true);
		}
	}
}