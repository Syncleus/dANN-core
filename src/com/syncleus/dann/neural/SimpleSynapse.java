/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.neural;

import com.syncleus.dann.graph.SimpleDirectedEdge;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.
 * 
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 * @see com.syncleus.dann.neural.Neuron
 */
public class SimpleSynapse extends SimpleDirectedEdge<Neuron> implements Synapse, java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

    /**
     * The current weight of the synapse.
     *
     * @since 1.0
     */
    private double weight;

    /**
     * The current output of the synapse.
     *
     * @since 1.0
     */
    private double output;

    /**
     * The current input from the synapse.
     *
     * @since 1.0
     */
    private double input;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new instance of SimpleSynapse
	 *
     *
     * @since 1.0
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public SimpleSynapse(Neuron sourceToSet, Neuron destinationToSet, double initialWeight)
    {
		super(sourceToSet, destinationToSet);

        this.weight = initialWeight;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">


    /**
     * Set the current input for the synapse.
	 *
     *
     * @since 1.0
     * @param newInput The new input value to set.
     */
    public void setInput(double newInput)
    {
        this.input = newInput;
    }

	/**
	 * Set the weight of the synapse.
	 *
	 *
	 * @param newWeight new weight for the synapse.
	 * @since 1.0
	 */
	public void setWeight(double newWeight)
	{
		this.weight = newWeight;
	}

	/**
	 * Get the weight of the synapse.
	 *
	 *
	 * @return The current weight of the synapse.
	 * @since 1.0
	 */
    public double getWeight()
    {
        return weight;
    }

	/**
	 * Get the current input of the synapse.
	 *
	 *
	 * @return The current input of the synapse.
	 * @since 1.0
	 */
	public double getInput()
	{
		return input;
	}
	// </editor-fold>
}
