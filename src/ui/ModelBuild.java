package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.UIManager;

import outils.Parametres;
import ui.panels.Console;
import ui.panels.Window;
import engine.AES;
import engine.ScriptManager;
import engine.TextAreaPrintStream;
/**
 * La Classe de l'application autonome. Hérite de l'ui de base.
 * @author Antoine Marchal
 *
 */
public class ModelBuild extends Window{
	/**
	 * Démarre l'application autonome.
	 */
	public static void main(String[] args) {
		Parametres.initializeKey();
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ModelBuild();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	TrayIcon trayIcon;
	SystemTray tray;
	Toolkit tk;
	/**
	 * Créé l'ui simplifiée de l'application autonome.
	 */
	public ModelBuild(){
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	}
	/**
	 * Ouvre le script embarqué.
	 * @throws Exception
	 */
	private void openScript() throws Exception{
		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("lib/script.os");
		sm.script=AES.decrypt(TextAreaPrintStream.convertStreamToString(fis));
		fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("lib/title.txt");
		String titl = AES.decrypt(TextAreaPrintStream.convertStreamToString(fis));
		frame.setTitle(titl);
		if(SystemTray.isSupported())trayIcon.setToolTip(titl);
	}
	/**
	 * Initialise la configuration de l'ui et lance le script embarqué.
	 * @throws Exception
	 */
	private void initialize() throws Exception {
		tk = Toolkit.getDefaultToolkit();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		sm=new ScriptManager(this);

		frame = new JFrame();

		if(SystemTray.isSupported()){
			//System.out.println("system tray supported");
			tray=SystemTray.getSystemTray();

			Image image=tk.getImage(Window.class.getResource("/lib/icone.png"));
			ActionListener exitListener=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting....");
					System.exit(0);
				}
			};
			PopupMenu popup=new PopupMenu();
			MenuItem defaultItem=new MenuItem("Quitter");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);
			defaultItem=new MenuItem("Ouvrir");
			ActionListener openListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//tray.remove(trayIcon);
					frame.setVisible(true);
					frame.setExtendedState(JFrame.NORMAL);
				}
			};
			defaultItem.addActionListener(openListener);
			popup.add(defaultItem);
			trayIcon=new TrayIcon(image, "OnSIDE", popup);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(openListener);
			
			frame.addWindowStateListener(new WindowStateListener() {
				public void windowStateChanged(WindowEvent e) {
					if(e.getNewState()==JFrame.ICONIFIED){
						try {
							//tray.add(trayIcon);
							frame.setVisible(false);
							//System.out.println("added to SystemTray");
						} catch (Exception ex) {
							//System.out.println("unable to add to tray");
						}
					}
					if(e.getNewState()==7){
						try{
							//tray.add(trayIcon);
							frame.setVisible(false);
							//System.out.println("added to SystemTray");
						}catch(Exception ex){
							//System.out.println("unable to add to system tray");
						}
					}
					if(e.getNewState()==JFrame.MAXIMIZED_BOTH){
						//tray.remove(trayIcon);
						frame.setVisible(true);
						//System.out.println("Tray icon removed");
					}
					if(e.getNewState()==JFrame.NORMAL){
						//tray.remove(trayIcon);
						frame.setVisible(true);
						//System.out.println("Tray icon removed");
					}
				}
			});
			frame.setIconImage(tk.getImage(Window.class.getResource("/lib/icone.png")));
			Dimension screen = tk.getScreenSize();
			frame.setBounds((int)Math.round(screen.width*0.1), (int)Math.round(screen.height*0.1), (int)Math.round(screen.width*0.6), (int)Math.round(screen.height*0.6));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			tray.add(trayIcon);
			frame.setVisible(false);
		}else{
			frame.setIconImage(tk.getImage(Window.class.getResource("/lib/icone.png")));
			frame.setBounds(100, 100, 1024, 700);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}


		openScript();


		cl = new CardLayout(0, 0);
		frame.getContentPane().setLayout(cl);

		console = new Console(this);
		console.btnRevenir.setVisible(false);

		System.setOut(new TextAreaPrintStream(Console.textArea,System.out));
		sm.lancerScript();


	}

}