package dank.meme.waystones;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainGUI implements Listener {
	
	public MainGUI() {
		Bukkit.getPluginManager().registerEvents(this, Waystones.plugin);
	}
	
	public static Inventory get() {
		Inventory mainGUI = Bukkit.createInventory(null, 45, ChatColor.BLUE + "Waystone Locations");
		
		ItemStack spawn = new ItemStack(Material.NETHER_STAR);
		ItemMeta spawnMeta = spawn.getItemMeta();
		
		ItemStack dungeon = new ItemStack(Material.IRON_FENCE);
		ItemMeta dungeonMeta = dungeon.getItemMeta();

		spawnMeta.setDisplayName(ChatColor.BLUE + "Spawn");
		spawnMeta.setLore(Arrays.asList(ChatColor.GREEN + "Many mysteries present",ChatColor.GREEN + "a curse on this land"));
		spawn.setItemMeta(spawnMeta);
		
		dungeonMeta.setDisplayName(ChatColor.RED + "Dungeons");
		dungeonMeta.setLore(Arrays.asList(ChatColor.GRAY + "A place full of evil",ChatColor.GRAY + "beasts"));
		dungeon.setItemMeta(dungeonMeta);
		
		mainGUI.setItem(21, spawn);
		mainGUI.setItem(23, dungeon);
		return mainGUI;
	}
	
	public void teleportInWorld(Player pl, int x, int y, int z) {
		pl.teleport(new Location(pl.getWorld(), x, y, z));
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		if (inv.getHolder() == null  && inv.getName().contains("Waystone Locations")){
			e.setCancelled(true);
		}
	}
}	
