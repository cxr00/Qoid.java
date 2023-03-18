package cxr.qoid.canon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cxr.qoid.canon.string.StringIndex;

public class Thing extends StringIndex<Property> {
	public static char flag = '#';

	public Thing(String tag) {
		super(tag);
	}

	public Thing(String tag, ArrayList<Property> val) {
		super(tag, val);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return false;
	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			Property p = (Property) var3.next();
			out.add(p.cxrString());
		}

		out.add(0, flag + (String) this.tag);
		return out;
	}

	public List<String> cxrPath() {
		return (List) (this.hasParent() ? ((StringIndex) this.getParent()).cxrPath() : new ArrayList());
	}
}