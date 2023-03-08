package engine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.sf.launch4j.Builder;
import net.sf.launch4j.Log;
import net.sf.launch4j.config.ConfigPersister;
/**
 * La classe Export permet de générer les applications autonomes pour un script donné
 * @author Antoine Marchal
 */
public class Export {
	/**
	 * Le Random est utilisé pour générer les noms des fichiers temporaires
	 */
	public static Random ran = new Random();
	/**
	 * Le chemin de l'icone du programme
	 */
	public static File ico = new File("data_lib/icone.ico");
	/**
	 * Le chemin de l'image de chargement.
	 */
	public static File splash = new File("data_lib/loading.bmp");
	/**
	 * Point d'entrée pour générer l'exécutable du logiciel.
	 */
	public static void main(String[] args) throws Exception{


		File xml = new File("build.xml");
		ConfigPersister.getInstance().load(xml);
		Builder b = new Builder(Log.getConsoleLog());
		b.build();

	}
	@SuppressWarnings("resource")
	private static void copyFileUsingChannel(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		}finally{
			sourceChannel.close();
			destChannel.close();
		}
	}
	/**
	 * Méthode assurant la création de l'exe autonome pour un script donné
	 * @param destination : La destination de l'exe
	 * @param titleString : Le titre de l'application autonome
	 * @param script : Le script de l'application autonome
	 * @throws Exception
	 */
	public static void exportExe(File destination,String titleString,File script) throws Exception {

		File source = new File("data_lib/modelbuild.jar");
		File xml = new File("data_lib/modelbuild.xml");

		File dest = new File("data_lib/"+ran.nextLong());
		File xmlo = new File("data_lib/"+ran.nextLong());
		File title = new File("data_lib/title.txt");
		System.out.println("Begin");


		try{

			copyFileUsingChannel(source,dest);
			FileUtils.writeStringToFile(title, AES.encrypt(titleString),Charset.defaultCharset(), false);

			File[] files = {title,script};
			addFilesToExistingZip(dest,files,"lib/");

			for(File f : new File("onside_lib/").listFiles()) {
				if(f.getName().toLowerCase().endsWith("jar"))addFileToExistingZip(dest,f,"onside_lib/");;
			}

			System.out.println("Done");

			SAXBuilder sxb = new SAXBuilder();

			Document  document = sxb.build(xml);
			Element racine = document.getRootElement();


			racine.getChild("jar").setText(dest.getAbsolutePath());
			racine.getChild("outfile").setText(destination.getAbsolutePath());
			racine.getChild("icon").setText(ico.getAbsolutePath());
			racine.getChild("splash").getChild("file").setText(splash.getAbsolutePath());



			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream fos = new FileOutputStream(xmlo);
			sortie.output(document, fos);
			fos.close();

			ConfigPersister.getInstance().load(xmlo);
			Builder b = new Builder(Log.getConsoleLog());
			b.build();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		title.delete();
		dest.delete();
		xmlo.delete();

	}
	public static void addFileToExistingZip(File zipFile, File f,String path) throws Exception {

		@SuppressWarnings("resource")
		ZipFile zF = new ZipFile(zipFile);

		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setFileNameInZip(path+f.getName());
		zF.addFile(f,zipParameters);

	}
	public static void addFilesToExistingZip(File zipFile, File[] files,String path) throws Exception {

		@SuppressWarnings("resource")
		ZipFile zF = new ZipFile(zipFile);

		for(File f : files) {
			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setFileNameInZip(path+f.getName());
			zF.addFile(f,zipParameters);
		}
	}	
	/**
	 * Méthode ajoutant une liste de fichier dans une archive ZIP (ou JAR) 
	 * @param zipFile : Chemin de l'archive
	 * @param files : Fichiers é ajouter
	 * @param path : Chemin interne é l'archive oé les fichiers sont à insérer
	 * @throws IOException
	 */
	public static void addFilesToExistingZipBACK(File zipFile,
			File[] files,String path) throws IOException {
		// get a temp file

		File tempFile = File.createTempFile("tmp", ".zip", zipFile.getParentFile());

		// delete it, otherwise you cannot rename your existing zip to it.
		tempFile.delete();

		boolean renameOk=zipFile.renameTo(tempFile);
		if (!renameOk)
		{
			throw new RuntimeException("could not rename the file "+zipFile.getAbsolutePath()+" to "+tempFile.getAbsolutePath());
		}
		final byte[] buf = new byte[8192];

		final ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
		final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		ZipEntry entry = zin.getNextEntry();
		// Transfer bytes from the ZIP file to the output file
		int len;
		while (entry != null) {
			String name = entry.getName();
			boolean notInFiles = true;
			for (File f : files) {
				if (f.getName().equals(name)) {
					notInFiles = false;
					break;
				}
			}
			if (notInFiles) {
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(name));

				while ((len = zin.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}
			entry = zin.getNextEntry();
		}
		System.out.println("HELLO");
		// Close the streams        
		zin.close();
		// Compress the files
		for (int i = 0; i < files.length; i++) {
			InputStream in = new FileInputStream(files[i]);
			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(path+files[i].getName()));
			// Transfer bytes from the file to the ZIP file

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Complete the entry
			out.closeEntry();
			in.close();
		}
		// Complete the ZIP file
		out.close();
		tempFile.delete();
	}

}
