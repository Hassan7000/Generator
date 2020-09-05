package me.hassan.riftgenerators.task;

import me.hassan.riftgenerators.RiftGenerators;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {
	@Override
	public void run() {
		if(RiftGenerators.getInstance().file.exists()) {
			RiftGenerators.getInstance().save(RiftGenerators.getInstance().file);
		}
	}
}
