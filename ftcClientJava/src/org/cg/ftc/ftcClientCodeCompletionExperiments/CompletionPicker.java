package org.cg.ftc.ftcClientCodeCompletionExperiments;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cg.common.check.Check;
import org.cg.common.structures.IntTuple;
import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.Completions;

import com.google.common.base.Optional;

public class CompletionPicker extends JPanel implements ListSelectionListener, WindowListener, KeyListener {
	private static final long serialVersionUID = -7917062917946392736L;

	private Optional<Item> itemSelected = Optional.absent();

	private final ItemChosenHandler onItemChosen;

	private static class Item {
		private AbstractCompletion value = null;
		private DefaultListModel<Item> subitems = new DefaultListModel<Item>();

		Item(AbstractCompletion value, Item[] subitems) {
			this.value = value;
			if (subitems != null)
				for (Item subitem : subitems)
					this.subitems.addElement(subitem);
		}

		public String toString() {
			return value.toString();
		}
	}

	private DefaultListModel<Item> model = new DefaultListModel<Item>();

	private JList<Item> itemListDisplay = new JList<Item>(model);
	private JList<Item> itemSubListDisplay = new JList<Item>();

	private CompletionPicker(ItemChosenHandler onItemChosen) {
		super(new GridLayout(0, 2));
		add(itemListDisplay);
		add(itemSubListDisplay);

		itemListDisplay.addListSelectionListener(this);
		itemSubListDisplay.addListSelectionListener(this);
		this.onItemChosen = onItemChosen;
	}

	public void addItem(AbstractCompletion item, Item[] subitems) {
		model.addElement(new Item(item, subitems));
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == itemListDisplay) {
			Item item = (Item) itemListDisplay.getSelectedValue();
			itemSubListDisplay.setModel(item.subitems);
			itemSelected = Optional.fromNullable(item);
		} else {
			itemSelected = Optional.fromNullable(itemSubListDisplay.getSelectedValue());
		}
	}

	private static Item[] toArray(List<AbstractCompletion> columns) {

		Item[] result = new Item[columns.size()];
		int count = 0;
		for (AbstractCompletion c : columns) {
			result[count] = new Item(c, null);
			count++;
		}
		return result;

	}

	private static CompletionPicker createCompletions(List<AbstractCompletion> completions,
			ItemChosenHandler onItemChosen) {
		CompletionPicker p = new CompletionPicker(onItemChosen);
		for (AbstractCompletion c : completions)
			p.addItem(c, toArray(c.children));
		return p;
	}

	public static void show(Completions completions, ItemChosenHandler onItemChosen, IntTuple xy) {
		Check.notNull(xy);
		Check.notNull(completions);
		
		JFrame frame = new JFrame("Nested Lists");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CompletionPicker c = createCompletions(completions.getAll(), onItemChosen);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.add(c);
		frame.addWindowListener(c);
		frame.addKeyListener(c);
		frame.pack();
		if (xy.i1 > 0 && xy.i2 > 0)
			frame.setLocation(xy.i1, xy.i2);
		frame.setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (itemSelected.isPresent())
			onItemChosen.onItemChosen(itemSelected.get().value);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		select(itemListDisplay, 0);
	}

	private void select(@SuppressWarnings("rawtypes") JList l, int index) {
		if (index < l.getModel().getSize())
			l.setSelectionInterval(index, index);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode = e.getKeyCode();
		System.out.println(Integer.toString(keycode));
		switch (keycode) {

		case KeyEvent.KEY_LOCATION_RIGHT:
			select(itemSubListDisplay, 0);
			break;
		case KeyEvent.KEY_LOCATION_LEFT:
			select(itemListDisplay, itemListDisplay.getSelectedIndex() >= 0 ? itemListDisplay.getSelectedIndex() : 0);
			break;
		default:
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

}
