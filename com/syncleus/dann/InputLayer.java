package com.syncleus.dann;

/**
 * This is a special type of layer that receives input. It
 * contains only InputNeurons.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.InputNeuron
 */
public class InputLayer extends Layer
{
    
    /**
	  * Creates a new instance of InputLayer<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This dna class will determine the various properties
	  *	of the layer.
	  * @param DestinationLayerToSet The next layer closer to the InputLayer of
	  *	the brain.
	  */
    public InputLayer(DNA OwnedDNAToSet, Layer DestinationLayerToSet)
    {
        super(OwnedDNAToSet, DestinationLayerToSet, null);
        this.SourceLayer = null;
    }

    /**
	  * This method sets the current input on the neurons.
	  * <BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param InputToSet An array that represents the values to be set as input
	  *	on the various input layer neurons. The order of the array isnt
	  *	important. However, whatever value is set at a perticular index, that
	  *	index, will always be assosiated with the same neuron.
	  * @see com.syncleus.dann.Brain#SetCurrentInput
	  */
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
