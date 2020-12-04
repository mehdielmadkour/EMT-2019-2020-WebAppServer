package fr.polytechnancyemt.data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SealedObject;

/**
 *
 * User is the class wich contains informations that allow to identify the user
 * 
 * @author Mehdi EL MADKOUR
 */
public class User {
    
    /**
     * The Id of the user
     * 
     * @see fr.polytechnancyemt.data.User#getId()
     * @see fr.polytechnancyemt.data.User#setId(int)
     */
    private int id;
        
    /**
     * The username
     * 
     * @see fr.polytechnancyemt.data.User#getName() 
     * @see fr.polytechnancyemt.data.User#setName(java.lang.String) 
     */
    private String name;
        
    /**
     * The hash of the password.
     * 
     * @see fr.polytechnancyemt.data.User#getHash() 
     * @see fr.polytechnancyemt.data.User#setHash(java.lang.String) 
     */
    private String hash;
    
    /**
     * The e-mail address of the user.
     * 
     * @see fr.polytechnancyemt.data.User#getMail() 
     * @see fr.polytechnancyemt.data.User#setMail(java.lang.String) 
     */
    private String mail;
        
    /**
     * The access of the user
     * 
     * @see fr.polytechnancyemt.data.User#getAccess() 
     * @see fr.polytechnancyemt.data.User#setAccess(java.lang.String) 
     */
    private String access = "guest";
        
    /**
     * The validation of the authentification of the user
     * 
     * @see fr.polytechnancyemt.data.User#validate() 
     */
    private Boolean validated = false;
    
    /**
     * The list that contains keys of the user
     * 
     * @see fr.polytechnancyemt.data.Key
     */
    private List<Key> keys = new ArrayList<>();

    /**
     * 
     * @return the object of type String that represents the Id of the user 
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id a string that represents the Id of the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return the object of type String that represents the hash of the user 
     */
    public String getHash() {
        return hash;
    }

    /**
     * 
     * @param hash a string that represents the hash of the user
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * 
     * @return the object of type String that represents the name of the user 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param access a string that represents the access of the user
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * 
     * @return the object of type String that represents the access of the user 
     */
    public String getAccess() {
        return access;
    }

    /**
     * 
     * @param name a string that represents the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * Set the value of the validated field to true
     */
    public void validate(){
        this.validated = true;
    }
    
    /**
     * 
     * Add a new key to the key list
     * 
     * @see fr.polytechnancyemt.data.Key
     */
    public void addKeys(Key key){
        this.keys.add(key);
    }

    /**
     * 
     * @return the object of type String that represents the e-mail address of the user 
     */
    public String getMail() {
        return mail;
    }

    /**
     * Set the e-mail address of the user
     * 
     * @param mail a string that represents the e-mail address of the user 
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
}
