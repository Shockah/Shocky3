package stell;

import java.util.LinkedList;
import java.util.List;
import pl.shockah.Pair;
import scommands.Command;
import scommands.CommandStack;
import shocky3.BotManager;
import shocky3.pircbotx.Bot;
import shocky3.pircbotx.event.GenericUserMessageEvent;
import sident.IdentHandler;

public class CmdTell extends Command {
	protected final Plugin pluginTell;
	
	public CmdTell(Plugin plugin) {
		super(plugin, "tell");
		pluginTell = plugin;
	}
	
	public String call(GenericUserMessageEvent e, String input, CommandStack stack) {
		String[] spl = input.split("\\s");
		if (spl.length >= 2) {
			String target = spl[0];
			input = input.substring(target.length() + 1);
			
			String[] targetSpl = target.split(";");
			List<Pair<IdentHandler, String>> list = new LinkedList<>();
			String hasServer = null;
			boolean hasOther = false;
			for (String targetS : targetSpl) {
				if (targetS.indexOf(':') == -1) {
					targetS = String.format("%s:%s", Plugin.pluginIdent.handlerNick.id, targetS);
				}
				IdentHandler handler = Plugin.pluginIdent.getIdentHandlerFor(null, targetS);
				if (handler == null)
					return null;
				if (handler.name.equals("server")) {
					hasServer = targetS.substring(targetS.indexOf(':') + 1);
				} else {
					hasOther = true;
				}
				list.add(new Pair<>(handler, targetS.substring(targetS.indexOf(':') + 1)));
			}
			
			if (!hasOther)
				return null;
			BotManager manager = e.<Bot>getBot().manager;
			if (hasServer == null) {
				hasServer = manager.name;
				list.add(new Pair<>(Plugin.pluginIdent.handlerServer, String.format("%s (%s)", manager.name, manager.host)));
			}
			
			BotManager managerReceiver = e.<Bot>getBot().botApp.serverManager.byServerName(hasServer);
			for (Pair<IdentHandler, String> pair : list) {
				pair.set1(pair.get1().copy(managerReceiver));
			}
			
			Tell tell = Tell.create(manager, e.getUser(), list, input);
			tell = Tell.read(plugin.botApp, Tell.write(tell)); //TODO: figure out why the fuck does it break without serialize cycling
			pluginTell.tells.add(tell);
			Tell.writeDB(plugin, tell);
			Tell.updateIDDB(plugin);
			return "I'll pass that along.";
		}
		return null;
	}
}