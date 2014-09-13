package sconsole.channel;

import org.pircbotx.Channel;
import sconsole.ConsoleThread;
import sconsole.ConsoleView;
import sconsole.ConsoleViewSplitter;
import com.googlecode.lanterna.input.Key;

public class ConsoleViewChannelInput extends ConsoleView {
	public final Channel channel;
	public StringBuilder sb = new StringBuilder();
	public ConsoleViewChannelUserList view;
	
	public ConsoleViewChannelInput(ConsoleThread thread, Channel channel) {
		super(thread);
		this.channel = channel;
	}
	
	public void update(ConsoleViewSplitter.Side side) {
		super.update(side);
		if (side != null && side.h) {
			throw new IllegalStateException();
		}
		rect.h = 1;
	}
	public void handleInput(ConsoleViewSplitter.Side side, Key key) {
		switch (key.getKind()) {
			case Tab:
				if (view != null) {
					rect.thread.replaceFocus(view);
				}
				break;
			case NormalKey:
				sb.append(key.getCharacter());
				break;
			case Backspace:
				sb.deleteCharAt(sb.length() - 1);
				break;
			case Enter:
				channel.send().message(sb.toString());
				sb = new StringBuilder();
				break;
			default:
				super.handleInput(side, key);
				break;
		}
	}
	
	public void draw(ConsoleViewSplitter.Side side) {
		try {
		rect.draw(0, 0, sb.toString());
		if (rect.thread.focus() == this) {
			rect.setCursor(sb.length(), 0);
		}
		} catch (Exception e) {e.printStackTrace();}
	}
}