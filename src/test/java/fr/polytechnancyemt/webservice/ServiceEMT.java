/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.webservice;

import fr.polytechnancyemt.data.CarData;
import fr.polytechnancyemt.gestion.DataBase;
import fr.polytechnancyemt.gestion.Authenticator;
import fr.polytechnancyemt.gestion.JSON;
import fr.polytechnancyemt.data.Message;
import fr.polytechnancyemt.data.User;
import fr.polytechnancyemt.websocket.WebSocketServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mehdi
 */
public class ServiceEMT {
    
    private final WebSocketServer socket;
    private DataBase dataBase = new DataBase();
    
    public ServiceEMT(WebSocketServer socket){
        
        this.socket = socket;
        
    }
    
    private void connect(String data){
        
        User user = (User) JSON.decode(data, User.class);
        
        boolean connection = Authenticator.connect(user, dataBase);
        
        if (connection) System.out.println("connected");
        else System.out.println("acces denied");
        
        CarData carData = new CarData();
        dataBase.saveCarData(carData);
        
    }
    
    private void register(String data){
        
        User user = (User) JSON.decode(data, User.class);
        
        dataBase.addUser(user);
        
    }
    
    private void saveCarData(String data){
        
        
        CarData carData = (CarData) JSON.decode(data, CarData.class);
        
        dataBase.saveCarData(carData);
        broadcast(JSON.encode(new Message("carData", data)));
        
    }
    
    private void broadcast(String message){
        
        socket.broadcast(message);
        System.out.println(String.format("broadcast : %s", message));
    }
    
    public void readMessage(String data){
        
        Message message = (Message) JSON.decode(data, Message.class);
        
        switch(message.getAction()){
            
            case "connect":
                this.connect(message.getData());
                break;
            
            case "register":
                this.register(message.getData());
                break;
            
            case "carData":
                this.saveCarData(message.getData());
                break;
        }        
    }
}
