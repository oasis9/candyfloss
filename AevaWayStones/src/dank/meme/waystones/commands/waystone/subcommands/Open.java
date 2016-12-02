package dank.meme.waystones.commands.waystone.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dank.meme.waystones.DungeonGUI;
import dank.meme.waystones.MainGUI;
import dank.meme.waystones.TownGUI;
import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.CommandUnit;

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
		if (cmd.equalsIgnoreCase("dungeons")) {
			pl.sendMessage(ChatColor.RED + "Darkness grows stronger in the mist...");
			pl.openInventory(DungeonGUI.get());
		} else if (cmd.equalsIgnoreCase("towns")) {
			pl.sendMessage(ChatColor.BLUE + "You hear laughter in the distance...");
			pl.openInventory(TownGUI.get());
		} else
			run(pl);
	}
}