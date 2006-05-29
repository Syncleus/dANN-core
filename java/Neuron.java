/*
 * Neuron.java
 *
 * Created on July 27, 2004, 10:44 PM
 */

package dANN;


import java.util.ArrayList;
import java.lang.Exception;
import java.lang.Math;


/**
 *
 * @author  freemo
 */
public class Neuron
{
    private double Activity = 0;
    public double Output = 0;
    private double BiasWeight = 0;
    private ArrayList DestinationSynapses = new ArrayList();
    private ArrayList SourceSynapses = new ArrayList();
    private Layer OwningLayer;
    public DNA OwnedDNA;
    protected double Desired = 0; //Only used on output neurons
    protected boolean IsInputNeuron = false;
    protected boolean IsOutputNeuron = false;
    protected double InputNeuronInput = 0;
    public double DeltaTrain = 0;
    
    /** Creates a new instance of Neuron */
    public Neuron(Layer OwningLayerToSet, DNA OwnedDNAToSet)
    {
        this.OwnedDNA = OwnedDNAToSet;
        this.OwningLayer = OwningLayerToSet;
        this.BiasWeight = (this.OwnedDNA.RandomGenerator.nextDouble()*2)-1;
    }

    //Use this methode to connect together neurons
    public void ConnectToNeuron(Neuron ToConnectTo)
    {
        //make sure you arent already connected to the neuron
        int test;
        if( ToConnectTo == null)
            return; // todo throw an exception
        
        //connect to the neuron
        Synapse NewSynapse = new Synapse(this, ToConnectTo, (this.OwnedDNA.RandomGenerator.nextDouble() *2) - 1);
        this.DestinationSynapses.add(NewSynapse);
        ToConnectTo.ConnectFromSynapse(NewSynapse);
    }
    
    //called from the ConnectToNeuron of the source neuron to connect
    private void ConnectFromSynapse(Synapse ToConnectFrom)
    {
        //make sure you arent already connected fromt his neuron
        
        //add the synapse to the source list
        this.SourceSynapses.add(ToConnectFrom);
    }
    
    //Disconnects all outgoing synapses
    public void DisconnectAllDestinationSynapses()
    {
        Object[] SynapseArray = this.SourceSynapses.toArray();
        for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
        {
            Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
            this.DisconnectSourceSynapse(CurrentSynapse);
        }
    }
    
    //Disconnects all incomming synapses
    public void DisconnectAllSourceSynpases()
    {
        Object[] SynapseArray = this.DestinationSynapses.toArray();
        for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
        {
            Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
            this.RemoveDestinationSynapse(CurrentSynapse);
        }        
    }
    
    //disconnects all synapses
    public void DisconnectAllSynapses()
    {
        this.DisconnectAllDestinationSynapses();
        this.DisconnectAllSourceSynpases();
    }
    
    //disconnects both sides of the Destination synapse
    public void DisconnectDestinationSynapse(Synapse ToDisconnect)
    {
        if(this.IsOutputNeuron == true)
            return; // TODO throw an error
            
        this.DestinationSynapses.remove(ToDisconnect);
        if(  ToDisconnect.DestinationNeuron != null )
            ToDisconnect.DestinationNeuron.RemoveSourceSynapse(ToDisconnect);
    }

    //disconnectects both sides of the Source synapse
    public void DisconnectSourceSynapse(Synapse ToDisconnect)
    {
        if(this.IsInputNeuron == true)
            return; // TODO throw an error
        
        this.SourceSynapses.remove(ToDisconnect);
        
        if( ToDisconnect.SourceNeuron != null )
            ToDisconnect.SourceNeuron.RemoveDestinationSynapse(ToDisconnect);
    }
    
    //removes the synapse from the list. called from the DisconnectSourceSynapse of the neuron sharing the synapse
    private void RemoveDestinationSynapse(Synapse ToDisconnect)
    {
        if(this.IsOutputNeuron == true)
            return; // TODO throw an exception'
            
        this.DestinationSynapses.remove(ToDisconnect);
    }
    
    //removes the synapse from the list. called from the DisconnectDestinationSynapse of the neuron sharing the synapse
    private void RemoveSourceSynapse(Synapse ToDisconnect)
    {
        if(this.IsInputNeuron == true)
            return; // TODO throw an exception

        this.SourceSynapses.remove(ToDisconnect);
    }
    
    //calculates the DeltaTrain for the neuron, and then modifies the weights of all source synapses and bias.
    public void BackPropogate()
    {
        this.CalculateDeltaTrain();
        
        //input layer has no source synapses to train
        if( this.IsInputNeuron == false)
        {
            //step thru source synapses and make them learn their new weight.
            Object[] SynapseArray = this.SourceSynapses.toArray();
            for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
            {
                Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
                CurrentSynapse.LearnWeight();
            }
            
            //learn the biases new weight
            this.BiasWeight += this.OwnedDNA.LearningRate * this.DeltaTrain;
        }
    }

    //Calculates the Delta Train based on all the destination synapses
    public void CalculateDeltaTrain()
    {
        if( this.IsOutputNeuron)
            this.DeltaTrain = this.ActivationFunctionDerivitive() * (this.Desired - this.Output);
        else
        {
            this.DeltaTrain = 0;
            Object[] SynapseArray = this.DestinationSynapses.toArray();
            for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
            {
                Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
                this.DeltaTrain += CurrentSynapse.CalculateDifferential();
            }
            this.DeltaTrain *= this.ActivationFunctionDerivitive();
        }
        
    }
    

    //Calculates the current activity and the resultant output.
    public void Propogate()
    {
        //calculate the current input activity
        this.Activity = 0;
        if( this.IsInputNeuron == false)
        {
            Object[] SynapseArray = this.SourceSynapses.toArray();
            for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
            {
                Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
                this.Activity += CurrentSynapse.CalculateOutput();
            }
            //Add the bias to the activity
            this.Activity += this.BiasWeight;
        }
        else
            this.Activity = this.InputNeuronInput;
        
        //calculate the activity function and set the result as the output
        this.Output = this.ActivationFunction();
    }
    
    //Sets the output as a function of the current activity.
    private double ActivationFunction()
    {
        //replace this with the tanh function in 1.5.0
        return (Math.exp(this.Activity) - Math.exp(-1 * this.Activity))/(Math.exp(this.Activity) + Math.exp(-1 * this.Activity));
    }
    
    //The derivitabve of the activation function
    private double ActivationFunctionDerivitive()
    {
        return 1 - Math.pow(this.ActivationFunction(), 2);
    }
}
