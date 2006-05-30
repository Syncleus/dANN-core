package com.syncleus.dann;

import java.util.ArrayList;

/**
 * This class represents a collection of neurons in a layer. Layers are
 * positioned in a chain from the inputs to the outputs.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.InputLayer
 * @see com.syncleus.dann.OutputLayer
 */
public class Layer 
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

	/**
	 * This contains all the neurons considered to be a part of this layer. Any
	 * one neuron can only belong to one layer. But one layer owns many neurons.
	 * <BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    public ArrayList<Neuron> NeuronsOwned = new ArrayList<Neuron>();
	 
	/**
	 * This layer is the layer that exists one slot closer to the input layer. Or
	 * the input layer itself, or even null. It can only be null when this is
	 * an InputLayer.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 * @see com.syncleus.dann.Layer#DestinationLayer
	 */
    public Layer SourceLayer;
	 
	/**
	 * This layer is the layer that exists one slot closer to the output layer.
	 * Or the output layer itself, or even null. It can only be null when this is
	 * an OutputLayer.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 * @see com.syncleus.dann.Layer#SourceLayer
	 */
    public Layer DestinationLayer;
	 
	/**
	 * This will determine most of the properties of the layer.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    DNA OwnedDNA;

    // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Constructors">
    
    /**
	  * Creates a new instance of Layer<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This dna class will determine the various properties
	  *	of the layer.
	  * @param DestinationLayerToSet The next layer closer to the InputLayer of
	  *	the brain. This will be null when called from an OutputLayer's
	  *	clonstructor.
	  * @param SourceLayerToSet The next layer closer to the OutputLayer of the
	  *	brain. This will be null when called from an InputLayer's constructor.
	  * @see com.syncleus.dann.OutputLayer#OutputLayer
	  * @see com.syncleus.dann.InputLayer#InputLayer
	  */
    public Layer(DNA OwnedDNAToSet, Layer DestinationLayerToSet, Layer SourceLayerToSet) 
    {
        this.SourceLayer = SourceLayerToSet;
        this.DestinationLayer = DestinationLayerToSet;
        this.OwnedDNA = OwnedDNAToSet;
    }
	 
	 // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
	  * Adds new neurons to the layer<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param CountToAdd The quantity of new neurons to add.
	  */
    public void AddNeurons(int CountToAdd)
    {
        for(int Lcv = 0; Lcv < CountToAdd; Lcv++)
        {
            if( this instanceof InputLayer )
                this.NeuronsOwned.add(new InputNeuron(this, OwnedDNA));
            else if(this instanceof OutputLayer)
                this.NeuronsOwned.add(new OutputNeuron(this, OwnedDNA));
            else
                this.NeuronsOwned.add(new Neuron(this, OwnedDNA));
        }        
    }
	 
    /**
	  * Connects all the neurons in the Layer to another layer.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param LayerToConnectTo This is the layer the neurons will be connecting
	  *	to.
	  * @see com.syncleus.dann.Layer#ConnectAllToNextLayer
	  */
    public void ConnectAllToLayer(Layer LayerToConnectTo)
    {
        Neuron[] ConnectFromNeurons = new  Neuron[this.NeuronsOwned.size()];
		  this.NeuronsOwned.toArray(ConnectFromNeurons);
        Neuron[] ConnectToNeurons = new Neuron[LayerToConnectTo.NeuronsOwned.size()];
		  LayerToConnectTo.NeuronsOwned.toArray(ConnectToNeurons);
        for(int FromLcv = 0; FromLcv < ConnectFromNeurons.length; FromLcv++)
        {
            for(int ToLcv = 0; ToLcv < ConnectToNeurons.length; ToLcv++)
            {
                ConnectFromNeurons[FromLcv].ConnectToNeuron(ConnectToNeurons[ToLcv]);
            }
        }
    }

    /**
	  * Connects all the neurons in the Layer to the next layer closer to the
	  * output layer.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Layer#ConnectAllToLayer
	  */
    public void ConnectAllToNextLayer()
    {
        this.ConnectAllToLayer(this.DestinationLayer);
    }
	 
	 // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Propogation">
    
    /**
	  * Propogates the output of the neurons from the previous layer to this one.
	  * <BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Layer#BackPropogateAll
	  */
    public void PropogateAll()
    {
        Object[] NeuronsToPropogate = this.NeuronsOwned.toArray();
        for(int Lcv = 0; Lcv < NeuronsToPropogate.length; Lcv++)
        {
            Neuron CurrentNeuron = (Neuron) NeuronsToPropogate[Lcv];
            CurrentNeuron.Propogate();
        }        
    }

    /**
	  * Back propogates the output of the neurons from the previous layer to this
	  * one.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Layer#PropogateAll
	  */
    public void BackPropogateAll()
    {
        Object[] NeuronsToBackPropogate = this.NeuronsOwned.toArray();
        for(int Lcv = 0; Lcv < NeuronsToBackPropogate.length; Lcv++)
        {
            Neuron CurrentNeuron = (Neuron) NeuronsToBackPropogate[Lcv];
            CurrentNeuron.BackPropogate();
        }        
    }
	 
	 // </editor-fold>
}
