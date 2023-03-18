package cxr.qoid.app;

import cxr.qoid.app.ide.FocusEditor;
import cxr.qoid.app.ide.NavTree;
import cxr.qoid.app.ide.UtilityPane;
import cxr.qoid.app.ide.toolbar.QoidMenuBar;
import cxr.qoid.app.ide.toolbar.QoidToolBar;
import cxr.qoid.canon.Bill;
import cxr.qoid.canon.raw.RawFolder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class IDE extends JFrame {
	private static final long serialVersionUID = 2791017028471019728L;
	QoidToolBar toolbar;
	QoidMenuBar menu;
	NavTree nav;
	FocusEditor editor;
	final String path;
	RawFolder workspace;

	public IDE(String path, Bill cfgContainer) {
		this.path = path;
		this.workspace = Sys.loadFolder(path);
		this.setIconImage((new ImageIcon("icon.png")).getImage());
		this.editor = new FocusEditor();
		this.nav = new NavTree(this.workspace, this.editor, cfgContainer);
		this.toolbar = new QoidToolBar(this.nav);
		JPanel container = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.333D;
		gbc.weighty = 0.5D;
		container.add(new JScrollPane(this.nav), gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0.333D;
		gbc.weighty = 0.5D;
		container.add(new JScrollPane(new UtilityPane(this.nav, this.workspace, this.toolbar)), gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.667D;
		gbc.weighty = 1.0D;
		container.add(new JScrollPane(this.editor), gbc);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 4;
		gbc.weightx = 1.0D;
		gbc.weighty = 0.05D;
		gbc.fill = 10;
		container.add(this.toolbar, gbc);
		this.add(container);
		this.pack();
		this.setJMenuBar(new QoidMenuBar(this.nav));
	}
}