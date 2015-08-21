package skylark.commands;

import skylark.pircbotx.event.GenericUserMessageEvent;

public abstract class CommandProvider {
	public static final int
		PRIORITY_HIGH = 1000,
		PRIORITY_LOW = 0,
		PRIORITY_MEDIUM = (PRIORITY_HIGH + PRIORITY_LOW) / 2,
		PRIORITY_MEDIUM_LOW = (PRIORITY_MEDIUM + PRIORITY_LOW) / 2,
		PRIORITY_MEDIUM_HIGH = (PRIORITY_MEDIUM + PRIORITY_HIGH) / 2;
	
	public final skylark.Plugin plugin;
	public final int priority;
	
	public CommandProvider(skylark.Plugin plugin, int priority) {
		this.plugin = plugin;
		this.priority = priority;
	}
	
	public abstract Command provide(GenericUserMessageEvent e, String name);
}