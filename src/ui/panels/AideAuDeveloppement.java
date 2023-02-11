package ui.panels;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import outils.Utils;
/**
 * Classe AAD:
 * Le panel de gauche qui permet de réunir toutes les fonctions utiles du header pour le développement d'un script.
 * @author Antoine Marchal
 *
 */
public class AideAuDeveloppement{
	/**
	 * Le Scroll Pane encadrant l'AAD
	 */
	public JScrollPane jsp;
	/**
	 * L'arbre / L'arborescence
	 */
	public JTree tree;
	/**
	 * Constructeur : Créé le scrollpane et initialise la construction de l'arbre.
	 * @param editor
	 */
	public AideAuDeveloppement(Editor editor){
		jsp = new JScrollPane();
		initializeTree();
	}
	/**
	 * Obtient toutes les feuilles de l'arborescence.
	 * @return une liste de toutes les feuilles
	 */
	public ArrayList<String> getAllLeaves(){
		ArrayList<String> al = new ArrayList<String>();
		recursiveAddLeaf(tree.getModel().getRoot(),tree.getModel(),al);
		return al;
	}
	/**
	 * Méthode récursive pour parcourir l'arbre
	 * @param o : Le parent 
	 * @param tm : L'arbre
	 * @param al : La liste à remplir de feuille
	 */
	private void recursiveAddLeaf(Object o,TreeModel tm,ArrayList<String> al){
		Object o2;
		for(int i = 0; i<tm.getChildCount(o);i++){
			o2=tm.getChild(o, i);
			if(!tm.isLeaf(o2)){
				recursiveAddLeaf(o2,tm,al);
			}
			else al.add(o2.toString());
		}
	}
	/**
	 * Créé l'arbre et ses interactions avec l'éditeur.
	 */
	private void initializeTree(){
		tree = new JTree();
		tree.setRootVisible(false);
		tree.setVisibleRowCount(50);
		replaceContents(new File("modules"));
		tree.setToolTipText("");
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				try{
					//if(jsp.getHorizontalScrollBar().isVisible())
					((JSplitPane)jsp.getParent()).setDividerLocation(-1);
				}catch(Exception e){

				}
				TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
				Object obj =tp==null?null:tp.getLastPathComponent();
				if(me.getClickCount()==2 && obj!=null && tree.getModel().isLeaf(obj)){

					int start = Editor.textArea.getSelectionStart();
					int end = Editor.textArea.getSelectionEnd();
					String t =Editor.textArea.getText();
					String s=""+obj;
					String[] sp = s.split("\\(");
					for(String sptmp : sp)if(sptmp.contains(")"))sp[1]=sptmp;
					int index = sp.length>1?s.indexOf(sp[1]):-1;
					String[] sa= s.split("//");
					String tmp="";
					if(start==end){
						if(start!=0 && t.charAt(start-1)!='(' && t.charAt(start-1) !='\n')tmp=";";
						if(end != t.length() && sa.length==2){
							int end2 = end;
							while((end2+1)!=t.length() && t.charAt(end2+1)!='\n')end2++;
							t=t.substring(0, start)+tmp+sa[0].trim()+t.substring(end,end2+1)+" : "+sa[1].trim()+t.substring(end2+1,t.length());
						}else{
							t=t.substring(0, start)+tmp+s+t.substring(start,t.length());
						}

					}
					else{
						if(sa.length==2){
							int end2 = end;
							while((end2+1)!=t.length() && t.charAt(end2+1)!='\n')end2++;
							t=t.substring(0, start)+sa[0].trim()+t.substring(end,end2+1)+" // "+sa[1].trim()+t.substring(end2+1,t.length());
						}else{
							t=t.substring(0, start)+s+t.substring(end,t.length());
						}

					}
					Editor.textArea.setText(t);
					if(index!=-1 && sp[1].contains(")")){
						start=start+index;
						if(sp[1].split("\\)").length >0){
							end=start+sp[1].split("\\)")[0].length();
							Editor.textArea.select(start+tmp.length(), end+tmp.length());
						}
					}
					Editor.textArea.requestFocus();

				}
			}
		});
		tree.setShowsRootHandles(true);
		jsp.setViewportView(tree);

	}
	public void replaceContents(File f){
		try {
			tree.setModel(Utils.generateTree(f));
			((JSplitPane)jsp.getParent()).setDividerLocation(-1);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		tree.setToolTipText("");
	}
}
