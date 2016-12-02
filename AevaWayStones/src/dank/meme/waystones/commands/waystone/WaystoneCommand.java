package dank.meme.waystones.commands.waystone;

import java.util.Arrays;

import org.bukkit.entity.Player;

import dank.meme.waystones.commands.CommandManager;
import dank.meme.waystones.commands.StaffCommandUnit;
import dank.meme.waystones.commands.waystone.subcommands.Create;
import dank.meme.waystones.commands.waystone.subcommands.Open;
import net.md_5.bungee.api.ChatColor;

public class WaystoneCommand extends StaffCommandUnit {
	
	CommandManager lcm = new CommandManager();
	
	public WaystoneCommand(CommandManager cm) {
		super(cm, Arrays.asList("waystone", "ws", "waystones"));
		register(this);
		new Open(lcm);
		new Create(lcm);
	}
	
	@Override
	public void run(Player pl) {
		pl.sendMessage(ChatColor.LIGHT_PURPLE + "Like, no.");
	}
	@Override
	public void run(Player pl, String cmd, String[] args) {
		if (!lcm.run(pl, cmd, args))
			pl.sendMessage(ChatColor.RED + "Please use a valid subcommand");
	}
}