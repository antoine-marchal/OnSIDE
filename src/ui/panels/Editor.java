package ui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import outils.Parametres;
import engine.AES;
/**
 * Classe Editor "hérite de JPanel"
 * L'éditeur permet de créer/éditer un script CSP (Groovy) et de l'executer.
 * Il comporte également l'AAD.
 * @author Antoine Marchal
 */
public class Editor extends JPanel{

	private static final long serialVersionUID = 1L;
	/**
	 * La zone d'édition
	 */
	public static RSyntaxTextArea textArea;
	/**
	 * L'AAD
	 */
	public AideAuDeveloppement aad;
	
	private Window window;
	
	public JSplitPane msp;
	/**
	 * Créé l'éditeur. Il est parent de la fenétre principale de l'application. Il s'y ajoute lui méme
	 * @param window
	 */
	public Editor(Window window){
		this.window = window;
		window.frame.getContentPane().add(this);
		initialize();
	}
	/**
	 * Initialise l'éditeur. Ajoute et configure tous ses éléments.
	 */
	private void initialize(){
		setLayout(new MigLayout("", "[300:n:300,left][grow]", "[grow][]"));
		initializeAAD();
		initializeTextArea();
		
		JButton btnLancerJusquLa = new JButton("Lancer jusqu'\u00E0 la s\u00E9lection");
		btnLancerJusquLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.sm.script = textArea.getText().substring(0, textArea.getSelectionEnd());
				try {
					window.sm.lancerScript();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		add(btnLancerJusquLa, "flowx,cell 0 1 2 1,alignx right,aligny center");
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		add(rigidArea, "cell 0 1 2 1,alignx right,aligny center");
		
		JButton btnLancer = new JButton("Lancer");
		btnLancer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.sm.script = textArea.getText();
				try {
					window.sm.lancerScript();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		add(btnLancer, "cell 0 1 2 1,alignx right,aligny center");
		
		
	}
	/**
	 * Ajoute l'AAD au panel.
	 */
	private void initializeAAD(){
		aad = new AideAuDeveloppement(this);
		//add(aad.jsp, "cell 0 0,grow");
	}
	/**
	 * Ajoute la possibilité d'encrypter une sélection au popup de la zone d'édition (Clic droit).
	 * @param popup
	 */
	private void initializePopupHack(JPopupMenu popup){
		popup.addSeparator();
		JMenuItem encrypt = new JMenuItem("Encrypter sélection (irréversible!)");
		encrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = textArea.getText();
				int start = textArea.getSelectionStart();
				int end = textArea.getSelectionEnd();
				if(start!=end){
					while(start!=0 && s.charAt(start-1)!=' ' && s.charAt(start-1)!='\n' && s.charAt(start-1)!='\t'){
						start--;
					}
					while(end-1<s.length() && s.charAt(end-1)!=' ' && s.charAt(end-1)!='\n' && s.charAt(end-1)!='\t'){
						end++;
					}
					String tobeEncrypted= s.substring(start, end-1);
					String encrypted = "[Encrypted::"+AES.encrypt(tobeEncrypted)+"]";
					textArea.setText(textArea.getText().replace(tobeEncrypted, encrypted));
				}
			}
		});
		popup.add(encrypt);
	}
	/**
	 * Initialise la zone d'édition.
	 */
	private void initializeTextArea(){
		textArea = new RSyntaxTextArea(20, 60);
		initializeAC();
		JPopupMenu popup = textArea.getPopupMenu();
		initializePopupHack(popup);			
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				if(window.sm.pathfile!="")window.mb.mntmEnregistrer.setEnabled(true);
			}
		});
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
	    textArea.setCodeFoldingEnabled(true);
	      
	    textArea.setAntiAliasingEnabled(true);
	    textArea.setLineWrap(true);
	    textArea.setAutoIndentEnabled(true);
	    textArea.setAnimateBracketMatching(true);
	    RTextScrollPane sp = new RTextScrollPane(textArea);
	    sp.setFoldIndicatorEnabled(true);
	    
	    msp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,aad.jsp,sp);
	    msp.setDividerSize(10);
	    msp.setContinuousLayout(true);
	    msp.setOneTouchExpandable(true);
	    msp.setDividerLocation(-1);
	    add(msp, "cell 0 0 2 1,grow");
	}
	/**
	 * Initialise l'auto-completion
	 */
	private void initializeAC(){
		DefaultCompletionProvider provider = new DefaultCompletionProvider();
		String keywords ="(";
		for(String s : Parametres.keywordsAC){
			keywords+=s+"|";
			provider.addCompletion(new BasicCompletion(provider, s));
		}
		for(String s : Parametres.basicAC){
			provider.addCompletion(new BasicCompletion(provider, s));
		}
		Pattern comment = Pattern.compile("//(.*)");
		
		keywords=keywords.subSequence(0, keywords.length()-1)+")";

		Pattern lhs = Pattern.compile(keywords+"(\\[\\]){0,999} (.*) =");
		Matcher m;
		for(String s : aad.getAllLeaves()){
			

			m=comment.matcher(s);
			while(m.find()){
				s=s.replace(m.group(), "");
			}
			
			m=lhs.matcher(s);
			if(m.find()){
				s=s.replace(m.group(), "");
			}
			
			s=s.replace("new ", "");
			provider.addCompletion(new BasicCompletion(provider, s.trim()));
		}
		AutoCompletion ac = new AutoCompletion(provider);
	    ac.install(textArea);
	}
	
}
