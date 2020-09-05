package me.hassan.riftgenerators.task;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.enums.Type;
import me.hassan.riftgenerators.utils.HologramUtils;

public class GeneratorTask extends BukkitRunnable {



	@Override
	public void run() {
		for(Map.Entry<String, GeneratorData> entry : RiftGenerators.getInstance().getData().entrySet()){
			Location location = RiftGenerators.getInstance().locFromString(entry.getKey());

			if(location.getBlock() != null && location.getBlock().getType() != Material.AIR) {
				int seconds = entry.getValue().getSeconds();

				if(seconds > 0) {
					seconds--;
					entry.getValue().setSeconds(seconds);


					HologramUtils.useHologram(location, entry.getValue());

				}else {
					if(seconds == 0) {

						handleReward(entry.getValue());

						HologramUtils.useHologram(location, entry.getValue());

					}
				}
			}else {
				HologramUtils.removeHologram(location);


			}




		}

	}

	public void handleReward(GeneratorData data) {
		int defaultSeconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel())  + ".Seconds");

		data.setSeconds(defaultSeconds);

		int rewardAmount = Integer.valueOf(RiftGenerators.getInstance().getConfig().getString("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel())  + ".Reward"));
		data.setItemAmount(data.getItemAmount() + rewardAmount);

	}
}
