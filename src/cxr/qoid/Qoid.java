package cxr.qoid;

import java.util.ArrayList;

import cxr.qoid.base.Index;

public class Qoid extends Index<Property>{

	public Qoid(String tag) {
		super(tag);
	}
	
	public Qoid(String tag, ArrayList<Property> val) {
		super(tag, val);
	}
	
	public String toString() {
		String output = "#" + tag;
		for (Property p : this) {
			output += "\n" + p.toString();
		}
		return output;
	}

}
