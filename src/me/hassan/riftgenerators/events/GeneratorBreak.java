package me.hassan.riftgenerators.events;

import java.util.Map;

import me.hassan.multiprinter.MultiPrinter;
import me.hassan.riftgenerators.GeneratorBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.objects.GeneratorItem;
import me.hassan.riftgenerators.objects.Storage;
import me.hassan.riftgenerators.utils.Common;
import me.hassan.riftgenerators.utils.HologramUtils;



public class GeneratorBreak implements Listener {

	@EventHandler
	public void onGeneratorBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		Location location = block.getLocation();
		String stringLocation = RiftGenerators.getInstance().stringFromLoc(location);


		if(Storage.isGenerator(location)) {



			if(player.getUniqueId().toString().equalsIgnoreCase(Storage.getGeneratorData(location).getOwner()) || player.hasPermission("riftgenerators.bypass")) {


				GeneratorBreakBlockEvent event = new GeneratorBreakBlockEvent(player, location);
				Bukkit.getPluginManager().callEvent(event);

				if(event.isCancelled()){
					e.setCancelled(true);

				}


				e.setCancelled(true);
				block.setType(Material.AIR);
				
				int level = Storage.getGeneratorData(location).getLevel();
				String name = Storage.getGeneratorData(location).getGeneratorName();
				
				GeneratorItem item = new GeneratorItem(player, level, name);
				
				item.createGenerator(level);
				
				//Handle break message here
				Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("BreakGenerator"));
				
				
				Storage.removeData(location);
				RiftGenerators.getInstance().data.remove(stringLocation);

				HologramUtils.removeHologram(location);
			}else {
				e.setCancelled(true);
				Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("NotHighEnoughRank"));
			}
			
			
			
			
			
			
			
		}
		
	}

}
