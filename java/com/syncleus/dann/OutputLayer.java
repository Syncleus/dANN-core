package com.syncleus.dann;

import java.util.ArrayList;

/**
 * This is a special type of layer that delivers the output. It
 * contains only OutputNeurons.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.OutputNeuron
 */
public class OutputLayer extends Layer
{
    
    /**
	  * Creates a new instance of OutputLayer<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This dna class will determine the various properties
	  *	of the layer.
	  * @param SourceLayerToSet The next layer closer to the OutputLayer of
	  *	the brain.
	  */
    public OutputLayer(DNA OwnedDNAToSet, Layer SourceLayerToSet) 
    {
        super(OwnedDNAToSet, null, SourceLayerToSet);
        this.DestinationLayer = null;
    }
    
    /**
	  * This method gets the current output on the neurons.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return An array that represents the values on the output
	  *	of the various OutputNeurons. The order of the array isnt
	  *	important. However, whatever value is set at a perticular index, that
	  *	index, will always be assosiated with the same neuron.
	  * @see com.syncleus.dann.Brain#GetCurrentOutput
	  */
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
    
    /**
	  * This method sets the current training set for the neurons.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param TrainingToSet An array that represents the values to be set as
	  *	the current training set. The order of the array isnt important.
	  *	However, whatever value is set at a perticular index, that index, will
	  *	always be assosiated with the same neuron.
	  * @see com.syncleus.dann.Brain#SetCurrentTraining
	  */
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
