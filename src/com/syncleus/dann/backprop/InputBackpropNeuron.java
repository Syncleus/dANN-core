/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.backprop;

import com.syncleus.dann.*;
import java.security.InvalidParameterException;
import com.syncleus.dann.activation.*;


/**
 * This is a special type of BackpropNeuron that receives input.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 0.1
 * @version 0.1
 */
public class InputBackpropNeuron extends BackpropNeuron implements InputNeuron<NeuronImpl, BackpropNeuron>
{
    /**
     * Holds the current input value for this neuron
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double input;



    /**
     * Creates a new instance of InputBackpropNeuron
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public InputBackpropNeuron()
    {
        super();
    }

    /**
     * Creates a new instance of InputBackpropNeuron that uses the specified
	 * activation function.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public InputBackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }

	/**
	 * Creates a new instance of this class with the specified learning rate.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param learningRate The learning rate for this neuron.
	 * @since 0.1
	 */
	public InputBackpropNeuron(double learningRate)
	{
		super(learningRate);
	}

	/**
	 * Creates a new instance of this class with the specified activation
	 * function and learning rate.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param activationFunction The activation function to use.
	 * @param learningRate The learning rate to use.
	 * @since 0.1
	 */
	public InputBackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction, learningRate);
	}


    /**
     * This method sets the current input on the neuron.
	 *
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
     * Refreshes the output of the neuron based on the current input
	 * 
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
	@Override
    public void propagate()
    {
        this.setOutput(this.input);
    }
    

	/**
	 * Back propogates and learns from the destination neurons. This should
	 * be called successivly from the output neurons back towards the input
	 * neurons on all BackpropNeurons.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	@Override
    public void backPropagate()
    {
        this.calculateDeltaTrain();
    }

    /**
     * This method is called internally, between Neurons, to
     * facilitate the connection process.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.Neuron#connectTo
     */
	@Override
    protected void connectFrom(Synapse inSynapse) throws InvalidConnectionTypeDannException
    {
        throw new InvalidConnectionTypeDannException("Can not connect a neuron to an InputNeuron");
    }
}
