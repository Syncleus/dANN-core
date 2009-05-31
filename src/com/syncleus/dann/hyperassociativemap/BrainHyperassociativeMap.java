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
package com.syncleus.dann.hyperassociativemap;

import com.syncleus.dann.backprop.InputBackpropNeuron;
import com.syncleus.dann.backprop.BackpropNeuron;
import com.syncleus.dann.backprop.OutputBackpropNeuron;
import com.syncleus.dann.*;
import java.util.Hashtable;
import java.util.Set;


public class BrainHyperassociativeMap extends HyperassociativeMap
{
    private Brain brain;
    private int dimensions;
    private Hashtable<Neuron, NeuronHyperassociativeNode> neurons = new Hashtable<Neuron, NeuronHyperassociativeNode>();



    public BrainHyperassociativeMap(Brain brain, int dimensions)
    {
		super(dimensions);
		
        this.brain = brain;
        this.dimensions = dimensions;

        this.refresh();
    }



    public void refresh()
    {
        this.nodes.clear();
        this.neurons.clear();

        Set<Neuron> brainNeurons = this.brain.getNeurons();
        for (Neuron neuron : brainNeurons)
        {
            NeuronHyperassociativeNode node = new NeuronHyperassociativeNode(this, this.dimensions, neuron);
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
 



    NeuronHyperassociativeNode getNodeFromNeuron(Neuron node)
    {
        return neurons.get(node);
    }
}
