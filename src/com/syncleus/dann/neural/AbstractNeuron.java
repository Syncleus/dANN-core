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
package com.syncleus.dann.neural;

import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.HyperbolicTangentActivationFunction;
import java.security.InvalidParameterException;
import java.util.*;


/**
 * An abstract implementation of the Neuron interface. Included activation
 * function handling.
 *
 *
 * @author Syncleus, Inc.
 * @param <SN> Source Neurons allowed to connect to this Neuron.
 * @param <DN> Destination Neurons this Neuron is allowed to connect to.
 * @since 1.0
 *
 */
public abstract class AbstractNeuron<SN extends AbstractNeuron, DN extends AbstractNeuron> implements Neuron<SN, DN>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

    /**
     * Represents the current excitation of the neuron from input
     * signals
     *
     * @since 1.0
     */
    protected double activity;

    /**
     * Represents the current output of the neuron
     *
     * @since 1.0
     */
    protected double output;

    /**
     * The current weight of the bias input. The bias is an input that is always
     * set to an on position. The bias weight usually adapts in the same manner
     * as the rest of the synapse's weights.
     *
     * @since 1.0
     */
    protected double biasWeight;

    /**
     * An array list of all the synapses that this neuron is currently
     * connection out to.
     *
     * @since 1.0
     */
    protected HashSet<Synapse> destinations = new HashSet<Synapse>();

	/**
     * All the synapses currently connecting into this neuron
     *
     * @since 1.0
     */
    private HashSet<Synapse> sources = new HashSet<Synapse>();

	/**
	 * The current activation function used by this neuron. This is used to
	 * calculate the output from the activity.
	 *
	 * @since 1.0
	 */
    protected ActivationFunction activationFunction;

	/**
	 * Random number generator used toproduce any needed random values
	 *
	 * @since 1.0
	 */
	protected static Random random = new Random();
	
	private final static HyperbolicTangentActivationFunction DEFAULT_ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new instance of NeuronImpl with a random bias weight and
	 * HyperbolicTangentActivationFunction as the activation function.
	 *
     *
     * @since 1.0
     */
    public AbstractNeuron()
    {
        this.biasWeight = ((random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = DEFAULT_ACTIVATION_FUNCTION;
    }



	/**
	 * Creates a new instance of NEuronImpl with a random bias weight and the
	 * specified activation function.
	 *
	 *
	 * @param activationFunction The activation function used to calculate the
	 * output fromt he neuron's activity.
	 * @since 1.0
	 */
    public AbstractNeuron(ActivationFunction activationFunction)
    {
        if (activationFunction == null)
            throw new NullPointerException("activationFunction can not be null");


        this.biasWeight = ((this.random.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = activationFunction;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
     * This method is called internally, between Neurons, to
     * facilitate the connection process.<BR>
     *
     * @since 1.0
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.neural.Neuron#connectTo
     */
    public void connectFrom(Synapse inSynapse) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected fromt his neuron

        //add the synapse to the source list
        this.sources.add(inSynapse);
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
    public Synapse connectTo(DN outUnit) throws InvalidConnectionTypeDannException
    {
        //make sure you arent already connected to the neuron
        if (outUnit == null)
            throw new NullPointerException("outUnit can not be null!");

        //connect to the neuron
        Synapse newSynapse = new Synapse(this, outUnit, ((this.random.nextDouble() * 2.0) - 1.0) / 10000.0);
        this.destinations.add(newSynapse);
        outUnit.connectFrom(newSynapse);

		return newSynapse;
    }

    /**
     * Causes the Neuron to disconnect all connections.
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.Neuron#disconnectAllSources
     * @see com.syncleus.dann.neural.Neuron#disconnectAllDestinations
     */
    public void disconnectAll()
    {
        this.disconnectAllDestinations();
        this.disconnectAllSources();
    }



    /**
     * Causes the Neuron to disconnect all outgoing connections.
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.Neuron#disconnectAllSources
     * @see com.syncleus.dann.neural.Neuron#disconnectAll
     */
    public void disconnectAllDestinations()
    {
		Synapse[] destinations = new Synapse[this.destinations.size()];
		this.destinations.toArray(destinations);
		
        for (Synapse currentDestination : destinations)
		{
            try
            {
                this.disconnectDestination(currentDestination);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
		}
    }



    /**
     * Causes the Neuron to disconnect all incomming connections.
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.Neuron#disconnectAllDestinations
     * @see com.syncleus.dann.neural.Neuron#disconnectAll
     */
    public void disconnectAllSources()
    {
		Synapse[] sources = new Synapse[this.sources.size()];
		this.sources.toArray(sources);
		
        for (Synapse currentSource : sources)
		{
            try
            {
                this.disconnectSource(currentSource);
            }
            catch (SynapseNotConnectedException caughtException)
            {
                caughtException.printStackTrace();
                throw new InternalError("Unexpected Runtime Exception: " + caughtException);
            }
		}
    }



    /**
     * Disconnects from a perticular outgoing connection.
	 *
     *
     * @since 1.0
     * @param outSynapse The outgoing synapse to disconnect from.
     * @see com.syncleus.dann.neural.NeuronImpl#removeSource
	 * @throws SynapseNotConnectedException Thrown if the specified synapse isnt
	 * currently connected.
     */
    public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not disconnect a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect destination, does not exist.");

        try
        {
            if (outSynapse.getDestination() != null)
                outSynapse.getDestination().removeSource(outSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            throw new SynapseNotConnectedException("can not disconnect destination, does not exist.", caughtException);
        }
    }



    /**
     * Disconnects from a perticular incomming connection.
	 *
     *
     * @since 1.0
     * @param inSynapse The incomming synapse to disconnect from.
     * @see com.syncleus.dann.neural.NeuronImpl#removeDestination
	 * @throws SynapseNotConnectedException Thrown if the specified synapse isnt
	 * currently connected.
     */
    public void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseNotConnectedException("can not disconnect source, does not exist.");

        try
        {
            if (inSynapse.getSource() != null)
                inSynapse.getSource().removeDestination(inSynapse);
        }
        catch (SynapseDoesNotExistException caughtException)
        {
            throw new SynapseNotConnectedException("can not disconnect source, does not exist.", caughtException);
        }
    }



    /**
     * Called internally to facilitate the removal of a connection.
	 *
     *
     * @since 1.0
     * @param outSynapse The incomming synapse to remove from memory.
     * @see com.syncleus.dann.neural.Neuron#disconnectSource
     */
    protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof OutputNeuron)
            throw new InvalidParameterException("Can not remove a destination for a OutputNeuron");

        if (this.destinations.remove(outSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     *
     * @since 1.0
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.neural.Neuron#disconnectDestination
     */
    protected void removeSource(Synapse inSynapse) throws SynapseDoesNotExistException
    {
        if (this instanceof InputNeuron)
            throw new InvalidParameterException("Can not disconnect a source for a InputNeuron");

        if (this.sources.remove(inSynapse) == false)
            throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }



	/**
	 * Gets all the destination Synapses this neuron's output is connected to.
	 *
	 *
	 * @return An unmodifiable Set of destination Synapses
	 * @since 1.0
	 */
    public Set<Synapse> getDestinations()
    {
        return Collections.unmodifiableSet(this.destinations);
    }



	/**
	 * Gets all the Neurons that either connect to, or are connected from, this
	 * Neuron.
	 *
	 *
	 * @return An unmodifiable Set of source and destination Neurons.
	 * @since 1.0
	 */
    public Set<Neuron> getNeighbors()
    {
        HashSet<Neuron> neighbors = new HashSet<Neuron>();
        for (Synapse source : this.getSources())
            neighbors.add(source.getSource());
        for (Synapse destination : this.getDestinations())
            neighbors.add(destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
    }



	/**
	 * Get all the source Neuron's connecting to this Neuron.
	 *
	 *
	 * @return An unmodifiable Set of source Neurons.
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
    public Set<SN> getSourceNeighbors()
    {
        HashSet<SN> neighbors = new HashSet<SN>();
		try
		{
			for (Synapse sourceSynapse : this.getSources())
				neighbors.add((SN)sourceSynapse.getSource());
		}
		catch(ClassCastException caughtException)
		{
			throw new AssertionError(caughtException);
		}

        return Collections.unmodifiableSet(neighbors);
    }



	/**
	 * Get all the destination Neuron's this Neuron connects to.
	 *
	 *
	 * @return An unmodifiable Set of destination Neurons.
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
    public Set<DN> getDestinationNeighbors()
    {
        HashSet<DN> neighbors = new HashSet<DN>();
        for (Synapse destination : this.getDestinations())
            neighbors.add((DN)destination.getDestination());

        return Collections.unmodifiableSet(neighbors);
    }

	/**
	 * Gets all the source Synapses connected to this neuron.
	 *
	 *
	 * @return An unmodifiable Set of source Synapses
	 * @since 1.0
	 */
    public Set<Synapse> getSources()
    {
        return Collections.unmodifiableSet(sources);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">

    /**
     * sets the current output on all outgoing synapses.
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#propagate
     * @param newOutput The output value.
     */
    protected void setOutput(double newOutput)
    {
        this.output = newOutput;

        for (Synapse current : this.destinations)
            current.setInput(newOutput);
    }



    /**
     * Gets the current output.
	 *
     *
     * @since 1.0
     * @return The current output.
     */
    protected double getOutput()
    {
        return this.output;
    }



    /**
     * obtains the output as a function of the current activity. This is a bound
     * function (usually between -1 and 1) based on the current activity of the
     * neuron.
	 *
     *
     * @since 1.0
     * @return a bound value (between -1 and 1 if this function is not
     * 	overwritten). It is a function of the neuron's current activity.
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#propagate
     */
    protected double activate()
    {
        return this.activationFunction.activate(this.activity);
    }



    /**
     * This must be the derivity of the ActivityFunction. As such it's output is
     * also based on the current activity of the neuron. If the
     * activationFunction is overwritten then this method must also be
     * overwritten with the proper derivative.
	 *
     *
     * @since 1.0
     * @return the derivative output of the activationFunction
     * @see com.syncleus.dann.neural.NeuronImpl#activationFunction
     */
    protected double activateDerivitive()
    {
        return this.activationFunction.activateDerivative(this.activity);
    }

	
    /**
     * Propogates the current output to all outgoing synapses.
	 *
     *
     * @since 1.0
     */
    public void propagate()
    {
        //calculate the current input activity
        this.activity = 0;
        for (Synapse currentSynapse : this.getSources())
            this.activity += currentSynapse.getInput() * currentSynapse.getWeight();
        //Add the bias to the activity
        this.activity += this.biasWeight;

        //calculate the activity function and set the result as the output
        this.setOutput(this.activate());
    }

    // </editor-fold>
}
