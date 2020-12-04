package fr.polytechnancyemt.data;

/**
 *
 * Key is the class wich contains informations about a key
 * @author mehdi
 */
public class Key {
    
    /**
     * 
     * The access of the user
     * 
     * @see fr.polytechnancyemt.data.Key#setAccess(java.lang.String) 
     * @see fr.polytechnancyemt.data.Key#getAccess()  
     */
    private String access;
    
    /**
     * 
     * The key of the user
     * 
     * @see fr.polytechnancyemt.data.Key#setKey(java.lang.String) 
     * @see fr.polytechnancyemt.data.Key#getKey() 
     */
    private String key;

    /**
     * 
     * Create the Key object with the specified key and access
     * 
     * @param access a string that represent the access
     * @param key a string that rpresent the key
     */
    public Key(String access, String key) {
        this.access = access;
        this.key = key;
    }
    
    /**
     * 
     * @return the object of type String that represents the access
     */
    public String getAccess() {
        return access;
    }

    /**
     * 
     * Sets the access of the user
     * 
     * @param access a String that represents the access of the user
     */
    public void setAccess(String access) {
        this.access = access;
    }
    
    /**
     * 
     * @return the object of type String that represents the key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * Sets the key of the user
     * 
     * @param key a String that represents the key of the user
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    
}
