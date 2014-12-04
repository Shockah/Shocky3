package sphp;

import scommands.Command;
import shocky3.pircbotx.event.GenericUserMessageEvent;

public class CmdPHP extends Command {
	public final Plugin pluginPHP;
	
	public CmdPHP(Plugin plugin) {
		super(plugin, "php");
		this.pluginPHP = plugin;
	}
	
	public String call(GenericUserMessageEvent e, String trigger, String args, boolean chain) {
		String _s = pluginPHP.php.parse(e, trigger, "", args);
		if (!chain) e.respond(_s);
		return _s;
	}
}