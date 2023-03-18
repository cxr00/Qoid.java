package cxr.qoid.canon;

import java.util.regex.Pattern;

import cxr.qoid.canon.string.AbstractStringProperty;
import cxr.qoid.canon.string.Variable;

public class Property extends AbstractStringProperty {
	public Property(String tag) {
		super(tag, "");
	}

	public Property(String tag, String val) {
		super(tag, val);
	}

	public void replaceCall(String var, String with) {
		Pattern r = Pattern.compile(Variable.delimiter + var + Variable.delimiter);
		this.val = r.matcher((CharSequence) this.val).replaceAll(with);
	}
}