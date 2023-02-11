package ui.panels;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
/**
 * Classe Console "hérite de JPanel"
 * Affiche la console de l'éxecution d'un script, ainsi que le chronométre, la barre de progression et le bouton pour revenir à l'éditeur.
 * @author Antoine MARCHAL
 */
public class Console extends JPanel{

	private static final long serialVersionUID = 1L;
	/**
	 * La zone de texte de la console.
	 */
	public static RSyntaxTextArea textArea;
	/**
	 * Le chronomètre
	 */
	public JLabel lblChronometer;
	/**
	 * Le boutton "Revenir à l'éditeur"
	 */
	public JButton btnRevenir;
	private long chron;
	/**
	 * La barre de progression.
	 */
	public static JProgressBar progressBar;
	static{
		progressBar=new JProgressBar();
		textArea = new RSyntaxTextArea(20, 60);
	}
	private Window window;
	/**
	 * Construit le panel console. Il est parent de la fenétre principale de l'application et s'y ajoute.
	 * @param window
	 */
	public Console(Window window){
		this.window=window;
		window.frame.getContentPane().add(this);
		initialize();
	}
	/**
	 * Initialise la construction du panel. (Ajoute et configure tous ses éléments)
	 */
	private void initialize(){
		setLayout(new MigLayout("", "[grow]", "[grow][][][]"));
		initializeTextArea();
		
		progressBar.setStringPainted(true);
		add(progressBar, "cell 0 1,alignx center,aligny center");
		
		lblChronometer = new JLabel("");
		add(lblChronometer, "cell 0 2,alignx center");
		
		btnRevenir = new JButton("Revenir");
		btnRevenir.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				try{
					window.sm.th.join();
				synchronized(window.sm.th){				
						window.sm.th.stop();
						Thread[] a = new Thread[1000];
						int n = Thread.enumerate(a);
						for(int i = 0; i< n ; i++){
							if(a[i].getName().contains("Thread"))a[i].stop();
						}
				}
				}catch(Exception e){
					e.printStackTrace();
				}

				Console.textArea.setText("");
				window.mb.setVisible(true);
				window.cl.previous(window.frame.getContentPane());
				window.frame.validate();
			}
		});
		add(btnRevenir, "flowx,cell 0 3,alignx center,aligny center");
		
		
	}
	private void initializeTextArea(){
		textArea.setCurrentLineHighlightColor(SystemColor.info);
	    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
	    textArea.setCodeFoldingEnabled(true);
	    textArea.setAntiAliasingEnabled(true);
	    textArea.setEditable(false);
	    textArea.setLineWrap(false);
	    textArea.setAnimateBracketMatching(false);
	    RTextScrollPane sp_c = new RTextScrollPane(textArea,false);
	    sp_c.setFoldIndicatorEnabled(true);
	    add(sp_c, "cell 0 0,grow");
	    
	}
	public void lancerChrono(){
		chron=System.currentTimeMillis();
		new Thread(){
			public void run(){
				int sec,min,hours,d;
				int time;
				while(chron!=0L){
					time = (int) ((System.currentTimeMillis()-chron)/100);
					hours=(time/36000);
					min=(time-hours*36000)/600;
					sec=(time-hours*36000-min*600)/10;
					d=(time-hours*36000-min*600-sec*10);
					lblChronometer.setText(String.format("%02d", hours)+":"+String.format("%02d", min)+":"+String.format("%02d", sec)+", "+d+"'");
					try {
						sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	public String arreterChrono(){
		long tmp = chron;
		chron = 0L;
		return (System.currentTimeMillis()-tmp)/1000+"sec";
	}
}
