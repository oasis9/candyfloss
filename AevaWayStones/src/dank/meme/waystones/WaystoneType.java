package dank.meme.waystones;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum WaystoneType {
	
	VILLAGE(new ItemStack(Material.SPRUCE_DOOR_ITEM), "&7We'll figure this out later."),
	TOWN(new ItemStack(Material.NETHER_STAR), "&9You hear laughter in the distance.."),
	CITY(new ItemStack(Material.IRON_INGOT), "&a"),
	DUNGEON(new ItemStack(Material.IRON_FENCE), "&cDarkness grows stronger in the mist..");
	
	private ItemStack item;
	private String msg;
	
	WaystoneType(ItemStack item, String msg) {
		this.item = item;
		this.msg = msg;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public String getName() {
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}
}