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

import java.io.Serializable;
import java.util.Set;


/**
 * Interface representing the general methods common to all types of neurons.
 * All neurons will connect to other neurons as well as be able to disconnect.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @param <SN> Source Neurons allowed to connect to this Neuron.
 * @param <DN> Destination Neurons this Neuron is allowed to connect to.
 * @since 1.0
 * @version 1.0
 */
public interface Neuron<SN extends NeuronImpl, DN extends NeuronImpl> extends Serializable
{
	/**
	 * Connects this Neuron to the specified Neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param outUnit The Neuron to connect to.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException The
	 * specified neuron to connect to is not valid.
	 * @since 1.0
	 */
    public void connectTo(DN outUnit) throws InvalidConnectionTypeDannException;

    /**
     * Causes the Neuron to disconnect all connections.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.Neuron#disconnectAllSources
     * @see com.syncleus.dann.Neuron#disconnectAllDestinations
     */
    public void disconnectAll();

    /**
     * Causes the Neuron to disconnect all outgoing connections.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.Neuron#disconnectAllSources
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllDestinations();

    /**
     * Causes the Neuron to disconnect all incomming connections.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @see com.syncleus.dann.Neuron#disconnectAllDestinations
     * @see com.syncleus.dann.Neuron#disconnectAll
     */
    public void disconnectAllSources();

    /**
     * Disconnects from a perticular outgoing connection.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.NeuronImpl#removeSource
	 * @throws SynapseNotConnectedException Thrown if the specified synapse isnt
	 * currently connected.
     */
    public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException;

    /**
     * Disconnects from a perticular incomming connection.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param inSynapse The incomming synapse to disconnect from.
     * @see com.syncleus.dann.NeuronImpl#removeDestination
	 * @throws SynapseNotConnectedException Thrown if the specified synapse isnt
	 * currently connected.
     */
    public void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException;

    /**
     * Propogates the current output to all outgoing synapses.
	 *
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
	public void propagate();

	/**
	 * Gets all the destination Synapses this neuron's output is connected to.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of destination Synapses
	 * @since 1.0
	 */
    public Set<Synapse> getDestinations();

	/**
	 * Gets all the source Synapses connected to this neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of source Synapses
	 * @since 1.0
	 */
	public Set<Synapse> getSources();

	/**
	 * Gets all the Neurons that either connect to, or are connected from, this
	 * Neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of source and destination Neurons.
	 * @since 1.0
	 */
    public Set<Neuron> getNeighbors();

	/**
	 * Get all the source Neuron's connecting to this Neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of source Neurons.
	 * @since 1.0
	 */
    public Set<SN> getSourceNeighbors();

	/**
	 * Get all the destination Neuron's this Neuron connects to.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return An unmodifiable Set of destination Neurons.
	 * @since 1.0
	 */
    public Set<DN> getDestinationNeighbors();
}
