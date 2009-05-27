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
package com.syncleus.dann.backprop;

import com.syncleus.dann.*;

public class BackpropSynapse extends Synapse<BackpropNeuron, BackpropNeuron>
{
    /**
     * The current synapse's deltaTrain<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double deltaTrain;

    /**
     * Creates a new instance of Synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public BackpropSynapse(BackpropNeuron sourceToSet, BackpropNeuron destinationToSet, double initialWeight)
    {
        super(sourceToSet, destinationToSet, initialWeight);
    }

    /**
     * learns the new weight based on the current training set<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#calculateDeltaTrain
     */
    public void learnWeight(double deltaTrainToSet, double learningRate)
    {
        this.deltaTrain = deltaTrainToSet;
        this.setWeight( this.getWeight() + (learningRate * this.getInput() * this.deltaTrain) );
    }

    /**
     * Calculates the synapse differential. This is used when back propogating
     * training sets.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#backPropagate
     * @return the current synapse differential.
     */
    public double getDifferential()
    {
        return this.deltaTrain * this.getWeight();
    }   
}
