package dank.meme.waystones.commands.waystone.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dank.meme.waystones.WaystoneType;
import dank.meme.waystones.Waystones;
import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.CommandUnit;
import dank.meme.waystones.gui.MainGUI;
import dank.meme.waystones.gui.WaystoneTypeGUI;

public class Open extends CommandUnit {
	
	public Open(CommandManager cm) {
		super(cm);
		register(this);
	}
	
	@Override
	public void run(Player pl) {
		pl.sendMessage(ChatColor.GREEN + "The souls of the past accept you...");
		pl.playSound(pl.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 3F, 1F);
		pl.openInventory(MainGUI.get());
	}
	
	@Override
	public void run(Player pl, String cmd, String[] args) {
		Boolean found = false;
		for (WaystoneType wst : WaystoneType.values()) {
			if (wst.name().equalsIgnoreCase(cmd)) {
				Waystones.sendMessage(pl, wst.getMessage());
				pl.openInventory(WaystoneTypeGUI.get(wst));
				found = true;
			}
		}
		if (!found)
			run(pl);
	}
}