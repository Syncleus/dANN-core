package com.syncleus.dann;

public interface InputNeuron<SN extends NeuronImpl, SS extends Synapse<? extends SN, ? extends NeuronImpl>, DN extends NeuronImpl, DS extends Synapse<? extends NeuronImpl, ? extends DN>> extends Neuron<SN, SS, DN, DS>
{
	public void setInput(double inputToSet);
}
