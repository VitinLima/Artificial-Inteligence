package entity;

import javax.swing.*;
import java.awt.*;

public class Wall extends Entity{
	public Wall(){
		super(true, true, true, true, false, "Wall", Color.white, 20.0f, 500.0f);
	}
	
	@Override
	public void step(){}
	
	@Override
	public String getInfo(){
		return "Wall";
	}
}