package dank.meme.waystones.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class CommandManager {
	
	List<CommandHandler> chl = new ArrayList<CommandHandler>();
	
	public void register(CommandHandler ch) {
		if (!chl.contains(ch))
			chl.add(ch);
	}
	
	public boolean run(Player pl, String cmd, String[] args) {
		Boolean found = false;
		for (CommandHandler ch : chl)
			if (ch.run(pl, cmd, args))
				found = true;
		return found;
	}
}