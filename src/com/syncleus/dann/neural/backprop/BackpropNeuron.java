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

import com.syncleus.dann.neural.InvalidConnectionTypeDannException;
import com.syncleus.dann.neural.SynapseNotConnectedException;
import com.syncleus.dann.neural.NeuronImpl;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.SynapseDoesNotExistException;
import com.syncleus.dann.*;
import com.syncleus.dann.neural.activation.ActivationFunction;
import java.util.Hashtable;


/**
 * The BackpropNeuron is the most fundemental component of a backprop network;
 * it is also the proccessor of the system. One neuron will usually connect to
 * many other Neurons through synapses and receive input from many other
 * Neurons in the same way.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 1.0
 * @version 1.0
 * @see com.syncleus.dann.Synapse
 */
public class BackpropNeuron extends NeuronImpl<NeuronImpl, BackpropNeuron>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

    /**
     * This represents the net effect of all the training data from all the
     * inputs. It is essentially the reverse of the activity value.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.NeuronImpl#activity
     */
    protected double deltaTrain = 0;

	private double learningRate = 0.001;

	/**
	 * A hashtable which contains the current delta train for each of the
	 * destination synapses.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 1.0
	 */
	protected Hashtable<Synapse, Double> deltaTrainDestinations = new Hashtable<Synapse, Double>();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new default instance of BackpropNeuron
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public BackpropNeuron()
    {
		super();
    }



	/**
	 * Creates a new instance of BackpropNeuron with the specified activation
	 * function.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param activationFunction The Neuron's activation function.
	 * @since 1.0
	 */
    public BackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }


	/**
	 * Creates a new instance of a BackpropNeuron using the default activation
	 * function with the specified learning rate.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param learningRate learning rate of this neuron.
	 * @since 1.0
	 */
	public BackpropNeuron(double learningRate)
	{
		super();
		this.learningRate = learningRate;
	}

	/**
	 * Creates a new instance of a BackpropNeuron with the specified activtion
	 * function and learning rate.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param activationFunction Activation function for this neuron.
	 * @param learningRate Learning rate for this neuron.
	 */
	public BackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction);
		this.learningRate = learningRate;
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
     * This method is called externally to connect to another BackpropNeuron.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param outUnit The BackpropNeuron to connect to.
     * @see com.syncleus.dann.NeuronImpl#connectFrom
     */
    public void connectTo(BackpropNeuron outUnit) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected to the neuron
        if (outUnit == null)
            throw new NullPointerException("outUnit can not be null!");

        //connect to the neuron
        Synapse newSynapse = new Synapse(this, outUnit, ((this.random.nextDouble() * 2.0) - 1.0) / 10000.0);
        this.destinations.add(newSynapse);
		this.deltaTrainDestinations.put(newSynapse, Double.valueOf(0.0));
        outUnit.connectFrom(newSynapse);
    }

    /**
     * Called internally to facilitate the removal of a connection.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param outSynapse The incomming synapse to remove from memory.
     * @see com.syncleus.dann.Neuron#disconnectSource
     */
	@Override
	protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
	{
		super.removeDestination(outSynapse);
		this.deltaTrainDestinations.remove(outSynapse);
	}

    /**
     * Disconnects from a perticular outgoing connection.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param outSynapse The outgoing synapse to disconnect from.
     * @see com.syncleus.dann.NeuronImpl#removeSource
	 * @throws SynapseNotConnectedException Thrown if the specified synapse isnt
	 * currently connected.
     */
	@Override
	public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
	{
		super.disconnectDestination(outSynapse);
		this.deltaTrainDestinations.remove(outSynapse);
	}

	// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">

    /**
     * Backpropogates the training data to all the incomming synapses.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public void backPropagate()
    {
        this.calculateDeltaTrain();

        //step thru source synapses and make them learn their new weight.
        for (Synapse currentSynapse : this.getSources())
		{
			NeuronImpl sourceNeuron = currentSynapse.getSource();
			if(sourceNeuron instanceof BackpropNeuron)
			{
				BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;

				sourceBackpropNeuron.deltaTrainDestinations.put(currentSynapse, Double.valueOf(this.deltaTrain));

				currentSynapse.setWeight(currentSynapse.getWeight() + (this.deltaTrain * this.learningRate * currentSynapse.getInput()));
			}
		}

        //learn the biases new weight
        this.biasWeight += this.learningRate * this.deltaTrain;
    }



    /**
     * Calculates the Delta Train based on all the destination synapses
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.backprop.BackpropNeuron#backPropagate
     */
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : this.destinations)
            this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());
        this.deltaTrain *= this.activateDerivitive();
    }



	/**
	 * Gets the current delta train of the neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return The delta train of the neuron.
	 * @since 1.0
	 */
    public double getDeltaTrain()
    {
        return deltaTrain;
    }
    // </editor-fold>
}
