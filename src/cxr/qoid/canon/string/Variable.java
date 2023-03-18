package cxr.qoid.canon.string;

import java.util.regex.Pattern;

import cxr.qoid.canon.Property;

public class Variable extends AbstractStringMacro {
	public static char delimiter = '@';
	public static Pattern regex = Pattern.compile("@.*@");

	public Variable(String tag) {
		super(tag);
	}

	public Variable(String tag, String val) {
		super(tag, val.trim());
	}

	public char delimiter() {
		return delimiter;
	}

	public static boolean validate(String s) {
		return s.startsWith("@") && s.contains(":");
	}

	public static Variable validateAndScrape(String s) {
		if (validate(s)) {
			String[] spl = s.split(":", 2);
			return new Variable(spl[0].substring(1), spl[1]);
		} else {
			return null;
		}
	}

	public void replaceCall(String var, String with) {
		Pattern r = Pattern.compile(delimiter + var + delimiter);
		this.val = r.matcher((CharSequence) this.val).replaceAll(with);
	}

	public String getNextCall() {
		int firstIndex = ((String) this.val).indexOf(delimiter) + 1;
		int secondIndex = ((String) this.val).indexOf(delimiter, firstIndex);
		return ((String) this.val).substring(firstIndex, secondIndex);
	}

	public static String getNextCall(Property p) {
		int firstIndex = ((String) p.val()).indexOf(delimiter) + 1;
		int secondIndex = ((String) p.val()).indexOf(delimiter, firstIndex);
		return ((String) p.val()).substring(firstIndex, secondIndex);
	}
}