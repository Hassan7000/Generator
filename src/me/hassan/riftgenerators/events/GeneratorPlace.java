package me.hassan.riftgenerators.events;

import java.util.ArrayList;
import java.util.List;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.objects.Storage;
import me.hassan.riftgenerators.safenbt.SafeNBT;
import me.hassan.riftgenerators.utils.Common;
import me.hassan.riftgenerators.utils.HologramUtils;

public class GeneratorPlace implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void generatorPlace(BlockPlaceEvent e) {
		Block block = e.getBlock();
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();

		if (item != null && item.getType() != Material.AIR) {

			SafeNBT nbt = SafeNBT.get(item);

			if (nbt.hasKey("riftgenerators") && !e.isCancelled()) {
				
				
				
				int level = Integer.valueOf(nbt.getString("level"));
				int seconds = Integer.valueOf(nbt.getString("seconds"));
				String generatorName = nbt.getString("riftgenerators");
				List<String> members = new ArrayList<>();

				RiftGenerators.getInstance().getData().put(
						RiftGenerators.getInstance().stringFromLoc(block.getLocation()),
						new GeneratorData(player.getUniqueId().toString(), generatorName, level, 0, members, seconds));

				// SHOW HOLOGRAM HERE

				if (Storage.isGenerator(block.getLocation())) {

					HologramUtils.createHologram(block.getLocation(), Storage.getGeneratorData(block.getLocation()));

				}

				// SEND THE PLACE MESSAGE HERE.
				Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("PlaceGenerator"));

			}
		}

	}

}
