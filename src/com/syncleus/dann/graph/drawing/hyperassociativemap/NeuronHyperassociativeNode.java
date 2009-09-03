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
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.Neuron;
import java.util.HashSet;
import org.apache.log4j.Logger;


/**
 * A HyperassociativeNode that is representing a Neuron.
 *
 *
 * @author Syncleus, Inc.
 *
 * @since 1.0
 */
public class NeuronHyperassociativeNode extends HyperassociativeNode
{
    private Neuron neuron;
	private final static Logger LOGGER = Logger.getLogger(NeuronHyperassociativeNode.class);
    

	/**
	 * Initializes a new NeuronHyperassociativeNode that is a part of the
	 * specified Map, and backs the specified neuron.
	 *
	 *
	 * @param network the map this node will belong to.
	 * @param backingNeuron the neuron backing this node.
	 * @since 1.0
	 */
    public NeuronHyperassociativeNode(BrainHyperassociativeMap network, Neuron backingNeuron)
    {
        super(network, randomCoordinates(network.getDimensions()));
        
        this.neuron = backingNeuron;
    }



	/**
	 * Initializes a new NeuronHyperassociativeNode that is a part of the
	 * specified Map, and backs the specified neuron, as well as using the
	 * specified initial point.
	 *
	 *
	 * @param network the map this node will belong to.
	 * @param location the initial Vector location of this node.
	 * @param backingNeuron the neuron backing this node.
	 * @since 1.0
	 */
    public NeuronHyperassociativeNode(BrainHyperassociativeMap network, Vector location, Neuron backingNeuron)
    {
        super(network, location);

        this.neuron = backingNeuron;
    }



	/**
	 * Gets the map this node belongs to.
	 *
	 *
	 * @return the map this node belongs to.
	 * @since 1.0
	 */
    @Override
    protected BrainHyperassociativeMap getNetwork()
    {
        return (BrainHyperassociativeMap) super.getNetwork();
    }



	/**
	 * Refreshes the associations of this node based off the current connections
	 * of the backing neuron.
	 *
	 *
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
    void refresh()
    {
        this.dissociateAll();

        HashSet<Neuron> neurons = new HashSet<Neuron>();
		try
		{
			neurons.addAll(this.getNetwork().getBrain().getNeighbors(this.neuron));
		}
		catch(ClassCastException caughtException)
		{
			LOGGER.error("unexpected class cast exception whn getting neighbors", caughtException);
			throw new UnexpectedDannError("unexpected class cast exception whn getting neighbors", caughtException);
		}
        for (Neuron neighborNeuron : neurons)
            this.associate(this.getNetwork().getNodeFromNeuron(neighborNeuron), 1.0);
    }



	/**
	 * Gets the backing neuron.
	 *
	 *
	 * @return the backing neuron.
	 * @since 1.0
	 */
    public Neuron getNeuron()
    {
        return neuron;
    }
}
