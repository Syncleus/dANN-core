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
package com.syncleus.dann.neural.som;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.IdentityActivationFunction;

/**
 * An input neuron for a SOM network. It essentialy just propgates the input
 * unchanged to the next layer.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public class SomInputNeuron extends NeuronImpl<NeuronImpl, SomNeuron> implements InputNeuron<NeuronImpl, SomNeuron>
{
	private double input;
	private static final IdentityActivationFunction ACTIVATION_FUNCTION = new IdentityActivationFunction();

	/**
	 * Creates a default SomInputNeuron using an IdentityActivationFunction
	 *
	 * @since 2.0
	 */
	public SomInputNeuron()
	{
		super(ACTIVATION_FUNCTION);
	}

	/**
	 * Propogates the input to all connected SomNeurons.
	 * 
	 * @since 2.0
	 */
	@Override
	public void propagate()
	{
		this.activity = this.input;
		this.setOutput(this.activity);
	}

	/**
	 * Sets the current input for this neuron.
	 *
	 * @since 2.0
	 * @param inputToSet The new input value you want to set.
	 */
	public void setInput(double inputToSet)
	{
		this.input = inputToSet;
	}

	public double getInput()
	{
		return this.input;
	}

    /**
     * This method is called externally to connect to another Neuron.
	 *
     *
     * @since 1.0
     * @param outUnit The Neuron to connect to.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException Not
	 * thrown, but children are allowed to throw this exception.
     * @see com.syncleus.dann.neural.NeuronImpl#connectFrom
     */
    public Synapse connectTo(SomNeuron outUnit) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected to the neuron
        if (outUnit == null)
            throw new NullPointerException("outUnit can not be null!");

        //connect to the neuron
        Synapse newSynapse = new Synapse(this, outUnit, this.random.nextDouble());
        this.destinations.add(newSynapse);
        outUnit.connectFrom(newSynapse);

		return newSynapse;
    }

    /**
	 * This should never be called, This class can not be connected to, only
	 * send. Any call to this method will always throw an
	 * InvalidConnectionTypeDannException.
     *
	 * @since 2.0
	 * @param outUnit This is ignored.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException Always
	 * thrown
     * @see com.syncleus.dann.neural.Neuron#connectTo
     */
	@Override
    public void connectFrom(Synapse inSynapse) throws InvalidConnectionTypeDannException
	{
		throw new InvalidConnectionTypeDannException("SOM networks' SomInputNeuron can not receive connections from any neurons.");
	}
}
