package com.syncleus.dann;

/**
 * This class acts as the center class of the whole library. It contains the
 * entire network's neurons and layers. It provides the highest level methods
 * of all the other dANN classes.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class Brain 
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
	
	/**
	 * This layer contains all the InputNeurons which will be used as inputs for
	 * the brain.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 * @see com.syncleus.dann.Brain#OutLayer
	 */
    public InputLayer InLayer;
	 
	 /**
	  * This layer contains all the OutputNEurons which will be used as outputs
	  * for the brain.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Brain#InLayer
	  */
    public OutputLayer OutLayer;
	 
	 /**
	  * This represents the basic properties of the brain. all elements of a
	  * brain have their own copy of DNA. Sometimes all copies will share the
	  * same DNA, but they dont have to.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public DNA OwnedDNA;
	 
	 // </editor-fold>
    
	// <editor-fold defaultstate="collapsed" desc="Constructors">
	 
    /**
	  * Creates a new instance of a Brain.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This will be the dna used for this instance.
	  * @param InputCount The number of neurons to be created in the input layer
	  * @param OutputCount the number of neurons to be created in the output
	  *	layer.
	  */
    public Brain(DNA OwnedDNAToSet, int InputCount, int OutputCount) 
    {
        this.OwnedDNA = OwnedDNAToSet;
        InLayer = new InputLayer(OwnedDNAToSet, null);
        OutLayer = new OutputLayer(OwnedDNAToSet, null);
        InLayer.DestinationLayer = OutLayer;
        OutLayer.SourceLayer = InLayer;
        
        InLayer.AddNeurons(InputCount);
        OutLayer.AddNeurons(OutputCount);
    }
	 
	 // </editor-fold>
    
	// <editor-fold defaultstate="collapsed" desc="Topogrophy Manipulation">
	 
	 /**
	  * Adds a new layer of neurons directly after the current input layer.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param NeuronCount The number of neurons to create in the newly added
	  *	layer.
	  */
    public void AddLayerAfterInput(int NeuronCount)
    {
        this.AddLayerAfter(NeuronCount, this.InLayer);
    }

	 /**
	  * Adds a new layer of neurons directly before the current output layer.
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param NeuronCount The number of neurons to create in the newly added
	  *	layer.
	  */
    public void AddLayerBeforeOutput(int NeuronCount)
    {
        this.AddLayerBefore(NeuronCount, this.OutLayer);
    }

	 /**
	  * Adds a new layer of neurons directly before the specified layer.
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param NeuronCount The number of neurons to create in the newly added
	  *	layer.
	  * @param LayerToAddBefore The newly added layer will be added just before
	  *	this layer.
	  */
    public void AddLayerBefore(int NeuronCount, Layer LayerToAddBefore)
    {
        Layer LayerToAdd = new Layer(this.OwnedDNA, LayerToAddBefore, LayerToAddBefore.SourceLayer);
        LayerToAddBefore.SourceLayer.DestinationLayer = LayerToAdd;
        LayerToAddBefore.SourceLayer = LayerToAdd;
        LayerToAdd.AddNeurons(NeuronCount);        
    }

	 /**
	  * Adds a new layer of neurons directly after the specified layer.
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param NeuronCount The number of neurons to create in the newly added
	  *	layer.
	  * @param LayerToAddAfter The newly added layer will be added just after
	  *	this layer.
	  */
    public void AddLayerAfter(int NeuronCount, Layer LayerToAddAfter)
    {
        Layer LayerToAdd = new Layer(this.OwnedDNA, LayerToAddAfter.DestinationLayer, LayerToAddAfter);
        LayerToAddAfter.DestinationLayer.SourceLayer = LayerToAdd;
        LayerToAddAfter.DestinationLayer = LayerToAdd;
        LayerToAdd.AddNeurons(NeuronCount);        
    }
    
	 /**
	  * This method causes all the current layers and neurons to connect in a
	  * feed-foreward configuration. This means all layers will connect every
	  * neuron in it to every other neuron in a layer after its own layer. This
	  * means every neuron will have connect to every neuron int he output layer,
	  * and so on.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public void ConnectAllFeedForward()
    {
        Layer CurrentLayer = this.InLayer;
        while((CurrentLayer != null)&&((CurrentLayer instanceof OutputLayer) == false))
        {
            CurrentLayer.ConnectAllToNextLayer();
            
            CurrentLayer = CurrentLayer.DestinationLayer;
        }
    }
	 
	 //</editor-fold>
	 
	// <editor-fold defaultstate="collapsed" desc="Input/Output">
    
	 /**
	  * This causes the inputs currently set on the input layer to propogate
	  * through to the output. This will cause the output values to refresh.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @see com.syncleus.dann.Brain#GetCurrentOutput
	  */
    public void PropogateOutput()
    {
        Layer CurrentLayer = this.InLayer;
        while(CurrentLayer != null)
        {
            CurrentLayer.PropogateAll();
            
            CurrentLayer = CurrentLayer.DestinationLayer;
        }        
    }
	 
	 /**
	  * This method obtains the current output sitting on the output layer. When
	  * you change the input on the brain you must first call PropogateOutput()
	  * in order to refresh the output neurons to reflect the new input.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @see com.syncleus.dann.Brain#PropogateOutput
	  * @since 0.1
	  * @return an array representing the various output layer neuron states. The
	  *	order of the array doesnt matter. However whatever value is at a
	  *	perticular index will always be assosiated with the same neuron.
	  */
    public double[] GetCurrentOutput()
    {
        return this.OutLayer.GetOutput();
    }
	 
	 /**
	  * This method sets the current input on the neurons for the input layer.
	  * After you change the inputs you must call PropogateOutput() in order for
	  * the output neurons to reflect the change.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @see com.syncleus.dann.Brain#PropogateOutput
	  * @since 0.1
	  * @param InputToSet An array that represents the values to be set as input
	  *	on the various input layer neurons. The order of the array isnt
	  *	important. However, whatever value is set at a perticular index, that
	  *	index, will always be assosiated with the same neuron.
	  */
	 public void SetCurrentInput(double[] InputToSet)
    {
        this.InLayer.SetInput(InputToSet);
    }
	 
	 // </editor-fold>
	 
	// <editor-fold defaultstate="collapsed" desc="Training">
    
	 /**
	  * This method will backpropogate the training set onto the neuron network.
	  * This step will actual cause the neural network to learn. The training set
	  * should only be set after the data has been input and propogated foreward.
	  * Therefore the steps that should be taken before back propogations are:<BR>
	  * <BR>
	  * SetCurrentInput()<BR>
	  * PropogateOutput()<BR>
	  * SetCurrentTraining(trainingSet)<BR>
	  * BackPropogateTraining()<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Brain#SetCurrentInput
	  * @see com.syncleus.dann.Brain#PropogateOutput
	  * @see com.syncleus.dann.Brain#SetCurrentTraining
	  * @see com.syncleus.dann.Brain#BackPropogateTraining
	  */
    public void BackPropogateTraining()
    {
        Layer CurrentLayer = this.OutLayer;
        while(CurrentLayer != null)
        {
            CurrentLayer.BackPropogateAll();
            
            CurrentLayer = CurrentLayer.SourceLayer;
        }          
    }
    
	 /**
	  * This method sets the current training set. The training isnt actually
	  * performed until after the BackPropogateTraining is called.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Brain#BackPropogateTraining
	  * @param TrainingToSet This is the training set to apply to the network.
	  *	The value at a given index in the array shoudl corespond to the same
	  *	index in the output array.
	  */
    public void SetCurrentTraining(double[] TrainingToSet)
    {
        this.OutLayer.SetTraining(TrainingToSet);
	 }
	 
	 // </editor-fold>
    
}
