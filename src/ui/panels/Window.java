package ui.panels;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import engine.ScriptManager;
import engine.TextAreaPrintStream;
import outils.Parametres;
import ui.Main;
/**
 * Fenétre principale de l'application
 * Parent de tous les éléments ayant un lien avec l'ui.
 * @author Antoine Marchal
 *
 */
public class Window {
	/**
	 * Frame de l'application
	 */
	public JFrame frame;
	/**
	 * Le Script Manager
	 */
	public ScriptManager sm;
	/**
	 * Le panel Editeur
	 */
	public Editor editor;
	/**
	 * Le Panel Console
	 */
	public Console console;
	/**
	 * La Barre de menu
	 */
	public MenuBar mb;
	/**
	 * Le Layout de la frame : CardLayout. Un Panel en cache un autre. (Editeur / Console)
	 */
	public CardLayout cl;

	/**
	 * Initialise la frame.
	 * Ouvre le script, si le point d'entrée comporte l'argument contenant le chemin du script é ouvrir.
	 */
	public Window(){
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Main.arg!=null)sm.openScript(new File(Main.arg));
		//frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	}
	/**
	 * Rafraichit le titre de l'application en fonction du nom du script en court de traitement.
	 */
	public void refreshTitle(){
		frame.setTitle(Parametres.title+" - "+sm.filename);
	}
	/**
	 * Initialise et configure la frame. Ajoute les différents éléments.
	 * - Editeur
	 * - Console
	 * - Barre de menu
	 * 
	 * Applique le Pipe "System.setOut(new TextAreaPrintStream(Console.textArea,System.out));"
	 * @throws Exception
	 */
	private void initialize() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		sm=new ScriptManager(this);
		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/lib/icone.png")));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds((int)Math.round(screen.width*0.1), (int)Math.round(screen.height*0.1), (int)Math.round(screen.width*0.6), (int)Math.round(screen.height*0.6));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		refreshTitle();
		
		
		
		cl = new CardLayout(0, 0);
		frame.getContentPane().setLayout(cl);
		
		editor = new Editor(this);
		console = new Console(this);
		
		mb=new MenuBar(this);
		
		System.setOut(new TextAreaPrintStream(Console.textArea,System.out));
	}
}
