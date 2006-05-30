package com.syncleus.dann;


import java.util.Random;

/**
 * Largly under developed this class will contain or generate a basic list of
 * properties used by the individual neurons, and layers. This will be the Class
 * genetic algorithms should inherit from.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class DNA 
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
	
	/**
	 * The learning rate is used by neurons to determine how influential each
	 * training set will be on its current state.<BR>
    * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 * @see com.syncleus.dann.Synapse#LearnWeight
	 */
	public double LearningRate = 0.02;
	
	/**
	 * A random number generator shared by the system<BR>
    * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	public static final Random RandomGenerator = new Random();
	
	// </editor-fold>
	 
   // <editor-fold defaultstate="collapsed" desc="Constructors">
	
    /**
	  * Creates a new instance of DNA with default values.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public DNA()
    {
    }
	 
	 // </editor-fold>
}
