/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.websocket;

import fr.polytechnancyemt.webservice.ServiceEMT;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author mehdi
 */
@ServerEndpoint("/socket")
public class WebSocketServer {

    private ServiceEMT service = new ServiceEMT(this);
    
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnOpen
    public void onOpen(Session session) {
        
        this.sessions.add(session);
    }
    
    @OnMessage
    public void onMessage(String message) {
        
        this.service.readMessage(message);
    }
    
    @OnClose
    public void onClose (Session session) {
        
        sessions.remove(session);
    }
    
    public void broadcast(String message){
        
        for (Session session : this.sessions) {
            
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
