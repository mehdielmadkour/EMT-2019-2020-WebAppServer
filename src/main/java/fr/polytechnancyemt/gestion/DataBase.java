package fr.polytechnancyemt.gestion;

import fr.polytechnancyemt.data.CarData;
import fr.polytechnancyemt.data.KeyPair;
import fr.polytechnancyemt.data.Point;
import fr.polytechnancyemt.data.SpeedControl;
import fr.polytechnancyemt.data.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;

/**
 *
 * DataBase is the class that controls the access to the informations
 * 
 * @author Mehdi EL MADKOUR
 */
public class DataBase {
    
    /**
     * 
     * The connection to the database
     * 
     * @see java.sql.Connection
     */
    private Connection conn;
    
    /**
     * 
     * Initialises the connection to the database
     */
    public DataBase(){
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            String DBurl = "jdbc:mysql://localhost:3306/emt_db?useSSL=false";
            conn = DriverManager.getConnection(DBurl, "root", "Hf36jdp280007CB49CC334.");
            
        } catch (SQLException | ClassNotFoundException ex) {
             
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * Execute an SQL query
     * 
     * @param query a string that represents the sql query
     * 
     * @return the object of type ResultSet that represents the result of the query
     * 
     * @see java.sql.ResultSet
     */
    public ResultSet executeQuery(String query){
        
        try {

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            return resultSet;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * 
     * Execute an SQL update
     * 
     * @param query a string that represents the sql query
     */
    public void executeUpdate(String query){
        
        try {
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
        }
        catch (SQLException ex){
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * Create a new user in the database
     * 
     * @param user an object of type User that represents the user
     * 
     * @return a boolean that represents the result of the query
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public boolean addUser(User user){
        
        if (getUserByName(user.getName()) == null){
            String query = String.format("INSERT INTO emt_users(name, hash, mail) VALUES ('%s', '%s', '%s')", user.getName(), user.getHash(), user.getMail());
            executeUpdate(query);

            int id = getUserId(user.getName());
            query = String.format("INSERT INTO emt_users_access(Id, access) VALUES (%s, 'guest')", String.valueOf(id));
            executeUpdate(query);
            
            return this.getUserByName(user.getName()) != null;
        }
        else return false;
    }
    
    /**
     * 
     * Create a new user in the database
     * 
     * @param user an object of type User that represents the user
     * @param access a String that represents the access
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public void setUserAccess(User user, String access){

        if (getUserByName(user.getName()) == null){
            String query = String.format("UPDATE emt_users_access SET access = '%s' WHERE Id = %s", access, String.valueOf(user.getId()));
            executeUpdate(query);
        }
    }
        
    /**
     * 
     * Find the id corresponding to the user
     * 
     * @param name a string representing the name of the user
     * 
     * @return an int that represents the user id
     */
    public int getUserId(String name){

        int id = -1;
        try {
            String query = String.format("SELECT * FROM emt_users WHERE name = '%s'", name);
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                id = resultSet.getInt("Id");

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
        
    /**
     * 
     * Find the user corresponding to the username
     * 
     * @param name a string representing the name of the user
     * 
     * @return the object of type User that represents the user
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public User getUserByName(String name){
        
        User user = null;

        try {
            String query = String.format("SELECT * FROM emt_users WHERE name = '%s'", name);
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                int id = resultSet.getInt("Id");
                String hash = resultSet.getString("hash");
                String mail = resultSet.getString("mail");
                
                user = new User();

                user.setId(id);
                user.setName(name);
                user.setHash(hash);
                user.setMail(mail);

            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            String query = String.format("SELECT * FROM emt_users_access WHERE Id = %s", String.valueOf(user.getId()));
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                String access = resultSet.getString("access");

                user.setAccess(access);

            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return user;
    }
    
    /**
     * 
     * Save informations about the car in the database
     * 
     * @param carData an object of type CarData
     * 
     * @see fr.polytechnancyemt.data.CarData
     */
    public void saveCarData(CarData carData){
        
        String query = String.format("INSERT INTO emt_cardata(vitesse, consommation, longitude, latitude, altitude, tension, intensite, NbTours, temps, date) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", 
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
        
        executeUpdate(query);
    }
    
    /**
     * 
     * Return last informations about the car
     * 
     * @return an object of type CarData
     * 
     * @see fr.polytechnancyemt.data.CarData
     */
    public CarData getCarData(){
        
        try {
            String query = String.format("SELECT * FROM emt_cardata WHERE ID = MAX(ID)");
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                CarData carData = new CarData();
                
                carData.setSpeed(resultSet.getString("vitesse"));
                carData.setConsumption(resultSet.getString("consommation"));
                carData.setLongitude(resultSet.getString("longitude"));
                carData.setLatitude(resultSet.getString("latitude"));
                carData.setAltitude(resultSet.getString("altitude"));
                carData.setVoltage(resultSet.getString("tension"));
                carData.setCurrent(resultSet.getString("intensite"));
                carData.setLap(resultSet.getString("NbTours"));
                carData.setTime(resultSet.getString("temps"));
                carData.setDate(resultSet.getString("date"));
                
                return carData;
            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
        
    /**
     * 
     * Return all informations about the car
     * 
     * @return an ArrayList of CarData
     * 
     * @see fr.polytechnancyemt.data.CarData
     * @see java.util.ArrayList
     */
    public ArrayList<CarData> getCarDataList(){
        
        try {
            String query = String.format("SELECT * FROM emt_cardata");
            ResultSet resultSet = executeQuery(query);
            
            ArrayList<CarData> carDataList = new ArrayList<>();
            
            while (resultSet.next()){
                
                CarData carData = new CarData();
                
                carData.setSpeed(resultSet.getString("vitesse"));
                carData.setConsumption(resultSet.getString("consommation"));
                carData.setLongitude(resultSet.getString("longitude"));
                carData.setLatitude(resultSet.getString("latitude"));
                carData.setAltitude(resultSet.getString("altitude"));
                carData.setVoltage(resultSet.getString("tension"));
                carData.setCurrent(resultSet.getString("intensite"));
                carData.setLap(resultSet.getString("NbTours"));
                carData.setTime(resultSet.getString("temps"));
                carData.setDate(resultSet.getString("date"));
                
                carDataList.add(carData);
                
            }
            return carDataList;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * 
     * Save a signal in the database
     * 
     * @param point an object of type Point
     * 
     * @see fr.polytechnancyemt.data.Point
     */
    public void saveSignal(Point point){

        String query = String.format("INSERT INTO emt_map_signal(Id, lng, lat, type) VALUES (%s, '%s', '%s', '%s')", point.getId(), point.getLng(), point.getLat(), point.getType());
        executeUpdate(query);
    }

    /**
     * 
     * Remove a signal in the database
     * 
     * @param id a String that represents the id of the point
     */
    public void removeSignal(String id){
        String query = String.format("DELETE FROM emt_map_signal WHERE Id='%s'", id);
        executeUpdate(query);
    }

    /**
     * 
     * Update the dashboard state
     * 
     * @param value a String that represents the new state of the dashboard
     */
    public void updateDashBoardState(String value){
        
        String query = String.format("UPDATE emt_dashboard SET running='%s'", value);
        executeUpdate(query);
    }

    /**
     * 
     * Read the state of the dashboard
     * 
     * @return a String that represents the state of the dashboard 
     */
    public String getDashBoardState(){

        try {
            String query = String.format("SELECT * FROM emt_dashboard");
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                return resultSet.getString("running");
            }
            else return "false";
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return "false";
        }
    }
    
    /**
     * 
     * List the users registered in the database
     * 
     * @return an ArrayList of User
     * 
     * @see java.util.ArrayList
     * @see fr.polytechnancyemt.data.User
     */
    public ArrayList<User> getUserList(){
        
        try {
            String query = String.format("SELECT name FROM emt_users");
            ResultSet resultSet = executeQuery(query);
            
            ArrayList<User> userList = new ArrayList<>();
            
            while (resultSet.next()) {
                
                User user = this.getUserByName(resultSet.getString("name"));
                userList.add(user);
                
            }
            
            return userList;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
        
    /**
     * 
     * List the usernames registered in the database
     * 
     * @return an ArrayList of Strings that represents usernames
     * 
     * @see java.util.ArrayList
     */
    public ArrayList<String> getUsernameList(){
        
        try {
            String query = String.format("SELECT name FROM emt_users");
            ResultSet resultSet = executeQuery(query);
            
            ArrayList<String> userList = new ArrayList<>();
            
            while (resultSet.next()) {
                
                userList.add(resultSet.getString("name"));
                
            }
            
            return userList;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * 
     * Update the access of a user
     * 
     * @param user an object of type User
     * 
     * @return a boolean that represents the result of the update
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public boolean updateUserAccess(User user){
        
        String name = user.getName();
        String access = user.getAccess();
        
        user = this.getUserByName(name);
        int Id = user.getId();
        
        String query = String.format("UPDATE emt_users_access SET access='%s' WHERE Id=%s", access, Id);
        executeUpdate(query);
        
        user = this.getUserByName(name);
        return access.equals(user.getAccess());
    }
        
    /**
     * 
     * Update the password of a user
     * 
     * @param user an object of type User
     * 
     * @return a boolean that represents the result of the update
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public boolean updatePassword(User user){
        
        String name = user.getName();
        String hash = user.getHash();
        
        user = this.getUserByName(name);
        int Id = user.getId();
        
        String query = String.format("UPDATE emt_users SET hash='%s' WHERE Id=%s", hash, Id);
        executeUpdate(query);
        
        user = this.getUserByName(name);
        return hash.equals(user.getHash());
    }
            
    /**
     * 
     * Remove a user from the database
     * 
     * @param user an object of type User
     * 
     * @return a boolean that represents the result of the update
     * 
     * @see fr.polytechnancyemt.data.User
     */
    public boolean removeUser(User user){
        
        String name = user.getName();
        user = this.getUserByName(name);
        String query = String.format("DELETE FROM emt_users WHERE Id=%s", user.getId());
        executeUpdate(query);
        query = String.format("DELETE FROM emt_users_access WHERE Id=%s", user.getId());
        executeUpdate(query);
        
        user = this.getUserByName(name);
        return user == null;
    }
    
    /**
     * 
     * Update the route on the map
     * 
     * @param data a String that represents a list of points
     */
    public void updateMap(String data){
        
        String query = String.format("UPDATE emt_map SET points='%s'", data);
        executeUpdate(query);
    }
    
    /**
     * 
     * Read the route to show on the map
     * 
     * @return a String that represents a list of points
     */
    public String getMap(){
        
        try {
            String query = String.format("SELECT points FROM emt_map");
            ResultSet resultSet = executeQuery(query);
            
            String map = "[]";
            
            while (resultSet.next()) {
                
                map = resultSet.getString("points");
                
            }
            
            return map;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return "[]";
        }
    }
    
    /**
     * 
     * Return the KeyPair object associated with a specified access
     * 
     * @param access a string that represents the access
     * 
     * @return the object of type KeyPair that contains the public and private keys 
     */
    public KeyPair getKeys(String access){
        
        KeyPair keyPair = null;
            
        try {
            String query = String.format("SELECT * FROM emt_keys WHERE access='%s'", access);
            ResultSet resultSet = executeQuery(query);
            
            
            while (resultSet.next()) {
                
                String privateKey = resultSet.getString("privateKey");
                String publicKey = resultSet.getString("publicKey");
                
                
                keyPair = new KeyPair(privateKey, publicKey);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return keyPair;
    }
    
    /**
     * 
     * Save keys is the database
     * 
     * @param keyPair an object of type KeyPair that contains the public and the private keys
     * @param access a string that represents the acces
     */
    public void setKeys(KeyPair keyPair, String access){
            
        String query = String.format("INSERT INTO emt_keys(access, privateKey, publicKey) VALUES ('%s', '%s', '%s')", access, keyPair.getPrivateKey(), keyPair.getPublicKey());
        executeUpdate(query);
        
    }
    
    /**
     * Sets the speed up strategy
     * 
     * @param speedUp a boolean that represents the speed up strategy 
     */
    public void setSpeedUp(boolean speedUp) {
        
        String query = String.format("UPDATE emt_speed_control SET speedUp='%s'", String.valueOf(speedUp));
        executeUpdate(query);
    }
    
    /**
     * Sets the slow down strategy
     * 
     * @param slowDown a boolean that represents the slow down strategy 
     */
    public void setSlowDown(boolean slowDown) {
        
        String query = String.format("UPDATE emt_speed_control SET slowDown='%s'", String.valueOf(slowDown));
        executeUpdate(query);
    }
    
    /**
     * 
     * @return speed strategy
     */
    public SpeedControl getSpeedControl(){
        
        SpeedControl speedControl = new SpeedControl();
        
        try {
            String query = String.format("SELECT * FROM emt_speed_control");
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                speedControl.setSpeedUp(resultSet.getBoolean("speedUp"));
                speedControl.setSlowDown(resultSet.getBoolean("slowDown"));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return speedControl;
    }
    
    /**
     * 
     * Returns the user associated with an e-mail address
     * 
     * @param mail the e-mail address of the user
     * 
     * @return the object of type User
     */
    public User getUserByMail(String mail){
        
        User user = null;

        try {
            String query = String.format("SELECT * FROM emt_users WHERE mail = '%s'", mail);
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                int id = resultSet.getInt("Id");
                String hash = resultSet.getString("hash");
                String name = resultSet.getString("name");
                
                user = new User();

                user.setId(id);
                user.setName(name);
                user.setHash(hash);
                user.setMail(mail);

            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            String query = String.format("SELECT * FROM emt_users_access WHERE Id = %s", String.valueOf(user.getId()));
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                String access = resultSet.getString("access");

                user.setAccess(access);

            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return user;
    }
    
    /**
     * 
     * Associate a recovery code with an e-mail address
     * 
     * @param code the recovery code
     * @param mail the e-mail address
     */
    public void add_recovery(String code, String mail){
        
        User user = this.getUserByMail(mail);
        
        String query = String.format("INSERT INTO emt_user_recovery VALUES ('%s', '%s')", user.getName(), code);
        executeUpdate(query);
        
    }
    
    /**
     * 
     * Update the hash of the user
     * 
     * @param code the recovery code
     * @param hash the new hash
     */
    public boolean recover(String code, String hash){
        
        User user = null;
        
        try {
            String query = String.format("SELECT * FROM emt_user_recovery WHERE code = '%s'", code);
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                String name = resultSet.getString("name");

                user = getUserByName(name);
                 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (user != null) {
            String query = String.format("DELETE FROM emt_user_recovery WHERE code='%s'", code);
            executeUpdate(query);
            
            user.setHash(hash);
            
            this.updatePassword(user);
            
            return true;
        }
        
        return false;
        
    }
}
