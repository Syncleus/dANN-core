package com.syncleus.dann;

import java.io.Serializable;
import java.util.Set;


public interface Neuron<SN extends NeuronImpl, SS extends Synapse<? extends SN, ? extends NeuronImpl>, DN extends NeuronImpl, DS extends Synapse<? extends NeuronImpl, ? extends DN>> extends Serializable
{
    public void connectTo(DN outUnit) throws InvalidConnectionTypeDannException;
    public void disconnectAll();
    public void disconnectAllDestinations();
    public void disconnectAllSources();
    public void disconnectDestination(DS outSynapse) throws SynapseNotConnectedException;
    public void disconnectSource(SS inSynapse) throws SynapseNotConnectedException;
    public Set<DS> getDestinations();
    public Set<Neuron> getNeighbors();
    public Set<SN> getSourceNeighbors();
    public Set<DN> getDestinationNeighbors();
    public Set<SS> getSources();
}
