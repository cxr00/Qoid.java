package cxr.qoid;

import java.util.ArrayList;

import cxr.qoid.base.Index;

public class Bill extends Index<Qoid>{

	public Bill(String tag) {
		super(tag);
	}
	
	public Bill(String tag, ArrayList<Qoid> val) {
		super(tag, val);
	}

	public String toString() {
		String output = "/ " + tag + "\n";
		for (Qoid q : this) {
			output += "\n" + q.toString() + "\n";
		}
		return output;
	}

}
