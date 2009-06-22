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
package com.syncleus.dann.neural.backprop;

import com.syncleus.dann.neural.*;
import java.security.InvalidParameterException;
import com.syncleus.dann.neural.activation.*;


/**
 * This is a special type of BackpropNeuron that receives input.
 *
 *
 * @author Syncleus, Inc.
 * @since 1.0
 *
 */
public class InputBackpropNeuron extends BackpropNeuron implements InputNeuron<NeuronImpl, BackpropNeuron>
{
    /**
     * Holds the current input value for this neuron
	 *
     *
     * @since 1.0
     */
    protected double input;



    /**
     * Creates a new instance of InputBackpropNeuron
	 *
     *
     * @since 1.0
     */
    public InputBackpropNeuron()
    {
        super();
    }

    /**
     * Creates a new instance of InputBackpropNeuron that uses the specified
	 * activation function.
	 *
     *
     * @since 1.0
     */
    public InputBackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }

	/**
	 * Creates a new instance of this class with the specified learning rate.
	 *
	 *
	 * @param learningRate The learning rate for this neuron.
	 * @since 1.0
	 */
	public InputBackpropNeuron(double learningRate)
	{
		super(learningRate);
	}

	/**
	 * Creates a new instance of this class with the specified activation
	 * function and learning rate.
	 *
	 *
	 * @param activationFunction The activation function to use.
	 * @param learningRate The learning rate to use.
	 * @since 1.0
	 */
	public InputBackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction, learningRate);
	}


    /**
     * This method sets the current input on the neuron.
	 *
     *
     * @since 1.0
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
     *
     * @since 1.0
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
	 *
	 * @since 1.0
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
     *
     * @since 1.0
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.neural.Neuron#connectTo
     */
	@Override
    protected void connectFrom(Synapse inSynapse) throws InvalidConnectionTypeDannException
    {
        throw new InvalidConnectionTypeDannException("Can not connect a neuron to an InputNeuron");
    }
}
