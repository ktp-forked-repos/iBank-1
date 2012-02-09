package com.iBank.Commands;

import org.bukkit.command.CommandSender;

import com.iBank.iBank;
import com.iBank.system.Command;
import com.iBank.system.CommandInfo;

/**
 *   /bank reload - Call reload function in iBank
 * @author steffengy
 *
 */
@CommandInfo(
		arguments = { }, 
		help = "", 
		permission = "iBank.reload",
		root = "bank", 
		sub = "reload"
)
public class CommandReload implements Command {
	public void handle(CommandSender sender, String[] arguments) {
		iBank.mainInstance.reloadConfig();
	}
}
