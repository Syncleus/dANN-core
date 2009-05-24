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

import com.syncleus.dann.activation.*;

/**
 * This is a special type of neuron that provides the output.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.InputNeuron
 */
public class OutputNeuron extends Neuron
{
    /**
     * holds the value for the current training set.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double desired;



    /**
     * Creates a new instance of OutputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public OutputNeuron(DNA ownedDNAToSet)
    {
        super(ownedDNAToSet);
    }
    
    /**
     * Creates a new instance of OutputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public OutputNeuron(DNA ownedDNAToSet, ActivationFunction activationFunction)
    {
        super(ownedDNAToSet, activationFunction);
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
     * @see com.syncleus.dann.Neuron#backPropagate
     */
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : super.destinations)
            this.deltaTrain += currentSynapse.getDifferential();

        super.deltaTrain += (this.desired - super.getOutput());

        super.deltaTrain *= super.activateDerivitive();
    }
    
    public void connectTo(NetworkNode outUnit) throws DannException
    {
        throw new DannException("Can not connect from a OutputNeuron");
    }
}
