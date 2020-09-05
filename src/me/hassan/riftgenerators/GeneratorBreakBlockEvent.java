package me.hassan.riftgenerators;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GeneratorBreakBlockEvent extends Event {

	private boolean isCancelled;
	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}


	private Location location;
	private Player player;
	public GeneratorBreakBlockEvent(Player player, Location location){
		this.location = location;
		this.player = player;
	}

	public Location getLocation(){
		return location;
	}

	public Player getPlayer(){
		return player;
	}

}
