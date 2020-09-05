package me.hassan.riftgenerators.utils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;

public class HologramUtils {
	
	
	private static double Y = RiftGenerators.getInstance().getConfig().getDouble("HologramOffset.Y");
	private static double X = RiftGenerators.getInstance().getConfig().getDouble("HologramOffset.X");
	private static double Z = RiftGenerators.getInstance().getConfig().getDouble("HologramOffset.Z");


	
	public static void createHologram(Location location, GeneratorData data){
        Hologram holo = HologramsAPI.createHologram(RiftGenerators.getInstance(), formatLocation(location));
		 
		 
		 for(String hologram : RiftGenerators.getInstance().getConfig().getStringList("Generator-Items." + data.getGeneratorName() + ".Hologram")) {
			 
			 hologram = hologram.replace("{level}", String.valueOf(data.getLevel()));
			 hologram = hologram.replace("{seconds}", Common.checkTime(data.getSeconds()));
			 hologram = hologram.replace("{rewards}", String.valueOf(data.getItemAmount()));
			 
			 holo.appendTextLine(ChatColor.translateAlternateColorCodes('&', hologram));			 
		 }

		 RiftGenerators.getInstance().getGenHologram().put(RiftGenerators.getInstance().stringFromLoc(location), holo);

		 
	  }
	
	public static boolean hasHologram(Location location) {
      //  return RiftGenerators.getInstance().getGenHologram().containsKey(RiftGenerators.getInstance().stringFromLoc(location));
		return true;
	}
	
	public static void useHologram(Location location, GeneratorData data) {
		location = formatLocation(location);
		for (Hologram hologram : HologramsAPI.getHolograms(RiftGenerators.getInstance())) {
			if (hologram.getX() != location.getX()  || hologram.getY() != location.getY() || hologram.getZ() != location.getZ()) continue;
				updateTime(hologram, data);
				break;
		}
		
	}
		
	
	public static void updateTime(Hologram hologram, GeneratorData data) {
        int place = 0;
        	for (String holo : RiftGenerators.getInstance().getConfig().getStringList("Generator-Items." + data.getGeneratorName() + ".Hologram")) {
                TextLine line = (TextLine) hologram.getLine(place);
                holo = holo.replace("{level}", String.valueOf(data.getLevel()));
                holo = holo.replace("{seconds}", Common.checkTime(data.getSeconds()));
                holo = holo.replace("{rewards}", String.valueOf(data.getItemAmount()));
                line.setText(Common.colorMessage(holo));
                place++;
            }


    }


	public static void removeHologram(Location location) {
		location = formatLocation(location);
		for (Hologram hologram : HologramsAPI.getHolograms(RiftGenerators.getInstance())) {
			if (hologram.getX() != location.getX()  || hologram.getY() != location.getY() || hologram.getZ() != location.getZ()) continue;
				hologram.delete();
				break;
		}
	}


	public static void loadHolograms() {
		for(Map.Entry<String, GeneratorData> entry : RiftGenerators.getInstance().getData().entrySet()){
			 Location location = RiftGenerators.getInstance().locFromString(entry.getKey());
			 if(location.getBlock() != null && location.getBlock().getType() != Material.AIR) {
				 createHologram(location, entry.getValue());
			 }
			
		}
		
	}

	public static Location formatLocation(Location location){
		location.setY(location.getY() + Y);
		location.setX(location.getX() + X);
		location.setZ(location.getZ() + Z);
		return location;
	}
    	

}
