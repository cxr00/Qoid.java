package cxr.qoid.canon.raw;

import cxr.qoid.app.Sys;
import cxr.qoid.canon.Property;
import cxr.qoid.canon.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RawFolder extends RawIndex<RawIndex<?>> {
	private RawFile meta;
	private static final String meta_key = ".meta";

	public RawFolder(String path, String tag) {
		super(path, tag);
		this.setMeta(new RawFile(path + Sys.fileSep + ".meta", ".meta"));
	}

	public RawFolder(String path, String tag, ArrayList<RawIndex<?>> val) {
		super(path, tag, val);
		this.setMeta(new RawFile(path + Sys.fileSep + ".meta", ".meta"));
	}

	public String alias() {
		return "Register";
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public String cxrString() {
		String out = "";

		String each;
		for (Iterator var3 = this.cxr().iterator(); var3.hasNext(); out = out + each) {
			each = (String) var3.next();
		}

		return out;
	}

	public RawFile getMeta() {
		return this.meta;
	}

	public void setMeta(RawFile meta) {
		this.meta = meta;
	}

	public void broadcastPath() {
		Iterator var2 = this.iterator();

		while (var2.hasNext()) {
			RawIndex<?> each = (RawIndex) var2.next();
			each.setFilePath(this.getFilePath() + Sys.fileSep + (String) each.tag() + ".cxr");
			if (each.className().equals("RawFolder")) {
				((RawFolder) each).broadcastPath();
			}
		}

	}

	public void sort() {
		ArrayList<RawFolder> reg = new ArrayList();
		ArrayList<RawFile> bill = new ArrayList();
		Iterator var4 = this.iterator();

		while (var4.hasNext()) {
			RawIndex<?> each = (RawIndex) var4.next();
			String var5;
			switch ((var5 = each.className()).hashCode()) {
				case -1644993116 :
					if (var5.equals("RawFile")) {
						bill.add((RawFile) each);
					}
					break;
				case -284876138 :
					if (var5.equals("RawFolder")) {
						((RawFolder) each).sort();
						reg.add((RawFolder) each);
					}
			}
		}

		this.val = new ArrayList();
		Collections.sort(reg);
		Collections.sort(bill);
		((ArrayList) this.val).addAll(reg);
		((ArrayList) this.val).addAll(bill);
	}

	public Thing index() {
		Thing out = new Thing((String) this.tag() + " index" + newline());
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			RawIndex<?> each = (RawIndex) var3.next();
			String var4;
			switch ((var4 = each.alias()).hashCode()) {
				case -625569085 :
					if (var4.equals("Register")) {
						out.add(new Property("Register", (String) each.tag() + " at " + each.cxrPath()));
						out.addAll(((RawFolder) each).index());
					}
					break;
				case 2070567 :
					if (var4.equals("Bill")) {
						out.add(new Property("Bill", (String) each.tag() + " at " + each.cxrPath()));
					}
			}
		}

		return out;
	}

	public Thing prettyIndex() {
		Thing out = new Thing((String) this.tag() + " index" + newline());
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			RawIndex<?> each = (RawIndex) var3.next();
			String var4;
			switch ((var4 = each.alias()).hashCode()) {
				case -625569085 :
					if (var4.equals("Register")) {
						out.add(new Property("Register", (String) each.tag() + " at " + each.cxrPath()));
						out.addAll(((RawFolder) each).prettyIndex(1));
					}
					break;
				case 2070567 :
					if (var4.equals("Bill")) {
						out.add(new Property("Bill", (String) each.tag() + " at " + each.cxrPath()));
					}
			}
		}

		return out;
	}

	private Thing prettyIndex(int n) {
		Thing out = new Thing((String) this.tag());
		Iterator var4 = this.iterator();

		while (var4.hasNext()) {
			RawIndex<?> each = (RawIndex) var4.next();
			String var5;
			switch ((var5 = each.alias()).hashCode()) {
				case -625569085 :
					if (var5.equals("Register")) {
						out.add(new Property(spaces(n) + "Register", (String) each.tag() + " at " + each.cxrPath()));
						out.addAll(((RawFolder) each).prettyIndex(n + 1));
					}
					break;
				case 2070567 :
					if (var5.equals("Bill")) {
						out.add(new Property(spaces(n) + "Bill", (String) each.tag() + " at " + each.cxrPath()));
					}
			}
		}

		return out;
	}

	public List<String> cxr() {
		return null;
	}
}