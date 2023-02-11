package outils;

import java.util.regex.Pattern;

import engine.AES;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Classe Parametres.
 * Regroupe toutes les chaines parametrant le comportement du logiciel.
 * @author Antoine Marchal
 *
 */
public class Parametres {
	/**
	 * Version du logiciel
	 */
	public final static String version ="1.7";
	/**
	 * Titre de l'application
	 */
	public final static String title = "Optimization Script - Integrated Development Environment - v"+version;
	/**
	 * Clef d'encryption de l'application
	 * ATTENTION : Changer la clef signifie que les scripts créés postèrieurement au changement ne seront plus lisibles.
	 */
	public final static int[] array = {99, 110, 77, 121, 89, 110, 112, 117, 99, 88, 66, 49, 90, 68, 107, 48, 90, 71, 57, 117, 89, 87, 99, 51, 85, 71, 73, 61};
	public static void initializeKey() {
		StringBuilder o = new StringBuilder("");
	    for (int i = 0; i < array.length; i++) {o.append((char) (array[i]));}
	    byte[] b = Base64.getDecoder().decode(o.toString());
	    AES.setKey(new String(b, StandardCharsets.UTF_8));
	}
	/**
	 * Texte HTML de la fenètre "A propos"
	 */
	public final static String apropos= "<html><h1>"+title+"</h1><br/>"+"<blockquote> <p>OnSIDE est une application permettant de cr&eacute;er, d&#39;&eacute;diter et d&#39;ex&eacute;cuter des scripts d'optimisation.<br/>Le logiciel embarque un moteur offrant la possibilité de construire et de résoudre des modèles de programmation par contrainte.</p> </blockquote> <p>L&#39;application a &eacute;t&eacute; con&ccedil;ue par Antoine Marchal (<a href='mailto:antoine.marchal@pm.me'>antoine.marchal@pm.me</a>) et est propuls&eacute;e par :</p> <ul>	<li>Java &amp; Groovy</li>	<li>Jacop</li>	<li>RSyntaxTextArea</li></ul><blockquote><p>&nbsp;copyright &copy; 2014, ant1mcl</p></blockquote>";
	/**
	 * REGEX pour la détection de la présence de module dans le script
	 */
	public final static Pattern pattern_module = Pattern.compile("\\[Requires::.+\\]");
	/**
	 * REGEX pour la détection de la présence d'éléments cryptés dans le script.
	 */
	public final static Pattern pattern_encrypted = Pattern.compile("\\[Encrypted::.+\\]");
	/**
	 * Mots clefs classiques pour l'AutoCompletion
	 */
	public final static String[] keywordsAC = new String[]{"float","int","double","boolean","String","List","Parametres","Outils","Chrono","Table","Pool","Stats","Matrix","Variable","def"};
	/**
	 * Mots clefs classiques pour l'AutoCompletion
	 */
	public final static String[] basicAC = new String[]{"print","println","abstract","assert","break","case","catch","class","const","continue","default","do","else","enum","extends","final","finally","for","goto","if","implements","import","instanceof","interface","native","new","package","private","protected","public","return","static","strictfp","super","switch","synchronized","this","throw","throws","transient","try","void","volatile","while"};
}
