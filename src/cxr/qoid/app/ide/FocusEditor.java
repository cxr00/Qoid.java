package cxr.qoid.app.ide;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cxr.qoid.Qoid;
import cxr.qoid.canon.raw.RawLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

public class FocusEditor extends JTextPane implements DocumentListener {
	private static final long serialVersionUID = -2030682189107593785L;
	NavTree nav;
	boolean changed = false;
	TitledBorder label;

	public FocusEditor() {
      this.getDocument().addDocumentListener(this);
      this.label = new TitledBorder("");
      this.setBorder(this.label);
   }

	public boolean isChanged() {
		return this.changed;
	}

	public void resetChangeMarker() {
		this.changed = false;
	}

	public ArrayList<RawLine> rawText() {
		return RawLine.wrap(Arrays.asList(this.getText().split(Qoid.newline())));
	}

	public void setNavTree(NavTree nav) {
		this.nav = nav;
		this.updateTitle();
	}

	public void updateTitle() {
		System.out.println("Nav focus: " + this.nav.focus().cxrPathString());
		this.label.setTitle("Focus Editor - " + this.pathFormat(this.nav.focus().cxrPath())
				+ (this.nav.focus().isClass("RawFolder") ? ".meta" : ""));
	}

	private String pathFormat(List<String> in) {
		if (in.size() == 0) {
			return "";
		} else {
			String out = (String) in.remove(0);

			String each;
			for (Iterator var4 = in.iterator(); var4.hasNext(); out = out + " | " + each) {
				each = (String) var4.next();
			}

			return out;
		}
	}

	public NavTree getNavTree() {
		return this.nav;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		this.changed = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		this.changed = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		this.changed = true;
	}
}