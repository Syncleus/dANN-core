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


/**
 * This represents a blackbox unit that takes any number of inputs and produces
 * anynumber of outputs. The inputs and outputs all connect to other
 * ProcessingUnits.
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public abstract class ProcessingUnit implements java.io.Serializable
{
    /**
     * A shared random number generator.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected static final Random random = new Random();



    /**
     * This method is called externally to connect to another ProcessingUnit.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outUnit The ProcessingUnit to connect to.
     * @see com.syncleus.dann.ProcessingUnit#connectFrom
     */
    public abstract void connectTo(ProcessingUnit outUnit);



    /**
     * This method is called internally, between ProcessingUnits, to
     * facilitate the connection process.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.ProcessingUnit#connectTo
     */
    protected abstract void connectFrom(Synapse inSynapse);



    /**
     * Causes the ProcessingUnit to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.ProcessingUnit#disconnectAllSources
     * @see com.syncleus.dann.ProcessingUnit#disconnectAll
     */
    public abstract void disconnectAllDestinations();



    /**
     * Causes the ProcessingUnit to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.ProcessingUnit#disconnectAllDestinations
     * @see com.syncleus.dann.ProcessingUnit#disconnectAll
     */
    public abstract void disconnectAllSources();



    /**
     * Causes the ProcessingUnit to disconnect all connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.ProcessingUnit#disconnectAllSources
     * @see com.syncleus.dann.ProcessingUnit#disconnectAllDestinations
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
     * @see com.syncleus.dann.ProcessingUnit#removeSource
     * @throws SynapseNotConnectedException Thrown if the specified synapse is not
     * 	currently connected.
     */
    public abstract void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException;



    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.ProcessingUnit#removeDestination
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
     * @see com.syncleus.dann.ProcessingUnit#disconnectSource
     * @throws SynapseDoesNotExistException Thrown if the specified synapse
     *	does not exist as a source synapse.
     */
    protected abstract void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException;



    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.ProcessingUnit#disconnectDestination
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
}
