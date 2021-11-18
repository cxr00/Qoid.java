package cxr.qoid;

import cxr.qoid.base.Qoid;

public class Property extends Qoid<String>{

	public Property(String tag) { super(tag); }
	public Property(String tag, String val) { super(tag, val); }

	public String toString() {
		String first = tag;
		boolean cond = val().length() > 0;
		String mid = cond ? ": " : "";
		String last = cond ? val : "";
		
		return first + mid + last;
	}
	
	public String val() { return val != null ? val : ""; }

}
