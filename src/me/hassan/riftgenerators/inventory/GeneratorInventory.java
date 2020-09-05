package me.hassan.riftgenerators.inventory;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.utils.Common;
import me.hassan.riftgenerators.utils.ItemBuilder;

public class GeneratorInventory {
	
	
	public void createInventory(Player player, GeneratorData data) {
		int size = RiftGenerators.getInstance().getConfig().getInt("Generator-Gui-Size");
		String title = RiftGenerators.getInstance().getConfig().getString("Generator-Gui-Name");
		Inventory inv = Bukkit.createInventory(player, size, Common.colorMessage(title));
		
		for(String menu : RiftGenerators.getInstance().getConfig().getConfigurationSection("Generator-Gui." + data.getGeneratorName()).getKeys(false)) {
			
			String path = "Generator-Gui." + data.getGeneratorName() + "." + menu;
			
			String material = RiftGenerators.getInstance().getConfig().getString(path + ".Material");
			
			if(Material.matchMaterial(material) == null) {
				material = "STONE";
			}
			
			String name = RiftGenerators.getInstance().getConfig().getString(path + ".Name");
			
			
			int price = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Price");
			int seconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + data.getGeneratorName() + "." + String.valueOf(data.getLevel()) + ".Seconds");
			ArrayList<String> formattedLore = new ArrayList<>();
			
			for(String lore : RiftGenerators.getInstance().getConfig().getStringList(path + ".Lore")) {
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
			
			ItemStack item = new ItemBuilder(Material.valueOf(material))
					.setDisplayName(Common.colorMessage(name))
					.setLore(formattedLore)
					.build();
			inv.setItem(slot, item);
			ItemStack fillItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
			this.fillEmptySlots(inv, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)
					.setDisplayName(Common.colorMessage("&f "))
					.build());
		}
		RiftGenerators.getInstance().inventory.put(player.getUniqueId(), inv);
		RiftGenerators.getInstance().generator.put(player.getUniqueId(), data);
		player.openInventory(inv);
		
		
	}
	
	private void fillEmptySlots(Inventory inv, ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                inv.setItem(i, item);
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
