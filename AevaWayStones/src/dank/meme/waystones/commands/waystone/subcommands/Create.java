package dank.meme.waystones.commands.waystone.subcommands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dank.meme.waystones.Waystone;
import dank.meme.waystones.WaystoneType;
import dank.meme.waystones.Waystones;
import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.StaffCommandUnit;

public class Create extends StaffCommandUnit {
	
	public Create(CommandManager cm) {
		super(cm, Arrays.asList("create"));
		register(this);
	}
	
	@Override
	public void run(Player pl) {
		pl.sendMessage(ChatColor.RED + "Usage /waystone create <name> <type>");
	}
	@Override
	public void run(Player pl, String cmd, String[] args) {
		if (args.length > 0)
			Waystones.plugin.addWaystone(cmd, new Waystone(pl.getLocation(), WaystoneType.valueOf(args[0])));
		else
			run(pl);
	}
	
}