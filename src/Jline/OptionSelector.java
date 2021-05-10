package Jline;

import static org.jline.keymap.KeyMap.ctrl;
import static org.jline.keymap.KeyMap.key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp.Capability;

public class OptionSelector {
	private enum Operation {
		FORWARD_ONE_LINE, BACKWARD_ONE_LINE, EXIT
	}

	private final Terminal terminal;
	private final List<String> lines = new ArrayList<>();
	private final Size size = new Size();
	private final BindingReader bindingReader;

	public OptionSelector(Terminal terminal, String title, Collection<String> options) {
		this.terminal = terminal;
		this.bindingReader = new BindingReader(terminal.reader());
		lines.add(title);
		lines.addAll(options);
	}

	private List<AttributedString> displayLines(int cursorRow) {
		List<AttributedString> out = new ArrayList<>();
		int i = 0;
		for (String s : lines) {
			if (i == cursorRow) {
				out.add(new AttributedStringBuilder().append(s, AttributedStyle.INVERSE).toAttributedString());
			} else {
				out.add(new AttributedString(s));
			}
			i++;
		}
		return out;
	}

	private void bindKeys(KeyMap<Operation> map) {
		map.bind(Operation.FORWARD_ONE_LINE, "e", ctrl('E'), key(terminal, Capability.key_down));
		map.bind(Operation.BACKWARD_ONE_LINE, "y", ctrl('Y'), key(terminal, Capability.key_up));
		map.bind(Operation.EXIT, "\r");
	}

	public String select() {
		Display display = new Display(terminal, true);
		Attributes attr = terminal.enterRawMode();
		try {
			terminal.puts(Capability.enter_ca_mode);
			terminal.puts(Capability.keypad_xmit);
			terminal.writer().flush();
			size.copy(terminal.getSize());
			display.clear();
			display.reset();
			int selectRow = 1;
			KeyMap<Operation> keyMap = new KeyMap<>();
			bindKeys(keyMap);
			while (true) {
				display.resize(size.getRows(), size.getColumns());
				display.update(displayLines(selectRow), size.cursorPos(0, lines.get(0).length()));
				Operation op = bindingReader.readBinding(keyMap);
				switch (op) {
				case FORWARD_ONE_LINE:
					selectRow++;
					if (selectRow > lines.size() - 1) {
						selectRow = 1;
					}
					break;
				case BACKWARD_ONE_LINE:
					selectRow--;
					if (selectRow < 1) {
						selectRow = lines.size() - 1;
					}
					break;
				case EXIT:
					return lines.get(selectRow);
				}
			}
		} finally {
			terminal.setAttributes(attr);
			terminal.puts(Capability.exit_ca_mode);
			terminal.puts(Capability.keypad_local);
			terminal.writer().flush();
		}
	}
}
