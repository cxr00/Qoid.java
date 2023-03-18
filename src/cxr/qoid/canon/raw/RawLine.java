package cxr.qoid.canon.raw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cxr.qoid.canon.string.AbstractStringProperty;

public class RawLine extends AbstractStringProperty {
	public static RawLine wrap(String input) {
		return new RawLine(input);
	}

	public static ArrayList<RawLine> wrap(List<String> input) {
		ArrayList<RawLine> out = new ArrayList();
		Iterator var3 = input.iterator();

		while (var3.hasNext()) {
			String each = (String) var3.next();
			out.add(wrap(each));
		}

		return out;
	}

	protected RawLine(String tag) {
		super(tag);
	}

	public String cxrVal() {
		return "";
	}

	public String cxrString() {
		return (String) this.tag;
	}

	public boolean contains(String searchTerm) {
		String lcase = ((String) this.tag).toLowerCase();
		String lcase2 = searchTerm.toLowerCase();
		return lcase.contains(lcase2);
	}
}