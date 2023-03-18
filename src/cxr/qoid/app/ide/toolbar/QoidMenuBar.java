package cxr.qoid.app.ide.toolbar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cxr.qoid.app.ide.NavTree;

public class QoidMenuBar extends JMenuBar {
	private static final long serialVersionUID = -8183566604288657072L;

	public QoidMenuBar(NavTree source) {
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("Add File");
		item.addActionListener(Listeners.get("AddFile", source));
		menu.add(item);
		item = new JMenuItem("Add Folder");
		item.addActionListener(Listeners.get("AddFolder", source));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Save");
		item.addActionListener(Listeners.get("Save", source));
		menu.add(item);
		this.add(menu);
		menu = new JMenu("Edit");
		item = new JMenuItem("Delete");
		item.addActionListener(Listeners.get("Delete", source));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Move");
		item.addActionListener(Listeners.get("Move", source));
		menu.add(item);
		this.add(menu);
		menu = new JMenu("Import");
		item = new JMenuItem("File");
		item.addActionListener(Listeners.get("ImportFile", source));
		menu.add(item);
		item = new JMenuItem("Folder");
		item.addActionListener(Listeners.get("ImportFolder", source));
		menu.add(item);
		this.add(menu);
		menu = new JMenu("Export");
		item = new JMenuItem("Raw Project");
		item.addActionListener(Listeners.get("ExportProject", source));
		menu.add(item);
		item = new JMenuItem("CXR");
		item.addActionListener(Listeners.get("ExportCXR", source));
		menu.add(item);
		item = new JMenuItem("JSCON");
		item.addActionListener(Listeners.get("ExportJSCON", source));
		menu.add(item);
		this.add(menu);
	}
}