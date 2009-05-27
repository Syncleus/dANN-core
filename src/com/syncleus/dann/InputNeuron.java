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

import java.security.InvalidParameterException;
import com.syncleus.dann.activation.*;


/**
 * This is a special type of neuron that receives input.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.OutputNeuron
 */
public class InputNeuron extends Neuron
{
    /**
     * Holds the current input value for this neuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double input;



    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public InputNeuron(DNA ownedDNAToSet)
    {
        super(ownedDNAToSet);
    }

    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public InputNeuron(DNA ownedDNAToSet, ActivationFunction activationFunction)
    {
        super(ownedDNAToSet, activationFunction);
    }


    /**
     * This method sets the current input on the neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inputToSet The value to set the current input to.
     */
    public void setInput(double inputToSet)
    {
        if( Math.abs(inputToSet) > 1.0 )
            throw new InvalidParameterException("InputToSet must be between -1 and +1");
        
        this.input = inputToSet;
    }



    /**
     * Refreshes the output of the neuron based on the current input<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public void propagate()
    {
        this.setOutput(this.input);
    }
    
    
    public void backPropagate()
    {
        this.calculateDeltaTrain();
    }
    
    protected void connectFrom(Synapse inSynapse) throws InvalidConnectionTypeDannException
    {
        throw new InvalidConnectionTypeDannException("Can not connect a neuron to an InputNeuron");
    }
}
