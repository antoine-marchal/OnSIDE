package ui.panels;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;



import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.CollapseAllFoldsAction;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.ExpandAllFoldsAction;

import ui.aPropos;
import engine.AES;
import engine.Export;
import engine.TextAreaPrintStream;
import outils.Parametres;
/**
 * Classe MenuBar
 * Comporte les menus utiles à l'application
 * @author Antoine Marchal
 */
public class MenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;

	private Window window;
	private JFileChooser open,save;
	private FileNameExtensionFilter filterOS,filterEXE;
	/**
	 * MenuItem Enregistrer (Visible par la zone d'édition)
	 */
	public JMenuItem mntmEnregistrer;
	/**
	 * Créé la barre de menu. Est parent de la fenetre principale de l'application. S'y ajoute elle méme.
	 * @param window
	 */
	public MenuBar(Window window){
		this.window=window;
		window.frame.setJMenuBar(this);

		initializeFileChoosers();
		initializeMenuFichier();
		initializeMenuAffichage();
		initializeItemAPropos();


	}
	/**
	 * Initialise les File Choosers (Sauvegarde et Ouverture de fichier)
	 */
	private void initializeFileChoosers(){
		open = new JFileChooser(new File("exemples/"));
		filterOS = new FileNameExtensionFilter("OS files", "os");
		filterEXE = new FileNameExtensionFilter("EXE files", "exe");

		save = new JFileChooser();
		open.setFileFilter(filterOS);
		save.setFileFilter(filterOS);
	}
	/**
	 * Initialise l'entrée "A propos"
	 */
	private void initializeItemAPropos(){
		JMenuItem mntmPropos = new JMenuItem("\u00E0 propos");
		mntmPropos.setMaximumSize(new Dimension(150,100));
		mntmPropos.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/text_replace.png")));
		mntmPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							aPropos frame = new aPropos();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

		mntmPropos.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(mntmPropos);
	}
	/**
	 * Initialise le Menu Affichage :
	 * - Afficher/Masquer Bibliothéque (AAD)
	 * - Plier/Déplier Tout (sur la zone d'édition)
	 */
	private void initializeMenuAffichage(){
		JMenu mnAffichage = new JMenu("Affichage");
		this.add(mnAffichage);

		//Item Afficher/Masquer é redéfinir avec le nouveau splitpane
		//		final JMenuItem mntmAfficherMasquer = new JMenuItem((window.editor.aad.jsp.isVisible()?"Masquer":"Afficher")+ " Biblioth\u00E8que");
		//		mntmAfficherMasquer.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/"+(window.editor.aad.jsp.isVisible()?"arrow_undo.png":"arrow_redo.png"))));
		//		mntmAfficherMasquer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		//		mntmAfficherMasquer.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent arg0) {
		//				window.editor.aad.jsp.setVisible(!window.editor.aad.jsp.isVisible());
		//				mntmAfficherMasquer.setText((window.editor.aad.jsp.isVisible()?"Masquer":"Afficher")+ " Biblioth\u00E8que");
		//				mntmAfficherMasquer.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/"+(window.editor.aad.jsp.isVisible()?"arrow_undo.png":"arrow_redo.png"))));
		//				MigLayout mg =(MigLayout)window.editor.getLayout();
		//				mg.setColumnConstraints(window.editor.aad.jsp.isVisible()?"[300:n:300,left][grow]":"[0:n:0,left][grow]");
		//				window.editor.repaint();
		//				window.editor.validate();
		//			}
		//		});

		//Item Plier Tout
		JMenuItem mntmPlierTout = new JMenuItem("Plier tout");

		mntmPlierTout.setSelected(true);
		mntmPlierTout.setHorizontalAlignment(SwingConstants.LEFT);
		mntmPlierTout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmPlierTout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 1;i<window.editor.aad.tree.getRowCount();i++)window.editor.aad.tree.collapseRow(i);			
				CollapseAllFoldsAction ca = new CollapseAllFoldsAction();
				ca.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			}
		});
		mntmPlierTout.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/bullet_green.png")));

		//Item Déplier Tout
		JMenuItem mntmDeplierTout = new JMenuItem("Déplier tout");
		mntmDeplierTout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmDeplierTout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				ExpandAllFoldsAction ca = new ExpandAllFoldsAction();
				ca.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			}
		});
		mntmDeplierTout.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/bullet_green.png")));
		mntmDeplierTout.setSelected(true);
		mntmDeplierTout.setHorizontalAlignment(SwingConstants.LEFT);


		//Ajout items
		//mnAffichage.add(mntmAfficherMasquer);
		//mnAffichage.add(new JSeparator());
		mnAffichage.add(mntmPlierTout);
		mnAffichage.add(mntmDeplierTout);
	}
	/**
	 * Initialise le Menu Fichier
	 * - Nouveau Script
	 * - Ouvrir Script
	 * - Enregistrer
	 * - Enregistrer Sous
	 * - Exporter (EXE) Sous
	 * - Quitter l'application
	 */
	private void initializeMenuFichier(){
		JMenu mnFichier = new JMenu("Fichier");
		this.add(mnFichier);



		//Item Ouvrir
		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mntmOuvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = open.showOpenDialog(open);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File f=open.getSelectedFile();
					window.sm.openScript(f);
				}
			}
		});
		mntmOuvrir.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/folder_page.png")));


		//Item Enregistrer
		mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmEnregistrer.setEnabled(false);
		mntmEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileOutputStream fos=new FileOutputStream(new File(window.sm.pathfile));
					byte[] data = AES.encrypt(Editor.textArea.getText()).getBytes("UTF-8");
					fos.write(data);
					fos.close();
					mntmEnregistrer.setEnabled(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}



			}
		});
		mntmEnregistrer.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/disk.png")));

		//Item Nouveau
		JMenuItem mntmNouveau = new JMenuItem("Nouveau");
		mntmNouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmEnregistrer.setEnabled(false);
				Editor.textArea.setText("");
				window.sm.filename = "NouveauScript.os";
				window.sm.pathfile ="";
				window.refreshTitle();
			}
		});
		mntmNouveau.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/page.png")));

		//Item Enregistrer Sous
		JMenuItem mntmEnregistrerSous = new JMenuItem("Enregistrer sous ...");
		mntmEnregistrerSous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmEnregistrerSous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = save.showSaveDialog(save);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File f=save.getSelectedFile();
					window.sm.pathfile=f.getPath();
					window.sm.filename=f.getName();
					if(!window.sm.pathfile.contains(".os")){
						window.sm.pathfile+=".os";
						window.sm.filename+=".os";
					}

					try {
						FileOutputStream fos=new FileOutputStream(new File(window.sm.pathfile));
						byte[] data = AES.encrypt(Editor.textArea.getText()).getBytes("UTF-8");
						fos.write(data);
						fos.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
					window.refreshTitle();
				}
			}
		});
		mntmEnregistrerSous.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/page_paste.png")));

		//Item Exporter Sous
		JMenuItem mntmExporterSous = new JMenuItem("Exporter script en .exe ...");
		mntmExporterSous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmExporterSous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					public void run() {

						try {
							window.console.lancerChrono();
							Console.progressBar.setVisible(false);
							window.cl.next(window.frame.getContentPane());
							window.frame.validate();

							window.mb.setVisible(false);
							File fscript = new File("data_lib/script.os");
							FileOutputStream fos=new FileOutputStream(fscript);
							String script = Editor.textArea.getText();
							Matcher m; 
							String tmp,match;
							boolean foundone;
							ArrayList<String> modules = new ArrayList<String>();
							// Import modules
							do{
								foundone=false;
								m= Parametres.pattern_module.matcher(script);
								while(m.find()){
									foundone=true;
									tmp = m.group();
									if(!modules.contains(tmp)){
										modules.add(tmp);
										script=script.replace(tmp,"");
										match=tmp.split("::")[1].replaceFirst(".$","");
										InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("modules/"+match+".os");
										try {
											tmp=TextAreaPrintStream.convertStreamToString(inputStream);
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										if(tmp.isEmpty() || tmp==null || tmp==""){
											try{
												inputStream=new FileInputStream("modules/"+match+".os");
												tmp=TextAreaPrintStream.convertStreamToString(inputStream);
											}catch(Exception e){
												e.printStackTrace();
											}
										}
										if(tmp.isEmpty() || tmp==null || tmp==""){

											try{
												inputStream=new FileInputStream(match+".os");
												tmp=TextAreaPrintStream.convertStreamToString(inputStream);
											}catch(Exception e){
												e.printStackTrace();
											}
										}
										script=AES.decrypt(tmp)+"\n"+script;
									}
								}
							}while(foundone);
							
							byte[] data = AES.encrypt(script).getBytes("UTF-8");
							fos.write(data);
							fos.close();

							String title = JOptionPane.showInputDialog(window.frame, "Quel est le titre de votre application ?", "Saisie du titre", 1);
							Console.textArea.append("Titre de l'application : '"+title+"'\n");
							Console.textArea.append("Export en cours ... Patientez ...\n");
							if(title!=null){
								save.setFileFilter(filterEXE);
								int returnVal = save.showSaveDialog(save);

								if(returnVal == JFileChooser.APPROVE_OPTION) {

									File exeDestination=save.getSelectedFile();
									if(!exeDestination.getAbsolutePath().toLowerCase().contains(".exe"))exeDestination=new File(exeDestination.getAbsolutePath()+".exe");
									Export.exportExe(exeDestination, title, fscript);

								}
								save.setFileFilter(filterOS);
							}
							fscript.delete();
						} catch (Exception e1) {
							Console.textArea.append(e1.toString());
						}
						window.console.arreterChrono();
					}
				}.start();
			}
		});
		mntmExporterSous.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/script_go.png")));

		//Item Quitter
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmQuitter.setIcon(new ImageIcon(MenuBar.class.getResource("/groovy/ui/icons/cross.png")));

		//Ajout des items
		mnFichier.add(mntmNouveau);
		mnFichier.add(mntmOuvrir);
		mnFichier.add(new JSeparator());
		mnFichier.add(mntmEnregistrer);
		mnFichier.add(mntmEnregistrerSous);
		mnFichier.add(mntmExporterSous);
		mnFichier.add(new JSeparator());
		mnFichier.add(mntmQuitter);
	}
}
