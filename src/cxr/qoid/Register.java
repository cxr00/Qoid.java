package cxr.qoid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cxr.qoid.base.Index;

/**
 * A Register contains both Bills and Registers.
 * 
 * The structure of Registers is virtually identical to the
 * structure of a file system, with the added benefit
 * of tagged elements within each file.
 */
public class Register extends Index<Index<?>>{

	public Register(String tag) {
		super(tag);
	}
	
	public Register(String tag, ArrayList<Index<?>> val) {
		super(tag, val);
	}
	
	@Override
	public boolean add(Index<?> i) {
		// Make sure only Bills and Registers are added
		if (i instanceof Bill || i instanceof Register) {
			return super.add(i);
		}
		else {
			// Fail on other types
			return false;
		}
	}

	public String toString() {
		String output = "// " + tag + "\n";
		for (Index<?> i : this) {
			output += "\n" + i.toString() + "\n";
		}
		return output;
	}

	public Bill getBill(String tag) {
		for (Index<?> i : this) {
			if (i.tag().equals(tag)) {
				if (i.getClass().getSimpleName().equals("Bill")) {
					return (Bill) i;
				}
			}
		}
		return new Bill(tag + " not found");
	}
	
	public Register getRegister(String tag) {
		for (Index<?> i : this) {
			if (i.tag().equals(tag)) {
				if (i.getClass().getSimpleName().equals("Register")) {
					return (Register) i;
				}
			}
		}
		return new Register(tag + " not found");
	}
	
	public Register getAll(String tag) {
		Register output = new Register(tag + " search results");
		for (Index<?> i : this) {
			if (i.tag().equals(tag)) {
				output.add(i);
			}
		}
		return output;
	}
	
	public void broadcastVariable(String var, String value) {
		for (Index<?> i : this) {
			if (i instanceof Register) {
				((Register) i).broadcastVariable(var, value);
			}
			else {
				((Bill) i).broadcastVariable(var, value);
			}
		}
	}

	public static Register open(String filepath) throws IOException {

		File directory = new File(filepath);
		
		if (directory.isFile()){
			throw new IOException("Invalid filepath: " + filepath + " is not a directory");
		}
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " does not end with .cxr");
		}
		
		String tag = directory.getName();
		tag = tag.substring(0, tag.length() - 4);
		
		Register output = new Register(tag);
		
		
		String[] contents = directory.list();
		
		for(String filename : contents) {
			// We only want valid cxr files
			if (filename.endsWith(".cxr")){
				String subfilepath = filepath + "/" + filename;
				File subfile = new File(subfilepath);
				if (subfile.isDirectory()) {
					output.add(Register.open(subfilepath));
				}
				else {
					output.add(Bill.open(subfilepath));
				}
			}
		}
		
		return output;
	}
	
	public void save(String filepath) throws IOException {
		if (!filepath.endsWith(".cxr")) {
			throw new IOException("Invalid file type: " + filepath + " does not end with .cxr");
		}
		File file = new File(filepath);
		if (!file.exists()){
			file.mkdir();
		}
		if (file.isFile()){
			throw new IOException("File already exists: directory " + filepath + " can not be constructed");
		}
		
		for(Index<?> i : this) {
			String subfilepath = filepath + "/" + i.tag() + ".cxr";
			if (i instanceof Bill){
				((Bill) i).save(subfilepath);
			}
			else {
				((Register) i).save(subfilepath);
			}
		}
	}
	
}
