package cxr.qoid.app.ide;

import cxr.qoid.app.Sys;
import cxr.qoid.app.ide.toolbar.QoidToolBar;
import cxr.qoid.canon.Register;
import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawFolder;
import cxr.qoid.canon.raw.RawIndex;
import cxr.qoid.canon.raw.RawLine;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class UtilityPane extends JTabbedPane implements ActionListener {
	
	class Glossary extends JPanel implements ActionListener {
		UtilityPane parent;
		JTextPane text;

		public Glossary(UtilityPane parent) {
			this.setLayout(new FlowLayout());
			this.parent = parent;
			this.parent.getButton("Save").addActionListener(this);
			this.text = new JTextPane();
			this.text.setFocusable(false);
			String s = Register.parse(parent.workspace()).prettyIndex().cxrString();
			this.text.setText(s);
			this.add(this.text);
		}

		public void actionPerformed(ActionEvent e) {
			String s = Register.parse(this.parent.workspace()).prettyIndex().cxrString();
			this.text.setText(s);
		}
	}
	
	class Overview extends JPanel implements ActionListener {
		UtilityPane parent;
		JTextArea text;

		public Overview(UtilityPane parent) {
			this.setLayout(new FlowLayout());
			this.parent = parent;
			this.parent.getButton("SetExportLoc").addActionListener(this);
			this.text = new JTextArea();
			this.text.setFocusable(false);
			String s = "Workspace: " + Sys.workspace + Sys.fileSep + Sys.root;
			s = s + "\nExport Location: " + (String) parent.nav().getConfig("startup", "export").val();
			this.text.setText(s);
			this.add(this.text);
		}

		public void actionPerformed(ActionEvent e) {
			String s = "Workspace: " + Sys.workspace + Sys.fileSep + Sys.root;
			s = s + "\nExport Location: " + (String) this.parent.nav().getConfig("startup", "export").val();
			this.text.setText(s);
		}
	}
	
	class Search extends JPanel implements ActionListener {
		UtilityPane parent;
		JTextField field;
		JButton button;
		JTextPane results;

		public Search(UtilityPane parent) {
			this.setLayout(new FlowLayout());
			this.parent = parent;
			this.field = new JTextField();
			this.field.setPreferredSize(new Dimension(100, 22));
			this.add(this.field);
			this.button = new JButton("Search");
			this.button.addActionListener(this);
			this.add(this.button);
			this.results = new JTextPane();
			this.results.setFocusable(false);
			this.add(this.results);
		}

		public void actionPerformed(ActionEvent e) {
			ArrayList<String> found = getSearchResults(this.field.getText(), this.parent.workspace());
			String s;
			if (found.size() > 0) {
				s = (String) found.get(0);

				String each;
				for (Iterator var5 = found.subList(1, found.size()).iterator(); var5.hasNext(); s = s + "\n" + each) {
					each = (String) var5.next();
				}

				s = s + "\n";
				this.results.setText(s);
			} else {
				s = "No results found.";
				this.results.setText(s);
			}

		}

		public ArrayList<String> getSearchResults(String searchTerm, RawIndex<?> searchLocation) {
			ArrayList<String> out = new ArrayList();
			String var3;
			Iterator var5;
			switch ((var3 = searchLocation.className()).hashCode()) {
				case -1644993116 :
					if (var3.equals("RawFile")) {
						var5 = ((RawFile) searchLocation).iterator();

						while (var5.hasNext()) {
							RawLine r = (RawLine) var5.next();
							if (r.contains(searchTerm)) {
								out.add(searchLocation.cxrPathString() + ": " + ((RawFile) searchLocation).indexOf(r));
							}
						}
					}
					break;
				case -284876138 :
					if (var3.equals("RawFolder")) {
						var5 = ((RawFolder) searchLocation).iterator();

						while (var5.hasNext()) {
							RawIndex<?> each = (RawIndex) var5.next();
							out.addAll(getSearchResults(searchTerm, each));
						}
					}
			}

			return out;
		}
	}
	
	
	NavTree nav;
	RawFolder workspace;
	Register cxr;
	QoidToolBar toolbar;

	public UtilityPane(NavTree nav, RawFolder workspace, QoidToolBar toolbar) {
		this.toolbar = toolbar;
		this.nav = nav;
		this.workspace = workspace;
		this.cxr = Register.parse(workspace);
		JPanel overview = new Overview(this);
		JPanel index = new Glossary(this);
		JPanel search = new Search(this);
		this.addTab("Overview", (Icon) null, new JScrollPane(overview),
				"General information about the current project");
		this.addTab("Glossary", (Icon) null, new JScrollPane(index),
				"See a list of all Things in the project and jump quickly between them");
		this.addTab("Search", (Icon) null, new JScrollPane(search),
				"Search the project for appearances of the given text");
		toolbar.getButton("Save").addActionListener(this);
	}

	public RawFolder workspace() {
		return this.workspace;
	}

	public Register searchable() {
		return this.cxr;
	}

	public NavTree nav() {
		return this.nav;
	}

	public JButton getButton(String name) {
		return this.toolbar.getButton(name);
	}

	public void actionPerformed(ActionEvent e) {
		this.cxr = Register.parse(this.workspace);
	}
}