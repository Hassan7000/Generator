package me.hassan.riftgenerators.objects;

import org.bukkit.Location;

import me.hassan.riftgenerators.RiftGenerators;
import me.hassan.riftgenerators.data.GeneratorData;

public class Storage {
	
	
	public static boolean isGenerator(Location location) {
		return RiftGenerators.getInstance().getData().containsKey(RiftGenerators.getInstance().stringFromLoc(location));
	}
	
	public static GeneratorData getGeneratorData(Location location) {
		return RiftGenerators.getInstance().getData().get(RiftGenerators.getInstance().stringFromLoc(location));
	}
	
	public static void removeData(Location location) {
		RiftGenerators.getInstance().getData().remove(RiftGenerators.getInstance().stringFromLoc(location));
	}
	

}
