package cxr.qoid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	public static Bill parse(String tag, String text) {
		Bill output = new Bill(tag);
		
		String[] split = text.split("\n");
		
		Qoid spool = null;
		boolean spool_added = false;
		int state = 0;
		
		for (String s : split) {
			if (state == 0) {
				if(s.length() == 0) {
					continue;
				}
				else if(s.charAt(0) == '#') {
					spool = new Qoid(s.substring(1));
					spool_added = false;
					state = 1;
				}
			}
			else {
				if(s.length() == 0) {
					if (!spool_added) {
						output.add(spool);
						spool_added = true;
					}
					state = 0;
				}
				else if (s.charAt(0) == '/') {
					continue;
				}
				else {
					String[] propertySplit = s.split(":");
					if (propertySplit.length > 2) {
						String new_tag = propertySplit[0].trim();
						String new_val = "";
						for (String part : propertySplit) {
							if (part.equals(new_tag)){
								continue;
							}
							else {
								new_val += part.trim();
								if(!part.equals(propertySplit[propertySplit.length - 1])) {
									new_val += ":";
								}
							}
						}
						spool.add(new Property(new_tag, new_val));
					}
					else if (propertySplit.length == 2) {
						spool.add(new Property(propertySplit[0].trim(), propertySplit[1].trim()));
					}
					else {
						spool.add(new Property(propertySplit[0].trim()));
					}
				}
			}
				
		}
		
		if (!spool_added && spool != null){
			output.add(spool);
		}
		
		return output;
	}
	
	public static Bill open(String filepath) throws IOException {
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " is not of type .cxr");
		}
		File file = new File(filepath);
		String tag = file.getName();
		
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		
		ArrayList<String> text = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			text.add(line);
		}
		br.close();
		
		return Bill.parse(tag, String.join("\n", text));
	}

}
