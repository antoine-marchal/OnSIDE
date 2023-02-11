package engine;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * Classe AES
 * La classe permet de gérer les encryptions et le décryptage des scripts et/ou des parties de code
 * @author Antoine Marchal
 */
public class AES
{

	
    private static SecretKeySpec secretKey ;
    private static byte[] key ;
    

 
    /**
     * Place la clef d'encryption
     * @param myKey La clef sous format String
     */
    public static void setKey(String myKey){
    	
   
    	MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
	    	key = Arrays.copyOf(key, 16); // use only first 128 bit
		    secretKey = new SecretKeySpec(key, "AES");
		    
		    
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
	    	  
	

    }
    /**
     * Encrypte une chaine de caractère.
     * @param strToEncrypt : La chaine à encrypter
     * @return La chaine encryptée
     */
	public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        

            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes(System.getProperty("file.encoding"))));
        }
        catch (Exception e)
        {
           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;

    }
	/**
	 * Decrypte une chaine de caractère
	 * @param strToDecrypt : La chaine encryptée
	 * @return La chaine decryptèe
	 */
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            
        }
        catch (Exception e)
        {
         
            System.out.println("Error while decrypting: "+e.toString());

        }
        return null;
    }
    
   
}