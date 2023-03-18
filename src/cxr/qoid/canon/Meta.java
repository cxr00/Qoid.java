package cxr.qoid.canon;

import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawLine;
import cxr.qoid.canon.string.StringIndex;
import cxr.qoid.canon.string.Variable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Meta extends StringIndex<Variable> {
	public static final String meta_key = ".meta";

	public Meta() {
		super(".meta");
	}

	public Meta(ArrayList<Variable> val) {
		super(".meta", val);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public String cxrString() {
		String out = "";

		String each;
		for (Iterator var3 = this.cxr().iterator(); var3.hasNext(); out = out + each + newline()) {
			each = (String) var3.next();
		}

		return out;
	}

	public static Meta parse(RawFile r) {
		Meta out = new Meta();
		Iterator var3 = r.iterator();

		while (var3.hasNext()) {
			RawLine each = (RawLine) var3.next();
			Variable v = Variable.validateAndScrape(each.cxrString());
			if (v != null) {
				out.add(v);
			}
		}

		return out;
	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			Variable p = (Variable) var3.next();
			out.add(p.cxrString());
		}

		return out;
	}
}