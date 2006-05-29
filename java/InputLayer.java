/*
 * InputLayer.java
 *
 * Created on July 28, 2004, 3:17 AM
 */

package dANN;

/**
 *
 * @author  freemo
 */
public class InputLayer extends Layer
{
    
    /** Creates a new instance of InputLayer */
    public InputLayer(DNA OwnedDNAToSet, Layer DestinationLayerToSet)
    {
        super(OwnedDNAToSet, DestinationLayerToSet, null);
        this.SourceLayer = null;
        this.IsInputLayer = true;
    }
    
    public void SetInput(double[] InputToSet)
    {
        Object[] InputNeuronArray =this.NeuronsOwned.toArray();
        
        int LcvMax = 0;
        if( InputToSet.length < InputNeuronArray.length)
            LcvMax = InputToSet.length;
        else
            LcvMax = InputNeuronArray.length;
        
        for(int Lcv = 0; Lcv < LcvMax; Lcv++)
        {
            InputNeuron CurrentNeuron = (InputNeuron) InputNeuronArray[Lcv];
            CurrentNeuron.SetInputNeuronInput(InputToSet[Lcv]);
        }
        
    }
    
}
