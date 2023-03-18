package cxr.qoid.app.ide.toolbar;

public class ToolTips {
	public static String get(String name) {
		switch (name.hashCode()) {
			case -1868626391 :
				if (name.equals("ExportCXR")) {
					return ExportCXR();
				}
				break;
			case -447328219 :
				if (name.equals("ExportJSCON")) {
					return ExportJSCON();
				}
				break;
			case -176660767 :
				if (name.equals("ImportFile")) {
					return ImportFile();
				}
				break;
			case 2404337 :
				if (name.equals("Move")) {
					return Move();
				}
				break;
			case 2569629 :
				if (name.equals("Save")) {
					return Save();
				}
				break;
			case 515654045 :
				if (name.equals("AddFile")) {
					return AddFile();
				}
				break;
			case 1573334181 :
				if (name.equals("ExportProject")) {
					return ExportProject();
				}
				break;
			case 1627841615 :
				if (name.equals("AddFolder")) {
					return AddFolder();
				}
				break;
			case 2033238163 :
				if (name.equals("ImportFolder")) {
					return ImportFolder();
				}
				break;
			case 2043376075 :
				if (name.equals("Delete")) {
					return Delete();
				}
				break;
			case 2126090442 :
				if (name.equals("SetExportLoc")) {
					return SetExportLoc();
				}
		}

		return "Tooltip Not Found";
	}

	private static String AddFile() {
		return "Add a new file to the project in the current folder";
	}

	private static String AddFolder() {
		return "Add a new folder to the project at the current folder";
	}

	private static String Delete() {
		return "Delete the currently-selected file or folder";
	}

	private static String ImportFile() {
		return "Import a cxr or json file from outside the project";
	}

	private static String ImportFolder() {
		return "Import a folder of cxr and/or json files from outside the project";
	}

	private static String ExportProject() {
		return "Export the project in its current, raw format. Great for creating backups";
	}

	private static String ExportCXR() {
		return "Export the project in cxr format. This also pushes macros like #echo";
	}

	private static String ExportJSCON() {
		return "Export the project in JSCON format. The recommended format for your game.";
	}

	private static String Move() {
		return "Move function not implemented yet - stay tuned!";
	}

	private static String SetExportLoc() {
		return "Set the automatic .cxr and .json export location of the project";
	}

	private static String Save() {
		return "Saves the project at the current workspace location";
	}
}