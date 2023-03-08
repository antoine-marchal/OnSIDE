package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.fs.archive.zip.ReadOnlySfxDriver;
import de.schlichtherle.truezip.socket.sl.IOPoolLocator;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.lingala.zip4j.exception.ZipException;
import outils.Parametres;
import ui.panels.Console;
import ui.panels.Editor;
import ui.panels.Window;
/**
 * Classe gérant le mécanisme d'un script Groovy au sein de l'applciation.
 * Le shell groovy est chargé d'executer le script.
 * @author Antoine Marchal
 *
 */
public class ScriptManager {
	/**
	 * Nom du script. Par défaut : "NouveauScript.os"
	 */
	public String filename = "NouveauScript.os";
	/**
	 * Chemin du script.
	 */
	public String pathfile = "";
	/**
	 * Contenu du script
	 */
	public String script = "";
	/**
	 * Contenu du "header"
	 * Le header est en fait un script qui se positionne en téte du script entré par l'utilisateur.
	 * Il regroupe toutes les fonctions utiles pour l'application.
	 */
	public static String header="";
	/**
	 * Thread chargé de l'execution du script
	 */
	public Thread th;
	/**
	 * Object résultant de l'execution du script
	 */
	public Object result;
	/**
	 * Le shell Groovy. Le moteur chargé de la traduction du script.
	 */
	public static GroovyShell shell;
	/**
	 * Le lien Groovy - Java. Il permet d'accéder é la progressBar et é la console depuis un script Groovy.
	 */
	private static Binding binding = new Binding();
	private static URLClassLoader childcl;
	private Window window;
	static{
		binding.setVariable("textArea_c",Console.textArea);
		binding.setVariable("progressBar",Console.progressBar);

		refreshChildCL();



		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("lib/header_encoded.os");
			header=AES.decrypt(TextAreaPrintStream.convertStreamToString(inputStream));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<File> extractEmbeddedLibrairies(String pattern) throws ZipException, IOException {
		
		TConfig.get().setArchiveDetector(new de.schlichtherle.truezip.file.TArchiveDetector(de.schlichtherle.truezip.file.TArchiveDetector.NULL,"exe",new ReadOnlySfxDriver(IOPoolLocator.SINGLETON)));
		String executableName = new File(System.getProperty("java.class.path")).getName();
		List<File> jarFiles = new ArrayList<>();
		TFile archive = new TFile(new File(executableName).getAbsolutePath());
		archive.setReadOnly();

		TFile[] entries = null ;
		TFile[] rootEntries = archive.listFiles();
		if(rootEntries != null) {
			for (TFile entry : rootEntries) {
				
				if(entry.getName().contains(pattern.substring(0,pattern.length()-1)))entries=entry.listFiles();
			}
			if(entries!=null) {
				new File(pattern).mkdir();
				for (TFile entry : entries) {
					String entryName = entry.getName();
					if (entryName.endsWith(".jar")) {
						File outputFile = new File(pattern+entryName);
						entry.toNonArchiveFile().cp(outputFile);
						jarFiles.add(outputFile);
					}
				}

			}
		}
		return jarFiles;
	}


	private static List<File> embeddedLibrairies = new ArrayList<File>(0);
	public static void refreshChildCL(){

		ArrayList<URL> tab_url = new ArrayList<URL>();
		String pattern = "onside_lib/";
		try {
			embeddedLibrairies=extractEmbeddedLibrairies(pattern);
		} catch (ZipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File dir = new File("onside_lib/");
		if(dir.exists()){
			File[] tab_file = dir.listFiles(new FilenameFilter(){
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".jar");
				}
			});

			for(int i = 0; i< tab_file.length;i++){
				try{
					tab_url.add(tab_file[i].toURI().toURL());
				}catch(Exception e){}
			}
			if(tab_url.size()>0){
				System.out.println("Librairies chargées :");
				for(URL u : tab_url){
					System.out.println("\t"+u.getFile().substring(u.getFile().lastIndexOf("/")+1, u.getFile().length()-4));
				}			
			}
		}
		URL[] ar_url = tab_url.toArray(new URL[tab_url.size()]);
		childcl = new URLClassLoader(ar_url,ScriptManager.class.getClassLoader());
		shell=new GroovyShell(childcl,binding);
		
		//Remove the embeddedLibrairies at the end of the execution and the folder if it was created for that purpose
	
		if(embeddedLibrairies.size()>0) {
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		        	try {
						childcl.close();
						shell.getClassLoader().close();
						for(File f : embeddedLibrairies) {
							f.delete();
						}
						if(dir.listFiles().length==0)dir.delete();
					} catch (Exception e) {
					}
		        }
			}, "Shutdown-thread"));
			
		}
	}
	/**
	 * Constructeur.
	 * @param window : Le scriptManager est parent é la fenétre principale de l'application
	 */
	public ScriptManager(Window window){
		this.window=window;

	}
	/**
	 * Ouvre un script OnSIDE (Groovy)
	 * (Le décode et l'affiche sur l'éditeur)
	 * @param f : Le fichier script OnSIDE
	 */
	public void openScript(File f){
		filename=f.getName();
		pathfile=f.getPath();
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] data = new byte[(int)f.length()];
			fis.read(data);
			fis.close();
			Editor.textArea.setText(AES.decrypt(new String(data, "UTF-8")));
		} catch (Exception e1) {
			e1.printStackTrace();
		}					
		window.refreshTitle();
	}
	/**
	 * Execute le script courant et affiche le résultat sur la console.
	 * NB : La console est un "pipe" de la console Java par défaut.
	 */
	public void lancerScript(){
		refreshChildCL();

		Console.progressBar.setValue(0);
		Console.progressBar.setVisible(false);
		window.console.lancerChrono();

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
		// Header
		script=header+"\n"+script;

		// Decrypter
		do{
			foundone=false;
			m= Parametres.pattern_encrypted.matcher(script);
			while(m.find()){
				foundone=true;
				tmp = m.group();
				script= (script.replace(tmp, AES.decrypt(tmp.split("::")[1].replaceFirst(".$",""))));
			}
		}while(foundone);

		window.cl.next(window.frame.getContentPane());
		window.frame.validate();
		th = new Thread(){
			public void run(){
				try{
					result = shell.evaluate(script);
				}catch(Exception e){
					result=e;
				}
				if(result!=null)Console.textArea.append((Console.textArea.getText().equals("")?"":"\n")+"RESULT:\n"+result.toString());
				Console.textArea.append("\nScript terminé en : "+window.console.arreterChrono()+"\n");
			}
		};
		th.start();
		window.mb.setVisible(false);
	}
}
