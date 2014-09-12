package sconsole;

import java.util.Collections;
import java.util.LinkedList;
import pl.shockah.SelectList;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class ConsoleViewTabs extends ConsoleView {
	public SelectList<ConsoleTab> tabs = new SelectList<>(Collections.synchronizedList(new LinkedList<ConsoleTab>()));
	public ConsoleViewTab view = null;
	
	public ConsoleViewTabs(ConsoleThread thread) {
		super(thread);
	}
	
	public void update(ConsoleViewSplitter.Side side) {
		super.update(side);
		if (side == null) return;
		if (view == null) return;
		
		synchronized (tabs) {
			if (side.h) {
				int w = 2;
				for (ConsoleTab tab : tabs) {
					tab.updateTabView(side, tabs.getCurrent() == tab);
					int ww = tab.caption.length() + 2;
					if (ww > w) w = ww;
				}
				rect.w = w;
			} else {
				int w = 0;
				for (ConsoleTab tab : tabs) {
					tab.updateTabView(side, tabs.getCurrent() == tab);
					w += tab.caption.length() + 2;
				}
				rect.w = w;
				rect.h = 1;
			}
		}
	}
	public void handleInput(ConsoleViewSplitter.Side side, Key key) {
		super.handleInput(side, key);
		if (side == null) return;
		if (view == null) return;
		
		synchronized (tabs) {
			if (side.h) {
				if (key.getKind() == Kind.ArrowUp) {
					tabs.previous();
				} else if (key.getKind() == Kind.ArrowDown) {
					tabs.next();
				}
			} else {
				if (key.getKind() == Kind.ArrowLeft) {
					tabs.previous();
				} else if (key.getKind() == Kind.ArrowRight) {
					tabs.next();
				}
			}
		}
		
		if (key.getKind() == Kind.Enter) {
			rect.thread.setFocus(view);
		}
	}
	
	public void draw(ConsoleViewSplitter.Side side) {
		if (side == null) return;
		if (view == null) return;
		boolean focus = rect.thread.focus() == this;
		
		synchronized (tabs) {
			if (side.h) {
				for (int i = 0; i < tabs.size(); i++) {
					ConsoleTab tab = tabs.get(i);
					boolean sel = tabs.getCurrentIndex() == i;
					String text = String.format(sel ? "<%s>" : " %s ", tab.caption);
					rect.draw(0, i, text, focus && sel ? Color.BLACK : Color.WHITE, focus && sel ? Color.WHITE : Color.BLACK);
				}
			} else {
				int xx = 0;
				for (int i = 0; i < tabs.size(); i++) {
					ConsoleTab tab = tabs.get(i);
					boolean sel = tabs.getCurrentIndex() == i;
					String text = String.format(sel ? "<%s>" : " %s ", tab.caption);
					rect.draw(xx, 0, text, focus && sel ? Color.BLACK : Color.WHITE, focus && sel ? Color.WHITE : Color.BLACK);
					xx += text.length();
				}
			}
		}
	}
}