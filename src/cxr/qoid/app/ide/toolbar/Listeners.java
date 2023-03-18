package cxr.qoid.app.ide.toolbar;

import cxr.qoid.app.Sys;
import cxr.qoid.app.ide.NavTree;
import cxr.qoid.app.ide.toolbar.Listeners.AbstractCooidListener;
import cxr.qoid.canon.Register;
import cxr.qoid.canon.raw.RawFile;
import cxr.qoid.canon.raw.RawFolder;
import cxr.qoid.canon.raw.RawIndex;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;

public class Listeners {
	public static ActionListener get(String name, NavTree source) {
		switch (name.hashCode()) {
			case -1868626391 :
				if (name.equals("ExportCXR")) {
					return new ExportCXR(source);
				}
				break;
			case -447328219 :
				if (name.equals("ExportJSCON")) {
					return new ExportJSCON(source);
				}
				break;
			case -176660767 :
				if (name.equals("ImportFile")) {
					return new ImportFile(source);
				}
				break;
			case 2404337 :
				if (name.equals("Move")) {
					return new Move(source);
				}
				break;
			case 2569629 :
				if (name.equals("Save")) {
					return new Save(source);
				}
				break;
			case 515654045 :
				if (name.equals("AddFile")) {
					return new AddFile(source);
				}
				break;
			case 1573334181 :
				if (name.equals("ExportProject")) {
					return new ExportProject(source);
				}
				break;
			case 1627841615 :
				if (name.equals("AddFolder")) {
					return new AddFolder(source);
				}
				break;
			case 2033238163 :
				if (name.equals("ImportFolder")) {
					return new ImportFolder(source);
				}
				break;
			case 2043376075 :
				if (name.equals("Delete")) {
					return new Delete(source);
				}
				break;
			case 2126090442 :
				if (name.equals("SetExportLoc")) {
					return new SetExportLoc(source);
				}
		}

		return null;
	}
	
	public static abstract class AbstractCooidListener implements ActionListener {
		public NavTree source;

		public AbstractCooidListener(NavTree source) {
			this.source = source;
		}

		protected File chooseFile(String filterName, int FileSelectionMode, String dialogMode, String... ext) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File((String) this.source.getConfig("startup", "export").val()));
			chooser.setFileSelectionMode(FileSelectionMode);
			if (FileSelectionMode == 0 && dialogMode != "SAVE") {
				FileNameExtensionFilter filter = new FileNameExtensionFilter(filterName + " " + Arrays.asList(ext), ext);
				chooser.setFileFilter(filter);
			}

			chooser.setAcceptAllFileFilterUsed(false);
			int choice = -1;
			switch (dialogMode.hashCode()) {
				case -1852692228 :
					if (dialogMode.equals("SELECT")) {
						choice = chooser.showOpenDialog(this.source.getParent());
					}
					break;
				case 2432586 :
					if (dialogMode.equals("OPEN")) {
						choice = chooser.showOpenDialog(this.source.getParent());
					}
					break;
				case 2537853 :
					if (dialogMode.equals("SAVE")) {
						choice = chooser.showSaveDialog(this.source.getParent());
					}
			}

			return choice == 0 ? chooser.getSelectedFile() : null;
		}

		protected String enterName(String type) {
			return JOptionPane.showInputDialog(this.source.getParent(), "Enter a name for the new " + type, "New " + type,
					3);
		}
		
		protected boolean validate(String name) {
			// TODO: Refactor this back into something readable
			try {
				boolean notNull = name != null;
				if (!notNull) {
					throw new AssertionError();
				} else {
					boolean notEmpty = name.length() != 0;
					if (!notEmpty) {
						throw new AssertionError();
					} else {
						boolean notAlreadyExists = !this.source.target().contains(name);
						if (!notAlreadyExists) {
							throw new AssertionError();
						} else {
							return true;
						}
					}
				}
			} catch (Exception var5) {
				return false;
			}
		}
	}
	
	public static class AddFile extends AbstractCooidListener {
		public AddFile(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String type = "File";
			String name = this.enterName(type);
			if (this.validate(name)) {
				RawFile toAdd = new RawFile(this.source.target().getFilePath() + Sys.fileSep + name + ".cxr", name);
				this.source.target().add(toAdd);
			}

			this.source.updateUI();
		}
	}
	
	public static class AddFolder extends AbstractCooidListener {
		public AddFolder(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String type = "Folder";
			String name = this.enterName(type);
			if (this.validate(name)) {
				String path = this.source.target().getFilePath() + Sys.fileSep + name + ".cxr";
				RawFolder toAdd = new RawFolder(path, name);
				toAdd.setMeta(new RawFile(path + Sys.fileSep + ".meta", ".meta"));
				this.source.target().add(toAdd);
			}

			this.source.updateUI();
		}
	}
	
	public static class Delete extends AbstractCooidListener {
		public Delete(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			if (this.source.rootFocused()) {
				JOptionPane.showMessageDialog(this.source.getParent(), "Cannot remove root directory.");
			} else {
				String confirm = "Are you sure you want to delete " + (String) this.source.focus().tag() + "?";
				String title = "Delete Complexor " + this.source.focus().alias();
				int choice = JOptionPane.showConfirmDialog(this.source.getParent(), confirm, title, 0);
				if (choice == 0) {
					RawIndex<?> toDelete = this.source.focus();
					this.source.setSelectionPath(this.source.getSelectionPath().getParentPath());
					this.source.focus().remove(toDelete);
					Sys.delete(toDelete);
				}
			}

			this.source.updateUI();
		}
	}
	
	public static class ExportCXR extends AbstractCooidListener {
		public ExportCXR(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String filterName = "Complexor Project (.cxr)";
			File selection = this.chooseFile(filterName, 1, "SAVE", new String[0]);
			if (selection != null) {
				selection.mkdirs();
				Register out = Register.parse(this.source.root()).prepare();
				out.setTag(selection.getName());
				boolean complete = Sys.saveAt(out, selection.getAbsolutePath());
				if (complete) {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Project successfully exported to " + selection.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Something went wrong - the project failed to be exported to " + selection.getAbsolutePath());
				}
			}

		}
	}
	
	public static class ExportJSCON extends AbstractCooidListener {
		public ExportJSCON(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String filterName = "JsCon Project (.json)";
			File selection = this.chooseFile(filterName, 1, "SAVE", new String[0]);
			if (selection != null) {
				selection.mkdirs();
				Register out = Register.parse(this.source.root()).prepare();
				out.setTag(selection.getName());
				boolean complete = Sys.exportJscon(selection.getParent(), out);
				if (complete) {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Project successfully exported to " + selection.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Something went wrong - the project failed to be exported to " + selection.getAbsolutePath());
				}
			}

		}
	}
	
	public static class ExportProject extends AbstractCooidListener {
		public ExportProject(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String filterName = "Raw Project (.cxr)";
			File selection = this.chooseFile(filterName, 1, "SAVE", new String[0]);
			if (selection != null) {
				selection.mkdirs();
				boolean complete = Sys.saveAt(this.source.root(), selection.getAbsolutePath());
				if (complete) {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Project successfully exported to " + selection.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(this.source.getParent(),
							"Something went wrong - the project failed to be exported to " + selection.getAbsolutePath());
				}
			}

		}
	}
	
	public static class ImportFile extends AbstractCooidListener {
		public ImportFile(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String filterName = "Complexor Project files";
			String[] ext = new String[]{"cxr", "convo", "json"};
			File selection = this.chooseFile(filterName, 0, "OPEN", ext);
			if (selection != null) {
				String tag = Sys.removeExt(selection.getName());
				if (selection.getName().endsWith("." + ext[2])) {
					this.source.target()
							.add(RawFile.convert(selection.getPath(), tag, Sys.loadJscon(tag, selection.getPath())));
				} else {
					this.source.target().add(Sys.load(selection.getPath()));
				}
			}

			this.source.updateUI();
		}
	}
	
	public static class ImportFolder extends AbstractCooidListener {
		public ImportFolder(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			String filterName = "Complexor Project files";
			File selection = this.chooseFile(filterName, 1, "OPEN", new String[0]);
			if (selection != null) {
				this.source.target().add(Sys.loadFolder(selection.getPath()));
			}

			this.source.updateUI();
		}
	}
	
	public static class Move extends AbstractCooidListener {
		public Move(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(this.source.getParent(), "Move function not implemented. Stay tuned!");
		}
	}
	
	public static class Save extends AbstractCooidListener {
		public Save(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			RawIndex<?> previous = this.source.focus();
			if (this.source.editor().isChanged()) {
				String var3;
				switch ((var3 = previous.className()).hashCode()) {
					case -1644993116 :
						if (var3.equals("RawFile")) {
							RawFile replace = new RawFile(previous.getFilePath(), (String) previous.tag(),
									this.source.editor().rawText());
							((RawFile) previous).changeTo(replace);
							this.source.editor().resetChangeMarker();
						}
						break;
					case -284876138 :
						if (var3.equals("RawFolder")) {
							((RawFolder) previous).setMeta(new RawFile(((RawFolder) previous).getMeta().getFilePath(),
									".meta", this.source.editor().rawText()));
							this.source.editor().resetChangeMarker();
						}
				}

				System.out.println("Editor changes from save");
			}

			Register out = Register.parse(this.source.root()).prepare();
			boolean saveComplete = Sys.saveAt(this.source.root(), this.source.root().getFilePath());
			String exportLoc = (String) this.source.getConfig("startup", "export").val() + Sys.fileSep + (String) out.tag();
			boolean exportComplete = Sys.saveAt(out, exportLoc);
			exportComplete = exportComplete
					&& Sys.exportJscon((new File(exportLoc)).getParentFile().getAbsolutePath(), out);
			if (!saveComplete && !exportComplete) {
				JOptionPane.showMessageDialog(this.source.getParent(),
						"Something went wrong - retry saving AND exporting.");
			} else if (!saveComplete && exportComplete) {
				JOptionPane.showMessageDialog(this.source.getParent(), "Something went wrong - retry saving.");
			} else if (saveComplete && !exportComplete) {
				JOptionPane.showMessageDialog(this.source.getParent(), "Something went wrong - retry exporting.");
			} else {
				JOptionPane.showMessageDialog(this.source.getParent(), "Save Complete!");
			}

		}
	}
	
	public static class SetExportLoc extends AbstractCooidListener {
		public SetExportLoc(NavTree source) {
			super(source);
		}

		public void actionPerformed(ActionEvent e) {
			File selection = this.chooseFile("Export Location", 1, "SELECT", new String[0]);
			if (selection != null && !selection.getAbsolutePath().equals(this.source.getConfig("startup", "export").val())
					&& !selection.getAbsolutePath().equals(this.source.root().getFilePath())) {
				selection.mkdirs();
				this.source.updateConfig("startup", "export", selection.getAbsolutePath());
				Register out = Register.parse(this.source.root()).prepare();
				out.setTag((new File((String) this.source.getConfig("startup", "export").val())).getName());
				boolean exportComplete = Sys.saveAt(out, (String) this.source.getConfig("startup", "export").val());
				exportComplete = exportComplete
						&& Sys.exportJscon((new File((String) this.source.getConfig("startup", "export").val()))
								.getParentFile().getAbsolutePath(), out);
				if (!exportComplete) {
					JOptionPane.showMessageDialog(this.source.getParent(), "Something went wrong - make sure to Save!");
				}
			}

		}
	}
}