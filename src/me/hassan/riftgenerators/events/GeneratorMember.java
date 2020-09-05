package me.hassan.riftgenerators.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.utils.Common;

public class GeneratorMember implements Listener {
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String chatMessage = event.getMessage();
		
		if(RiftGenerators.getInstance().memberState.containsKey(player.getUniqueId())) {
			event.setCancelled(true);
			
			if(chatMessage.equalsIgnoreCase("cancel")) {
				Common.sendMessage(player, "&cYou have cancled the action");
				RiftGenerators.getInstance().memberState.remove(player.getUniqueId());
				return;
			}
			
			Player target = Bukkit.getPlayer(chatMessage);
			
			if(target != null) {
				
				GeneratorData data = RiftGenerators.getInstance().memberState.get(player.getUniqueId());
				if(!data.getMembers().contains(target.getUniqueId().toString())) {
					data.addMember(target.getUniqueId().toString());
					RiftGenerators.getInstance().memberState.remove(player.getUniqueId());
					Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("MemberAdded")
							.replace("{member}", target.getName()));
				}else {
					data.removeMember(target.getUniqueId().toString());
					RiftGenerators.getInstance().memberState.remove(player.getUniqueId());
					Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("MemberRemoved")
							.replace("{member}", target.getName()));
				}
				
				
			}else {
				Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("PlayerNotFound"));
			}
		}
	}

}
