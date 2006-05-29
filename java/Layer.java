/*
 * Layer.java
 *
 * Created on July 27, 2004, 10:48 PM
 */

package dANN;

import java.util.ArrayList;

/**
 *
 * @author  freemo
 */
public class Layer 
{
    public ArrayList NeuronsOwned = new ArrayList();
    public Layer SourceLayer;
    public Layer DestinationLayer;
    DNA OwnedDNA;
    protected boolean IsInputLayer = false;
    protected boolean IsOutputLayer = false;
    
    /** Creates a new instance of Layer */
    public Layer(DNA OwnedDNAToSet, Layer DestinationLayerToSet, Layer SourceLayerToSet) 
    {
        this.SourceLayer = SourceLayerToSet;
        this.DestinationLayer = DestinationLayerToSet;
        this.OwnedDNA = OwnedDNAToSet;
    }
    
    public void AddNeurons(int CountToAdd)
    {
        for(int Lcv = 0; Lcv < CountToAdd; Lcv++)
        {
            if( this.IsInputLayer)
                this.NeuronsOwned.add(new InputNeuron(this, OwnedDNA));
            else if(this.IsOutputLayer)
                this.NeuronsOwned.add(new OutputNeuron(this, OwnedDNA));
            else
                this.NeuronsOwned.add(new Neuron(this, OwnedDNA));
        }        
    }
    
    public void ConnectAllToLayer(Layer LayerToConnectTo)
    {
        Object[] ConnectFromNeurons = this.NeuronsOwned.toArray();
        Object[] ConnectToNeurons = LayerToConnectTo.NeuronsOwned.toArray();
        for(int FromLcv = 0; FromLcv < ConnectFromNeurons.length; FromLcv++)
        {
            for(int ToLcv = 0; ToLcv < ConnectToNeurons.length; ToLcv++)
            {
                Neuron CurrentFromNeuron = (Neuron)ConnectFromNeurons[FromLcv];
                Neuron CurrentToNeuron = (Neuron) ConnectToNeurons[ToLcv];
                CurrentFromNeuron.ConnectToNeuron(CurrentToNeuron);
            }
        }
    }
    
    public void ConnectAllToNextLayer()
    {
        this.ConnectAllToLayer(this.DestinationLayer);
    }
    
    public void PropogateAll()
    {
        Object[] NeuronsToPropogate = this.NeuronsOwned.toArray();
        for(int Lcv = 0; Lcv < NeuronsToPropogate.length; Lcv++)
        {
            Neuron CurrentNeuron = (Neuron) NeuronsToPropogate[Lcv];
            CurrentNeuron.Propogate();
        }        
    }

    public void BackPropogateAll()
    {
        Object[] NeuronsToBackPropogate = this.NeuronsOwned.toArray();
        for(int Lcv = 0; Lcv < NeuronsToBackPropogate.length; Lcv++)
        {
            Neuron CurrentNeuron = (Neuron) NeuronsToBackPropogate[Lcv];
            CurrentNeuron.BackPropogate();
        }        
    }    
}
