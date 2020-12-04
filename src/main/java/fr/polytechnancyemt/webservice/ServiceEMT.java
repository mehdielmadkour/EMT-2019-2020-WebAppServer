package fr.polytechnancyemt.webservice;

import fr.polytechnancyemt.data.CarData;
import fr.polytechnancyemt.data.Key;
import fr.polytechnancyemt.data.KeyPair;
import fr.polytechnancyemt.gestion.DataBase;
import fr.polytechnancyemt.gestion.Authenticator;
import fr.polytechnancyemt.gestion.JSON;
import fr.polytechnancyemt.data.Message;
import fr.polytechnancyemt.data.User;
import fr.polytechnancyemt.data.Point;
import fr.polytechnancyemt.data.Recovery;
import fr.polytechnancyemt.data.SpeedControl;
import fr.polytechnancyemt.gestion.Encryptor;
import fr.polytechnancyemt.websocket.WebSocketServer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.util.Base64;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.crypto.NoSuchPaddingException;

import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

/**
 *
 * Service EMT is the class that processes informations sent or requested by clients
 * 
 * @author Mehdi EL MADKOUR
 */
public class ServiceEMT {
    
    /**
     * 
     * The websocket used for the communication between the server and clients
     * 
     * @see fr.polytechnancyemt.webservice.ServiceEMT#ServiceEMT(fr.polytechnancyemt.websocket.WebSocketServer) 
     */
    private final WebSocketServer socket;
    
    /**
     * 
     * The access to the database
     * 
     * @see fr.polytechnancyemt.gestion.DataBase
     */
    private final DataBase dataBase = new DataBase();
    
    /**
     * 
     * The management of the encryption
     * 
     * @see fr.polytechnancyemt.gestion.Encryption
     */
    private Encryptor encryptor = null;
    
    /**
     * 
     * Initializes the socket attribute of type WebSocketServer
     * 
     * @param socket an object of type WebSocketServer
     * 
     * @see fr.polytechnancyemt.websocket.WebSocketServer
     */
    public ServiceEMT(WebSocketServer socket){
        
        this.socket = socket;
        try {
            this.encryptor = new Encryptor(2048);
            this.generateEncryptionKeys();
            
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            Logger.getLogger(ServiceEMT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * Generate encryption keys used by the encryptor. Those keys are read in the database if they are stored and generate randomly if they are not.
     * 
     */
    public void generateEncryptionKeys(){
        KeyPair teamKeyPair = this.dataBase.getKeys("team");
        if (teamKeyPair == null) {
            String privateKey = Base64.getEncoder().encodeToString(this.encryptor.getTeamPrivateKey().getEncoded());
            String publicKey = Base64.getEncoder().encodeToString(this.encryptor.getTeamPublicKey().getEncoded());
            teamKeyPair = new KeyPair(privateKey, publicKey);
            this.dataBase.setKeys(teamKeyPair, "team");    
        }
        this.encryptor.setTeamKeys(teamKeyPair);
        
        KeyPair strategyKeyPair = this.dataBase.getKeys("strategy");
        if (strategyKeyPair == null) {
            String privateKey = Base64.getEncoder().encodeToString(this.encryptor.getStrategyPrivateKey().getEncoded());
            String publicKey = Base64.getEncoder().encodeToString(this.encryptor.getStrategyPublicKey().getEncoded());
            strategyKeyPair = new KeyPair(privateKey, publicKey);
            this.dataBase.setKeys(strategyKeyPair, "strategy");
        }
        this.encryptor.setStrategyKeys(strategyKeyPair);
        
        KeyPair adminKeyPair = this.dataBase.getKeys("admin");
        if (adminKeyPair == null) {
            String privateKey = Base64.getEncoder().encodeToString(this.encryptor.getAdminPrivateKey().getEncoded());
            String publicKey = Base64.getEncoder().encodeToString(this.encryptor.getAdminPublicKey().getEncoded());
            adminKeyPair = new KeyPair(privateKey, publicKey);
            this.dataBase.setKeys(adminKeyPair, "admin");
        }
        this.encryptor.setAdminKeys(adminKeyPair);
    }
    /**
     * 
     * Verify match of credentials
     * 
     * @param data a string to the Json format that represents the user
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * 
     * @see javax.websocket.Session
     */
    private void connect(String data, Session session){
        
        User user = (User) JSON.decode(data, User.class);
        boolean connection = Authenticator.connect(user, dataBase);
        
        user = dataBase.getUserByName(user.getName());
        if (connection) {
            user.validate();
            Key teamKey = new Key("team", Base64.getEncoder().encodeToString(this.encryptor.getTeamPrivateKey().getEncoded()));
            Key strategyKey = new Key("strategy", Base64.getEncoder().encodeToString(this.encryptor.getStrategyPrivateKey().getEncoded()));
            Key adminKey = new Key("admin", Base64.getEncoder().encodeToString(this.encryptor.getAdminPrivateKey().getEncoded()));
            
            if (user.getAccess().equals("team")){
                user.addKeys(teamKey);
            }
            if (user.getAccess().equals("strategy")){
                user.addKeys(teamKey);
                user.addKeys(strategyKey);
            }
            if (user.getAccess().equals("admin")){
                user.addKeys(teamKey);
                user.addKeys(strategyKey);
                user.addKeys(adminKey);
            }
            
            send(session, JSON.encode(new Message("user", JSON.encode(user))));
            this.log(String.format("Connexion de %s", user.getName()));
            //this.UserSessions.put(user, session);
        }
        else {
            send(session, JSON.encode(new Message("user", JSON.encode(user))));
        } 
    }
    
    /**
     * 
     * Creates a new user
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param data a string to the Json format that represents the user
     * 
     * @see javax.websocket.Session
     */
    private void register(Session session, String data){
        
        User user = (User) JSON.decode(data, User.class);
        
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (user.getMail() == null) {
            this.send(session, JSON.encode(new Message("registerResult", "invalid_mail")));
            this.log("invalid mail : " + user.getMail());
            return;
        } 
        if (!pat.matcher(user.getMail()).matches()) {
            this.send(session, JSON.encode(new Message("registerResult", "invalid_mail")));
            this.log("invalid mail : " + user.getMail());
            return;
        }
        
        boolean result = this.dataBase.addUser(user);
        this.log(String.format("Inscription de %s : %s", user.getName(), String.valueOf(result)));
        this.send(session, JSON.encode(new Message("registerResult", String.valueOf(result))));
        
    }
    
    /**
     * 
     * Save and broadcast informations about the car
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param data a string to the Json format that represents the informations about the car
     * 
     * @see javax.websocket.Session
     */
    private void saveCarData(Session session, String data){
        
        CarData carData = (CarData) JSON.decode(data, CarData.class);
        
        this.dataBase.saveCarData(carData);
        try {
            String test = this.encryptor.encrypt("test", this.encryptor.getAdminPublicKey());
            
            broadcast(session, JSON.encode(new Message("team", "carData", this.encryptor.encrypt(JSON.encode(carData), this.encryptor.getTeamPublicKey()))));
            broadcast(session, JSON.encode(new Message("strategy", "carData", this.encryptor.encrypt(JSON.encode(carData), this.encryptor.getStrategyPublicKey()))));
            broadcast(session, JSON.encode(new Message("admin", "carData", this.encryptor.encrypt(JSON.encode(carData), this.encryptor.getAdminPublicKey()))));
            carData.setGuestMode();
            broadcast(session, JSON.encode(new Message("guest", "carData", JSON.encode(carData))));
        } catch (IOException | GeneralSecurityException ex) {
            Logger.getLogger(ServiceEMT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * 
     * Execute an SQL query
     * 
     * @param query a String that represents the query
     */
    private void execute(String query) {
        
        if (query.contains("insert") 
         || query.contains("INSERT") 
         || query.contains("update") 
         || query.contains("UPDATE") 
         || query.contains("delete") 
         || query.contains("DELETE")) {
            dataBase.executeUpdate(query);
            this.log(String.format("Exécution de la commande SQL %s", query));
        }
        else {
            dataBase.executeQuery(query);
            this.log(String.format("Exécution de la commande SQL %s", query));
        }
    }
    
    /**
     * 
     * Broadcast a message
     * 
     * @param message a string to the Json format that represents the message 
     * @param access a string that represent the access of users
     *
     * @see javax.websocket.Session
     */
    private void broadcast(Session session, String message) {
        
        this.socket.broadcast(session, message);
    }

    /**
     * 
     * Send a message
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param message a string to the Json format that represents the message 
     *
     * @see javax.websocket.Session
     */
    private void send(Session session, String message) {
        
        socket.send(session, message);
    }

    /**
     * 
     * Save and broadcast signals on the map
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param message a string to the Json format that represents the message 
     *
     * @see javax.websocket.Session
     */
    private void signalOnMap(Session session, String message) {

        Point point = (Point) JSON.decode(message, Point.class);
        dataBase.saveSignal(point);
        this.log("Signalement d'un obstacle");
        this.broadcast(session, JSON.encode(new Message("signalUpdate", JSON.encode(point))));
        
    }

    /**
     * 
     * Remove a signals on the map
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param message a string to the Json format that represents the message 
     *
     * @see javax.websocket.Session
     * 
     */
    private void removeSignal(Session session, String message){
        
        dataBase.removeSignal(message);
        this.log("Suppression d'un obstacle");
        this.broadcast(session, JSON.encode(new Message("removeSignal", message)));
        
    }

    /**
     * 
     * Send the value of the state of the dashboard
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     *
     * @see javax.websocket.Session
     */
    private void getDashBoardState(Session session){
	
	String dashBoardSate = this.dataBase.getDashBoardState();
        this.send(session, JSON.encode(new Message("dashBoardState", dashBoardSate)));
    }

    /**
     * 
     * Start the dashboard
     */
    private void startDashBoard(){
        
	this.dataBase.updateDashBoardState("true");
        this.log("Demarrage du tableau de bord");
    }

    /**
     * 
     * Stop the dashboard
     */
    private void stopDashBoard(){
        
	this.dataBase.updateDashBoardState("false");
        this.log("Arret du tableau de bord");
    }

    /**
     * 
     * Send the list of registered users
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     *
     * @see javax.websocket.Session
     */
    private void getUserList(Session session){
        
        ArrayList userList = this.dataBase.getUserList();
        this.log("Accès à la liste des utilisateurs");
        this.send(session, JSON.encode(new Message("userList", JSON.encode(userList))));
    }
    
    /**
     * 
     * Send the list of registered usernames
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     *
     * @see javax.websocket.Session
     */
    private void getUsernameList(Session session){
        
        ArrayList usernameList = this.dataBase.getUsernameList();
        this.log("Accès à la liste des noms d'utilisateur");
        this.send(session, JSON.encode(new Message("usernameList", JSON.encode(usernameList))));
    }
    
    /**
     * 
     * Update the access of a user
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param data a String to the Json format that represents the user
     * 
     * @see javax.websocket.Session
     */
    private void updateUserAccess(Session session, String data){
        
        User user = (User) JSON.decode(data, User.class);
        boolean result = this.dataBase.updateUserAccess(user);
        this.log(String.format("Mise à jour de l'accès de %s à %s : %s", user.getName(), user.getAccess(), String.valueOf(result)));
        this.send(session, JSON.encode(new Message("updateUserAccessResult", String.valueOf(result))));
    }
    
    /**
     * 
     * Update the password of a user
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param data a String to the Json format that represents the user
     * 
     * @see javax.websocket.Session
     */
    private void updatePassword(Session session, String data){
        
        User user = (User) JSON.decode(data, User.class);
        boolean result = this.dataBase.updatePassword(user);
        this.log(String.format("Mise à jour du mot de passe de %s : %s", user.getName(), String.valueOf(result)));
        this.send(session, JSON.encode(new Message("updatePasswordResult", String.valueOf(result))));
    }
        
    /**
     * 
     * Remove a user
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * @param data a String to the Json format that represents the user
     * 
     * @see javax.websocket.Session
     */
    private void removeUser(Session session, String data){
        
        User user = (User) JSON.decode(data, User.class);
        boolean result = this.dataBase.removeUser(user);
        this.log(String.format("Suppression de %s : %s", user.getName(), String.valueOf(result)));
        this.send(session, JSON.encode(new Message("removeUserResult", String.valueOf(result))));
    }
        
    /**
     * 
     * Write informations about the car in a file and send the link to the client
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * 
     * @see javax.websocket.Session
     */
    private void writeDataFile(Session session){
        
        String fileName = String.format("data_%s.csv", String.valueOf(System.currentTimeMillis()));
        String path = "/usr/share/jetty9/webapps/appEMT/" + fileName;
        ArrayList<CarData> carDataList = this.dataBase.getCarDataList();
        
        try {
            
            String data = "vitesse; consommation; longitude; latitude; altitude; tension; intensite; NbTours; temps; date\n";
            
            for (CarData carData : carDataList){
                
                String dataLine = String.format("%s; %s; %s; %s; %s; %s; %s; %s; %s; %s\n", 
                carData.getSpeed(), 
                carData.getConsumption(),
                carData.getLongitude(),
                carData.getLatitude(),
                carData.getAltitude(),
                carData.getVoltage(),
                carData.getCurrent(),
                carData.getLap(),
                carData.getTime(),
                carData.getDate());
                
                data = data + dataLine;
            }
            
            this.send(session, JSON.encode(new Message("dataFile", data)));
        } catch (Exception ex) {
            this.log(String.format("Erreur lors de la sauvegarde des données : %s", ex.toString()));
        }
    }
    
    /**
     * 
     * Update the route to show on the map
     * 
     * @param data a string that represents a list of points
     */
    private void updateMap(String data){
        
        this.dataBase.updateMap(data);
        this.log("Modification du circuit");
    }
        
    /**
     * 
     * Send the route to show on the map to the client
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * 
     * @see javax.websocket.Session
     */
    private void getMap(Session session){
        
        String map = this.dataBase.getMap();
        this.send(session, JSON.encode(new Message("map", map)));
    }
            
    /**
     * 
     * Send the number of actived sessions
     * 
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * 
     * @see javax.websocket.Session
     */
    private void getSessionNumber(Session session){
        
        String SessionNumber = String.valueOf(session.getOpenSessions().size());
        broadcast(session, JSON.encode(new Message("sessionNumber", SessionNumber)));
        
    }
    
    /**
     * 
     * Write informations in the log file
     * 
     * @param data a string that represents the information to write in the log file
     */
    public void log(String data){
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        
        data = String.format("[%s] %s", dtf.format(now), data);
        System.out.println(data);
        
        String path = "/var/lib/jetty9/log.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(data);
            writer.write('\n');
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ServiceEMT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * Control the speed up strategy
     */
    private void speedUp(){
        
        this.dataBase.setSpeedUp(true);
        
        final Timer timer = new Timer();
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dataBase.setSpeedUp(false);
                timer.cancel();
                timer.purge();
            }
        };
        
        timer.schedule(task, 5000);
    }
    
    /**
     * 
     * Control the slow down strategy
     */
    private void slowDown(){
        
        this.dataBase.setSlowDown(true);
        
        final Timer timer = new Timer();
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dataBase.setSlowDown(false);
                timer.cancel();
                timer.purge();
            }
        };
        
        timer.schedule(task, 5000);
    }
    
    /**
     * 
     * Send the speed strategy
     * 
     * @param session the session of the pilot
     */
    private void getSpeedControl(final Session session){
        
        SpeedControl speedControl = this.dataBase.getSpeedControl();
        this.send(session, JSON.encode(new Message("speedControl", JSON.encode(speedControl))));
        
        final Timer timer = new Timer();
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                
                SpeedControl speedControl = dataBase.getSpeedControl();
                send(session, JSON.encode(new Message("speedControl", JSON.encode(speedControl))));
                timer.cancel();
                timer.purge();
            }
        };
        
        timer.schedule(task, 5000);
    }
    
    /**
     * 
     * Add a recovery code in the database
     * 
     * @param data the informations about the user to recover
     * @param session the connection of the user
     */
    private void add_recovery(String data, Session session){
        
        User user = (User) JSON.decode(data, User.class);
        Random random = new Random();
        
        int digit1 = random.nextInt(9);
        int digit2 = random.nextInt(9);
        int digit3 = random.nextInt(9);
        int digit4 = random.nextInt(9);
        
        String code = String.valueOf(1000*digit1 + 100*digit2 + 10*digit3 + digit4);
        
        if (code.length() == 3) code = "0" + code;
        if (code.length() == 2) code = "00" + code;
        if (code.length() == 1) code = "000" + code;
        if (code.length() == 0) code = "0000";
        
        boolean result = this.dataBase.getUserByMail(user.getMail()) != null;
        
        if (result) {
            this.dataBase.add_recovery(code, user.getMail());
            this.sendMail(user.getMail(), "code", code);
        }
        
        this.send(session, JSON.encode(new Message("recoveryResult", String.valueOf(result))));
        
        
    }
    
    /**
     * 
     * Update the passeword of the user
     * 
     * @param data the informations about the user to recover
     */
    private void recover(String data, Session session){
        
        Recovery recovery = (Recovery) JSON.decode(data, Recovery.class);
        
        boolean result = this.dataBase.recover(recovery.getCode(), recovery.getHash());
        
        this.send(session, JSON.encode(new Message("recoverResult", String.valueOf(result))));
    }
    
    /**
     * 
     * Send the an e-mail
     * 
     * @param to e-mail address of the user
     * @param subject subject of the e-mail
     * @param content content of the e-mail
     */
    private void sendMail(String to, String subject, String content){
        
        String host = "smtp.gmail.com";
        String from = "ecomotionteam.supp@gmail.com";
                
        Properties properties = new Properties(); 
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        
        
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {

            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

                return new javax.mail.PasswordAuthentication("ecomotionteam.supp@gmail.com", "77)WA<7~7Czq=wZt");

            }

        });
        
        try{  
            javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);  
            message.setFrom(new javax.mail.internet.InternetAddress(from));  
            message.addRecipient(javax.mail.Message.RecipientType.TO,new javax.mail.internet.InternetAddress(to));  
            message.setSubject(subject);  
            message.setText(content);  

            
            javax.mail.Transport.send(message);
            this.log("mail envoyé à : " + to);
  
      }catch (Exception e) {
            this.log(e.toString());
      }  
   
        
        
    }
    
    /**
     * 
     * Read the message and call the method that correspond to the action of the message
     * 
     * @param data a string that represents the message
     * @param session an object of type javax.websocket.Session that represents the connection between the server and a client
     * 
     * @see javax.websocket.Session
     */
    public void readMessage(String data, Session session){

        Message message = (Message) JSON.decode(data, Message.class);
        
        switch(message.getAction()){
            
            case "connect":
                this.connect(message.getData(), session);
                break;
            
            case "register":
                this.register(session, message.getData());
                break;
            
            case "carData":
                this.saveCarData(session, message.getData());
                break;

            case "sql":
                this.execute(message.getData());
                break;

            case "signalOnMap":
                this.signalOnMap(session, message.getData());
                break;

            case "removeSignal":
                this.removeSignal(session, message.getData());
                break;
            case "getSessionNumber":
                getSessionNumber(session);
                break;
            case "startDashBoard":
                this.startDashBoard();
                break;
            case "stopDashBoard":
                this.stopDashBoard();
                break;
            case "getDashBoardState":
                this.getDashBoardState(session);
                break;
            case "getUserList":
                this.getUserList(session);
                break;
            case "getUsernameList":
                this.getUsernameList(session);
                break;
            case "updateUserAccess":
                this.updateUserAccess(session, message.getData());
                break;
            case "updatePassword":
                this.updatePassword(session, message.getData());
                break;
            case "removeUser":
                this.removeUser(session, message.getData());
                break;
            case "downloadData":
                this.writeDataFile(session);
                break;
            case "updateMap":
                this.updateMap(message.getData());
                break;
            case "getMap":
                this.getMap(session);
                break;
            case "speedUp":
                this.speedUp();
                break;
            case "slowDown":
                this.slowDown();
                break;
            case "getSpeedControl":
                this.getSpeedControl(session);
                break;
            case "add_recovery":
                this.add_recovery(message.getData(), session);
                break;
            case "recover":
                this.recover(message.getData(), session);
                break;
                
        }
    }
}
