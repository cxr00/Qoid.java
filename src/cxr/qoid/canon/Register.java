package cxr.qoid.canon;

import cxr.qoid.Qoid;
import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawFolder;
import cxr.qoid.canon.raw.RawIndex;
import cxr.qoid.canon.string.StringIndex;
import cxr.qoid.canon.string.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Register extends StringIndex<StringIndex<?>> {
	private Meta meta;

	public Register(String tag) {
		super(tag);
		this.setMeta(new Meta());
	}

	public Register(String tag, Meta meta) {
		super(tag);
		this.setMeta(meta);
	}

	public Register(String tag, ArrayList<StringIndex<?>> val) {
		super(tag, val);
		this.setMeta(new Meta());
	}

	public Register(String tag, ArrayList<StringIndex<?>> val, Meta meta) {
		super(tag, val);
		this.setMeta(meta);
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return false;
	}

	public static Register parse(RawFolder r) {
		Register out = new Register((String) r.tag());
		out.setMeta(r.getMeta().parseMeta());
		Iterator var3 = r.iterator();

		while (var3.hasNext()) {
			RawIndex<?> each = (RawIndex) var3.next();
			out.add((StringIndex<?>) (each.isClass("RawFolder")
					? parse((RawFolder) each)
					: Bill.parse((String) each.tag(), ((RawFile) each).cxr())));
		}

		out.sort();
		return out;
	}

	public Register prepare() {
		this.broadcastVariables();
		this.callVariables();
		return push(this);
	}

	private void callVariables() {
		Iterator var2 = this.iterator();

		while (true) {
			StringIndex each;
			String var3;
			label41 : do {
				while (var2.hasNext()) {
					each = (StringIndex) var2.next();
					switch ((var3 = each.className()).hashCode()) {
						case -625569085 :
							if (var3.equals("Register")) {
								((Register) each).callVariables();
							}
							break;
						case 2070567 :
							continue label41;
					}
				}

				return;
			} while (!var3.equals("Bill"));

			Iterator var5 = ((Bill) each).iterator();

			while (var5.hasNext()) {
				Thing t = (Thing) var5.next();
				Iterator var7 = t.iterator();

				while (var7.hasNext()) {
					Property p = (Property) var7.next();

					while (p.containsCall()) {
						String var = Variable.getNextCall(p);

						try {
							p.replaceCall(var, (String) ((Variable) this.getMeta().get(var)).val());
						} catch (NullPointerException var10) {
							p.replaceCall(var, "");
						}
					}
				}
			}
		}
	}

	private void broadcastVariables() {
		Iterator var2 = this.iterator();

		while (true) {
			StringIndex each;
			do {
				if (!var2.hasNext()) {
					return;
				}

				each = (StringIndex) var2.next();
			} while (!each.isClass("Register"));

			Iterator var4 = ((Register) each).getMeta().iterator();

			while (var4.hasNext()) {
				Variable v = (Variable) var4.next();

				while (v.containsCall()) {
					String var = v.getNextCall();

					try {
						v.replaceCall(var, (String) ((Variable) this.getMeta().get(var)).val());
					} catch (NullPointerException var7) {
						v.replaceCall(var, "");
					}
				}
			}

			((Register) each).broadcastVariables();
		}
	}

	public void sort() {
		ArrayList<Register> reg = new ArrayList();
		ArrayList<Bill> bill = new ArrayList();
		Iterator var4 = this.iterator();

		while (var4.hasNext()) {
			StringIndex<?> each = (StringIndex) var4.next();
			String var5;
			switch ((var5 = each.className()).hashCode()) {
				case -625569085 :
					if (var5.equals("Register")) {
						((Register) each).sort();
						reg.add((Register) each);
					}
					break;
				case 2070567 :
					if (var5.equals("Bill")) {
						bill.add((Bill) each);
					}
			}
		}

		this.val = new ArrayList();
		Collections.sort(reg);
		Collections.sort(bill);
		((ArrayList) this.val).addAll(reg);
		((ArrayList) this.val).addAll(bill);
	}

	public Meta getMeta() {
		return this.meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Thing index() {
		Thing out = new Thing((String) this.tag());
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			StringIndex<?> each = (StringIndex) var3.next();
			String var4;
			switch ((var4 = each.className()).hashCode()) {
				case -625569085 :
					if (var4.equals("Register")) {
						out.add(new Property("Register", (String) each.tag() + " at " + each.cxrPath()));
						out.addAll(((Register) each).index());
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
		Thing out = new Thing((String) this.tag());
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			StringIndex<?> each = (StringIndex) var3.next();
			String var4;
			switch ((var4 = each.className()).hashCode()) {
				case -625569085 :
					if (var4.equals("Register")) {
						out.add(new Property((String) each.tag()));
						out.addAll(((Register) each).prettyIndex(1));
					}
					break;
				case 2070567 :
					if (var4.equals("Bill")) {
						out.add(new Property((String) each.tag()));
						out.addAll(((Bill) each).prettyIndex(1));
					}
			}
		}

		return out;
	}

	public Thing prettyIndex(int n) {
		Thing out = new Thing((String) this.tag() + "sub-index");
		Iterator var4 = this.iterator();

		while (var4.hasNext()) {
			StringIndex<?> each = (StringIndex) var4.next();
			String var5;
			switch ((var5 = each.className()).hashCode()) {
				case -625569085 :
					if (var5.equals("Register")) {
						out.add(new Property(spaces(3 * n) + (String) each.tag()));
						out.addAll(((Register) each).prettyIndex(n + 1));
					}
					break;
				case 2070567 :
					if (var5.equals("Bill")) {
						out.add(new Property(spaces(3 * n) + (String) each.tag()));
						out.addAll(((Bill) each).prettyIndex(n + 1));
					}
			}
		}

		return out;
	}

	public static Register push(Register r) {
		Register out = new Register((String) r.tag());
		Iterator var3 = r.iterator();

		while (true) {
			StringIndex c;
			String var4;
			label37 : do {
				while (var3.hasNext()) {
					c = (StringIndex) var3.next();
					switch ((var4 = c.className()).hashCode()) {
						case -625569085 :
							if (var4.equals("Register")) {
								out.add(push((Register) c));
							}
							break;
						case 2070567 :
							continue label37;
					}
				}

				return out;
			} while (!var4.equals("Bill"));

			Bill b = new Bill((String) c.tag());

			Thing x;
			label53 : for (Iterator var7 = ((Bill) c).iterator(); var7.hasNext(); b.add(x)) {
				Thing t = (Thing) var7.next();
				x = new Thing((String) t.tag());
				Iterator var10 = t.iterator();

				while (true) {
					while (true) {
						if (!var10.hasNext()) {
							continue label53;
						}

						Property p = (Property) var10.next();
						String var11;
						switch ((var11 = (String) p.tag()).hashCode()) {
							case 35430600 :
								if (var11.equals("#echo")) {
									Bill.echo((Bill) c, x, p);
									break;
								}
							default :
								x.add(p);
						}
					}
				}
			}

			out.add(b);
		}
	}

	public Bill glossary() {
		Bill out = new Bill((String) this.tag());
		Iterator var3 = this.iterator();

		while (true) {
			StringIndex c;
			String var4;
			label30 : do {
				while (var3.hasNext()) {
					c = (StringIndex) var3.next();
					switch ((var4 = c.className()).hashCode()) {
						case -625569085 :
							if (var4.equals("Register")) {
								out.addAll((Collection) ((Register) c).glossary().val());
							}
							break;
						case 2070567 :
							continue label30;
					}
				}

				return out;
			} while (!var4.equals("Bill"));

			Thing n = new Thing(c.cxrPathString());
			Iterator var7 = ((Bill) c).iterator();

			while (var7.hasNext()) {
				Thing t = (Thing) var7.next();
				n.add(new Property((String) t.tag()));
			}

			out.add(n);
		}
	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			StringIndex<?> p = (StringIndex) var3.next();
			out.add(p.cxrString());
		}

		return out;
	}
}