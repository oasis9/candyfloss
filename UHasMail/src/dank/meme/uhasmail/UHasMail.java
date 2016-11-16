package dank.meme.uhasmail;

import static org.bukkit.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class UHasMail extends JavaPlugin {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = cmd.getName().toLowerCase();
		if (Arrays.asList("message", "m", "w", "mag").contains(name)) {
			if (args.length > 0) {
				
			} else
				sender.sendMessage(RED + "Usage /msg <name> <message>");
		}
		return true;
	}
}