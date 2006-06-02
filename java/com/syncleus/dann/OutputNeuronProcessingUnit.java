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
 * This is a special type of neuron that provides the output.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
  * @see com.syncleus.dann.InputNeuronProcessingUnit
 */
public class OutputNeuronProcessingUnit extends NeuronProcessingUnit implements java.io.Serializable
{
    /**
	  * holds the value for the current training set.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
	 protected double desired = 0;
    
    /**
     * Creates a new instance of OutputNeuronProcessingUnit<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public OutputNeuronProcessingUnit(DNA ownedDNAToSet) 
    {
        super(ownedDNAToSet);
    }
    
    /**
	  * This method sets the current training set on the neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param trainingToSet sets the current training set.
	  */
    public void setDesired(double trainingToSet)
    {
        this.desired = trainingToSet;
    }
	 
    /**
     * Calculates the Delta Train based on all the destination synapses<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#backPropogate
     */
    public void calculateDeltaTrain()
    {
			this.deltaTrain = 0;
			Object[] SynapseArray = super.destination.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 this.deltaTrain += CurrentSynapse.getDifferential();
			}
			
			super.deltaTrain += (this.desired - super.getOutput());
			
			super.deltaTrain *= super.activationFunctionDerivitive();
    }
    
}
