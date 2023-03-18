package cxr.qoid.canon;

import cxr.qoid.Index;
import cxr.qoid.canon.string.StringIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Bill extends StringIndex<Thing> implements Comparator<Thing>{
	public Bill(String tag) {
		super(tag);
	}

	public Bill(String tag, ArrayList<Thing> val) {
		super(tag, val);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public void sort() {
		this.val().sort(null);
   }

	public Thing prettyIndex(int n) {
		Thing out = new Thing((String) this.tag() + "sub-index");
		Iterator var4 = this.iterator();

		while (var4.hasNext()) {
			Thing each = (Thing) var4.next();
			out.add(new Property(spaces(3 * n) + (String) each.tag()));
		}

		return out;
	}

	public static Bill parse(String tag, List<String> source) {
		Bill out = new Bill(tag);
		if (source.size() == 0) {
			return new Bill(tag);
		} else {
			int state = 0;
			Thing spool = null;
			Iterator var6 = source.iterator();

			while (var6.hasNext()) {
				String line = (String) var6.next();
				switch (state) {
					case 0 :
						if (line.length() != 0 && line.charAt(0) == '#') {
							spool = new Thing(line.substring(1).trim());
							out.add(spool);
							++state;
						}
						break;
					case 1 :
						if (line.trim().length() == 0) {
							--state;
						} else if (line.charAt(0) != '/') {
							String[] s = line.split(":", 2);
							if (s.length == 1) {
								spool.add(new Property(s[0].trim()));
							} else {
								spool.add(new Property(s[0].trim(), s[1].trim()));
							}
						}
				}
			}

			return out;
		}
	}

	public static Bill parse(String path, String tag) {
		File f = new File(path);
		if (f.isDirectory()) {
			return new Bill("FILE_IS_DIRECTORY_LOAD_AS_REGISTER");
		} else {
			ArrayList source = new ArrayList();

			try {
				f.createNewFile();
				BufferedReader br = new BufferedReader(new FileReader(f));
				String l = null;

				while ((l = br.readLine()) != null) {
					source.add(l.trim());
				}

				br.close();
				return parse(tag, (List) source);
			} catch (IOException var6) {
				return new Bill("FILE_FAILED_TO_LOAD");
			}
		}
	}

	public static void echo(Bill b, Thing t, Property p) {
		String[] path = ((String) p.val()).split(Pattern.quote("|"));
		Index<String, ?> query = ((Index) b.getParent(path.length - 1)).query(Arrays.asList(path));
		if (query != null && query.isClass("Thing")) {
			Iterator var6 = ((Thing) query).iterator();

			while (var6.hasNext()) {
				Property i = (Property) var6.next();
				t.add(i);
			}
		}

	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			Thing p = (Thing) var3.next();
			out.add(p.cxrString() + "\n");
		}

		return out;
	}
	
	public int compare(Thing one, Thing two) {
		return ((String) one.tag()).compareTo((String) two.tag());
	}
}