/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.gestion;

import com.google.gson.Gson;

/**
 *
 * @author mehdi
 */
public class JSON {
    
    public static String encode(Object object){
        
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
    public static Object decode(String json, Class type){
        
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
