package fr.polytechnancyemt.data;

/**
 *
 * Message is the class which contains the type of action and the associated informations
 * 
 * @author Mehdi EL MADKOUR
 */
public class Message {
    
    /**
     * 
     * The type of action
     * 
     * @see fr.polytechnancyemt.data.Message#getAction() 
     * @see fr.polytechnancyemt.data.Message#Message(java.lang.String, java.lang.String) 
     */
    private final String action;
        
    /**
     * 
     * The informations associated to the action
     * 
     * @see fr.polytechnancyemt.data.Message#getData()
     * @see fr.polytechnancyemt.data.Message#Message(java.lang.String, java.lang.String) 
     */
    private final String data;
    
    /**
     * 
     * The access of the message
     * 
     * @see fr.polytechnancyemt.data.Message#getAction() 
     * @see fr.polytechnancyemt.data.Message#Message(java.lang.String, java.lang.String) 
     */
    private final String access;
    
    /**
     * 
     * Initializes an object of type Message
     * 
     * @param action a string that represents the action of the message
     * @param data a string that represents the message data
     */
    public Message(String action, String data) {
        this.action = action;
        this.data = data;
        this.access = null;
    }
        
    /**
     * 
     * Initializes an object of type Message with an access
     * 
     * @param action a string that represents the action of the message
     * @param data a string that represents the message data
     * @param access a string that represents the access of the message
     */
    public Message(String access, String action, String data) {
        this.action = action;
        this.data = data;
        this.access = access;
    }
    
    /**
     * 
     * @return the object of type String that represents the action
     */
    public String getAction() {
        return action;
    }
    
    /**
     * 
     * @return the object of type String that represents the data
     */
    public String getData() {
        return data;
    }
    
    
}
