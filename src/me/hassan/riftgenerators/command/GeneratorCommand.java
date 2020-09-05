package me.hassan.riftgenerators.command;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.hassan.riftgenerators.data.GeneratorData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.objects.GeneratorItem;
import me.hassan.riftgenerators.utils.Common;

import java.util.Map;

public class GeneratorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 5) {
			if(args[0].equalsIgnoreCase("give")) {
				if(sender.hasPermission("riftgenerators.give")) {
					Player target = Bukkit.getPlayer(args[1]);
					
					if(target != null) {
						
						
						String generatorName = args[2];
						
						int level = Integer.valueOf(args[3]);
						int amount = Integer.valueOf(args[4]);
						
						GeneratorItem item = new GeneratorItem(target, amount, generatorName);
						
						
						item.createGenerator(sender, level);
						Common.sendMessage(target, RiftGenerators.getInstance().getConfig().getString("GiveGeneratorTarget")
								.replace("{type}", generatorName));
						Common.sendMessage(sender, RiftGenerators.getInstance().getConfig().getString("GiveGenerator")
								.replace("{type}", generatorName)
								.replace("{target}", target.getName()));
						
					}else {
						Common.sendMessage(sender, "&cSender does not exist");
					}
				}else {
					Common.sendMessage(sender, "&cYou don't have permission to execute this command");
				}
				
				
				
			}
			
			
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("reload")) {
				if(sender.hasPermission("riftgenerators.reload")) {
					RiftGenerators.getInstance().reloadConfig();
					Common.sendMessage(sender, "&7You have reloaded the config file");
				}else {
					Common.sendMessage(sender, "&cYou don't have permission to execute this command");
				}
				
			}
		}
		if(args.length == 0){

		}
		
		
		return false;
	}

}
