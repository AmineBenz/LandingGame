package com.benz.beneathskies.commands;

import com.benz.beneathskies.actors.HoverCraft;

/**
 * Created by amineBenz on 24/03/2016.
 * will ask the hoverCraft gently to hover right
 */
public class hoverRightCommand implements Command {
	float force;

	public hoverRightCommand(float force){
		this.force = force;
	}
	@Override
	public void execute(HoverCraft hoverCraft) {
		hoverCraft.hoverRight(force);
	}
}
