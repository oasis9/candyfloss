package dank.meme.blockfountain;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class BlockFountain extends JavaPlugin implements Listener {
	
	List<Integer> entityIds = new ArrayList<Integer>();
	Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		final Player pl = e.getPlayer();
		if (!e.getHand().equals(EquipmentSlot.HAND))
			return;
		ItemStack is = e.getItem();
		if (is != null && is.getType().equals(Material.GHAST_TEAR) && is.hasItemMeta() && ChatColor.stripColor(is.getItemMeta().getDisplayName()).equalsIgnoreCase("Block Fountain (Right Click)")) {
			UUID uuid = pl.getUniqueId();
			if (!pl.hasPermission("blockfountain.bypass") && cooldowns.containsKey(uuid)) {
				Long start = cooldowns.get(uuid);
				Long now = System.currentTimeMillis();
				Integer seconds = (int) ((now - start) / 1000);
				Integer cooldown = 5;
				Integer time = cooldown - seconds;
				if (time > 0) {
					pl.sendMessage(RED + "Thou shalt not do that! (For another " + AQUA + time + " seconds" + RED + ")");
					return;
				} else
					cooldowns.remove(uuid);
			}
			if (!pl.hasPermission("blockfountain.bypass"))
				cooldowns.put(uuid, System.currentTimeMillis());
        	new BukkitRunnable() {
        		int runs = 0;
        		int max = 30;
        		@Override
        		public void run() {
        			runs++;
        			if (runs >= max) {
        				cancel();
        				return;
        			}
        			List<Integer> dataValues = Arrays.asList(0, 1, 2, 3, 4, 11, 9);
        			ItemStack pop = new ItemStack(Material.WOOL, 1, dataValues.get(new Random().nextInt(dataValues.size())).byteValue());
        			ItemMeta im = pop.getItemMeta();
        			im.setDisplayName(UUID.randomUUID().toString());
        			pop.setItemMeta(im);
        			final Item item = pl.getWorld().dropItem(pl.getEyeLocation(), pop);
        			item.setVelocity(new Vector(new Random().nextDouble() / 2 - .25, .5, new Random().nextDouble() / 2 - .25));
        			final int eid = item.getEntityId();
        			entityIds.add(eid);
        			new BukkitRunnable() {
        				public void run() {
        					item.remove();
        					entityIds.remove(entityIds.indexOf(eid));
        				}
        			}.runTaskLater(BlockFountain.getPlugin(BlockFountain.class), 20 * 2);
        		}
        	}.runTaskTimer(this, 0, 20 / 10);
		}
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent e) {
		Item item = e.getItem();
		if (item != null && entityIds.contains(item.getEntityId()))
			e.setCancelled(true);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to execute this command");
			return true;
		}
		Player pl = (Player) sender;
		if (containsIgnoreCase(Arrays.asList("blockfountain", "bf"), cmd.getName())) {
			ItemStack is = new ItemStack(Material.GHAST_TEAR);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(GREEN + "Block Fountain " + GRAY + "(Right Click)");
			im.setLore(Arrays.asList(AQUA + "Pretty colors"));
			is.setItemMeta(im);
			pl.getInventory().addItem(is);
		}
		return true;
	}
	
	public boolean containsIgnoreCase(List<String> haystack, String needle) {
		if (haystack == null)
			return false;
		for (String st : haystack)
			if (st.equalsIgnoreCase(needle))
				return true;
		return false;
	}
}