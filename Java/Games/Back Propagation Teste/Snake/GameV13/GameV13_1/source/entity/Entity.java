package entity;

import game.Game_Object;

import java.awt.*;

public class Entity extends Thread{
	private int id;
	private Color color;
	private int[][] coordinates = new int[0][0];
	
	private Game_Object obj;
	
	public Color fget_color(){
		return color;
	}
	public void fset_color(Color new_color){
		color = new_color;
	}
	public int[][] fget_coordinates(){
		return coordinates;
	}
	public void fset_coordinates(int[][] new_coordinates){
		coordinates = new_coordinates;
		obj.fget_map().fadd(getId(), color, coordinates);
	}
	public void fset_fruits(){
		for(int[] c:coordinates)
			obj.fget_map().fadd_fruit(c[0],c[1]);
		coordinates = new int[0][0];
	}
	public void fset_obj(Game_Object new_obj){
		obj = new_obj;
	}
	public Game_Object fget_obj(){
		return obj;
	}
	public boolean faction(){
		return false;
	}
	
	public void run(){
		boolean temp =  true;
		while(temp){
			synchronized(obj){
				obj.notifyAll();
				if(!faction() || !obj.fis_running()){
					fset_coordinates(new int[0][0]);
					temp = false;
				}
				try{
					obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		synchronized(obj){
			obj.notifyAll();
		}
	}
}