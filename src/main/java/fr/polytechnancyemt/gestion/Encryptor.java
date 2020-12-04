package fr.polytechnancyemt.gestion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;


/**
 *
 * @author mehdi
 * 
 * Encryptor is the class which allows to encrypt and decrypt the informations.
 */
public class Encryptor {

    /**
     * 
     * The object that generates the keys
     */
    private KeyPairGenerator keyGen;
    
    /**
     * 
     * The public key used to encrypt the information for the team
     */
    private PublicKey teamPublicKey;
    
    /**
     * 
     * The private key used to decrypt the information for the team
     */
    private PrivateKey teamPrivateKey;
    
    /**
     * 
     * The public key used to encrypt the information for the strategy
     */
	private PublicKey strategyPublicKey;
    
    /**
     * 
     * The private key used to decrypt the information for the strtegy
     */
	private PrivateKey strategyPrivateKey;
    
    /**
     * 
     * The public key used to encrypt the information for the admin
     */
	private PublicKey adminPublicKey;
    
    /**
     * 
     * The private key used to decrypt the information for the admin
     */
	private PrivateKey adminPrivateKey;
    
    /**
     * 
     * The constructor of the class. Initialize the key generator and generates key pairs
     *  
     * @param keylength An int that represent the size of the key
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
	public Encryptor(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
            
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
        
        KeyPair teamKeys = this.keyGen.generateKeyPair();
        KeyPair strategyKeys = this.keyGen.generateKeyPair();
        KeyPair adminKeys = this.keyGen.generateKeyPair();
        
        this.teamPrivateKey = teamKeys.getPrivate();
        this.teamPublicKey = teamKeys.getPublic();
        this.strategyPrivateKey = strategyKeys.getPrivate();
        this.strategyPublicKey = strategyKeys.getPublic();
        this.adminPrivateKey = adminKeys.getPrivate();
        this.adminPublicKey = adminKeys.getPublic();
	}

    /**
     * 
     * Returns the private key of the team
     * @return the object of type PrivateKey that represent the key
     */
	public PrivateKey getTeamPrivateKey() {
		return this.teamPrivateKey;
	}

    /**
     * 
     * Returns the public key of the team
     * @return the object of type PublicKey that represent the key
     */
	public PublicKey getTeamPublicKey() {
		return this.teamPublicKey;
	}

    /**
     * 
     * Returns the private key of the strategy
     * @return the object of type PrivateKey that represent the key
     */
	public PrivateKey getStrategyPrivateKey() {
		return this.strategyPrivateKey;
	}

    /**
     * 
     * Returns the public key of the strategy
     * @return the object of type PublicKey that represent the key
     */
	public PublicKey getStrategyPublicKey() {
		return this.strategyPublicKey;
	}

    /**
     * 
     * Returns the private key of the admin
     * 
     * @return the object of type PrivateKey that represent the key
     */
	public PrivateKey getAdminPrivateKey() {
		return this.adminPrivateKey;
	}

    /**
     * 
     * Returns the public key of the admin
     * 
     * @return the object of type PublicKey that represent the key
     */
	public PublicKey getAdminPublicKey() {
		return this.adminPublicKey;
	}

    /**
     * 
     * Sets the key pair of the team
     * 
     * @param teamKeys an object of type KeyPair that contains the public and the private key
     * 
     * @see fr.polytechnancyemt.data.KeyPair
     */
    public void setTeamKeys(fr.polytechnancyemt.data.KeyPair teamKeys) {
        try {
            this.teamPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(teamKeys.getPrivateKey())));
            this.teamPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(teamKeys.getPublicKey())));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * Sets the key pair of the strategy
     * 
     * @param strategyKeys an object of type KeyPair that contains the public and the private key
     * 
     * @see fr.polytechnancyemt.data.KeyPair
     */
    public void setStrategyKeys(fr.polytechnancyemt.data.KeyPair strategyKeys) {
        try {
            this.strategyPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(strategyKeys.getPrivateKey())));
            this.strategyPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(strategyKeys.getPublicKey())));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * Sets the key pair of the admin
     * 
     * @param adminKeys an object of type KeyPair that contains the public and the private key
     * 
     * @see fr.polytechnancyemt.data.KeyPair
     */
    public void setAdminKeys(fr.polytechnancyemt.data.KeyPair adminKeys) {
        try {
            this.adminPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(adminKeys.getPrivateKey())));
            this.adminPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(adminKeys.getPublicKey())));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * Encrypt the message
     * 
     * @param input the message to encrypt
     * @param key the public key
     * 
     * @return the encrypted message
     * 
     * @throws IOException
     * @throws GeneralSecurityException 
     */
    public String encrypt(String input, PublicKey key) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
	}
    
    /**
     * 
     * Decrypt the message
     * 
     * @param input the message to decrypt
     * @param key the private key
     * 
     * @return the decrypted message
     * 
     * @throws IOException
     * @throws GeneralSecurityException 
     */
    public String decrypt(String input, PrivateKey key) throws IOException, GeneralSecurityException, ClassNotFoundException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(input.getBytes())));
    }

}
