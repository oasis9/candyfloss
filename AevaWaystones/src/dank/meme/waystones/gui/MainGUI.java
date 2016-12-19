package dank.meme.waystones.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dank.meme.waystones.WaystoneType;
import dank.meme.waystones.Waystones;

public class MainGUI implements Listener {
	
	public MainGUI() {
		Bukkit.getPluginManager().registerEvents(this, Waystones.getInstance());
	}
	
	public static Inventory get() {
		int size = 45;
		Inventory mainGUI = Bukkit.createInventory(null, size, "Waystone Locations");
		
		int i = (size - 1) / 2 - WaystoneType.values().length + 1;
		for (WaystoneType wst : WaystoneType.values()) {
			ItemStack type = wst.getItem();
			ItemMeta typeMeta = type.getItemMeta();
			
			typeMeta.setDisplayName(wst.getColor() + wst.getName());
			typeMeta.setLore(wst.getLore());
			type.setItemMeta(typeMeta);
			
			mainGUI.setItem(i, type);
			i += 2;
		}
		
		return mainGUI;
	}
	
	public void teleportInWorld(Player pl, int x, int y, int z) {
		pl.teleport(new Location(pl.getWorld(), x, y, z));
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
		Player pl = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if (inv.getHolder() == null && inv.getName().equalsIgnoreCase("Waystone Locations")) {
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			for (WaystoneType wst : WaystoneType.values())
				if (item != null && item.hasItemMeta() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(wst.name()))
					pl.openInventory(WaystoneTypeGUI.get(wst));
		}
	}
}	
