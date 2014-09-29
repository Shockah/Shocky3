package sphp;

import pl.shockah.json.JSONObject;
import sfactoids.FactoidParser;
import shocky3.pircbotx.Bot;
import shocky3.pircbotx.event.GenericUserMessageEvent;

public class PHPFactoidParser extends FactoidParser {
	public final Plugin plugin;
	
	public PHPFactoidParser(Plugin plugin) {
		super("php");
		this.plugin = plugin;
	}
	
	public int resultType() {
		return TYPE_STRING_CODE;
	}
	
	public String parseStringCode(JSONObject j, GenericUserMessageEvent<Bot> e, String trigger, String args, String code) {
		return plugin.php.parse(e, trigger, args, code);
	}
}