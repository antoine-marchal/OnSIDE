package ui;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import outils.Parametres;

/**
 * Frame "à propos" :
 * Présente la version et le résumé du produit.
 * @author Antoine Marchal
 *
 */
public class aPropos extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	/**
	 * Créé la frame "à Propos"
	 * @throws IOException 
	 */
	public aPropos() throws IOException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(aPropos.class.getResource("/lib/icone.png")));
		setTitle("A propos");
		this.setResizable(false);
		setBounds(100, 100, 900, 670);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[20px:n][grow,fill][20px:n]", "[20px:n][grow,fill][20px:n]"));
		BufferedImage myPicture = ImageIO.read(new File("data_lib/loading.bmp"));
		JLabel splash = new JLabel(new ImageIcon(myPicture));
		contentPane.add(splash,"cell 1 0,alignx center");
		JLabel lblonside = new JLabel(Parametres.apropos);
		lblonside.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblonside, "cell 1 1,alignx center");
	}

}
