package fr.polytechnancyemt.data;

/**
 *
 * SpeedControl is the class that contains the informations about the strategy the pilot should follow
 * 
 * @author mehdi
 */
public class SpeedControl {
    
    /**
     * Tell if the pilot should speed up
     * 
     * @see fr.polytechnancyemt.data.SpeedControl#isSpeedUp() 
     * @see fr.polytechnancyemt.data.SpeedControl#setSpeedUp(boolean) 
     */
    private boolean speedUp = false;
    
    /**
     * Tell if the pilot should slow down
     * 
     * @see fr.polytechnancyemt.data.SpeedControl#isSlowDown() 
     * @see fr.polytechnancyemt.data.SpeedControl#setSlowDown(boolean) 
     */
    private boolean slowDown = false;

    /**
     * 
     * @return the speed up strategy 
     */
    public boolean isSpeedUp() {
        return speedUp;
    }

    /**
     * 
     * Sets the speed up strategy
     * 
     * @param speedUp a boolean that represents the speed up strategy
     */
    public void setSpeedUp(boolean speedUp) {
        this.speedUp = speedUp;
    }

    /**
     * 
     * @return the slow down strategy 
     */
    public boolean isSlowDown() {
        return slowDown;
    }

    /**
     * 
     * Sets the speed up strategy
     * 
     * @param slowDown a boolean that represents the slow down strategy
     */
    public void setSlowDown(boolean slowDown) {
        this.slowDown = slowDown;
    }
    
    
}
