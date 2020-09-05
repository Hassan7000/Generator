package me.hassan.riftgenerators.events;

import java.util.UUID;

import me.hassan.riftgenerators.utils.HologramUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.enums.Type;
import me.hassan.riftgenerators.inventory.GeneratorInventory;
import me.hassan.riftgenerators.objects.Storage;
import me.hassan.riftgenerators.utils.Common;

public class GeneratorClick implements Listener {
	
	@EventHandler
	public void onGeneratorClick(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		if(block != null) {
			Location location = block.getLocation();
			Player player = e.getPlayer();
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {								
				if(Storage.isGenerator(location)) {
					GeneratorData data = Storage.getGeneratorData(location);
					
					if(e.getHand() == EquipmentSlot.OFF_HAND) return;

					
					if(player.isSneaking()) {
						if(this.isMember(data, player.getUniqueId()) || player.getUniqueId().toString().equalsIgnoreCase(data.getOwner()) || player.hasPermission("riftgenerators.bypass")) {
							String type = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + data.getGeneratorName() + ".Type");

							if (Type.valueOf(type.toUpperCase()) == Type.COMMAND) {
								String command = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + data.getGeneratorName() + ".Reward");
								command = command.replace("{player}", player.getName());
								command = command.replace("{amount}", String.valueOf(data.getItemAmount()));
								Common.executeConsoleCommand(command);
								data.setItemAmount(0);
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
								return;
							}
							if (Type.valueOf(type.toUpperCase()) == Type.MATERIAL) {
								String material = RiftGenerators.getInstance().getConfig()
										.getString("Generator-Items." + data.getGeneratorName() + ".Reward");

								if (Material.matchMaterial(material) == null) {
									material = "STONE";
								}

								ItemStack reward = new ItemStack(Material.valueOf(material), data.getItemAmount());
								data.setItemAmount(0);
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
								return;
							}

							if (Type.valueOf(type.toUpperCase()) == Type.VAULT) {
								int amount = data.getItemAmount();
								RiftGenerators.getInstance().econ.depositPlayer(player, amount);
								data.setItemAmount(0);
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
								return;
							}

						}else {
							Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("NotHighEnoughRank"));
							return;
						}
						
					}
					
					e.setCancelled(true);



						if(this.isMember(data, player.getUniqueId()) || player.getUniqueId().toString().equalsIgnoreCase(data.getOwner())) {
							GeneratorInventory inv = new GeneratorInventory();

							if(HologramUtils.hasHologram(location)){

							}

							inv.createInventory(player, data);
							RiftGenerators.getInstance().location.put(player.getUniqueId(), location);
						}else {
							Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("NotHighEnoughRank"));
						}																									
				}
				
			}
		}
			
	}
	
	
	
	private boolean isMember(GeneratorData data, UUID uuid) {
		
		if(data.getMembers().size() > 0) {
			return data.getMembers().contains(uuid.toString());
		}
		
		return false;
	}
	
	

}
