package fr.polytechnancyemt.data;

/**
 *
 * Recovery is the class that contains the new hash of an account
 * @author mehdi
 */
public class Recovery {
    
    /**
     * 
     * The recovery code
     * 
     * @see fr.polytechnancyemt.data.Recovery#getCode() 
     * @see fr.polytechnancyemt.data.Recovery#setCode(java.lang.String) 
     */
    private String code;
        
    /**
     * 
     * The new hash
     * 
     * @see fr.polytechnancyemt.data.Recovery#getHash() 
     * @see fr.polytechnancyemt.data.Recovery#setHash(java.lang.String) 
     */
    private String hash;

    /**
     * 
     * @return the recovery code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * Sets the recovery code
     * 
     * @param code a string that represents the recovery code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return the new hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * 
     * Sets the new hash
     * 
     * @param hash a string that represents the new hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    
}
