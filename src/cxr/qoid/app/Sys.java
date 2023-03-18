package cxr.qoid.app;

import cxr.qoid.Qoid;
import cxr.qoid.canon.Bill;
import cxr.qoid.canon.Property;
import cxr.qoid.canon.Register;
import cxr.qoid.canon.Thing;
import cxr.qoid.canon.Jscon.JsconBill;
import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawFolder;
import cxr.qoid.canon.raw.RawIndex;
import cxr.qoid.canon.raw.RawLine;
import cxr.qoid.canon.string.StringIndex;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class Sys {
	public static final String version = "1.0.2.0";
	public static final String ext = ".cxr";
	public static final int len = ".cxr".length();
	public static final String meta_ext = ".meta";
	public static final int meta_len = ".meta".length();
	public static final String json_ext = ".json";
	public static final int json_len = ".json".length();
	public static final String[] EXT = new String[]{".cxr", ".json"};
	public static final String userDir = System.getProperty("user.dir");
	public static final String fileSep = System.getProperty("file.separator");
	public static final String appData;
	public static final String cfgLoc;
	public static final String cxrSep = "|";
	public static String workspace;
	public static String root;
	public static final Map<String, Object> jsonProperties;

	static {
		appData = System.getenv("localappdata") + fileSep + "CO-OID";
		cfgLoc = appData + fileSep + ".config";
		workspace = userDir;
		root = "";
		jsonProperties = getJsonProperties();
	}

	public static Dimension resolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static boolean isProjectFile(String input) {
		Iterator var2 = Arrays.asList(EXT).iterator();

		while (var2.hasNext()) {
			String each = (String) var2.next();
			if (input.endsWith(each)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isMeta(String input) {
		return input.equals(".meta");
	}

	public static String removeExt(String input) {
		return input.lastIndexOf(46) >= 0 ? input.substring(0, input.lastIndexOf(46)) : input;
	}

	public static boolean save(RawIndex<?> r) {
		String var1;
		switch ((var1 = r.className()).hashCode()) {
			case -1644993116 :
				if (var1.equals("RawFile")) {
					return save((RawFile) r);
				}
				break;
			case -284876138 :
				if (var1.equals("RawFolder")) {
					return save((RawFolder) r);
				}
		}

		return false;
	}

	public static boolean save(RawFile r) {
		try {
			System.out.println(r.getFilePath());
			FileWriter fw = new FileWriter(r.getFilePath());
			Iterator var3 = r.cxr().iterator();

			while (var3.hasNext()) {
				String each = (String) var3.next();
				fw.write(each + Qoid.newline());
			}

			fw.close();
			return true;
		} catch (IOException var4) {
			return false;
		}
	}

	public static boolean save(RawFolder r) {
		(new File(r.getFilePath())).mkdir();
		Iterator var2 = r.iterator();

		while (var2.hasNext()) {
			RawIndex<?> each = (RawIndex) var2.next();
			String var3;
			switch ((var3 = each.className()).hashCode()) {
				case -1644993116 :
					if (!var3.equals("RawFile")) {
						return false;
					}

					save((RawFile) each);
					break;
				case -284876138 :
					if (var3.equals("RawFolder")) {
						save((RawFolder) each);
						break;
					}

					return false;
				default :
					return false;
			}
		}

		save(r.getMeta());
		return true;
	}

	public static boolean saveAt(RawFolder r, String path) {
		RawFolder out = new RawFolder(path, (String) r.tag(), (ArrayList) r.val());
		out.setMeta(r.getMeta());
		out.broadcastPath();
		return save(out);
	}

	public static boolean saveAt(RawFile r, String path) {
		RawFile out = new RawFile(path, (String) r.tag(), (ArrayList) r.val());
		return save(out);
	}

	public static boolean saveAt(Register out, String absolutePath) {
		(new File(absolutePath)).mkdirs();
		Iterator var3 = out.iterator();

		while (var3.hasNext()) {
			StringIndex<?> each = (StringIndex) var3.next();
			String var4;
			switch ((var4 = each.className()).hashCode()) {
				case -625569085 :
					if (var4.equals("Register")
							&& !saveAt((Register) each, absolutePath + fileSep + (String) each.tag())) {
						return false;
					}
					break;
				case 2070567 :
					if (var4.equals("Bill")) {
						try {
							if (!saveAt((Bill) each, absolutePath)) {
								return false;
							}
						} catch (IOException var6) {
							var6.printStackTrace();
							return false;
						}
					}
			}
		}

		return true;
	}

	public static boolean saveAt(Bill b, String absolutePath) throws IOException {
		FileWriter fw = new FileWriter(new File(absolutePath + fileSep + (String) b.tag()) + ".cxr");
		fw.write(b.cxrString());
		fw.close();
		return true;
	}

	public static RawIndex<?> load(String path) {
		File f = new File(path);
		if (f.exists() && !f.isFile()) {
			return (RawIndex) (f.isDirectory() ? loadFolder(path) : new RawFile(path, "PATH_NOT_FOUND"));
		} else {
			return loadFile(path);
		}
	}

	public static RawFile loadFile(String path) {
		File f = new File(path);
		if (f.exists() && !f.isFile()) {
			return new RawFile(f.getAbsolutePath(), "DIRECTORY_WITH_NAME_EXISTS");
		} else if (f.getPath().endsWith(".json")) {
			String tag = f.getName().substring(0, f.getName().length() - 5);
			return RawFile.convert(path, tag, loadJscon(tag, path));
		} else {
			RawFile text = new RawFile(f.getAbsolutePath(), removeExt(f.getName()));

			try {
				f.createNewFile();
				(new File(path)).createNewFile();
				BufferedReader br = new BufferedReader(new FileReader(path));
				String l = null;

				while ((l = br.readLine()) != null) {
					text.add(RawLine.wrap(l));
				}

				br.close();
				return text;
			} catch (IOException var5) {
				return text;
			}
		}
	}

	public static RawFolder loadFolder(String path) {
		System.out.println("Loading " + path);
		File dir = new File(path);
		boolean hasMeta = false;
		if (!dir.exists()) {
			dir.mkdirs();
		}

		RawFolder out = new RawFolder(dir.getAbsolutePath(), removeExt(dir.getName()));
		List<File> list = Arrays.asList(dir.listFiles());
		String name = null;
		Iterator var7 = list.iterator();

		while (var7.hasNext()) {
			File f = (File) var7.next();
			name = f.getName();
			if (isMeta(name)) {
				hasMeta = true;
				out.setMeta(loadFile(f.getAbsolutePath()));
			} else if (isProjectFile(name)) {
				out.add((RawIndex<?>) (f.isDirectory() ? loadFolder(f.getAbsolutePath()) : loadFile(f.getAbsolutePath())));
			}
		}

		if (!hasMeta) {
			String meta_path = path + fileSep + ".meta";

			try {
				(new File(meta_path)).createNewFile();
			} catch (IOException var8) {
				System.out.println("Something went wrong creating the .meta file at " + meta_path);
			}

			out.setMeta(new RawFile(meta_path, ".meta"));
		}

		out.sort();
		return out;
	}

	public static boolean delete(RawIndex<?> r) {
		String var1;
		switch ((var1 = r.className()).hashCode()) {
			case -1644993116 :
				if (var1.equals("RawFile")) {
					return delete((RawFile) r);
				}
				break;
			case -284876138 :
				if (var1.equals("RawFolder")) {
					return delete((RawFolder) r);
				}
		}

		return false;
	}

	public static boolean delete(RawFile r) {
		File f = new File(r.getFilePath());
		return f.delete();
	}

	public static boolean delete(RawFolder r) {
		Iterator var2 = r.iterator();

		while (var2.hasNext()) {
			RawIndex<?> each = (RawIndex) var2.next();
			String var3;
			switch ((var3 = each.className()).hashCode()) {
				case -1644993116 :
					if (var3.equals("RawFile")) {
						delete((RawFile) each);
					}
					break;
				case -284876138 :
					if (var3.equals("RawFolder")) {
						delete((RawFolder) each);
					}
			}
		}

		(new File(r.getFilePath() + fileSep + ".meta")).delete();
		return (new File(r.getFilePath())).delete();
	}

	public static Bill loadJscon(String tag, String path) {
		try {
			JsonParser jp = Json.createParser(new FileReader(path));
			Bill out = new Bill(tag);
			Event var7 = jp.next();

			while (jp.hasNext() && jp.next().toString().equals("KEY_NAME")) {
				Thing spool = new Thing(jp.getString());
				jp.next();
				jp.next();
				ArrayList t = new ArrayList();

				while (!jp.next().toString().equals("END_ARRAY")) {
					t.add(jp.getString());
				}

				jp.next();
				ArrayList v = new ArrayList();

				while (!jp.next().toString().equals("END_ARRAY")) {
					v.add(jp.getString());
				}

				for (int x = 0; x < t.size(); ++x) {
					spool.add(new Property((String) t.get(x), (String) v.get(x)));
				}

				jp.next();
				out.add(spool);
			}

			jp.close();
			return out;
		} catch (IllegalStateException var9) {
			return new Bill("JSON_INVALID_FORMAT_ERROR");
		} catch (ArrayIndexOutOfBoundsException var10) {
			return new Bill("JSON_IMBALANCE_ERROR");
		} catch (FileNotFoundException var11) {
			return new Bill("FILE_NOT_FOUND");
		}
	}

	public static boolean exportJscon(String path, Register r) {
		try {
			(new File(path)).mkdirs();
			Iterator var3 = r.iterator();

			while (var3.hasNext()) {
				StringIndex<?> each = (StringIndex) var3.next();
				String filepath = path + each.relativeFilePath();
				System.out.println(filepath + ".json");
				String var5;
				switch ((var5 = each.className()).hashCode()) {
					case -625569085 :
						if (var5.equals("Register")) {
							(new File(filepath)).mkdir();
							if (!exportJscon(path, (Register) each)) {
								return false;
							}
						}
						break;
					case 2070567 :
						if (var5.equals("Bill")) {
							File f = new File(filepath + ".json");
							if (!f.exists()) {
								f.createNewFile();
							}

							JsonWriter jw = Json.createWriterFactory(jsonProperties).createWriter(new FileWriter(f));
							jw.writeObject((new JsconBill((Bill) each)).pack());
							jw.close();
						}
				}
			}

			return true;
		} catch (IOException var8) {
			var8.printStackTrace();
			return false;
		}
	}

	public static Map<String, Object> getJsonProperties() {
		Map<String, Object> properties = new HashMap(1);
		properties.put("javax.json.stream.JsonGenerator.prettyPrinting", true);
		return properties;
	}
}