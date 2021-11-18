package cxr.qoid;

import java.util.ArrayList;

import cxr.qoid.base.Index;

public class Register extends Index<Index<?>>{

	public Register(String tag) {
		super(tag);
	}
	
	public Register(String tag, ArrayList<Index<?>> val) {
		super(tag, val);
	}

	public String toString() {
		String output = "// " + tag + "\n";
		for (Index<?> i : this) {
			output += "\n" + i.toString() + "\n";
		}
		return output;
	}

}
