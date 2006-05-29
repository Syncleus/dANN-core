/*
 * OutputLayer.java
 *
 * Created on July 28, 2004, 3:17 AM
 */

package dANN;

import java.util.ArrayList;

/**
 *
 * @author  freemo
 */
public class OutputLayer extends Layer
{
    
    /** Creates a new instance of OutputLayer */
    public OutputLayer(DNA OwnedDNAToSet, Layer SourceLayerToSet) 
    {
        super(OwnedDNAToSet, null, SourceLayerToSet);
        this.DestinationLayer = null;
        this.IsOutputLayer = true;
    }
    
    public double[] GetOutput()
    {
        Object[] NeuronsToCalc = this.NeuronsOwned.toArray();
        double[] RetVal = new double[NeuronsToCalc.length];
        for(int Lcv = 0; Lcv < NeuronsToCalc.length; Lcv++)
        {
            Neuron CurrentNeuron = (Neuron) NeuronsToCalc[Lcv];
            RetVal[Lcv] = CurrentNeuron.Output;
        }
        return RetVal;
    }
    
    public void SetTraining(double[] TrainingToSet)
    {
        Object[] OutputNeuronArray = this.NeuronsOwned.toArray();
        
        int LcvMax = 0;
        if( TrainingToSet.length < OutputNeuronArray.length)
            LcvMax = TrainingToSet.length;
        else
            LcvMax = OutputNeuronArray.length;
        
        for(int Lcv = 0; Lcv < LcvMax; Lcv++)
        {
            OutputNeuron CurrentNeuron = (OutputNeuron) OutputNeuronArray[Lcv];
            CurrentNeuron.SetTrainingData(TrainingToSet[Lcv]);
        }
        
    }    
    
}
