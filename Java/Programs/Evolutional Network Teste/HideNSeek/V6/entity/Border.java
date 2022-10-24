package entity;

import javax.swing.*;
import java.awt.*;

public class Border extends Entity{
	public Border(){
		super(false, false, true, true, false, "Border Wall", Color.yellow, 10.0f, 500.0f);
	}
	
	@Override
	public void step(){}
	
	@Override
	public String getInfo(){
		return "Border Wall";
	}
}