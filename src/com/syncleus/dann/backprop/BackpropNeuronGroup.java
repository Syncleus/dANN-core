package com.syncleus.dann.backprop;
import com.syncleus.dann.DNA;
import com.syncleus.dann.NeuronGroup;
import java.util.Set;

public class BackpropNeuronGroup extends NeuronGroup<BackpropNeuron>
{
    public BackpropNeuronGroup(DNA ownedDNAToSet)
    {
        super(ownedDNAToSet);
    }


    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * Propogates the output of the NetworkNodes from the incoming synapse to
     * the outgoign one.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#propagate
     */
    public void propagate()
    {
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.propagate();
    }



    /**
     * Back propogates the taining set of the NetworkNodes from the outgoing
     * synapse to the incomming one.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NetworkNode#backPropagate
     */
    public void backPropagate()
    {
        for (BackpropNeuron currentChild : this.getChildrenNeuronsRecursivly())
            currentChild.backPropagate();
    }

   public Set<BackpropNeuron> getNeighbors()
    {
        throw new Error("Not yet implemented");
    }



    public Set<BackpropNeuron> getSourceNeighbors()
    {
        throw new Error("Not yet implemented");
    }



    public Set<BackpropNeuron> getDestinationNeighbors()
    {
        throw new Error("Not yet implemented");
    }


    // </editor-fold>

}
