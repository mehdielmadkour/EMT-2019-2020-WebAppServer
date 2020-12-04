/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytechnancyemt.gestion;

import fr.polytechnancyemt.data.CarData;
import fr.polytechnancyemt.data.User;
import fr.polytechnancyemt.gestion.DataBase;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mehdi
 */
public class DataBase {
    
    private Connection conn;
    
    public DataBase(){
        
        try {
            
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //String DBurl = "jdbc:derby://localhost:1527/EmtDB;create=true;user=root;password=root";
            String DBurl = "DBI:mysql:database=BaseEMT;host=localhost;port=3306;create=true;user=root;password=Hf36jdp280007CB49CC334.";
            conn = DriverManager.getConnection(DBurl);
            
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
             
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
    public void executeUpdate(String query){
        
        try {
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
        }
        catch (SQLException ex){
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getNewID(String table){
        
        try {
            
            ResultSet resultSet = executeQuery(String.format("SELECT MAX(ID) FROM %s", table));
            
            if (resultSet.next()){
                int maxId = resultSet.getInt(1);
                return maxId + 1;
            }
            else return 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public void addUser(User user){
        
        if (getUserByName(user.getName()) == null){
            int id = getNewID("USERS");
            String query = String.format("INSERT INTO USERS VALUES (%s, '%s', '%s')", String.valueOf(id), user.getName(), user.getHash());
            executeUpdate(query);
        }
    }
    
    public User getUserByName(String name){
        
        try {
            String query = String.format("SELECT * FROM USERS WHERE USERS.NAME = '%s'", name);
            
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                int id = resultSet.getInt("ID");
                String hash = resultSet.getString("HASH");

                User user = new User();

                user.setId(id);
                user.setName(name);
                user.setHash(hash);

                return user;
            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }
    
    public void saveCarData(CarData carData){
        
        int id = getNewID("CARDATA");
        
        String query = String.format("INSERT INTO CARDATA VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)", 
                carData.getVitesse(), 
                carData.getConsommation(),
                carData.getLongitude(),
                carData.getLatitude(),
                carData.getAltitude(),
                carData.getTension(),
                carData.getIntensite(),
                carData.getNbTours(),
                carData.getTemps(),
                carData.getDate(),
                id);
        
        Pattern word = Pattern.compile("null");
        Matcher match = word.matcher(query);
        
        if (!match.find()) executeUpdate(query);
        
    }
    
    public CarData getCarData(){
        
        try {
            String query = String.format("SELECT * FROM CARDATA WHERE ID = MAX(ID)");
            ResultSet resultSet = executeQuery(query);
            
            if (resultSet.next()){
                
                CarData carData = new CarData();
                
                carData.setVitesse(resultSet.getString("VITESSE"));
                carData.setConsommation(resultSet.getString("CONSOMMATION"));
                carData.setLongitude(resultSet.getString("LONGITUDE"));
                carData.setLatitude(resultSet.getString("LATITUDE"));
                carData.setAltitude(resultSet.getString("ALTITUDE"));
                carData.setTension(resultSet.getString("TENSION"));
                carData.setIntensite(resultSet.getString("INTENSITE"));
                carData.setNbTours(resultSet.getString("NBTOURS"));
                carData.setTemps(resultSet.getString("TEMPS"));
                carData.setDate(resultSet.getString("DATE"));
                
                return carData;
            }
            else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
