package com.benz.beneathskies.commands;

import com.benz.beneathskies.actors.HoverCraft;

/**
 * Created by amineBenz on 24/03/2016.
 * this interface describes a basic command on any hover, just iplement it to add a new command, ex : shootMissiles
 */
public interface Command {

	/**
	 * represents the execution code of this command, it only calls another method in the player
	 * @param hoverCraft
	 */
	public void execute(HoverCraft hoverCraft);

}
