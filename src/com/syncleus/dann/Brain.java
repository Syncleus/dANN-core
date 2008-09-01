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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class Brain implements Serializable
{
    private HashSet<NetworkNode> children = new HashSet<NetworkNode>();
    private HashSet<OutputNeuron> outputNeurons = new HashSet<OutputNeuron>();
    private HashSet<InputNeuron> inputNeurons = new HashSet<InputNeuron>();



    public void addChild(NetworkNode newChild)
    {
        if (this.children.contains(newChild))
            return;

        this.children.add(newChild);

        if (newChild instanceof OutputNeuron)
            this.outputNeurons.add((OutputNeuron) newChild);

        if (newChild instanceof InputNeuron)
            this.inputNeurons.add((InputNeuron) newChild);
    }



    public void removeChild(NetworkNode removeChild)
    {
        if (this.children.contains(removeChild) == false)
            return;
        
        this.children.remove(removeChild);
        
        if (removeChild instanceof OutputNeuron)
            this.outputNeurons.remove((OutputNeuron) removeChild);

        if (removeChild instanceof InputNeuron)
            this.inputNeurons.remove((InputNeuron) removeChild);
    }



    public Set<InputNeuron> getInputNeurons()
    {
        return Collections.unmodifiableSet(this.inputNeurons);
    }



    public Set<OutputNeuron> getOutputNeurons()
    {
        return Collections.unmodifiableSet(this.outputNeurons);
    }



    public Set<NetworkNode> getChildrenNodes()
    {
        return Collections.unmodifiableSet(this.children);
    }



    public Set<Neuron> getChildrenNeuronsRecursivly()
    {
        HashSet<Neuron> allChildren = new HashSet<Neuron>();
        for (NetworkNode child : this.children)
            if (child instanceof Neuron)
            {
                Neuron childNeuron = (Neuron) child;
                allChildren.add(childNeuron);
            }
            else if (child instanceof NeuronGroup)
            {
                NeuronGroup childGroup = (NeuronGroup) child;
                allChildren.addAll(childGroup.getChildrenNeuronsRecursivly());
            }

        return Collections.unmodifiableSet(allChildren);
    }
}
