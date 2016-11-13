package com.benz.beneathskies.commands;

import com.benz.beneathskies.actors.HoverCraft;

/**
 * Created by amineBenz on 24/03/2016.
 * will ask the hoverCraft to hover left
 */
public class hoverLeftCommand implements Command {
	float force;

	public hoverLeftCommand(float force){
		this.force = force;
	}
	@Override
	public void execute(HoverCraft hoverCraft) {
		hoverCraft.hoverLeft(force);
	}
}
