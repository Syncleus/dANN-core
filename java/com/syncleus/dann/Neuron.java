/******************************************************************************
*                                                                             *
*  Copyright: (c) Jeffrey Phillips Freeman                                    *
*                                                                             *
*  You may redistribute and modify this source code under the terms and       *
*  conditions of the Open Source Community License - Type C version 1.0       *
*  or any later version as published by syncleus at http://www.syncleus.com.  *
*  There should be a copy of the license included with this file. If a copy   *
*  of the license is not included you are granted no right to distribute or   *
*  otherwise use this file except through a legal and valid license. You      *
*  should also contact syncleus at the information below if you cannot find   *
*  a license:                                                                 *
*                                                                             *
*  Syncleus                                                                   *
*  1116 McClellan St.                                                         *
*  Philadelphia, PA 19148                                                     *
*                                                                             *
******************************************************************************/

package com.syncleus.dann;


import java.util.ArrayList;
import java.lang.Exception;
import java.lang.Math;


/**
 * The neuron acts like a node in the overall neural network. It acts as one of
 * the most fundemantal parts of the neural network. One neuron will usually
 * connect to many other neurons through synapses and receive input from many
 * other neurons in the same way.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.Synapse
 */
public class Neuron
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
	
    /**
	  * Represents the current excitation of the neuron from input
	  * signals<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected double Activity = 0;

    /**
	  * Represents the current output of the neuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double Output = 0;
	 
    /**
	  * The current weight of the bias input. The bias is an input that is always
	  * set to an on position. The bias weight usually adapts in the same manner
	  * as the rest of the synapse's weights.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private double BiasWeight = 0;
	 
    /**
	  * An array list of all the synapses that this neuron is currently
	  * connection out to.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private ArrayList<Synapse> DestinationSynapses = new ArrayList<Synapse>();
	 
    /**
	  * All the synapses currently connecting into this neuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private ArrayList<Synapse> SourceSynapses = new ArrayList<Synapse>();
	 
    /**
	  * This is the layer that owns the neuron. This may be null if layers arent
	  * used.
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    private Layer OwningLayer;
	 
    /**
	  * The DNA determines this neurons basic properties.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public DNA OwnedDNA;
	 
	/**
	 * This represents the net effect of all the training data from all the
	 * inputs. It is essentially the reverse of the Activity value.
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 * @see com.syncleus.dann.Neuron#Activity
	 */
    public double DeltaTrain = 0;
	 
	 // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
	 
    /**
	  * Creates a new instance of Neuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param OwnedDNAToSet This dna class will determine the various properties
	  *	of the layer.
	  * @param OwningLayerToSet This is the layer the new neuron will belong to.
	  *	This can be null if you arent using layers.
	  */
    public Neuron(Layer OwningLayerToSet, DNA OwnedDNAToSet)
    {
        this.OwnedDNA = OwnedDNAToSet;
        this.OwningLayer = OwningLayerToSet;
        this.BiasWeight = (this.OwnedDNA.RandomGenerator.nextDouble()*2)-1;
    }
	 
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
	 
    /**
	  * This method is called externally to connect to another neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToConnectTo The neuron to connect to.
	  */
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
    
    /**
	  * This method is called internally, between neurons, to connect to
	  * facilitate the connection process.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToConnectFrom The synapse to connect from.
	  * @see com.syncleus.dann.Neuron#ConnectToNeuron
	  */
    private void ConnectFromSynapse(Synapse ToConnectFrom)
    {
        //make sure you arent already connected fromt his neuron
        
        //add the synapse to the source list
        this.SourceSynapses.add(ToConnectFrom);
    }
    
    /**
	  * Causes the neuron to disconnect all outgoing connections.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#DisconnectAllSourceSynapses
	  * @see com.syncleus.dann.Neuron#DisconnectAllSynapses
	  */
    public void DisconnectAllDestinationSynapses()
    {
        Object[] SynapseArray = this.SourceSynapses.toArray();
        for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
        {
            Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
            this.DisconnectSourceSynapse(CurrentSynapse);
        }
    }
    
    /**
	  * Causes the neuron to disconnect all incomming connections.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#DisconnectAllDestinationSynapses
	  * @see com.syncleus.dann.Neuron#DisconnectAllSynapses
	  */
    public void DisconnectAllSourceSynapses()
    {
        Object[] SynapseArray = this.DestinationSynapses.toArray();
        for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
        {
            Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
            this.RemoveDestinationSynapse(CurrentSynapse);
        }        
    }
    
    /**
	  * Causes the neuron to disconnect all connections.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#DisconnectAllSourceSynapses
	  * @see com.syncleus.dann.Neuron#DisconnectAllDestinationSynapses
	  */
    public void DisconnectAllSynapses()
    {
        this.DisconnectAllDestinationSynapses();
        this.DisconnectAllSourceSynapses();
    }
    
    /**
	  * Disconnects from a perticular outgoing synapse.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToDisconnect The outgoing synapse to disconnect from.<BR>
	  * @see com.syncleus.dann.Neuron#DisconnectSourceSynapse
	  */
    public void DisconnectDestinationSynapse(Synapse ToDisconnect)
    {
        if(this instanceof OutputNeuron)
            return; // TODO throw an error
            
        this.DestinationSynapses.remove(ToDisconnect);
        if(  ToDisconnect.getDestination() != null )
            ToDisconnect.getDestination().RemoveSourceSynapse(ToDisconnect);
    }

    /**
	  * Disconnects from a perticular incomming synapse.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToDisconnect The incomming synapse to disconnect from.<BR>
	  * @see com.syncleus.dann.Neuron#DisconnectDestinationSynapse
	  */
    public void DisconnectSourceSynapse(Synapse ToDisconnect)
    {
        if(this instanceof InputNeuron)
            return; // TODO throw an error
        
        this.SourceSynapses.remove(ToDisconnect);
        
        if( ToDisconnect.getSource() != null )
            ToDisconnect.getSource().RemoveDestinationSynapse(ToDisconnect);
    }
    
    /**
	  * Called internally to facilitate the removal of a outgoing synapse.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToDisconnect The outgoing synapse to remove from memory.<BR>
	  * @see com.syncleus.dann.Neuron#DisconnectSourceSynapse
	  */
    private void RemoveDestinationSynapse(Synapse ToDisconnect)
    {
        if(this instanceof OutputNeuron)
            return; // TODO throw an exception'
            
        this.DestinationSynapses.remove(ToDisconnect);
    }
    
    /**
	  * Called internally to facilitate the removal of a incomming synapse.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param ToDisconnect The incomming synapse to remove from memory.<BR>
	  * @see com.syncleus.dann.Neuron#DisconnectDestinationSynapse
	  */
    private void RemoveSourceSynapse(Synapse ToDisconnect)
    {
        if(this instanceof InputNeuron)
            return; // TODO throw an exception

        this.SourceSynapses.remove(ToDisconnect);
    }
	 
    // </editor-fold>
	 
    // <editor-fold defaultstate="collapsed" desc="Propogation">
    
    /**
	  * calculates the DeltaTrain for the neuron, and then modifies the weights
	  * of all source synapses and bias.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#Propogate
	  */
    public void BackPropogate()
    {
        this.CalculateDeltaTrain();
        
			//step thru source synapses and make them learn their new weight.
			Object[] SynapseArray = this.SourceSynapses.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 CurrentSynapse.learnWeight(this.DeltaTrain, this.OwnedDNA.LearningRate);
			}

			//learn the biases new weight
			this.BiasWeight += this.OwnedDNA.LearningRate * this.DeltaTrain;
    }

    /**
	  * Calculates the Delta Train based on all the destination synapses<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#BackPropogate
	  */
    public void CalculateDeltaTrain()
    {
			this.DeltaTrain = 0;
			Object[] SynapseArray = this.DestinationSynapses.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 this.DeltaTrain += CurrentSynapse.getDifferential();
			}
			this.DeltaTrain *= this.ActivationFunctionDerivitive();
        
    }
    

    /**
	  * Calculates the current activity and the resultant output.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#BackPropogate
	  */
    public void Propogate()
    {
        //calculate the current input activity
        this.Activity = 0;
			Object[] SynapseArray = this.SourceSynapses.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 this.Activity += CurrentSynapse.getOutput();
			}
			//Add the bias to the activity
			this.Activity += this.BiasWeight;
        
        //calculate the activity function and set the result as the output
        this.setOutput( this.ActivationFunction() );
    }
	 
    /**
	  * sets the current output on all outgoing synapses.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.Neuron#Propogate
	  * @param newOutput The output value.
	  */
    protected void setOutput(double newOutput)
    {
		 this.Output = newOutput;
		 
		 for( Synapse current : this.DestinationSynapses )
		 {
			 current.setInput(newOutput);
		 }
    }
	 
    /**
	  * Gets the current output.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return The current output.
	  */
    protected double getOutput()
    {
		 return this.Output;
    }
	 
    /**
	  * obtains the output as a function of the current activity. This is a bound
	  * function (usually between -1 and 1) based on the current activity of the
	  * neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return a bound value (between -1 and 1 if this function is not
	  *	overwritten). It is a function of the neuron's current activity.
	  * @see com.syncleus.dann.Neuron#Propogate
	  */
    protected double ActivationFunction()
    {
        //replace this with the tanh function in 1.5.0
        return (Math.exp(this.Activity) - Math.exp(-1 * this.Activity))/(Math.exp(this.Activity) + Math.exp(-1 * this.Activity));
    }

    /**
	  * This must be the derivity of the ActivityFunction. As such it's output is
	  * also based on the current activity of the neuron. If the
	  * ActivationFunction is overwritten then this method must also be
	  * overwritten with the proper derivative.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return the derivative output of the ActivationFunction
	  * @see com.syncleus.dann.Neuron#ActivationFunction
	  */
    protected double ActivationFunctionDerivitive()
    {
        return 1 - Math.pow(this.ActivationFunction(), 2);
    }
	 
    // </editor-fold>
}
