package dank.meme.waystones.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

public class CommandHandler {
	
	CommandUnit cu;
	List<String> aliases;
	
	public CommandHandler(CommandUnit cu, List<String> aliases) {
		this.cu = cu;
		this.aliases = aliases;
	}
	
	public boolean run(Player pl, String cmd, String[] args) {
		if (!aliases.contains(cmd) && !cu.getClass().getSimpleName().equalsIgnoreCase(cmd))
			return false;
		if (args == null || args.length <= 0)
			cu.run(pl);
		else {
			String name = args[0];
			String[] ars = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
			cu.run(pl, name, ars);
		}
		return true;
	}
}