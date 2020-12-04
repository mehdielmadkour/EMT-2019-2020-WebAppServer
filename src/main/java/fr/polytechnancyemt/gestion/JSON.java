package fr.polytechnancyemt.gestion;

import com.google.gson.Gson;

/**
 * 
 * JSON is the class which allows to serialize an object to a formated String 
 *
 * @author Mehdi EL MADKOUR
 */
public class JSON {
    
    /**
     * 
     * Encode an object to the Json format 
     * 
     * @param object an object of any type
     * 
     * @return an object of type String that represent the encoded object
     */
    public static String encode(Object object){
        
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
    /**
     * 
     * Decode an object of type String encoded to the Json format
     * 
     * @param json a string that represent the encoded object
     * @param type an object of type Class that represents the type of the object
     * 
     * @return the decoded object
     */
    public static Object decode(String json, Class type){
        
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
