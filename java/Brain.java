/*
 * Brain.java
 *
 * Created on July 27, 2004, 10:48 PM
 */

package dANN;

/**
 *
 * @author  freemo
 */
public class Brain 
{
    public InputLayer InLayer;
    public OutputLayer OutLayer;
    public DNA OwnedDNA;
    
    /** Creates a new instance of Brain */
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
    
    public void AddLayerAfterInput(int NeuronCount)
    {
        this.AddLayerAfter(NeuronCount, this.InLayer);
    }
    
    public void AddLayerBeforeOutput(int NeuronCount)
    {
        this.AddLayerBefore(NeuronCount, this.OutLayer);
    }
    
    public void AddLayerBefore(int NeuronCount, Layer LayerToAddBefore)
    {
        Layer LayerToAdd = new Layer(this.OwnedDNA, LayerToAddBefore, LayerToAddBefore.SourceLayer);
        LayerToAddBefore.SourceLayer.DestinationLayer = LayerToAdd;
        LayerToAddBefore.SourceLayer = LayerToAdd;
        LayerToAdd.AddNeurons(NeuronCount);        
    }
    
    public void AddLayerAfter(int NeuronCount, Layer LayerToAddAfter)
    {
        Layer LayerToAdd = new Layer(this.OwnedDNA, LayerToAddAfter.DestinationLayer, LayerToAddAfter);
        LayerToAddAfter.DestinationLayer.SourceLayer = LayerToAdd;
        LayerToAddAfter.DestinationLayer = LayerToAdd;
        LayerToAdd.AddNeurons(NeuronCount);        
    }
    
    public void ConnectAllFeedForward()
    {
        Layer CurrentLayer = this.InLayer;
        while((CurrentLayer != null)&&(CurrentLayer.IsOutputLayer == false))
        {
            CurrentLayer.ConnectAllToNextLayer();
            
            CurrentLayer = CurrentLayer.DestinationLayer;
        }
    }
    
    public void PropogateOutput()
    {
        Layer CurrentLayer = this.InLayer;
        while(CurrentLayer != null)
        {
            CurrentLayer.PropogateAll();
            
            CurrentLayer = CurrentLayer.DestinationLayer;
        }        
    }
    
    public void BackPropogateTraining()
    {
        Layer CurrentLayer = this.OutLayer;
        while(CurrentLayer != null)
        {
            CurrentLayer.BackPropogateAll();
            
            CurrentLayer = CurrentLayer.SourceLayer;
        }          
    }
    
    public double[] GetCurrentOutput()
    {
        return this.OutLayer.GetOutput();
    }
    
    public void SetCurrentTraining(double[] TrainingToSet)
    {
        this.OutLayer.SetTraining(TrainingToSet);
    }
    
    public void SetCurrentInput(double[] InputToSet)
    {
        this.InLayer.SetInput(InputToSet);
    }
    
}
