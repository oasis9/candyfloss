package dank.meme.waystones.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public abstract class CommandUnit {
	
	CommandManager cm = new CommandManager();
	List<String> aliases = new ArrayList<String>();
	CommandHandler ch;
	
	public CommandUnit(CommandManager cm) {
		this.cm = cm;
	}
	
	public CommandUnit(CommandManager cm, List<String> aliases) {
		this.cm = cm;
		this.aliases = aliases;
	}
	
	public void register(CommandUnit cu) {
		this.ch = new CommandHandler(cu, aliases);
		cm.register(ch);
	}
	
	public abstract void run(Player pl);
	public abstract void run(Player pl, String cmd, String[] args);
}