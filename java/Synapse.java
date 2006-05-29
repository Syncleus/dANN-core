package dANN;

public class Synapse
{
    public Neuron DestinationNeuron;
    public Neuron SourceNeuron;
    public double Weight;
    
    /** Creates a new instance of Synapse */
    public Synapse(Neuron SourceToSet, Neuron DestinationToSet, double InitialWeight)
    {
        this.DestinationNeuron = DestinationToSet;
        this.SourceNeuron = SourceToSet;
        this.Weight = InitialWeight;
    }
    
    public void LearnWeight()
    {
        this.Weight += this.DestinationNeuron.OwnedDNA.LearningRate * this.SourceNeuron.Output * this.DestinationNeuron.DeltaTrain;
    }
    
    public double CalculateOutput()
    {
        return this.SourceNeuron.Output * this.Weight;
    }
    
    public double CalculateDifferential()
    {
        return this.Weight * this.DestinationNeuron.DeltaTrain;
    }
}
