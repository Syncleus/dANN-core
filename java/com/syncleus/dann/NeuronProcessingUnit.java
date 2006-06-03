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
 * The neuron is the most fundemental component of the network; it is also the
 * thinktank of the system. One neuron will usually connect to many other
 * ProcessingUnits through synapses and receive input from many other
 * ProcessingUnits in the same way.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.Synapse
 */
public class NeuronProcessingUnit extends ProcessingUnit implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
	
    /**
	  * Represents the current excitation of the neuron from input
	  * signals<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected double activity = 0;

    /**
	  * Represents the current output of the neuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected double output = 0;
	 
    /**
	  * The current weight of the bias input. The bias is an input that is always
	  * set to an on position. The bias weight usually adapts in the same manner
	  * as the rest of the synapse's weights.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected double biasWeight = 0;
	 
    /**
	  * An array list of all the synapses that this neuron is currently
	  * connection out to.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected ArrayList<Synapse> destination = new ArrayList<Synapse>();
	 
    /**
	  * All the synapses currently connecting into this neuron<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    protected ArrayList<Synapse> sourceSynapses = new ArrayList<Synapse>();
	 
    /**
	  * The DNA determines this neurons basic properties.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public DNA ownedDNA;
	 
	/**
     * This represents the net effect of all the training data from all the
     * inputs. It is essentially the reverse of the activity value.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#activity
     */
    public double deltaTrain = 0;
	 
	 // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
	 
    /**
     * Creates a new instance of NeuronProcessingUnit<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param OwnedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public NeuronProcessingUnit(DNA OwnedDNAToSet)
    {
        this.ownedDNA = OwnedDNAToSet;
        this.biasWeight = (super.random.nextDouble()*2.0)-1.0;
    }
	 
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">
	 
    /**
     * This method is called externally to connect to another ProcessingUnit.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outUnit The ProcessingUnit to connect to.
     * @see com.syncleus.dann.NeuronProcessingUnit#connectFrom
     */
    public void connectTo(ProcessingUnit outUnit)
    {
        //make sure you arent already connected to the neuron
        int test;
        if( outUnit == null)
            return; // todo throw an exception
        
        //connect to the neuron
        Synapse NewSynapse = new Synapse(this, outUnit, (super.random.nextDouble() *2.0) - 1.0);
        this.destination.add(NewSynapse);
        outUnit.connectFrom(NewSynapse);
    }
    
    /**
     * This method is called internally, between ProcessingUnits, to
     * facilitate the connection process.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The synapse to connect from.
     * @see com.syncleus.dann.NeuronProcessingUnit#connectTo
     */
    protected void connectFrom(Synapse inSynapse)
    {
        //make sure you arent already connected fromt his neuron
        
        //add the synapse to the source list
        this.sourceSynapses.add(inSynapse);
    }
    
    /**
     * Causes the ProcessingUnit to disconnect all outgoing connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectAllSources
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectAll
     */
    public void disconnectAllDestinations()
    {
		 for( Synapse currentDestination : this.destination )
		 {
			 try
			 {
				this.disconnectDestination(currentDestination);
			 }
			 catch(SynapseNotConnectedException caughtException)
			 {
				 caughtException.printStackTrace();
				 throw new InternalError("this shouldnt happen: " + caughtException);
			 }
		 }
    }
    
    /**
     * Causes the ProcessingUnit to disconnect all incomming connections.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectAllDestinations
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectAll
     */
    public void disconnectAllSources()
    {
		 for( Synapse currentSource : this.sourceSynapses )
		 {
			 try
			 {
				this.disconnectSource(currentSource);
			 }
			 catch(SynapseNotConnectedException caughtException)
			 {
				 caughtException.printStackTrace();
				 throw new InternalError("this shouldnt happen: " + caughtException);
			 }
		 }     
    }
    
    /**
     * Disconnects from a perticular outgoing connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.NeuronProcessingUnit#removeSource
     */
    public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
    {
        if(this instanceof OutputNeuronProcessingUnit)
            return; // TODO throw an error
            
        if( this.destination.remove(outSynapse) == false )
			  throw new SynapseNotConnectedException("can not disconnect destination, does not exist.");
		  
		  try
		  {
			  if(  outSynapse.getDestination() != null )
					outSynapse.getDestination().removeSource(outSynapse);
		  }
		  catch(SynapseDoesNotExistException caughtException)
		  {
			  //do nothing, its a recoverable exception
		  }
    }

    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.NeuronProcessingUnit#removeDestination
     */
    public void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException
    {
        if(this instanceof InputNeuronProcessingUnit)
            return; // TODO throw an error
        
        if( this.sourceSynapses.remove(inSynapse) == false )
			  throw new SynapseNotConnectedException("can not disconnect source, does not exist.");

		  try
		  {
			  if( inSynapse.getSource() != null )
					inSynapse.getSource().removeDestination(inSynapse);
		  }
		  catch(SynapseDoesNotExistException caughtException)
		  {
			  //do nothing, its a recoverable exception
		  }
    }
    
    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectSource
     */
    protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
    {
        if(this instanceof OutputNeuronProcessingUnit)
            return; // TODO throw an exception'
            
        if( this.destination.remove(outSynapse) == false )
			  throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }
    
    /**
     * Called internally to facilitate the removal of a connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to remove from memory.<BR>
     * @see com.syncleus.dann.NeuronProcessingUnit#disconnectDestination
     */
    protected void removeSource(Synapse inSynapse) throws SynapseDoesNotExistException
    {
        if(this instanceof InputNeuronProcessingUnit)
            return; // TODO throw an exception

        if( this.sourceSynapses.remove(inSynapse) == false )
			  throw new SynapseDoesNotExistException("Can not remove destination, does not exist.");
    }
	 
    // </editor-fold>
	 
    // <editor-fold defaultstate="collapsed" desc="Propogation">
    
    /**
	  * Backpropogates the training data to all the incomming synapses.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public void backPropogate()
    {
        this.calculateDeltaTrain();
        
			//step thru source synapses and make them learn their new weight.
			Object[] SynapseArray = this.sourceSynapses.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 CurrentSynapse.learnWeight(this.deltaTrain, this.ownedDNA.learningRate);
			}

			//learn the biases new weight
			this.biasWeight += this.ownedDNA.learningRate * this.deltaTrain;
    }

    /**
     * Calculates the Delta Train based on all the destination synapses<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#backPropogate
     */
    public void calculateDeltaTrain()
    {
			this.deltaTrain = 0;
			Object[] SynapseArray = this.destination.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 this.deltaTrain += CurrentSynapse.getDifferential();
			}
			this.deltaTrain *= this.activationFunctionDerivitive();
    }
    

    /**
	  * Propogates the current output to all outgoing synapses.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
    public void propogate()
    {
        //calculate the current input activity
        this.activity = 0;
			Object[] SynapseArray = this.sourceSynapses.toArray();
			for(int Lcv = 0; Lcv < SynapseArray.length; Lcv++)
			{
				 Synapse CurrentSynapse = (Synapse) SynapseArray[Lcv];
				 this.activity += CurrentSynapse.getOutput();
			}
			//Add the bias to the activity
			this.activity += this.biasWeight;
        
        //calculate the activity function and set the result as the output
        this.setOutput( this.activationFunction() );
    }
	 
    /**
     * sets the current output on all outgoing synapses.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#propogate
     * @param newOutput The output value.
     */
    protected void setOutput(double newOutput)
    {
		 this.output = newOutput;
		 
		 for( Synapse current : this.destination )
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
    public double getOutput()
    {
		 return this.output;
    }
	 
    /**
     * obtains the output as a function of the current activity. This is a bound
     * function (usually between -1 and 1) based on the current activity of the
     * neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return a bound value (between -1 and 1 if this function is not
     * 	overwritten). It is a function of the neuron's current activity.
     * @see com.syncleus.dann.NeuronProcessingUnit#propogate
     */
    protected double activationFunction()
    {
		 return Math.tanh(this.activity);
    }

    /**
     * This must be the derivity of the ActivityFunction. As such it's output is
     * also based on the current activity of the neuron. If the
     * activationFunction is overwritten then this method must also be
     * overwritten with the proper derivative.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return the derivative output of the activationFunction
     * @see com.syncleus.dann.NeuronProcessingUnit#activationFunction
     */
    protected double activationFunctionDerivitive()
    {
        return 1.0 - Math.pow(this.activationFunction(), 2.0);
    }
	 
    // </editor-fold>
}
