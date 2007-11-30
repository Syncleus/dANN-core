/******************************************************************************
*                                                                             *
*  Copyright: (c) Jeffrey Phillips Freeman                                    *
*                                                                             *
*  You may redistribute and modify this source code under the terms and       *
*  conditions of the Open Source Community License - Type C version 1.0       *
*  or any later version as published by syncleus at http://www.syncleus.com.  *
*  There should be a copy of the license included with this file. If a copy   *
*  of the license is not included you are granted no right to distribute or   *
*  otherwise use this file except through a legal and valid license. You      *
*  should also contact syncleus at the information below if you cannot find   *
*  a license:                                                                 *
*                                                                             *
*  Syncleus                                                                   *
*  1116 McClellan St.                                                         *
*  Philadelphia, PA 19148                                                     *
*                                                                             *
******************************************************************************/

package com.syncleus.dann;

/**
 * This is a special type of neuron that receives input.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
  * @see com.syncleus.dann.OutputNeuronProcessingUnit
 */
public class InputNeuronProcessingUnit extends NeuronProcessingUnit implements java.io.Serializable
{
	/**
	 * Holds the current input value for this neuron<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	protected double input = 0;
    
    /**
     * Creates a new instance of InputNeuronProcessingUnit<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public InputNeuronProcessingUnit(DNA ownedDNAToSet)
    {
        super(ownedDNAToSet);
    }
    
    /**
	  * This method sets the current input on the neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param inputToSet The value to set the current input to.
	  */
    public void setInput(double inputToSet)
    {
        this.input = inputToSet;
    }
	 
	/**
	 * Refreshes the output of the neuron based on the current input<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	 public void propogate()
	 {
			//calculate the current input activity
			this.activity = 0;
			for( Synapse currentSynapse : super.sources )
			{
				this.activity += currentSynapse.getOutput();
			}
			//Add the bias to the activity
			super.activity += super.biasWeight;
			
			//add the input to the activity
			super.activity += this.input;
        
			//calculate the activity function and set the result as the output
			super.setOutput( super.activationFunction() );
	 }
}
