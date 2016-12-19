package dank.meme.waystones;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum WaystoneType {
	
	VILLAGE(new ItemStack(Material.SPRUCE_DOOR_ITEM), ChatColor.BLUE, Arrays.asList("&7Get the fuck out of my village."), "&7We'll figure this out later."),
	TOWN(new ItemStack(Material.NETHER_STAR), ChatColor.GREEN, Arrays.asList("&7Go back to your own town."), "&9You hear laughter in the distance.."),
	CITY(new ItemStack(Material.IRON_INGOT), ChatColor.AQUA, Arrays.asList("&7Rule #1 of the city:", "&7Never talk about the city."), "&a"),
	DUNGEON(new ItemStack(Material.IRON_FENCE), ChatColor.RED, Arrays.asList("&7You are not welcome here."), "&cDarkness grows stronger in the mist..");
	
	private ItemStack item;
	private ChatColor color;
	private List<String> lore;
	private String msg;
	
	WaystoneType(ItemStack item, ChatColor color, List<String> lore, String msg) {
		this.item = item;
		this.color = color;
		List<String> formatted = new LinkedList<String>();
		for (String string : lore)
			formatted.add(ChatColor.translateAlternateColorCodes('&', string));
		this.lore = formatted;
		this.msg = msg;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public String getName() {
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}

	public List<String> getLore() {
		return lore;
	}
}