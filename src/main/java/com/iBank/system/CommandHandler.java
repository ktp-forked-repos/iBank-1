package com.ibank.system;

import com.ibank.iBank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


/**
 * The "new" command handler a bit based on iBank
 * @author steffengy
 *
 */
public class CommandHandler 
{
	private static HashMap<String, HashMap<String, Command>> cmds = new HashMap<String, HashMap<String, Command>>();
	private static HashMap<String, HashMap<String, CommandInfo>> info = new HashMap<String, HashMap<String, CommandInfo>>();
	/**
	 * Registers a command
	 * @param a The command
	 */
	public static void register(Command a) 
	{
		CommandInfo tmp;
		if((tmp = a.getClass().getAnnotation(CommandInfo.class)) == null)
		{
			System.out.println("[iBank] Error while enabling Command!");
			return;
		}
		
		if(!cmds.containsKey(tmp.root())) 
		{
			cmds.put(tmp.root(), new HashMap<String, Command>());
		}
		if(!info.containsKey(tmp.root())) 
		{
			info.put(tmp.root(), new HashMap<String, CommandInfo>());
		}
		cmds.get(tmp.root()).put(tmp.sub(), a);
		info.get(tmp.root()).put(tmp.sub(), tmp);
	}
	
	/**
	 * Handles a command
	 */
	public static boolean handle(CommandSender sender, String root, String[] args) 
	{
		 if(!cmds.containsKey(root) || !info.containsKey(root)) return false;
		 if(args == null || args.length == 0) args = new String[] { "" };
		 String sub = args[0];
		 //remove first arg from args
		 List<String> argtmp = new ArrayList<String>(Arrays.asList(args));
		 if(argtmp.size()>0) argtmp.remove(0);
		 args = argtmp.toArray(new String[argtmp.size()]);
		 //check existance of sub command
		 if(!cmds.get(root).containsKey(sub) || !info.get(root).containsKey(sub)) return false;
		 //console/player check
		 if(!(sender instanceof Player))
		 {
			 cmds.get(root).get(sub).handle(sender, args);
		 }
		 else if(iBank.hasPermission((Player) sender, info.get(root).get(sub).permission())) 
		 {
			 cmds.get(root).get(sub).handle(sender, args);
		 }
		 else
		 {
			 MessageManager.send(sender, "&r& Permission denied!");
		 }
		return true;
	}
	
	/**
	 * Return the arg info of a method
	 */
	public static String getArgInfo(String root, String name) 
	{
		 if(!cmds.containsKey(root) || !info.containsKey(root) || !cmds.get(root).containsKey(name) || !info.get(root).containsKey(name)) 
			 return "";
		 String ret = "";
		 for(String alpha : info.get(root).get(name).arguments()) 
		 {
			 if(!alpha.startsWith("(")) 
				 ret += "["+alpha+"] ";
			 else 
				 ret += alpha;
		 }
		 return ret;
	}
	
	/**
	 * Return if 2 call
	 */
	public static boolean isCallable(Player p, String root, String sub) {
        String perm = CommandHandler.info.get(root).get(sub).permission();
        return perm == null || perm.equals("") || iBank.hasPermission(p, CommandHandler.info.get(root).get(sub).permission());
    }
	
	/**
	 * Get commands by root command
	 */
	public static List<String> getCommands(String string) 
	{
		List<String> ret = new ArrayList<String>();
		if(cmds.get(string) == null) return ret;
		for(Entry<String, Command> cmd : cmds.get(string).entrySet()) 
		{
			ret.add(cmd.getKey());
		}
		return ret;
	}
	
	/**
	 * Returns the help entry from this command
	 */
	public static String getHelp(String root, String name) 
	{
		if(!cmds.containsKey(root) || !info.containsKey(root) || !cmds.get(root).containsKey(name) || !info.get(root).containsKey(name)) 
			return "";
		return cmds.get(root).get(name).getHelp();
	}
}
