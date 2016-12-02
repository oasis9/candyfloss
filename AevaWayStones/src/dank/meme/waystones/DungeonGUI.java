package dank.meme.waystones;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DungeonGUI implements Listener {
	
	public DungeonGUI() { 
		Bukkit.getPluginManager().registerEvents(this, Waystones.plugin);
	}
	
	public static Inventory get() {
		Inventory dungeonGUI = Bukkit.createInventory(null, 45, ChatColor.BLUE + "Dungeons");
		
		@SuppressWarnings("deprecation")
		ItemStack nodungeons = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
		ItemMeta nodungeonsMeta = nodungeons.getItemMeta();
		
		nodungeonsMeta.setDisplayName(ChatColor.RED + "Uh Oh!");
		nodungeonsMeta.setLore(Arrays.asList(ChatColor.GRAY + "There are currently no dungeons",ChatColor.GRAY + "to display."));
		nodungeons.setItemMeta(nodungeonsMeta);
		
		dungeonGUI.setItem(22, nodungeons); 
		return dungeonGUI;
	}
	
	public void teleportInWorld(Player pl, int x, int y, int z) {
		pl.teleport(new Location(pl.getWorld(), x, y, z));
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		if (inv.getHolder() == null  && inv.getName().contains("Dungeons")){
			e.setCancelled(true);
		}
	}
}	