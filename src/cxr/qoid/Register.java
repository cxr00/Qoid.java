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
