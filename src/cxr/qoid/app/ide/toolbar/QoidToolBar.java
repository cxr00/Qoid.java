package cxr.qoid.app.ide.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;

import cxr.qoid.app.ide.NavTree;

public class QoidToolBar extends JToolBar {
	private static final long serialVersionUID = 8041941130064367072L;
	private static final String[] toolbar_frame = new String[]{"AddFile", "AddFolder", "Delete", "ImportFile",
			"ImportFolder", "SetExportLoc", "Save"};

	public QoidToolBar(NavTree source) {
		for (int n = 0; n < toolbar_frame.length; ++n) {
			JButton toolbar_button = new JButton(toolbar_frame[n]);
			toolbar_button.addActionListener(Listeners.get(toolbar_frame[n], source));
			toolbar_button.setToolTipText(ToolTips.get(toolbar_frame[n]));
			this.add(toolbar_button);
		}

	}

	public JButton getButton(String name) {
		for (int n = 0; n < toolbar_frame.length; ++n) {
			JButton toolbar_button = (JButton) this.getComponent(n);
			if (toolbar_button.getText().equals(name)) {
				return toolbar_button;
			}
		}

		return null;
	}
}