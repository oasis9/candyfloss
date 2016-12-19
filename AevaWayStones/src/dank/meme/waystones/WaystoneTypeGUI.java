package dank.meme.waystones;

import java.util.Arrays;
import java.util.Map.Entry;

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

public class WaystoneTypeGUI implements Listener {
	
	public WaystoneTypeGUI() { 
		Bukkit.getPluginManager().registerEvents(this, Waystones.getInstance());
	}
	
	public static Inventory get(WaystoneType wst) {
		Inventory GUI = Bukkit.createInventory(null, 45, wst.getName());
		
		if (Waystones.getInstance().waystones.size() > 0) {
			int i = 0;
			for (Entry<String, Waystone> e : Waystones.getInstance().waystones.entrySet()) {
				Waystone ws = e.getValue();
				if (ws.type != wst)
					continue;
				ItemStack place = wst.getItem();
				ItemMeta meta = place.getItemMeta();
				
				meta.setDisplayName(ChatColor.AQUA + Waystones.getInstance().waystones.inverse().get(ws));
				place.setItemMeta(meta);
				
				GUI.setItem(i, place);
				i++;
			}
		} else {
			@SuppressWarnings("deprecation")
			ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
			ItemMeta meta = none.getItemMeta();
		
			meta.setDisplayName(ChatColor.RED + "Uh Oh!");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "There are currently no " + wst.name().toLowerCase(), ChatColor.GRAY + "waystone to display."));
			none.setItemMeta(meta);
			
			GUI.setItem(22, none);
		}
		return GUI;
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