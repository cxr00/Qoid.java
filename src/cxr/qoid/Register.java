package cxr.qoid;

import java.io.File;
import java.io.IOException;
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

	public static Register open(String tag, String filepath) throws IOException {
		Register output = new Register(tag);
		
		File directory = new File(filepath);
		
		if (directory.isFile()){
			throw new IOException("Invalid filepath: " + filepath + " is not a directory");
		}
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " does not end with .cxr");
		}
		
		String[] contents = directory.list();
		
		for(String filename : contents) {
			if (filename.endsWith(".cxr")){
				String subtag = filename.substring(0, filename.length() - 4);
				String subfilepath = filepath + "/" + filename;
				File subfile = new File(subfilepath);
				if (subfile.isDirectory()) {
					output.add(Register.open(subtag, subfilepath));
				}
				else {
					output.add(Bill.open(subfilepath));
				}
			}
		}
		
		return output;
	}
	
}
