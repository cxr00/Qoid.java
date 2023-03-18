package cxr.qoid;

public class AbstractProperty<T extends Comparable<T>, V> extends Qoid<T, V> {
	public AbstractProperty(T tag) {
		super(tag);
	}

	public AbstractProperty(T tag, V val) {
		super(tag, val);
	}

	public boolean equals(Object o) {
		if (o instanceof AbstractProperty) {
			return this.tag.equals(((AbstractProperty) o).tag()) && this.val.equals(((AbstractProperty) o).val());
		} else {
			return false;
		}
	}
}