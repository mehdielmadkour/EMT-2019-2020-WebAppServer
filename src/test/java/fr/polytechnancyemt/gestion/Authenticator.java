/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.gestion;

import fr.polytechnancyemt.data.User;

/**
 *
 * @author mehdi
 */
public class Authenticator {
    
    public static boolean connect(User user, DataBase dataBase){
        
        try{
            return user.getHash().equals(dataBase.getUserByName(user.getName()).getHash());
        }
        catch (Exception e){
            return false;
        }
    }
}
