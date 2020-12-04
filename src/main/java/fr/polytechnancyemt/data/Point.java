package fr.polytechnancyemt.data;

/**
 *
 * Point is the class which contains informations about a signal on the map
 * 
 * @author Mehdi EL MADKOUR
 */
public class Point {
    
    /**
     * The Id of the point
     * 
     * @see fr.polytechnancyemt.data.Point#getId() 
     * @see fr.polytechnancyemt.data.Point#setId(java.lang.String)  
     */
    private String id;
        
    /**
     * The longitude of the point
     * 
     * @see fr.polytechnancyemt.data.Point#getLng() 
     * @see fr.polytechnancyemt.data.Point#setLng(java.lang.String) 
     */
    private String lng;
        
    /**
     * The latitude of the point
     * 
     * @see fr.polytechnancyemt.data.Point#getLat() 
     * @see fr.polytechnancyemt.data.Point#setLat(java.lang.String) 
     */
    private String lat;
        
    /**
     * The type of signal :
     * <ul>
     * <li>accident</li>
     * <li>traffic</li>
     * </ul>
     * 
     * @see fr.polytechnancyemt.data.Point#getType() 
     * @see fr.polytechnancyemt.data.Point#setType(java.lang.String) 
     */
    private String type;
    
    /**
     * 
     * @return the object of type String that represents the Id of the point
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id a String 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return the object of type String that represents the longitude of the point
     */
    public String getLng() {
        return this.lng;
    }

    /**
     * 
     * @param lng a String
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * 
     * @return the object of type String that represents the latitude of the point
     */
    public String getLat() {
        return this.lat;
    }

    /**
     * 
     * @param lat a String 
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * 
     * @return the object of type String that represents the type of the point
     */
    public String getType() {
        return this.type;
    }
}
