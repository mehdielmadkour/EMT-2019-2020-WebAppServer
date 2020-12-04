package fr.polytechnancyemt.data;

/**
 *
 * CarData is the class wich contains informations about the car
 * 
 * @author Mehdi EL MADKOUR
 */
public class CarData {
    
    /**
     * The speed of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setSpeed(java.lang.String)
     * @see fr.polytechnancyemt.data.CarData#getSpeed()
     */
    private String speed;
        
    /**
     * The consumption of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setConsumption(java.lang.String)
     * @see fr.polytechnancyemt.data.CarData#getConsumption() 
     */
    private String consumption;
        
    /**
     * The longitude of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setLongitude(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getLongitude() 
     */
    private String longitude;
        
    /**
     * The latitude of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setLatitude(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getLatitude() 
     */
    private String latitude;
        
    /**
     * The altitude of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setAltitude(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getAltitude() 
     */
    private String altitude;
        
    /**
     * The voltage of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setVoltage(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getVoltage() 
     */
    private String voltage;
        
    /**
     * The current of the car
     * 
     * @see fr.polytechnancyemt.data.CarData#setCurrent(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getCurrent() 
     */
    private String current;
        
    /**
     * The number of remaining laps of the run
     * 
     * @see fr.polytechnancyemt.data.CarData#setLap(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getLap() 
     */
    private String lap;
        
    /**
     * The remaining time of the run
     * 
     * @see fr.polytechnancyemt.data.CarData#setTime(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getTime() 
     */
    private String time;
        
    /**
     * The date of the run
     * 
     * @see fr.polytechnancyemt.data.CarData#setDate(java.lang.String) 
     * @see fr.polytechnancyemt.data.CarData#getDate() 
     */
    private String date;

    
    /**
     * 
     * Returns the speed of the car
     * 
     * @return the object of type String that represents the speed of the car
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * 
     * Sets the speed of the car
     * 
     * @param speed a String that represents the speed of the car
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * 
     * Returns the consumption of the car
     * 
     * @return the object of type String that represents the comsumption of the car
     */
    public String getConsumption() {
        return consumption;
    }
    
    /**
     * 
     * Sets the consumption of the car
     * 
     * @param consumption a String that represents the consumption of the car
     */
    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }
    
    /**
     * 
     * Returns the longitude of the car
     * 
     * @return the object of type String that represents the longitude of the car
     */
    public String getLongitude() {
        return longitude;
    }
    
    /**
     * 
     * Sets the longitude of the car
     * 
     * @param longitude a String that represents the longitude of the car
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * Returns the latitude of the car
     * 
     * @return the object of type String that represents the latitude of the car
     */
    public String getLatitude() {
        return latitude;
    }
    
    /**
     * 
     * Sets the latitude of the car
     * 
     * @param latitude a String that represents the latitude of the car
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * Returns the altitude of the car
     * 
     * @return the object of type String that represents the altitude of the car
     */
    public String getAltitude() {
        return altitude;
    }
    
    /**
     * 
     * Sets the altitude of the car
     * 
     * @param altitude a String that represents the altitude of the car
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * 
     * Returns the voltage of the car
     * 
     * @return the object of type String that represents the voltage of the car
     */
    public String getVoltage() {
        return voltage;
    }
    
    /**
     * 
     * Sets the voltage of the car
     * 
     * @param voltage a String that represents the voltage of the car
     */
    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    /**
     * 
     * Returns the current of the car
     * 
     * @return the object of type String that represents the current of the car
     */
    public String getCurrent() {
        return current;
    }
    
    /**
     * 
     * Sets the current of the car
     * 
     * @param current a String that represents the current of the car
     */
    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * 
     * Returns the number of remaining laps of the run
     * 
     * @return the object of type String that represents the remaining number of laps of the run
     */
    public String getLap() {
        return lap;
    }
    
    /**
     * 
     * Sets the remaining number of laps of the run
     * 
     * @param lap a String that represents the remaining number of laps of the run
     */
    public void setLap(String lap) {
        this.lap = lap;
    }

    /**
     * 
     * Returns the remaining time of the run
     * 
     * @return the object of type String that represents the remaining time of the run
     */
    public String getTime() {
        return time;
    }
    
    /**
     * 
     * Sets the remaining time of the run
     * 
     * @param time a String that represents the remaining time of the run
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * Returns the date of the run
     * 
     * @return the object of type String that represents the date of the run
     */
    public String getDate() {
        return date;
    }
    
    /**
     * 
     * Sets the date of the run 
     * 
     * @param date a String that represents the date of the run
     */
    public void setDate(String date) {
        this.date = date;
    } 
    
    /**
     * 
     * Removes fields that guests ar enot allowed to read
     */
    public void setGuestMode(){
        this.voltage = null;
        this.current = null;
    }
}
