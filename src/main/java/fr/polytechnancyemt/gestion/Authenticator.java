package fr.polytechnancyemt.gestion;

import fr.polytechnancyemt.data.User;

/**
 * 
 * Authenticator is the class which allows to compare informations send by the user to those in the database
 *
 * @author Mehdi EL MADKOUR
 */
public class Authenticator {
    
    /**
     * 
     * Verify match of credentials
     * 
     * @param user an object of type User that represents the user
     * @param dataBase an object of type DataBase that control the access to data
     * 
     * @return the object of type Boolean that represents the matching of credentials 
     * 
     * @see fr.polytechnancyemt.data.User
     * @see fr.polytechnancyemt.gestion.DataBase
     */
    public static boolean connect(User user, DataBase dataBase){
        
        try{
            return user.getHash().equals(dataBase.getUserByName(user.getName()).getHash());
        }
        catch (Exception e){
            System.out.println("Erreur d'authentification");
	    return false;
        }
    }
}
