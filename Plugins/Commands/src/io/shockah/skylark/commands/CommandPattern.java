package io.shockah.skylark.commands;

import io.shockah.skylark.event.GenericUserMessageEvent;

public abstract class CommandPattern {
	public abstract Command provide(GenericUserMessageEvent e);
}