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
package com.syncleus.dann;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.NetworkNode
 */
public class Synapse implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The outgoing neuron connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronImpl destination;
    /**
     * The incomming neuron connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronImpl source;
    /**
     * The current weight of the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double weight;
    /**
     * The current output of the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double output;
    /**
     * The current input from the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double input;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of Synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public Synapse(NeuronImpl sourceToSet, NeuronImpl destinationToSet, double initialWeight)
    {
        this.destination = destinationToSet;
        this.source = sourceToSet;
        this.weight = initialWeight;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Link Traversal">
    /**
     * Obtains the incomming neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The source neuron.
     */
    public NeuronImpl getSource()
    {
        return this.source;
    }



    /**
     * Obtains the outgoing neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The destination neuron.
     */
    public NeuronImpl getDestination()
    {
        return this.destination;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * Calculates the current output of the synapse based on the input and
     * weight<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#propagate
     * @return the current synapse output.
     */
    public double getOutput()
    {
        this.output = this.input * this.weight;
        return this.output;
    }



    /**
     * Set the current input for the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#propagate
     * @param newInput The new input value to set.
     */
    public void setInput(double newInput)
    {
        this.input = newInput;
    }

	public void setWeight(double newWeight)
	{
		this.weight = newWeight;
	}

    public double getWeight()
    {
        return weight;
    }

	public double getInput()
	{
		return input;
	}
	// </editor-fold>
}
