package cxr.qoid.app.ide;

import cxr.qoid.app.Sys;
import cxr.qoid.canon.Bill;
import cxr.qoid.canon.Property;
import cxr.qoid.canon.Thing;
import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawFolder;
import cxr.qoid.canon.raw.RawIndex;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class NavTree extends JTree {
	
	public static class NavListener implements TreeSelectionListener, Runnable {
		NavTree source;
		FocusEditor editor;
		RawIndex<?> previous;

		public NavListener(NavTree source, FocusEditor editor) {
			this.source = source;
			this.editor = editor;
			this.previous = null;
		}

		public void setPrevious(RawIndex<?> previous) {
			this.previous = previous;
		}

		public void valueChanged(TreeSelectionEvent e) {
	      if (this.previous != null && this.editor.isChanged()) {
	         String var2;
	         switch((var2 = this.previous.className()).hashCode()) {
	         case -1644993116:
	            if (var2.equals("RawFile")) {
	               RawFile replace = new RawFile(this.previous.getFilePath(), (String)this.previous.tag(), this.source.editor().rawText());
	               ((RawFile)this.previous).changeTo(replace);
	            }
	            break;
	         case -284876138:
	            if (var2.equals("RawFolder")) {
	               ((RawFolder)this.previous).setMeta(new RawFile(((RawFolder)this.previous).getMeta().getFilePath(), ".meta", this.editor.rawText()));
	            }
	         }
	      }

	      this.previous = this.source.focus();
	      String var4;
	      switch((var4 = this.previous.className()).hashCode()) {
	      case -1644993116:
	         if (var4.equals("RawFile")) {
	            this.editor.setText(this.previous.cxrString());
	            this.editor.resetChangeMarker();
	         }
	         break;
	      case -284876138:
	         if (var4.equals("RawFolder")) {
	            this.editor.setText(((RawFolder)this.previous).getMeta().cxrString());
	            this.editor.resetChangeMarker();
	         }
	      }

	      this.editor.updateTitle();
	   }

		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.editor.updateUI();
			this.source.updateUI();
		}
	}
	
	private static final long serialVersionUID = -2392589710354013251L;
	private final RawFolder root;
	private final FocusEditor editor;
	private final Bill cfgContainer;

	public NavTree(RawFolder root, FocusEditor editor, Bill cfg) {
		super(root);
		this.root = root;
		this.editor = editor;
		this.cfgContainer = cfg;
		this.setSelectionPath(new TreePath(root));
		this.addTreeSelectionListener(new NavListener(this, editor));
		editor.setNavTree(this);
		this.getSelectionModel().setSelectionMode(1);
	}

	public RawFolder root() {
		return this.root;
	}

	public Bill cfgContainer() {
		return this.cfgContainer;
	}

	public Property getConfig(String thingTag, String propertyTag) {
		return (Property) ((Thing) this.cfgContainer.get(thingTag)).get(propertyTag);
	}

	public void updateConfig(String thingTag, String propertyTag, String propertyVal) {
		((Property) ((Thing) this.cfgContainer.get(thingTag)).get(propertyTag)).setVal(propertyVal);
		Sys.save(RawFile.convert(Sys.cfgLoc, ".config", this.cfgContainer));
	}

	public RawIndex<?> focus() {
		return (RawIndex) this.getLastSelectedPathComponent();
	}

	public boolean rootFocused() {
		return this.focus().equals(this.root());
	}

	public FocusEditor editor() {
		return this.editor;
	}

	public RawFolder target() {
		RawIndex<?> foc = this.focus();
		String type = foc.className();
		boolean isFolder = type.equals("RawFolder");
		RawFolder out = isFolder ? (RawFolder) foc : (RawFolder) foc.getParent();
		return out;
	}
}