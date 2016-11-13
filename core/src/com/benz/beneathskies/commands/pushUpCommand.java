package com.benz.beneathskies.commands;

import com.benz.beneathskies.actors.HoverCraft;

/**
 * Created by amineBenz on 24/03/2016.
 * will ask the hover to pulse up yeah!
 */
public class pushUpCommand implements Command {
	@Override
	public void execute(HoverCraft hoverCraft) {
		hoverCraft.pushUp();
	}
}
