/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.data;

/**
 *
 * @author mehdi
 */
public class Message {
    
    private String action;
    private String data;

    public Message(String action, String data) {
        this.action = action;
        this.data = data;
    }
    
    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }
    
    
}
