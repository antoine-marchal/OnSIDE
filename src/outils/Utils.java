package outils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
/**
 * Classe Utils:
 * Le couteau suisse du logiciel: regroupe des fonctions diverses et utiles pour le logiciel.
 * @author Antoine Marchal
 *
 */
public class Utils {
	/**
	 * Point d'entrée pour tester les fonctions
	 */
	public static void main(String[] args) throws Exception{
		File xml = new File("modules");
		System.out.println(generateTree(xml));	    
	    	   
	}


	
	public static DefaultTreeModel generateTree(File moduledirectory) throws Exception{
		return generateTree(Arrays.asList(moduledirectory.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".os");
			}
		})));
	}
	/**
	 * Génére un arbre d'arborescence é partir d'une liste de fichiers XML du type Aide Au Développement
	 * @param fs : La liste des fichiers XML du type Aide Au Développement
	 * @return tm : L'arbre d'arborescence pour l'AAD
	 * @throws Exception
	 */
	public static DefaultTreeModel generateTree(List<File> fs) throws Exception{
		SAXBuilder sxb = new SAXBuilder();
	    DefaultMutableTreeNode nr = new DefaultMutableTreeNode("Biblioth\u00E8que");
	    DefaultTreeModel tm = new DefaultTreeModel(nr);
	    Document document;
	    Element racine;
	    for(File f : fs){
	    	String name = f.getName().substring(0,f.getName().lastIndexOf("."));
	    	String xmlpath = f.getPath().substring(0,f.getPath().lastIndexOf("."))+".xml";
	    	DefaultMutableTreeNode ntmp = new DefaultMutableTreeNode(name.substring(0,1).toUpperCase()+name.substring(1));
	    	ntmp.add(new DefaultMutableTreeNode("[Requires::"+name+"] // Import du fichier module " + f.getName()));
		    nr.add(ntmp);
		    f = new File(xmlpath);
		    if(f.exists()){
		    	document = sxb.build(f);
		    	racine = document.getRootElement();
		    	recursive_add(racine,ntmp);
		    }
	    }
	    return tm;
	}
	/**
	 * Méthode récursive pour la création d'arbre d'arborescence AAD à partir d'un XML
	 * @param el
	 * @param nr
	 */
	private static void recursive_add(Element el,DefaultMutableTreeNode nr){
		for(Element el2 : el.getChildren()){
			if(el2.getName().toLowerCase()=="menu"){
				DefaultMutableTreeNode ntmp = new DefaultMutableTreeNode(el2.getAttribute("titre").getValue());
				recursive_add(el2,ntmp);
				nr.add(ntmp);
			}
			else if(el2.getName().toLowerCase()=="item"){
				nr.add(new DefaultMutableTreeNode(el2.getText()));
			}
		}
	}
}
