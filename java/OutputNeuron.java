/*
 * OutputNeuron.java
 *
 * Created on July 28, 2004, 3:40 AM
 */

package dANN;

/**
 *
 * @author  freemo
 */
public class OutputNeuron extends Neuron
{
    
    /** Creates a new instance of OutputNeuron */
    public OutputNeuron(Layer OwningLayerToSet, DNA OwnedDNAToSet) 
    {
        super(OwningLayerToSet, OwnedDNAToSet);
        this.IsOutputNeuron = true;
    }
    
    public void SetTrainingData(double TrainingToSet)
    {
        this.Desired = TrainingToSet;
    }
    
}
