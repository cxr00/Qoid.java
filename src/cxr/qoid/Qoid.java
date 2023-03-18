package cxr.qoid;

public abstract class Qoid<T extends Comparable<T>, V> implements Comparable<Qoid<T, ?>> {
	private static String newline = System.getProperty("line.separator");
	protected T tag;
	protected V val;

	public static String newline() {
		return newline;
	}

	public static String spaces(int n) {
		String out = "";

		for (int k = 0; k < n; ++k) {
			out = out + " ";
		}

		return out;
	}

	public Qoid(T tag) {
		this.tag = tag;
		this.val = null;
	}

	public Qoid(T tag, V val) {
		this.tag = tag;
		this.val = val;
	}

	public boolean hasTag(T tag) {
		return this.tag.equals(tag);
	}

	public void setTag(T tag) {
		this.tag = tag;
	}

	public T tag() {
		return this.tag;
	}

	public boolean hasVal(T val) {
		return this.val.equals(val);
	}

	public void setVal(V val) {
		this.val = val;
	}

	public V val() {
		return this.val;
	}

	public String className() {
		return this.getClass().getSimpleName();
	}

	public int compareTo(Qoid<T, ?> t) {
		return -t.tag().compareTo(this.tag());
	}

	public boolean isClass(String name) {
		return this.className().equals(name);
	}

	public boolean equals(Object t) {
		if (t instanceof Qoid && t.getClass().getTypeParameters().equals(this.getClass().getTypeParameters())) {
			Qoid<T, V> c = (Qoid) t;
			return this.tag().equals(c.tag()) && this.val().equals(c.val());
		} else {
			return false;
		}
	}

	public String toString() {
		return this.tag().toString();
	}
}