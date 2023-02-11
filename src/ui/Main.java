package ui;

import java.awt.EventQueue;
import java.io.IOException;
import engine.AES;
import outils.Parametres;
import ui.panels.Window;


/**
 * Point d'entrée du logiciel
 * @author Antoine Marchal
 *
 */
public class Main {
	/**
	 * Seuil argument (falcultatif) de l'application : Le chemin du script à ouvrir.
	 */
	public static String arg;
	/**
	 * Démarre le logiciel.
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {
		Parametres.initializeKey();

		if(args.length > 0)arg = args[0];
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


	}

}

