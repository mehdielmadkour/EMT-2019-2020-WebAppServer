/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.data;

/**
 *
 * KeyPair is the class wich contains the keys used to encrypt and decrypt an information
 * @author mehdi
 */
public class KeyPair {
    
    /**
     * 
     * The private key
     * 
     * @see fr.polytechnancyemt.data.KeyPair#setPrivateKey(java.lang.String) 
     * @see fr.polytechnancyemt.data.KeyPair#getPrivateKey()  
     */
    private String privateKey;
    
     /**
     * 
     * The public key
     * 
     * @see fr.polytechnancyemt.data.KeyPair#setPubliceKey(java.lang.String) 
     * @see fr.polytechnancyemt.data.KeyPair#getPublicKey()  
     */
    private String publicKey;

    
    /**
     * 
     * Create the KeyPair object with the specified keys
     * 
     * @param privateKey a string that represents the private key
     * @param publicKey a string that represents the public key
     */
    public KeyPair(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    
    /**
     * 
     * @return the object of type String that represents the private key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 
     * Sets the private key
     * 
     * @param privateKey a String that represents the private key
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    
    /**
     * 
     * @return the object of type String that represents the public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 
     * Sets the public key
     * 
     * @param publicKey a String that represents the public key
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    
    
}
