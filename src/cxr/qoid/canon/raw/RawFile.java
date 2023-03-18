package cxr.qoid.canon.raw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cxr.qoid.canon.Bill;
import cxr.qoid.canon.Meta;
import cxr.qoid.canon.Thing;

public class RawFile extends RawIndex<RawLine> {
	public static char flag = '/';

	public RawFile(String path, String tag) {
		super(path, tag);
	}

	public RawFile(String path, String tag, ArrayList<RawLine> val) {
		super(path, tag, val);
	}

	public String alias() {
		return "Bill";
	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			RawLine each = (RawLine) var3.next();
			out.add(each.toString());
		}

		return out;
	}

	public static RawFile convert(String path, String tag, List<String> source) {
		RawFile out = new RawFile(path, tag);
		Iterator var5 = source.iterator();

		while (var5.hasNext()) {
			String each = (String) var5.next();
			out.add(RawLine.wrap(each.replace(newline(), "")));
		}

		return out;
	}

	public static RawFile convert(String path, String tag, Bill source) {
		List<String> out = new ArrayList();
		Iterator var5 = source.iterator();

		while (var5.hasNext()) {
			Thing each = (Thing) var5.next();
			out.addAll(each.cxr());
		}

		return convert(path, tag, (List) out);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public Bill parse() {
		return Bill.parse((String) this.tag(), this.cxr());
	}

	public Meta parseMeta() {
		return Meta.parse(this);
	}
}