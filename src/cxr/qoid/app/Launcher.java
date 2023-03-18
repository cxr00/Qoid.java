package cxr.qoid.app;

import cxr.qoid.canon.Bill;
import cxr.qoid.canon.Property;
import cxr.qoid.canon.Thing;
import cxr.qoid.canon.raw.RawFile;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

public class Launcher extends JPanel implements ActionListener {
	private static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	static final int launch_x = 760;
	static final int launch_window_y = 75;
	static final int window_x;
	static final int window_y;
	Bill cfgContainer;
	Thing cfg;
	JTextField Loc;
	JButton Launch;
	JButton Cancel;
	JButton Browse;
	JButton Current;
	JFileChooser Select;

	static {
		window_x = (int) screen.getWidth() / 3 * 2;
		window_y = (int) screen.getHeight() - 90;
	}

	public Launcher() throws IOException {
		this.setLayout(new FlowLayout());
		ToolTipManager.sharedInstance().setInitialDelay(125);
		this.validateConfig();
		Sys.workspace = (String) ((Property) this.cfg.get("workspace")).val();
		this.Loc = new JTextField();
		this.Loc.setPreferredSize(new Dimension(200, 25));
		this.Loc.setText(Sys.workspace + Sys.fileSep + (String) ((Property) this.cfg.get("root")).val());
		System.out.println(this.Loc.getText());
		this.Loc.setFocusable(false);
		this.add(this.Loc);
		this.Launch = new JButton("Launch Qoid");
		this.Launch.setActionCommand("Launch");
		this.Launch.setToolTipText("Start Qoid in " + this.Loc.getText());
		this.Launch.addActionListener(this);
		this.add(this.Launch);
		this.Browse = new JButton("Change Workspace");
		this.Browse.setActionCommand("Browse");
		this.Browse.setToolTipText("Select a different folder to load in Qoid");
		this.Browse.addActionListener(this);
		this.add(this.Browse);
		this.Current = new JButton("Use Current Directory");
		this.Current.setActionCommand("Current");
		this.Current.setToolTipText("Use " + Sys.userDir + " as the Qoid workspace");
		this.Current.addActionListener(this);
		this.add(this.Current);
		this.Cancel = new JButton("Cancel");
		this.Cancel.setActionCommand("Cancel");
		this.Cancel.setToolTipText("Don't launch Qoid");
		this.Cancel.addActionListener(this);
		this.add(this.Cancel);
	}

	private JFrame getWindow() {
		return (JFrame) SwingUtilities.getWindowAncestor(this);
	}

	private void validateConfig() throws IOException {
		(new File(Sys.appData)).mkdirs();
		File init = new File(Sys.cfgLoc);
		if (!init.exists()) {
			this.createConfig(init);
		}

		this.cfgContainer = Bill.parse("Config", Sys.loadFile(Sys.cfgLoc).cxr());
		this.cfg = (Thing) this.cfgContainer.get("startup");
		Property v = (Property) this.cfg.get("version");
		if (v == null || !((String) v.val()).equals("1.0.2.0")) {
			this.updateConfig(this.cfg);
		}

		System.out.println(this.cfg.cxrString());
	}

	private void createConfig(File init) throws IOException {
		File u = new File(Sys.userDir);
		init.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(init));
		bw.write("#startup");
		bw.newLine();
		bw.write("version: 1.0.2.0");
		bw.write("workspace: " + u.getParent());
		bw.newLine();
		bw.write("root: " + u.getName());
		bw.write("export: " + u.getParentFile().getAbsolutePath() + Sys.fileSep + u.getName() + "_export");
		bw.close();
	}

	private void updateConfig(Thing cfg) {
		if (cfg.contains("version")) {
			((Property) cfg.get("version")).setVal("1.0.2.0");
		} else {
			cfg.add(new Property("version", "1.0.2.0"));
		}

		if (!cfg.contains("export")) {
			cfg.add(new Property("export", (String) ((Property) cfg.get("workspace")).val() + Sys.fileSep
					+ (String) ((Property) cfg.get("root")).val() + "_export"));
		}

		Sys.save(RawFile.convert(Sys.cfgLoc, ".config", this.cfgContainer));
	}

	public void actionPerformed(ActionEvent a) {
		String var2;
		switch ((var2 = a.getActionCommand()).hashCode()) {
			case -2025975853 :
				if (var2.equals("Launch")) {
					if ((new File(this.Loc.getText())).exists()) {
						String[] path = this.Loc.getText().split(Pattern.quote(Sys.fileSep));
						String wk = path[0];

						String s;
						for (Iterator var6 = Arrays.asList(path).subList(1, path.length - 1).iterator(); var6
								.hasNext(); wk = wk + Sys.fileSep + s) {
							s = (String) var6.next();
						}

						this.cfg.set(0, new Property("workspace", wk));
						this.cfg.set(1, new Property("root", path[path.length - 1]));
						Sys.save(RawFile.convert(Sys.cfgLoc, (String) this.cfgContainer.tag(), this.cfgContainer));
						this.getWindow().dispose();
						Sys.workspace = (String) ((Property) this.cfg.get("workspace")).val();
						Sys.root = (String) ((Property) this.cfg.get("root")).val();
						this.createApplicationWindow(Sys.workspace + Sys.fileSep + Sys.root);
					} else {
						JOptionPane.showMessageDialog(this,
								"Invalid file name - use Choose Workspace to select or create the folder!");
					}
				}
				break;
			case -1503373991 :
				if (var2.equals("Current")) {
					this.Loc.setText(System.getProperty("user.dir"));
				}
				break;
			case 1998230186 :
				if (var2.equals("Browse") && this.chooseFile() == 0) {
					this.Loc.setText(this.Select.getSelectedFile().getPath());
					this.Launch.setToolTipText("Start Qoid in " + this.Loc.getText());
				}
				break;
			case 2011110042 :
				if (var2.equals("Cancel")) {
					System.exit(0);
				}
		}

	}

	private void createApplicationWindow(String root) {
		IDE app = new IDE(root, this.cfgContainer);
		app.setIconImage((new ImageIcon("icon.png")).getImage());
		app.setDefaultCloseOperation(3);
		app.setFocusable(true);
		app.setSize(Sys.resolution().width, Sys.resolution().height - 50);
		app.setVisible(true);
		app.setTitle("Qoid v1.0.2.0 by Conrad Complexor");
	}

	private int chooseFile() {
		this.Select = new JFileChooser(Sys.workspace);
		this.Select.setAcceptAllFileFilterUsed(false);
		this.Select.setFileSelectionMode(1);
		this.Select.addActionListener(this);
		return this.Select.showOpenDialog(this);
	}

	public static void main(String[] args) throws IOException {
		JFrame launchWindow = new JFrame("Qoid v1.0.2.0 Launcher - Select or create your workspace!");
		launchWindow.setDefaultCloseOperation(3);
		launchWindow.setIconImage((new ImageIcon("cxr.png")).getImage());
		launchWindow.setResizable(false);
		launchWindow.setSize(760, 75);
		launchWindow.setLocation((int) (screen.getWidth() - 760.0D) / 2, (int) (screen.getHeight() - 75.0D) / 2);
		Launcher L = new Launcher();
		launchWindow.add(L);
		launchWindow.setVisible(true);
	}
}