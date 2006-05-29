/*
 * InputNeuron.java
 *
 * Created on July 28, 2004, 3:37 AM
 */

package dANN;

/**
 *
 * @author  freemo
 */
public class InputNeuron extends Neuron
{
    
    /** Creates a new instance of InputNeuron */
    public InputNeuron(Layer OwningLayerToSet, DNA OwnedDNAToSet)
    {
        super(OwningLayerToSet, OwnedDNAToSet);
        this.IsInputNeuron = true;
    }
    
    public void SetInputNeuronInput(double InputToSet)
    {
        this.InputNeuronInput = InputToSet;
    }
    
}
