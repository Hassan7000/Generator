package me.hassan.riftgenerators.data;

import java.io.Serializable;
import java.util.List;

public class GeneratorData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String owner;
	private String generatorName;
	private int level;
	private int itemAmount;
	private List<String> members;
    private int seconds;
	
	public GeneratorData(String owner, String generatorName, int level, int itemAmount, List<String> members, int seconds) {
		this.owner = owner;
		this.generatorName = generatorName;
		this.level = level;
		this.itemAmount = itemAmount;
		this.members = members;
		this.seconds = seconds;
	}
	
	public String getOwner() { return this.owner; }
	
	public String getGeneratorName() { return this.generatorName; }
	
	public int getLevel() { return this.level; }
	
	public int getItemAmount() { return this.itemAmount; }
	
	public List<String> getMembers() { return this.members; }
	
	public int getSeconds() { return this.seconds; }

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}
	
	public void addMember(String name) {
		this.members.add(name);
	}
	
	public void removeMember(String name) {
		this.members.remove(name);
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
