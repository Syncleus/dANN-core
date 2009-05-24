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

import java.util.Random;
import java.util.Set;


/**
 * This represents a blackbox unit that takes any number of inputs and produces
 * anynumber of outputs. The inputs and outputs all connect to other
 * NetworkNodes.
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public abstract class NetworkNode implements java.io.Serializable
{
    /**
     * A shared random number generator.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected static final Random random = new Random();



    /**
     * This method is called externally to connect to another NetworkNode.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outUnit The NetworkNode to connect to.
     * @see com.syncleus.dann.NetworkNode#connectFrom
     */
    public abstract void connectTo(NetworkNode outUnit) throws DannException;



    /**
     * This method is called internally, between NetworkNodes, to
     * facilitate the connection process.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.NetworkNode#connectTo
     */
    protected abstract void connectFrom(Synapse inSynapse) throws DannException;



    /**
     * Causes the NetworkNode to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllSources
     * @see com.syncleus.dann.NetworkNode#disconnectAll
     */
    public abstract void disconnectAllDestinations();



    /**
     * Causes the NetworkNode to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllDestinations
     * @see com.syncleus.dann.NetworkNode#disconnectAll
     */
    public abstract void disconnectAllSources();



    /**
     * Causes the NetworkNode to disconnect all connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#disconnectAllSources
     * @see com.syncleus.dann.NetworkNode#disconnectAllDestinations
     */
    public void disconnectAll()
    {
        this.disconnectAllDestinations();
        this.disconnectAllSources();
    }



    /**
     * Disconnects from a perticular outgoing connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.NetworkNode#removeSource
     * @throws SynapseNotConnectedException Thrown if the specified synapse is not
     * 	currently connected.
     */
    public abstract void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException;



    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.NetworkNode#removeDestination
     * @throws SynapseNotConnectedException Thrown if the specified synapse is not
     * 	currently connected.
     */
    public abstract void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException;



    /**
     * Called internally to facilitate the removal of a connection. It removes
     * the specified synapse from memory assuming it has already been
     * disconnected<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.NetworkNode#disconnectSource
     * @throws SynapseDoesNotExistException Thrown if the specified synapse
     *	does not exist as a source synapse.
     */
    protected abstract void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException;



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.NetworkNode#disconnectDestination
     * @throws SynapseDoesNotExistException Thrown if the specified synapse
     *	does not exist as a source synapse.
     */
    protected abstract void removeSource(Synapse inSynapse) throws SynapseDoesNotExistException;



    /**
     * Backpropogates the training data to all the incomming synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public abstract void backPropagate();



    /**
     * Propogates the current output to all outgoing synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public abstract void propagate();
    
    public abstract Set<NetworkNode> getNeighbors();
    public abstract Set<NetworkNode> getSourceNeighbors();
    public abstract Set<NetworkNode> getDestinationNeighbors();
}
