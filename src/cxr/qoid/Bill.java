package cxr.qoid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cxr.qoid.base.Index;

/**
 * A Bill is an Index of Qoids.
 * 
 * Bills can be parsed from text either directly with Bill.parse, or indirectly
 * with Bill.open.
 * 
 * Bills also have basic variable support with the broadcastVariable function.
 */
public class Bill extends Index<Qoid>{
	
	private static boolean DEFAULT_PRESERVE_COMMENTS = true;

	public Bill(String tag) { super(tag); }
	public Bill(String tag, ArrayList<Qoid> val) { super(tag, val); }
	
	public String toString() {
		String output = "/ " + tag + "\n";
		for (Qoid q : this) {
			output += "\n" + q.toString() + "\n";
		}
		return output;
	}

	public Bill getAll(String tag) {
		Bill output = new Bill(tag + " search results");
		for (Qoid q : this) {
			if (q.tag().equals(tag)) {
				output.add(q);
			}
		}
		return output;
	}
	
	public Bill broadcastVariable(String var, String value) {
		Bill output = new Bill(this.tag());
		for (Qoid q : this) {
			output.add(q.broadcastVariable(var, value));
		}
		return output;
	}
	
	public static Bill parse(String tag, String text) { return Bill.parse(tag, text.split("\n")); }
	public static Bill parse(String tag, ArrayList<String> text) { return Bill.parse(tag,  text.toArray(new String[0])); }
	public static Bill parse(String tag, String[] text) { return Bill.parse(tag, text, DEFAULT_PRESERVE_COMMENTS); }
	
	public static Bill parse(String tag, String text, boolean preserveComments) { return Bill.parse(tag, text.split("\n"), preserveComments); }
	public static Bill parse(String tag, ArrayList<String> text, boolean preserveComments) { return Bill.parse(tag,  text.toArray(new String[0]), preserveComments); }
	
	public static Bill parse(String tag, String[] text, boolean preserveComments) {
		Bill output = new Bill(tag);
		
		Qoid spool = null;
		boolean spool_added = false;
		int state = 0;
		
		for (String s : text) {
			// Seeking state
			if (state == 0) {
				// Ignore empty strings
				if(s.length() == 0) {
					continue;
				}
				// If the tag delimiter starts the line, start a new Qoid
				else if(s.charAt(0) == '#') {
					spool = new Qoid(s.substring(1));
					spool_added = false;
					state = 1;
				}
				else if(s.charAt(0) == '/') {
					if (preserveComments) {
						output.add(new Qoid(s));
					}
				}
			}
			// Building state
			else {
				// If empty, end creation of current Qoid
				if(s.length() == 0) {
					if (!spool_added) {
						output.add(spool);
						spool_added = true;
					}
					state = 0;
				}
				// Stores comments and stays with the current Qoid
				else if (s.charAt(0) == '/') {
					if (preserveComments) {
						spool.add(new Property(s));
					}
					continue;
				}
				// Scrape and add the property
				else {
					String[] propertySplit = s.split(":", 2);
					if (propertySplit.length == 2) {
						spool.add(new Property(propertySplit[0].trim(), propertySplit[1].trim()));
					}
					else {
						spool.add(new Property(propertySplit[0].trim()));
					}
				}
			}
				
		}
		
		// In case the end of the text is reached before adding the final Qoid, add it here
		if (!spool_added && spool != null){
			output.add(spool);
		}
		
		return output;
	}
	
	public static Bill open(String filepath) throws IOException {
		return Bill.open(filepath, DEFAULT_PRESERVE_COMMENTS);
	}
	
	public static Bill open(String filepath, boolean preserveComments) throws IOException {
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " is not of type .cxr");
		}
		File file = new File(filepath);
		String tag = file.getName();
		tag = tag.substring(0, tag.length() - 4);
		
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		
		ArrayList<String> text = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			text.add(line);
		}
		br.close();
		
		return Bill.parse(tag, text, preserveComments);
	}
	
	public void save(String filepath) throws IOException {
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " is not of type .cxr");
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
		
		bw.write(this.toString());
		bw.close();
	}

}
