package com.syncleus.dann;

public interface OutputNeuron<SN extends NeuronImpl, SS extends Synapse<? extends SN, ? extends NeuronImpl>, DN extends NeuronImpl, DS extends Synapse<? extends NeuronImpl, ? extends DN>> extends Neuron<SN, SS, DN, DS>
{
	public double getOutput();
}
