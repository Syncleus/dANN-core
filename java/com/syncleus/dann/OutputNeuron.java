package com.syncleus.dann;

/**
 * This is a special type of neuron that provides the output.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.InputLayer
 * @see com.syncleus.dann.InputNeuron
 */
public class OutputNeuron extends Neuron
{
    /**
	  * holds the value for the current training set.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
	 protected double Desired = 0;
    
    /**
	  * Creates a new instance of OutputNeuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This dna class will determine the various properties
	  *	of the layer.
	  * @param OwningLayerToSet This is the layer the new neuron will belong to.
	  *	This can be null if you arent using layers.
	  */
    public OutputNeuron(Layer OwningLayerToSet, DNA OwnedDNAToSet) 
    {
        super(OwningLayerToSet, OwnedDNAToSet);
    }
    
    /**
	  * This method sets the current training set on the neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param TrainingToSet sets the current training set.
	  * @see com.syncleus.dann.OutputLayer#SetTraining
	  */
    public void SetTrainingData(double TrainingToSet)
    {
        this.Desired = TrainingToSet;
    }
	 
    public void CalculateDeltaTrain()
    {
		 super.DeltaTrain = super.ActivationFunctionDerivitive() * (this.Desired - super.Output);
    }
    
}
