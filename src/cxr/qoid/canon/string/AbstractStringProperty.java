package cxr.qoid.canon.string;

import java.util.ArrayList;
import java.util.List;

import cxr.qoid.AbstractProperty;

public abstract class AbstractStringProperty extends AbstractProperty<String, String> {
	public AbstractStringProperty(String tag) {
		super(tag, "");
	}

	public AbstractStringProperty(String tag, String val) {
		super(tag, val);
	}

	public boolean equals(Object o) {
		if (o instanceof AbstractStringProperty && o != null) {
			return ((String) this.tag).equals(((AbstractStringProperty) o).tag())
					&& ((String) this.val).equals(((AbstractStringProperty) o).val());
		} else {
			return false;
		}
	}

	public List<String> cxr() {
		List<String> out = new ArrayList();
		out.add(this.cxrString());
		return out;
	}

	public String cxrString() {
		return (String) this.tag + this.cxrVal();
	}

	public String cxrVal() {
		return ((String) this.val).toString().length() > 0 ? ": " + ((String) this.val).toString() : "";
	}

	public boolean containsCall() {
		return Variable.regex.matcher(((String) this.val).toString()).find();
	}
}