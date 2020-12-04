package fr.polytechnancyemt.websocket;

import fr.polytechnancyemt.webservice.ServiceEMT;

import java.util.Collections;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * WebSocketServer is the class that communicates with clients
 * 
 * @author Mehdi EL MADKOUR
 */
@ServerEndpoint("/socket")
public class WebSocketServer {

    /**
     * The webService used to process informations
     */
    private ServiceEMT service = new ServiceEMT(this);
    
    /**
     * The opened sessions that represents the connection between the server and clients
     */
    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    
    /**
     * 
     * The method executed when a new session is open
     * 
     * @param session the object of type javax.websocket.Session that represents the connection between the client and the server
     * 
     * @see javax.websocket.OnOpen
     */
    @OnOpen
    public void onOpen(Session session) {
        
        synchronized(this.sessions){
            this.sessions.add(session);
        }
    }
        
    /**
     * 
     * The method executed when a new message is received from a client
     * 
     * @param message a string that represents the message
     * @param session the object of type javax.websocket.Session that represents the connection between the client and the server
     * 
     * @see javax.websocket.OnMessage
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        
        synchronized(this.sessions){
            this.service.readMessage(message, session);
        }
    }
        
    /**
     * 
     * The method executed when a session is closed
     * 
     * @param session the object of type javax.websocket.Session that represents the connection between the client and the server
     * 
     * @see javax.websocket.OnClose
     */
    @OnClose
    public void onClose (Session session) {
        
        synchronized(this.sessions){
            sessions.remove(session);
        }
    }
    
    /**
     * 
     * The method executed when a error occurs between a client and the server
     * 
     * @param session the object of type javax.websocket.Session that represents the connection between the client and the server
     * @param throwable the error
     * 
     * @see javax.websocket.OnError
     */
    @OnError
    public void OnError (Session session, Throwable throwable){

        System.out.println(throwable);
        this.service.log(throwable.toString());
        onClose(session);
    }
    
    /**
     * 
     * Broadcast a message to clients
     * 
     * @param session an object of type Session that represents the connection between the server and a client
     * @param message a string to the Json format that represents message
     * 
     * @see javax.websocket.Session
     */
    public void broadcast(Session session, String message){
        
        Set<Session> allSessions = session.getOpenSessions();

        synchronized(this.sessions){
            for (Session s : allSessions) {
                
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    this.service.log(ex.toString());
                }
            }
        }
    }
    
    /**
     * 
     * Send a message to a client
     * 
     * @param session an object of type Session that represents the connection between the server and a client
     * @param message a string to the Json format that represents message
     * 
     * @see javax.websocket.Session
     */
    public void send(Session session, String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            this.service.log(ex.toString());
        }
    }
}
