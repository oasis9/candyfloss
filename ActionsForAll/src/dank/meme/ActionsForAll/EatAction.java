package dank.meme.ActionsForAll;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EatAction extends JavaPlugin {
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
	        Player pl = (Player) sender;
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("You must be a player to execute this command");
    			return true;
    		}
    			
    			Player target = getServer().getPlayer(args[0]);
    			if (containsIgnoreCase(null, "eat")) {
    				pl.sendMessage(ChatColor.AQUA + sender.getName() + " has eaten a bit of " + target.getName() + " Yum!");
            		pl.playSound(pl.getLocation(), Sound.ENTITY_PLAYER_BURP, 3F, 1F);
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