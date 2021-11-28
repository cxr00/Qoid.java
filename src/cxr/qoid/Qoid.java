package cxr.qoid;

import java.util.ArrayList;

import cxr.qoid.base.Index;

/**
 * A Qoid is an Index of Properties. Not much else to say about it.
 */
public class Qoid extends Index<Property>{

	public Qoid(String tag) { super(tag); }
	public Qoid(String tag, ArrayList<Property> val) { super(tag, val); }
	
	public String toString() {
		String output;
		// Check if the Qoid is a comment
		if(this.tag().startsWith("/")) {
			output = tag;
		}
		else {
			output = "#" + tag;
		}
		
		for (Property p : this) {
			output += "\n" + p.toString();
		}
		return output;
	}

	public Qoid getAll(String tag) {
		Qoid output = new Qoid(tag + " search results");
		for (Property p : this) {
			if (p.tag().equals(tag)) {
				output.add(p);
			}
		}
		return output;
	}
	
	public Qoid broadcastVariable(String var, String value) {
		Qoid output = new Qoid(this.tag());
		for (Property p : this) {
			output.add(p.broadcastVariable(var, value));
		}
		return output;
	}
}
