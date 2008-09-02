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

import com.syncleus.dann.*;
import java.util.Hashtable;
import java.util.Set;


public class BrainAssociativeMap extends AssociativeMap
{
    private Brain brain;
    private int dimentions;
    private Hashtable<Neuron, NetworkNodeAssociativeNode> neurons = new Hashtable<Neuron, NetworkNodeAssociativeNode>();
    
    public BrainAssociativeMap(Brain brain, int dimentions)
    {
        this.brain = brain;
        this.dimentions = dimentions;
    }
    
    public void refresh()
    {
        this.nodes.clear();
        this.neurons.clear();
        
        Set<Neuron> neurons = this.brain.getChildrenNeuronsRecursivly();
        for(Neuron neuron : neurons)
        {
            NetworkNodeAssociativeNode node = new NetworkNodeAssociativeNode(this, this.dimentions, neuron);
            this.nodes.add(node);
            this.neurons.put(neuron, node);
        }
        
        for(AssociativeNode node : this.nodes)
            if(node instanceof NetworkNodeAssociativeNode)
                ((NetworkNodeAssociativeNode)node).refresh();
    }
    
    NetworkNodeAssociativeNode getNodeFromNeuron(NetworkNode node)
    {
        return neurons.get(node);
    }
}
