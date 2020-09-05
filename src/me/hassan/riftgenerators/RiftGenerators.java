package me.hassan.riftgenerators;

import java.io.*;
import java.util.*;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import me.hassan.riftgenerators.task.SaveTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import me.hassan.riftgenerators.command.GeneratorCommand;
import me.hassan.riftgenerators.data.GeneratorData;
import me.hassan.riftgenerators.events.GeneratorBreak;
import me.hassan.riftgenerators.events.GeneratorClick;
import me.hassan.riftgenerators.events.GeneratorMember;
import me.hassan.riftgenerators.events.GeneratorPlace;
import me.hassan.riftgenerators.events.InventoryClick;
import me.hassan.riftgenerators.task.GeneratorTask;
import me.hassan.riftgenerators.utils.HologramUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.scheduler.BukkitTask;


public class RiftGenerators extends JavaPlugin {
	
	private static RiftGenerators instance;
	
	public HashMap<String, GeneratorData> data;
	public File file = new File(getDataFolder(), "GeneratorData.dat");
	public HashMap<UUID, Inventory> inventory = new HashMap<>();
	public HashMap<UUID, GeneratorData> generator = new HashMap<>();
	public HashMap<UUID, Location> location = new HashMap<>();
	public HashMap<String, Hologram> genHologram = new HashMap<>();
	public HashMap<UUID, GeneratorData> memberState = new HashMap<>();
	private BukkitTask task;
	private BukkitTask saveTask;
	public Economy econ = null;
	
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		registerListeners(new GeneratorMember(), new GeneratorBreak(), new GeneratorClick(), new GeneratorPlace(), new InventoryClick());
		getCommand("riftgenerators").setExecutor(new GeneratorCommand());
		this.setupEconomy();
		if(!file.exists()) {
			 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
		data = (HashMap<String, GeneratorData>) load(file);
		 
		 if(data == null) {
			 data = new HashMap<String, GeneratorData>();
		 }


		 Bukkit.getScheduler ().runTaskLater (this, () -> HologramUtils.loadHolograms(), 20);
		 task = new GeneratorTask().runTaskTimer(this, 40, 20);
		 saveTask = new SaveTask().runTaskTimer(this,40,1500*20);

	}
	
	public void onDisable() {
		task.cancel();
		saveTask.cancel();
		this.location.clear();
		this.generator.clear();
		this.inventory.clear();
		this.genHologram.clear();
		if(file.exists()) {
			 this.save(file);
		 }
		this.data.clear();
	}
	
	public HashMap<String, GeneratorData> getData(){ return this.data; }

	public HashMap<String, Hologram> getGenHologram() { return  genHologram; }
	private void registerListeners(Listener...listeners) {
        for (Listener lis: listeners) getServer().getPluginManager().registerEvents(lis, this);
    }
	
	public static RiftGenerators getInstance() {
		return instance;
	}
	
	public Location locFromString(String string) {
		String[] loc = string.split(":");

		return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), (float) Double.parseDouble(loc[4]), (float) Double.parseDouble(loc[5]));
	}

	public String stringFromLoc(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
	}
	
	public void save(File f) {
		 if(f.exists()) {
			 try {			 
				 ObjectOutputStream dataa = new ObjectOutputStream(new FileOutputStream(f));
				 dataa.writeObject(data);
				 dataa.flush();
				 dataa.close();
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 }
	 }
	 
	 public Object load(File f) {
		 try {
			 ObjectInputStream data = new ObjectInputStream(new FileInputStream(f));
			 Object result = data.readObject();
			 data.close();
			 return result;
		 } catch (Exception e) {
			return null;
		 }
	 }
	 
	 private boolean setupEconomy() {
			if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
				return false;
			} else {
				RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
				if (rsp == null) {
					return false;
				} else {
					econ = (Economy) rsp.getProvider();
					return econ != null;
				}
			}
		}


}


	
	
	

