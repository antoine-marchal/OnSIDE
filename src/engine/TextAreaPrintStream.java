package engine;



import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.InputStreamReader;
import java.io.BufferedReader;



import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/**
 * Class TextAreaPrintStream
 * extends PrintStream.
 * A custom made PrintStream which overrides methods println(String)
 * and print(String).
 * Thus, when the out stream is set as this PrintStream (with System.setOut
 * method), all calls to System.out.println(String) or System.out.print(String)
 * will result in an output stream of characters in the JTextArea given as an
 * argument of the constructor of the class.
 * 
 * Permet le "Pipe" entre la console et la console JAVA.
 **/
public class TextAreaPrintStream extends PrintStream {

    //The JTextArea to wich the output stream will be redirected.
    private RSyntaxTextArea textArea;


    /**
     * Method TextAreaPrintStream
     * The constructor of the class.
     * @param the JTextArea to wich the output stream will be redirected.
     * @param a standard output stream (needed by super method)
     **/
    public TextAreaPrintStream(RSyntaxTextArea area, OutputStream out) {
	super(out);
	textArea = area;
    }

    /**
     * Method println
     * @param the String to be output in the JTextArea textArea (private
     * attribute of the class).
     * After having printed such a String, prints a new line.
     **/
    public void println(String string) {
	textArea.append(string+"\n");
    }



    /**
     * Method print
     * @param the String to be output in the JTextArea textArea (private
     * attribute of the class).
     **/
    public void print(String string) {
	textArea.append(string);
    }
    
	public static String convertStreamToString(InputStream is) throws IOException {
	    if (is != null) {
	        Writer writer = new StringWriter();

	        char[] buffer = new char[1024];
	        try {
	            Reader reader = new BufferedReader(
	                    new InputStreamReader(is, "UTF-8"));
	            int n;
	            while ((n = reader.read(buffer)) != -1) {
	                writer.write(buffer, 0, n);
	            }
	        } finally {
	            is.close();
	        }
	        return writer.toString();
	    } else {        
	        return "";
	    }
	}
}