package com.syncleus.dann;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.Neuron
 */
public class Synapse
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
	
    /**
	  * The outgoing neuron connection.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public Neuron DestinationNeuron;
	 
    /**
	  * The incomming neuron connection.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public Neuron SourceNeuron;
	 
    /**
	  * The current weight of the synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public double Weight;
	 
	 // </editor-fold>
	 
	// <editor-fold defaultstate="collapsed" desc="Constructors">
    
    /**
	  * Creates a new instance of Synapse<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param SourceToSet The incomming neuron connection.
	  * @param DestinationToSet The outgoing neuron connection.
	  */
    public Synapse(Neuron SourceToSet, Neuron DestinationToSet, double InitialWeight)
    {
        this.DestinationNeuron = DestinationToSet;
        this.SourceNeuron = SourceToSet;
        this.Weight = InitialWeight;
    }
	 
	 // </editor-fold>
    
	// <editor-fold defaultstate="collapsed" desc="Propogation">
	 
    /**
	  * learns the new weight based on the current training set<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#CalculateDeltaTrain
	  */
    public void LearnWeight()
    {
        this.Weight += this.DestinationNeuron.OwnedDNA.LearningRate * this.SourceNeuron.Output * this.DestinationNeuron.DeltaTrain;
    }

    /**
	  * Calculates the current output of the synapse based on the input and
	  * weight<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#Propogate
	  * @return the current synapse output.
	  */
    public double CalculateOutput()
    {
        return this.SourceNeuron.Output * this.Weight;
    }
    
    /**
	  * Calculates the synapse differential. This is used when back propogating
	  * training sets.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#BackPropogate
	  * @return the current synapse differential.
	  */
    public double CalculateDifferential()
    {
        return this.Weight * this.DestinationNeuron.DeltaTrain;
    }
	 
	 // </editor-fold>
}
