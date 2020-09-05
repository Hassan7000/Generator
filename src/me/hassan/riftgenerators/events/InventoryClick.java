package me.hassan.riftgenerators.events;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.enums.Action;
import me.hassan.riftgenerators.enums.Type;
import me.hassan.riftgenerators.objects.GeneratorItem;
import me.hassan.riftgenerators.objects.Storage;
import me.hassan.riftgenerators.utils.Common;
import me.hassan.riftgenerators.utils.HologramUtils;
import me.hassan.riftgenerators.utils.ItemBuilder;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if (RiftGenerators.getInstance().inventory.containsKey(player.getUniqueId())
				&& RiftGenerators.getInstance().generator.containsKey(player.getUniqueId())) {
			Inventory playerInventory = RiftGenerators.getInstance().inventory.get(player.getUniqueId());

			if (inventory == playerInventory || inventory.equals(playerInventory)) {
				e.setCancelled(true);
				GeneratorData data = RiftGenerators.getInstance().generator.get(player.getUniqueId());

				for (String menu : RiftGenerators.getInstance().getConfig().getConfigurationSection("Generator-Gui." + data.getGeneratorName()).getKeys(false)) {
					String path = "Generator-Gui." + data.getGeneratorName() + "." + menu;

					String action = RiftGenerators.getInstance().getConfig().getString(path + ".Action");
					int slot = RiftGenerators.getInstance().getConfig().getInt(path + ".Slot");
					
					if(e.getSlot() == slot) {
						if (Action.valueOf(action.toUpperCase()) == Action.CLAIM_REWARDS) {

							if (data.getItemAmount() > 0) {

								String type = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + data.getGeneratorName() + ".Type");

								if (Type.valueOf(type.toUpperCase()) == Type.COMMAND) {
									
									if(data.getItemAmount() > 0) {
										String command = RiftGenerators.getInstance().getConfig()
												.getString("Generator-Items." + data.getGeneratorName() + ".Reward");
										command = command.replace("{player}", player.getName());
										command = command.replace("{amount}", String.valueOf(data.getItemAmount()));
										Common.executeConsoleCommand(command);
										data.setItemAmount(0);
										this.updateInventory(inventory, data);
										Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
									}else {
										Common.sendMessage(player, "&cYou don't have any rewards to claim");
									}
									
								}

								if (Type.valueOf(type.toUpperCase()) == Type.MATERIAL) {
									String material = RiftGenerators.getInstance().getConfig()
											.getString("Generator-Items." + data.getGeneratorName() + ".Reward");

									if (Material.matchMaterial(material) == null) {
										material = "STONE";
									}

									ItemStack reward = new ItemStack(Material.valueOf(material), data.getItemAmount());
									data.setItemAmount(0);
									this.updateInventory(inventory, data);
									Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
									player.getInventory().addItem(reward);
								}

								if (Type.valueOf(type.toUpperCase()) == Type.VAULT) {
									int amount = data.getItemAmount();
									RiftGenerators.getInstance().econ.depositPlayer(player, amount);
									data.setItemAmount(0);
									this.updateInventory(inventory, data);
									Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("ClaimRewards"));
								}

							} else {
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("NoRewardsToClaim"));
							}
						}

						if (Action.valueOf(action.toUpperCase()) == Action.UPGRADE_BUTTON) {

							int price = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards."
									+ data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Price");

							if (RiftGenerators.getInstance().econ.getBalance(player) >= price) {
								Boolean lastRank = RiftGenerators.getInstance().getConfig().getBoolean("Generator-Rewards."
										+ data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Last-Level");

								if (lastRank) {

									Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("AlreadyLastRank"));
									return;
								}

								RiftGenerators.getInstance().econ.withdrawPlayer(player, price);

								int defaultSeconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards."+ data.getGeneratorName() + "." + String.valueOf(data.getLevel() + 1) + ".Seconds");
								data.setSeconds(defaultSeconds);
								data.setLevel(data.getLevel() + 1);
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("BoughtUpgrade")
										.replace("{price}", Common.formatValue(price)));
								this.updateInventory(inventory, data);
							} else {
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("NotEnoughMoney"));
							}
						}

						if (Action.valueOf(action.toUpperCase()) == Action.DESTORY_GENERATOR) {

							if (RiftGenerators.getInstance().location.containsKey(player.getUniqueId())) {
								Location location = RiftGenerators.getInstance().location.get(player.getUniqueId());

								location.getBlock().setType(Material.AIR);

								int level = data.getLevel();
								String name = data.getGeneratorName();

								GeneratorItem item = new GeneratorItem(player, level, name);

								item.createGenerator(level);
								Common.sendMessage(player, RiftGenerators.getInstance().getConfig().getString("BreakGenerator"));
								Storage.removeData(location);
								RiftGenerators.getInstance().data.remove(RiftGenerators.getInstance().stringFromLoc(location));								
								HologramUtils.removeHologram(location);
								player.closeInventory();
								
								
							}
						}
						
						if (Action.valueOf(action.toUpperCase()) == Action.MEMBER_BUTTON) {
								RiftGenerators.getInstance().memberState.put(player.getUniqueId(), data);
								player.closeInventory();
								for(String memberMessage : RiftGenerators.getInstance().getConfig().getStringList("Member-Message")) {
									Common.sendMessage(player, memberMessage);																	
							}
						}
					}
					
				}

			}

		}
	}

	private void updateInventory(Inventory inv, GeneratorData data) {
		for (String menu : RiftGenerators.getInstance().getConfig()
				.getConfigurationSection("Generator-Gui." + data.getGeneratorName()).getKeys(false)) {

			String path = "Generator-Gui." + data.getGeneratorName() + "." + menu;

			String material = RiftGenerators.getInstance().getConfig().getString(path + ".Material");

			if (Material.matchMaterial(material) == null) {
				material = "STONE";
			}

			String name = RiftGenerators.getInstance().getConfig().getString(path + ".Name");

			int price = RiftGenerators.getInstance().getConfig().getInt(
					"Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Price");
			int seconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName()
					+ "." + String.valueOf(data.getLevel()) + ".Seconds");
			ArrayList<String> formattedLore = new ArrayList<>();

			for (String lore : RiftGenerators.getInstance().getConfig().getStringList(path + ".Lore")) {
				lore = lore.replace("{level}", String.valueOf(data.getLevel()));
				lore = lore.replace("{amount}", String.valueOf(data.getItemAmount()));
				lore = lore.replace("{price}", Common.formatValue(price));
				lore = lore.replace("{seconds}", String.valueOf(seconds));
				lore = lore.replace("{nextlevel}", String.valueOf(this.nextLevel(data)));
				lore = lore.replace("{newrate}", String.valueOf(this.nextRate(data)));
				lore = lore.replace("{newamt}", String.valueOf(this.nextReward(data)));
				lore = lore.replace("{rate}", String.valueOf(this.getRate(data)));
				lore = lore.replace("{amt}", String.valueOf(this.getReward(data)));
				if(data.getMembers().size() > 0) {
					for(String members : data.getMembers()) {
						lore = lore.replace("{member}", this.getMemberName(UUID.fromString(members)));
					}
				}else {
					lore = lore.replace("{member}", "None");
				}
				
				formattedLore.add(Common.colorMessage(lore));
			}
			int slot = RiftGenerators.getInstance().getConfig().getInt(path + ".Slot");

			ItemStack item = new ItemBuilder(Material.valueOf(material)).setDisplayName(Common.colorMessage(name))
					.setLore(formattedLore).build();
			inv.setItem(slot, item);
			

		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory inventory = e.getInventory();
		Player player = (Player) e.getPlayer();
		if (RiftGenerators.getInstance().inventory.containsKey(player.getUniqueId()) && RiftGenerators.getInstance().generator.containsKey(player.getUniqueId())) {
			Inventory playerInventory = RiftGenerators.getInstance().inventory.get(player.getUniqueId());

			if (inventory == playerInventory || inventory.equals(playerInventory)) {
				RiftGenerators.getInstance().inventory.remove(player.getUniqueId());
				RiftGenerators.getInstance().generator.remove(player.getUniqueId());
				RiftGenerators.getInstance().location.remove(player.getUniqueId());
			}
				
		}
			
	}
	
	private String getMemberName(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if(player != null) {
			return player.getName();
		}else {
			return Bukkit.getOfflinePlayer(uuid).getName();
		}
	}
	
	private int nextLevel(GeneratorData data) {
		
		Boolean lastRank = RiftGenerators.getInstance().getConfig().getBoolean("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Last-Level");

		if (lastRank) {
			return Integer.valueOf(data.getLevel());
		}
		
		return Integer.valueOf(data.getLevel()) + 1;
		
	}
	
	
	private int getRate(GeneratorData data) {
		return RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel())  + ".Seconds");
	}
	
	private int getReward(GeneratorData data) {
		return Integer.valueOf(RiftGenerators.getInstance().getConfig().getString("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel())  + ".Reward"));
	}
	
	private int nextRate(GeneratorData data) {
		return RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(this.nextLevel(data))  + ".Seconds");
	}
	
	private int nextReward(GeneratorData data) {
		return Integer.valueOf(RiftGenerators.getInstance().getConfig().getString("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(this.nextLevel(data))  + ".Reward"));
	}
	

}
