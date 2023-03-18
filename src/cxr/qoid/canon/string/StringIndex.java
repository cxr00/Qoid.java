package cxr.qoid.canon.string;

import cxr.qoid.Qoid;
import cxr.qoid.Index;
import cxr.qoid.app.Sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.TreeNode;

public abstract class StringIndex<V extends Qoid<?, ?>> extends Index<String, V> {
	public StringIndex(String tag) {
		super(tag);
	}

	public StringIndex(String tag, ArrayList<V> val) {
		super(tag, val);
	}

	public abstract List<String> cxr();

	public List<String> cxrPath() {
		List<String> out = new ArrayList();
		out.add(((String) this.tag()).toString());

		for (TreeNode p = this.getParent(); p != null; p = p.getParent()) {
			out.add(0, ((Index) p).tag().toString());
		}

		return out;
	}

	public String cxrPathString() {
		List<String> p = this.cxrPath();
		if (p.size() == 1) {
			return (String) p.get(0);
		} else {
			String out = (String) p.get(0);

			String each;
			for (Iterator var4 = this.cxrPath().subList(1, this.cxrPath().size()).iterator(); var4
					.hasNext(); out = out + " | " + each) {
				each = (String) var4.next();
			}

			return out;
		}
	}

	public String cxrString() {
		String out = "";

		String each;
		for (Iterator var3 = this.cxr().iterator(); var3
				.hasNext(); out = out + (each.equals(newline()) ? newline() : each + newline())) {
			each = (String) var3.next();
		}

		return out;
	}

	public String relativeFilePath() {
		String out = "";

		String each;
		for (Iterator var3 = this.cxrPath().iterator(); var3.hasNext(); out = out + Sys.fileSep + each) {
			each = (String) var3.next();
		}

		return out;
	}
}