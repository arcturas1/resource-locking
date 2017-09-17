
/**
 * Solution development for 4-bit/2-disclosure device.
 *
 * @version API: 4.1.4
 * <br>
 * Source code: 4.1.7
 * @author Dr. Jody Paul: API specification, 
 * <br>
 *  Heather DeMarco, Jonathan Grant: Source code matching API
 *  
 * @see <a href = "http://jodypaul.com/cs/sweprin/deviceProj/projectDescription.html"> Project Description </a>
 * 
 */

public class FourBitTwoDisclosureDeviceUnlocker extends DeviceUnlocker {
	
	/*
	 * Suppress Instantiation of objects from this utility class
	 */
	private FourBitTwoDisclosureDeviceUnlocker() {
		
	}
	
	
    /**
     *Unlocks a resource controlled by a 4-bit/2-disclosure device. Behavior is unspecified if parameter is not a reference to a valid 4-bit/2-disclosure device.
     *
     * @param  dev - the device controlling the resource to unlock; must be a 4-bit device with 2 peek/poke bits.
     * @return  true if the resource is successfully unlocked (all bits are now identical); false otherwise
     */
    public static boolean unlock(Device dev){
        //Set up variables
        boolean deviceUnlocked = false; //denotes whether the device is unlocked, assumed false
        final int max = 100;		//The max number of spins per cycle of the heuristic
        final int MAX_CYCLES = 3;		//The max number of unlocking attempts to be made
        final CharSequence STANDARD_PATTERN = "??--"; // the typical peek pattern.
        final CharSequence ALTERNATIVE_PATTERN = "--??"; // an alternative peek pattern.
        CharSequence peekRequestPattern; //the pattern to peek from the device
        CharSequence pokePattern; 		//the pattern to poke into the device
        CharSequence peekResponse; 		//the partial state obtained by peeking into the device

        // Reset the trace
        FourBitTwoDisclosureDeviceUnlocker.log(null);

        // We're staring the unlock
        FourBitTwoDisclosureDeviceUnlocker.log("Unlock start");

        //Unlocking heuristic: 
        	//loop halts after MAX_CYCLES iterations of MAX_SPINS attempts
        	//to unlock the device, or when the device is unlocked.
        unlockHeuristic:{//begin unlockHeuristic
        	for(int j = 0; j < MAX_CYCLES; j ++) {
            	
        		if(j%2==0) { // if the cycle number is even, use the standard pattern for peek/poke
        			peekRequestPattern = STANDARD_PATTERN;
        			pokePattern = STANDARD_PATTERN.toString().replace('?', 'T');
        		}else { //otherwise use the alternative pattern
        			peekRequestPattern = ALTERNATIVE_PATTERN;
        			pokePattern = ALTERNATIVE_PATTERN.toString().replace('?', 'T');
        		}
        			
            	for(int i=0; i <= max; i++) {
                    deviceUnlocked = dev.spin();
                    
                    //If the device is unlocked, log the state and exit the heuristic loop
                    if (deviceUnlocked) {
                        FourBitTwoDisclosureDeviceUnlocker.log("Spin: unlocked");
                        break unlockHeuristic;
                    }

                    FourBitTwoDisclosureDeviceUnlocker.log("Spin: locked");
                    FourBitTwoDisclosureDeviceUnlocker.log("Peek pattern: " + peekRequestPattern.toString());

                    peekResponse = dev.peek(peekRequestPattern);
                    FourBitTwoDisclosureDeviceUnlocker.log("Peek result: " + peekResponse.toString());

                    FourBitTwoDisclosureDeviceUnlocker.log("Poke: " + pokePattern.toString());
                    dev.poke(pokePattern);

                    FourBitTwoDisclosureDeviceUnlocker.log(dev.toString());

                }
            	
            }
        }//end unlockHeuristic
        
        FourBitTwoDisclosureDeviceUnlocker.log("Unlock Successful: " +  deviceUnlocked);

        return deviceUnlocked;
    }

}
