package com.syncleus.dann;

import java.io.Serializable;
import java.util.HashSet;


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



    public HashSet<InputNeuron> getInputNeurons()
    {
        return new HashSet<InputNeuron>(this.inputNeurons);
    }



    public HashSet<OutputNeuron> getOutputNeurons()
    {
        return new HashSet<OutputNeuron>(this.outputNeurons);
    }



    public HashSet<NetworkNode> getChildrenNodes()
    {
        return new HashSet<NetworkNode>(this.children);
    }



    public HashSet<Neuron> getChildrenNeuronsRecursivly()
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

        return allChildren;
    }
}
