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

/**
 * A special ProcessingUnit which can contain other ProcessingUnits as children.
 * <BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class LayerProcessingUnit extends ProcessingUnit implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

	/**
	 * This contains all the neurons considered to be a part of this layer. Any
	 * one neuron can only belong to one layer. But one layer owns many neurons.
	 * <BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    public ArrayList<ProcessingUnit> children = new ArrayList<ProcessingUnit>();
	 
	/**
	 * This will determine most of the properties of the layer.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
    DNA ownedDNA;

    // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Constructors">
    
    /**
     * Creates a new instance of LayerProcessingUnit<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public LayerProcessingUnit(DNA ownedDNAToSet) 
    {
        this.ownedDNA = ownedDNAToSet;
    }
	 
	 // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Topology Manipulation">

    /**
	  * Adds another processing unit to this layer.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param toAdd the ProcessingUnit to add.
	  */
    public void add(ProcessingUnit toAdd)
    {
		 this.children.add(toAdd);
    }
	 
    /**
     * Connects all the ProcessingUnits in this layer recursivly to all the
     * ProcessingUnits in another layer.
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param toConnectTo This is the layer the neurons will be connecting
     * 	to.
     * @see com.syncleus.dann.LayerProcessingUnit#connectTo
     */
    public void connectAllTo(ProcessingUnit toConnectTo)
    {
		 for( ProcessingUnit currentChild : this.children )
		 {
			 if( currentChild instanceof LayerProcessingUnit )
			 {
				 ((LayerProcessingUnit)currentChild).connectAllTo(toConnectTo);
			 }
			 else
			 {
				 if( toConnectTo instanceof LayerProcessingUnit )
				 {
					 ArrayList<ProcessingUnit> toConnectToChildren = ((LayerProcessingUnit)toConnectTo).getChildrenRecursivly();
					 for( ProcessingUnit currentOut : toConnectToChildren )
					 {
						 currentChild.connectTo(currentOut);
					 }
				 }
				 else
				 {
					 currentChild.connectTo(toConnectTo);
				 }
			 }
		 }
    }
	 
	 /**
	  * Obtains all the ProcessingUnits owned recursivly excluding
	  * LayerProcessingUnits.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  */
	 private ArrayList<ProcessingUnit> getChildrenRecursivly()
	 {
		 ArrayList<ProcessingUnit> returnList = new ArrayList<ProcessingUnit>();
		 
		 for( ProcessingUnit currentChild : this.children )
		 {
			 if( currentChild instanceof LayerProcessingUnit )
				 returnList.addAll(((LayerProcessingUnit)currentChild).getChildrenRecursivly());
			 else
				 returnList.add(currentChild);
		 }
		 
		 return returnList;
	 }
	 
	 /**
	  * Randomly returns one of the children.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @return A randomly selected child.
	  */
	 private ProcessingUnit getRandomChild()
	 {
		 return this.children.get(this.random.nextInt(this.children.size()));
	 }
	 
    /**
	  * This causes a random child ProcessingUnit to create a connection with
	  * the specified ProcessingUnti.
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param outUnit The ProcessingUnit to connect to.
	  * @see com.syncleus.dann.ProcessingUnit#connectFrom
	  */
    public void connectTo(ProcessingUnit outUnit)
	 {
		 this.getRandomChild().connectTo(outUnit);
	 }
    
    /**
	  * This will cause the incomming connection synapse to be passed randomly to
	  * one of its children.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param inSynapse The synapse to connect from.
	  * @see com.syncleus.dann.ProcessingUnit#connectTo
	  */
    protected void connectFrom(Synapse inSynapse)
	 {
		 this.getRandomChild().connectFrom(inSynapse);
	 }
    
    /**
	  * Causes the ProcessingUnit to disconnect all outgoing connections.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.ProcessingUnit#disconnectAllSources
	  * @see com.syncleus.dann.ProcessingUnit#disconnectAll
	  */
    public void disconnectAllDestinations()
	 {
		 for( ProcessingUnit currentChild : this.children )
		 {
			 currentChild.disconnectAllDestinations();
		 }
	 }
    
    /**
	  * Causes the ProcessingUnit to disconnect all incomming connections.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.ProcessingUnit#disconnectAllDestinations
	  * @see com.syncleus.dann.ProcessingUnit#disconnectAll
	  */
    public void disconnectAllSources()
	 {
		 for( ProcessingUnit currentChild : this.children )
		 {
			 currentChild.disconnectAllSources();
		 }
	 }
    
    /**
     * Disconnects from a perticular outgoing connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param outSynapse The outgoing synapse to disconnect from.<BR>
     * @see com.syncleus.dann.ProcessingUnit#removeSource
     * @throws SynapseNotConnectedException Thrown if the specified synapse is not
     * 	currently connected.
     */
    public void disconnectDestination(Synapse outSynapse) throws SynapseNotConnectedException
	 {
		 boolean found = true;
		 for( ProcessingUnit currentChild : this.children )
		 {
			 found = true;
			 try
			 {
				 currentChild.disconnectDestination(outSynapse);
			 }
			 catch(SynapseNotConnectedException caughtException)
			 {
				 found = false;
			 }
			 
			 if( found == true )
				 return;
		 }
	 }

    /**
     * Disconnects from a perticular incomming connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inSynapse The incomming synapse to disconnect from.<BR>
     * @see com.syncleus.dann.ProcessingUnit#removeDestination
     * @throws SynapseNotConnectedException Thrown if the specified synapse is not
     * 	currently connected.
     */
    public void disconnectSource(Synapse inSynapse) throws SynapseNotConnectedException
	 {
		 boolean found = true;
		 for( ProcessingUnit currentChild : this.children )
		 {
			 found = true;
			 try
			 {
				 currentChild.disconnectSource(inSynapse);
			 }
			 catch(SynapseNotConnectedException caughtException)
			 {
				 found = false;
			 }
			 
			 if( found == true )
				 return;
		 }
	 }
    
    /**
	  * Called internally to facilitate the removal of a connection. It removes
	  * the specified synapse from memory assuming it has already been
	  * disconnected<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param outSynapse The incomming synapse to remove from memory.<BR>
	  * @see com.syncleus.dann.ProcessingUnit#disconnectSource
	  * @throws SynapseDoesNotExistException Thrown if the specified synapse
	  *	does not exist as a source synapse.
	  */
    protected void removeDestination(Synapse outSynapse) throws SynapseDoesNotExistException
	 {
		 boolean found = true;
		 for( ProcessingUnit currentChild : this.children )
		 {
			 found = true;
			 try
			 {
				 currentChild.removeDestination(outSynapse);
			 }
			 catch(SynapseDoesNotExistException caughtException)
			 {
				 found = false;
			 }
			 
			 if( found == true )
				 return;
		 }
	 }
    
    /**
	  * Called internally to facilitate the removal of a connection.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param inSynapse The incomming synapse to remove from memory.<BR>
	  * @see com.syncleus.dann.ProcessingUnit#disconnectDestination
	  * @throws SynapseDoesNotExistException Thrown if the specified synapse
	  *	does not exist as a source synapse.
	  */
    protected void removeSource(Synapse inSynapse) throws SynapseDoesNotExistException
	 {
		 boolean found = true;
		 for( ProcessingUnit currentChild : this.children )
		 {
			 found = true;
			 try
			 {
				 currentChild.removeSource(inSynapse);
			 }
			 catch(SynapseDoesNotExistException caughtException)
			 {
				 found = false;
			 }
			 
			 if( found == true )
				 return;
		 }
	 }

	 // </editor-fold>
	 
	 // <editor-fold defaultstate="collapsed" desc="Propogation">
    
    /**
	  * Propogates the output of the ProcessingUnits from the incoming synapse to
	  * the outgoign one.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.ProcessingUnit#propagate
	  */
    public void propagate()
    {
		 for( ProcessingUnit currentChild : this.children )
		 {
			 currentChild.propagate();
		 }
    }

    /**
	  * Back propogates the taining set of the ProcessingUnits from the outgoing
	  * synapse to the incomming one.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @see com.syncleus.dann.ProcessingUnit#backPropagate
	  */
    public void backPropagate()
    {
		 for( ProcessingUnit currentChild : this.children )
		 {
			 currentChild.backPropagate();
		 }
    }
	 
	 // </editor-fold>
}
