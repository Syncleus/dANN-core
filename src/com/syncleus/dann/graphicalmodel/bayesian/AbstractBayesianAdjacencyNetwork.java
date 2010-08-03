/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graphicalmodel.bayesian;

import java.util.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.xml.*;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNetworkElementXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNetworkXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

public abstract class AbstractBayesianAdjacencyNetwork<N extends BayesianNode, E extends BayesianEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements BayesianNetwork<N, E>
{
	protected AbstractBayesianAdjacencyNetwork()
	{
		super();
	}

	protected AbstractBayesianAdjacencyNetwork(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractBayesianAdjacencyNetwork(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public void learnStates()
	{
		for(final N node : this.getNodes())
			node.learnState();
	}

	public double jointProbability()
	{
		double probabilityProduct = 1.0;
		for(final N node : this.getNodes())
			probabilityProduct *= node.stateProbability();
		return probabilityProduct;
	}

	public double conditionalProbability(final Set<N> goals, final Set<N> influences)
	{
		List<N> varyingNodes = new ArrayList<N>(this.getNodes());

		//calculate numerator
		varyingNodes.removeAll(influences);
		varyingNodes.removeAll(goals);
		resetNodeStates(varyingNodes);
		double numerator = 0.0;
		do
		{
			numerator += this.jointProbability();
		}
		while( !incrementNodeStates(varyingNodes) );

		//calculate denominator
		varyingNodes = new ArrayList<N>(this.getNodes());
		varyingNodes.removeAll(influences);
		resetNodeStates(varyingNodes);
		double denominator = 0.0;
		do
		{
			denominator += this.jointProbability();
		}
		while( !incrementNodeStates(varyingNodes) );

		//all done
		return numerator / denominator;
	}

	@SuppressWarnings("unchecked")
	private static <N extends BayesianNode> void resetNodeStates(final List<N> incNodes)
	{
		for(final N incNode : incNodes)
			incNode.setState((incNode.getLearnedStates().toArray())[0]);
	}

	private static <N extends BayesianNode> boolean incrementNodeStates(final List<N> incNodes)
	{
		for(final N incNode : incNodes)
			if( !incrementNodeState(incNode) )
				return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private static <N extends BayesianNode> boolean incrementNodeState(final N incNode)
	{
		final List stateTypes = Arrays.asList(incNode.getLearnedStates().toArray());
		final int currentStateIndex = stateTypes.indexOf(incNode.getState());
		if( (currentStateIndex + 1) >= stateTypes.size() )
		{
			incNode.setState(stateTypes.get(0));
			return true;
		}
		else
		{
			incNode.setState(stateTypes.get(currentStateIndex + 1));
			return false;
		}
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final N newNode)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> clone()
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>) super.clone();
	}

    @Override
    public BayesianNetworkXml toXml()
    {
        BayesianNetworkElementXml networkXml = new BayesianNetworkElementXml();
        Namer namer = new Namer();

        networkXml.setNodeInstances( new BayesianNetworkElementXml.NodeInstances() );
        networkXml.setStateInstances( new BayesianNetworkElementXml.StateInstances() );
        Set<Object> writtenStates = new HashSet<Object>();
        for( N node : this.getNodes() )
        {
            //add the node
            NamedValueXml nodeXml = new NamedValueXml();
            nodeXml.setName(namer.getNameOrCreate(node));
            if( node instanceof XmlSerializable)
                nodeXml.setValue(((XmlSerializable)node).toXml(namer));
            else
                nodeXml.setValue(node);
            networkXml.getNodeInstances().getNodes().add(nodeXml);

            //add all the node's learned states
            for( Object learnedState : node.getLearnedStates() )
            {
                //only add the learnedState if it hasnt yet been added
                if( writtenStates.add(learnedState) )
                {
                    System.out.println("3");
                    NamedValueXml stateXml = new NamedValueXml();
                    stateXml.setName(namer.getNameOrCreate(learnedState));
                    if( learnedState instanceof XmlSerializable)
                        stateXml.setValue(((XmlSerializable) learnedState).toXml(namer));
                    else
                        stateXml.setValue(learnedState);
                    networkXml.getStateInstances().getStates().add(stateXml);
                }
            }

            //add the nodes current state if it wasnt already
            Object state = node.getState();
            if( writtenStates.add(state) )
            {
                NamedValueXml stateXml = new NamedValueXml();
                stateXml.setName(namer.getNameOrCreate(state));
                if( state instanceof XmlSerializable)
                    stateXml.setValue(((XmlSerializable)state).toXml(namer));
                else
                    stateXml.setValue(state);
                networkXml.getStateInstances().getStates().add(stateXml);
            }
        }

        this.toXml(networkXml, new Namer());
        return networkXml;
    }

    @Override
    public BayesianNetworkXml toXml(Namer namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("namer can not be null");

        BayesianNetworkXml xml = new BayesianNetworkXml();
        this.toXml(xml, namer);
        return xml;
    }

    @Override
    public void toXml(GraphXml jaxbObject, Namer namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("nodeNames can not be null");
        if(jaxbObject == null)
            throw new IllegalArgumentException("jaxbObject can not be null");

        super.toXml(jaxbObject, namer);

        /*
        if( jaxbObject instanceof BayesianNetworkXml )
        {
            BayesianNetworkXml networkXml = ((BayesianNetworkXml)jaxbObject);
            if( networkXml.getStates() == null )
                networkXml.setStates( new BayesianNetworkXml.States() );

            for( N node : this.getNodes() )
            {
                for( Object state : node.getLearnedStates() )
                {
                    NamedValueXml stateXml = new NamedValueXml();
                    stateXml.setName(namer.getNameOrCreate(state));
                    if( state instanceof XmlSerializable)
                        stateXml.setValue(((XmlSerializable)state).toXml(namer));
                    else
                        stateXml.setValue(state);
                    networkXml.getStates().getState().add(stateXml);
                }

                Object state = node.getState();
                NamedValueXml stateXml = new NamedValueXml();
                stateXml.setName(namer.getNameOrCreate(state));
                if( state instanceof XmlSerializable)
                    stateXml.setValue(((XmlSerializable)state).toXml(namer));
                else
                    stateXml.setValue(state);
                networkXml.getStates().getState().add(stateXml);
            }
        }*/
    }

	protected static class NodeConnectivity<N extends BayesianNode, E extends BayesianEdge<N>> extends HashMap<N, Set<E>>
	{
		private static final long serialVersionUID = -3068604309573134643L;

		public Set<E> get(final N keyNode)
		{

			Set<E> edges = super.get(keyNode);
			if( edges == null )
			{
				edges = new HashSet<E>();
				super.put(keyNode, edges);
			}
			return edges;
		}
	}
}
