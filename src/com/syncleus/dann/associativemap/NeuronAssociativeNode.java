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
package com.syncleus.dann.associativemap;

import com.syncleus.dann.Neuron;
import java.util.HashSet;


public class NeuronAssociativeNode extends AssociativeNode
{
    private Neuron neuron;
    
    
    public NeuronAssociativeNode(BrainAssociativeMap network, int dimensions, Neuron networkNode)
    {
        super(network, randomCoordinates(dimensions));
        
        this.neuron = networkNode;
    }



    public NeuronAssociativeNode(BrainAssociativeMap network, Hyperpoint location, Neuron networkNode)
    {
        super(network, location);

        this.neuron = networkNode;
    }



    @Override
    protected BrainAssociativeMap getNetwork()
    {
        return (BrainAssociativeMap) super.getNetwork();
    }



    void refresh()
    {
        this.dissociateAll();

        HashSet<Neuron> neurons = new HashSet<Neuron>();
        neurons.addAll(this.neuron.getNeighbors());
        for (Neuron neighborNeuron : neurons)
            this.associate(this.getNetwork().getNodeFromNeuron(neighborNeuron), 1.0);
    }



    public Neuron getNeuron()
    {
        return neuron;
    }
}
