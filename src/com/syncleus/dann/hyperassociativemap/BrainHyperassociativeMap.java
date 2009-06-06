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
package com.syncleus.dann.hyperassociativemap;

import com.syncleus.dann.*;
import java.util.*;


/**
 * An hyperassociative map used to represent a Brain.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @version 0.1
 * @since 0.1
 */
public class BrainHyperassociativeMap extends HyperassociativeMap
{
    private Brain brain;
    private int dimensions;

	/**
	 * A Hashtable containing all the neurons in the brain and their counterpart
	 * NeuronHyperassociativeNode.
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    protected Hashtable<Neuron, NeuronHyperassociativeNode> neurons = new Hashtable<Neuron, NeuronHyperassociativeNode>();

	/**
	 * Initializes a new BrainHyperassociativeMap using the specified brain and
	 * dimensions
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param brain Brain to represent by this map
	 * @param dimensions Number of dimensions to represent this brain in
	 * @since 0.1
	 */
    public BrainHyperassociativeMap(Brain brain, int dimensions)
    {
		super(dimensions);
		
        this.brain = brain;
        this.dimensions = dimensions;

        this.refresh();
    }



	/**
	 * Refresh this Map by pulling any new neurons from the brain as well as any
	 * new associations between nodes.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    public void refresh()
    {
        this.nodes.clear();
        this.neurons.clear();

        Set<Neuron> brainNeurons = this.brain.getNeurons();
        for (Neuron neuron : brainNeurons)
        {
            NeuronHyperassociativeNode node = new NeuronHyperassociativeNode(this, neuron);
            this.nodes.add(node);
            this.neurons.put(neuron, node);
        }
        
        for(HyperassociativeNode node : this.nodes)
            if( node instanceof NeuronHyperassociativeNode)
                ((NeuronHyperassociativeNode)node).refresh();

        for (OutputNeuron neuron : this.brain.getOutputNeurons())
            for (OutputNeuron toNeuron : this.brain.getOutputNeurons())
                if(neuron != toNeuron)
                    this.neurons.get(neuron).associate(this.neurons.get(toNeuron), 0.5);

        for (InputNeuron neuron : this.brain.getInputNeurons())
            for (InputNeuron toNeuron : this.brain.getInputNeurons())
                if(neuron != toNeuron)
                    this.neurons.get(neuron).associate(this.neurons.get(toNeuron), 0.5);
 
    }
 



	/**
	 * Gets the NeuronHyperassociativeNode lined with the specified neuron.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param node Neuron to get Node from.
	 * @return Node The Node used to represent the neuron.
	 * @since 0.1
	 */
    NeuronHyperassociativeNode getNodeFromNeuron(Neuron node)
    {
        return neurons.get(node);
    }
}
