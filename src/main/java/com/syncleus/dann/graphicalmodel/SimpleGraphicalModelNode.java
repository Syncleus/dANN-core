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
package com.syncleus.dann.graphicalmodel;

import java.util.*;
import com.syncleus.dann.graph.BidirectedEdge;
import com.syncleus.dann.graph.event.context.AbstractContextNode;
import com.syncleus.dann.graphicalmodel.xml.GraphicalModelNodeXml;
import com.syncleus.dann.graphicalmodel.xml.SimpleGraphicalModelNodeElementXml;
import com.syncleus.dann.graphicalmodel.xml.SimpleGraphicalModelNodeXml;
import com.syncleus.dann.xml.NameXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

public class SimpleGraphicalModelNode<S> extends AbstractContextNode<GraphicalModelNode<S>, BidirectedEdge<GraphicalModelNode<S>>, GraphicalModel<GraphicalModelNode<S>, BidirectedEdge<GraphicalModelNode<S>>>> implements GraphicalModelNode<S>
{
	private EvidenceMap<S> evidence;
	private S state;
	private final SortedSet<S> learnedStates;

	public SimpleGraphicalModelNode(final S initialState)
	{
		super(false);

		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");

		this.state = initialState;
		this.learnedStates = new TreeSet<S>();
	}

	/**
	 * If we leave a network, lets clear the states.
	 */
	@Override
	public boolean leavingGraph(final GraphicalModel<GraphicalModelNode<S>, BidirectedEdge<GraphicalModelNode<S>>> graph)
	{
		if( super.leavingGraph(graph) )
		{
			this.reset();
			return true;
		}
		else
			return false;
	}

	@Override
	public void reset()
	{
		this.evidence.clear();
		this.learnedStates.clear();
	}

	@Override
	public void setState(final S newState)
	{
		if( newState == null )
			throw new IllegalArgumentException("newState can not be null");

		this.state = newState;
	}

	@Override
	public S getState()
	{
		return this.state;
	}

	@Override
	public Set<S> getLearnedStates()
	{
		return Collections.unmodifiableSet(this.learnedStates);
	}

	@Override
	public void learnState()
	{
		this.updateInfluence();

		this.evidence.incrementState(this.getInfluencingStates(), this.state);
		this.learnedStates.add(this.state);
	}

	@Override
	public double stateProbability()
	{
		this.updateInfluence();

		final StateEvidence<S> stateEvidence = this.evidence.get(this.getInfluencingStates());
		return ((stateEvidence == null) ? 0.0 : stateEvidence.getPercentage(this.state));
	}

	@Override
	public double stateProbability(Set<? extends GraphicalModelNode> ignoredInfluences)
	{
		final Set<GraphicalModelNode> influences = new HashSet<GraphicalModelNode>(this.getInfluencingNodes());
		influences.removeAll(ignoredInfluences);

		int evidenceOccurrence = 0;
		int totalOccurrence = 0;

		NextEvidence:
		for(final Map.Entry<Map<GraphicalModelNode, Object>, StateEvidence<S>> evidenceEntry : this.evidence.entrySet())
		{
			final Map<GraphicalModelNode, Object> influencingEvidence = evidenceEntry.getKey();
			for(GraphicalModelNode influence : influences)
			{
				final Object influencingEvidenceState = influencingEvidence.get(influence);
				if( (influencingEvidenceState == null)||(!influencingEvidenceState.equals(influence.getState())) )
					continue NextEvidence;
			}

			final StateEvidence<S> evidence = evidenceEntry.getValue();

			final Integer currentEvidenceOccurrence = evidence.get(this.getState());
			if( currentEvidenceOccurrence != null )
				evidenceOccurrence += evidence.get(this.getState());
			totalOccurrence += evidence.getTotalEvidence();
		}

		if( totalOccurrence == 0 )
			return 0.0;

		return ((double)evidenceOccurrence) / ((double)totalOccurrence);
	}

	private Map<GraphicalModelNode, Object> getInfluencingStates()
	{
		//TODO change this so it only cares if it has edges to work with and doesnt care what networks its a part of
		if( !this.isGraphMember() )
			throw new IllegalStateException("This graphical model node is not currently a member of any network");

		final Map<GraphicalModelNode, Object> inStates = new HashMap<GraphicalModelNode, Object>();

		final Set<BidirectedEdge<GraphicalModelNode<S>>> inEdges = this.getJoinedGraphs().iterator().next().getAdjacentEdges(this);
		for(final BidirectedEdge<GraphicalModelNode<S>> inEdge : inEdges)
		{
			//if it is traversable to this node it is an influence
			List<GraphicalModelNode<S>> otherNodes = new ArrayList<GraphicalModelNode<S>>(inEdge.getTargets());
			otherNodes.remove(this);
			GraphicalModelNode<S> otherNode = otherNodes.get(0);
			if( inEdge.isTraversable(otherNode) )
				inStates.put(otherNode, otherNode.getState());
		}

		return inStates;
	}

	protected Set<GraphicalModelNode> getInfluencingNodes()
	{
		//TODO change this so it only cares if it has edges to work with and doesnt care what networks its a part of
		if( !this.isGraphMember() )
			throw new IllegalStateException("This graphical model node is not currently a member of any network");

		final Set<BidirectedEdge<GraphicalModelNode<S>>> inEdges = this.getJoinedGraphs().iterator().next().getAdjacentEdges(this);
		final Set<GraphicalModelNode> inNodes = new HashSet<GraphicalModelNode>();
		for(final BidirectedEdge<GraphicalModelNode<S>> inEdge : inEdges)
		{
			//if it is traversable to this node it is an influence
			List<GraphicalModelNode<S>> otherNodes = new ArrayList<GraphicalModelNode<S>>(inEdge.getTargets());
			otherNodes.remove(this);
			GraphicalModelNode<S> otherNode = otherNodes.get(0);
			if( inEdge.isTraversable(otherNode) )
				inNodes.add(otherNode);
		}
		return Collections.unmodifiableSet(inNodes);
	}

	private boolean updateInfluence()
	{
		final Set<GraphicalModelNode> currentInfluences = this.getInfluencingNodes();
		if( this.evidence == null )
		{
			this.evidence = new EvidenceMap<S>(currentInfluences);
			this.learnedStates.clear();
			return true;
		}
		else if( !currentInfluences.equals(this.evidence.getInfluencingNodes()) )
		{
			this.evidence = new EvidenceMap<S>(currentInfluences);
			this.learnedStates.clear();
			return true;
		}

		return false;
	}

	@Override
	public SimpleGraphicalModelNodeXml toXml()
	{
		final Namer<Object> namer = new Namer<Object>();
		final SimpleGraphicalModelNodeElementXml xml = new SimpleGraphicalModelNodeElementXml();

		xml.setStateInstances(new SimpleGraphicalModelNodeElementXml.StateInstances());
		final Set<S> writtenStates = new HashSet<S>();
		for (S learnedState : this.learnedStates)
		{
			if (writtenStates.add(learnedState))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(learnedState));
				if (learnedState instanceof XmlSerializable)
				{
					named.setValue(((XmlSerializable) learnedState).toXml(namer));
				}
				else
				{
					named.setValue(learnedState);
				}
				xml.getStateInstances().getStates().add(named);
			}
		}

		if (writtenStates.add(this.state))
		{
			final NamedValueXml named = new NamedValueXml();
			named.setName(namer.getNameOrCreate(this.state));
			if (this.state instanceof XmlSerializable)
			{
				named.setValue(((XmlSerializable) this.state).toXml(namer));
			}
			else
			{
				named.setValue(this.state);
			}
			xml.getStateInstances().getStates().add(named);
		}

		this.toXml(xml, namer);

		return xml;
	}

	@Override
	public SimpleGraphicalModelNodeXml toXml(final Namer<Object> namer)
	{
		if (namer == null)
		{
			throw new IllegalArgumentException("namer can not be null");
		}

		final SimpleGraphicalModelNodeXml xml = new SimpleGraphicalModelNodeXml();
		this.toXml(xml, namer);
		return xml;
	}

	@Override
	public void toXml(final GraphicalModelNodeXml jaxbObject, final Namer<Object> namer)
	{
		//set learned states
		if (jaxbObject.getLearnedStates() == null)
		{
			jaxbObject.setLearnedStates(new SimpleGraphicalModelNodeXml.LearnedStates());
		}
		for (S learnedState : learnedStates)
		{
			final NameXml stateXml = new NameXml();
			stateXml.setName(namer.getNameOrCreate(learnedState));
			jaxbObject.getLearnedStates().getStates().add(stateXml);
		}

		//set current state
		if (jaxbObject.getState() == null)
		{
			jaxbObject.setState(new NameXml());
		}
		jaxbObject.getState().setName(namer.getNameOrCreate(this.state));

		//set evidence map
		if ((jaxbObject instanceof SimpleGraphicalModelNodeXml) && (this.evidence != null))
		{
			((SimpleGraphicalModelNodeXml) jaxbObject).setEvidence(this.evidence.toXml(namer));
		}
	}
}
