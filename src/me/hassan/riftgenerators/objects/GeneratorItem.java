package me.hassan.riftgenerators.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.utils.Common;
import me.hassan.riftgenerators.utils.ItemBuilder;

public class GeneratorItem {
	
	private Player player;
	private int amount;
	private String generatorName;
	
	public GeneratorItem(Player player, int amount, String generatorName) {
		this.player = player;
		this.amount = amount;
		this.generatorName = generatorName;
	}
	
	public void createGenerator(CommandSender sender, int level) {
		
		if(this.getGenerators().contains(generatorName)) {
			
			
			String material = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + generatorName + ".Material");
			int data = RiftGenerators.getInstance().getConfig().getInt("Generator-Items." + generatorName + ".Data");
			String name = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + generatorName + ".Name");
			
			
			int seconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + generatorName + "." + String.valueOf(level)  + ".Seconds");
			
			
			ArrayList<String> formattedLore = new ArrayList<>();
			
			for(String lore : RiftGenerators.getInstance().getConfig().getStringList("Generator-Items." + generatorName + ".Lore")) {
				lore = lore.replace("{level}", String.valueOf(level));
				lore = lore.replace("{seconds}", String.valueOf(seconds));
				formattedLore.add(Common.colorMessage(lore));
			}
			
			ItemStack item = new ItemBuilder(Material.matchMaterial(material.toUpperCase()))
					.setDisplayName(Common.colorMessage(name))
					.setAmount(amount)
					.setLore(formattedLore)
					.setGlow(true)
					.setKey("riftgenerators", this.generatorName)
					.setKey("level", String.valueOf(level))
					.setKey("seconds", String.valueOf(seconds))
					.build();
			
			this.player.getInventory().addItem(item);
			
			
			
			
		}else {
			Common.sendMessage(sender, "&aThat generator does not exist");
		}
		
	}
	
	public void createGenerator(int level) {
		
		if(this.getGenerators().contains(generatorName)) {
			
			
			String material = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + generatorName + ".Material");
			int data = RiftGenerators.getInstance().getConfig().getInt("Generator-Items." + generatorName + ".Data");
			String name = RiftGenerators.getInstance().getConfig().getString("Generator-Items." + generatorName + ".Name");
			
			
			int seconds = RiftGenerators.getInstance().getConfig().getInt("Generator-Rewards." + generatorName + "." + String.valueOf(level)  + ".Seconds");
			
			
			ArrayList<String> formattedLore = new ArrayList<>();
			
			for(String lore : RiftGenerators.getInstance().getConfig().getStringList("Generator-Items." + generatorName + ".Lore")) {
				lore = lore.replace("{level}", String.valueOf(level));
				lore = lore.replace("{seconds}", String.valueOf(seconds));
				formattedLore.add(Common.colorMessage(lore));
			}
			
			ItemStack item = new ItemBuilder(Material.matchMaterial(material.toUpperCase()))
					.setDisplayName(Common.colorMessage(name))
					.setLore(formattedLore)
					.setGlow(true)
					.setKey("riftgenerators", this.generatorName)
					.setKey("level", String.valueOf(level))
					.setKey("seconds", String.valueOf(seconds))
					.build();
			
			this.player.getInventory().addItem(item);
			
			
			
			
		}
		
	}
	
	private List<String> getGenerators(){
		
		List<String> generators = new ArrayList<>();
		
		for(String generator : RiftGenerators.getInstance().getConfig().getConfigurationSection("Generator-Items").getKeys(false)) {
			generators.add(generator);
		}
		
		
		return generators;
	}
	

}
