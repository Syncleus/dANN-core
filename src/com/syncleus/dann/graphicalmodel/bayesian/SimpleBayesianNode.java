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

public class SimpleBayesianNode<S> implements BayesianNode<S>
{
	private EvidenceMap<S> evidence;
	private S state;
	private final SortedSet<S> learnedStates;
	private final BayesianNetwork network;

	public SimpleBayesianNode(final S initialState, final BayesianNetwork network)
	{
		if(initialState == null)
			throw new IllegalArgumentException("initialState can not be null");
		if(network == null)
			throw new IllegalArgumentException("network must not be null");
		
		this.state = initialState;
		this.network = network;
		this.learnedStates = new TreeSet<S>();
	}

	public void setState(final S newState)
	{
		if(newState == null)
			throw new IllegalArgumentException("newState can not be null");

		this.state = newState;
	}

	public S getState()
	{
		return this.state;
	}

	public Set<S> getLearnedStates()
	{
		return Collections.unmodifiableSet(this.learnedStates);
	}

	public void learnState()
	{
		this.updateInfluence();

		this.evidence.incrementState(this.getInputStates(),this.getState());
		this.learnedStates.add(this.state);
	}

	public double stateProbability()
	{
		this.updateInfluence();

		final StateEvidence<S> stateEvidence = this.evidence.get(this.getInputStates());
		return (stateEvidence != null ? stateEvidence.getPercentage(this.getState()) : 0.0 );
	}

	private Map<BayesianNode,Object> getInputStates()
	{
		final Map<BayesianNode,Object> inStates = new HashMap<BayesianNode,Object>();

		final Set<BayesianEdge> inEdges = this.network.getInEdges(this);
		for(final BayesianEdge inEdge : inEdges)
			inStates.put(inEdge.getSourceNode(), inEdge.getSourceNode().getState());
		
		return inStates;
	}

	protected Set<BayesianNode> getInfluencingNodes()
	{
		final Set<BayesianEdge> inEdges = this.network.getInEdges(this);
		final Set<BayesianNode> inNodes = new HashSet<BayesianNode>();
		for(final BayesianEdge inEdge : inEdges)
			inNodes.add( (inEdge.getLeftNode().equals(this) ? inEdge.getRightNode() : inEdge.getLeftNode()) );
		return Collections.unmodifiableSet( inNodes );
	}

	private boolean updateInfluence()
	{
		final Set<BayesianNode> currentInfluences = this.getInfluencingNodes();
		if(this.evidence == null)
		{
			this.evidence = new EvidenceMap<S>(currentInfluences);
			this.learnedStates.clear();
			return true;
		}
		else
			if( !currentInfluences.equals(this.evidence.getInfluencingNodes()) )
			{
				this.evidence = new EvidenceMap<S>(currentInfluences);
				this.learnedStates.clear();
				return true;
			}

		return false;
	}
}
