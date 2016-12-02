package dank.meme.waystones.commands;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class StaffCommandUnit extends CommandUnit {

	public StaffCommandUnit(CommandManager cm) {
		super(cm);
	}
	public StaffCommandUnit(CommandManager cm, List<String> aliases) {
		super(cm, aliases);
	}
	public abstract void run(Player pl);
	public abstract void run(Player pl, String cmd, String[] args);
}