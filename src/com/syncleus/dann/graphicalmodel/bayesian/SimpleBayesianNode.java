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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleBayesianNode implements BayesianNode
{
	private EvidenceMap evidence;
	private Enum state;
	private BayesianNetwork network;

	public SimpleBayesianNode(Enum initialState, BayesianNetwork network)
	{
		if(initialState == null)
			throw new IllegalArgumentException("initialState can not be null");
		if(network == null)
			throw new IllegalArgumentException("network must not be null");
		
		this.state = initialState;
		this.network = network;
	}

	public Class getStateDeclaringClass()
	{
		return state.getDeclaringClass();
	}

	public void setState(Enum newState)
	{
		if(newState == null)
			throw new IllegalArgumentException("newState can not be null");

		this.state = newState;
	}

	public Enum getState()
	{
		return this.state;
	}

	public void learnState()
	{
		this.updateInfluence();

		this.evidence.incrementState(this.getInputStates(),this.getState());
	}

	public double stateProbability()
	{
		this.updateInfluence();

		StateEvidence stateEvidence = this.evidence.get(this.getInputStates());
		return stateEvidence.getPercentage(this.getState());
	}

	private Map<BayesianNode,Enum> getInputStates()
	{
		Map<BayesianNode,Enum> inStates = new HashMap<BayesianNode,Enum>();

		List<BayesianEdge> inEdges = this.network.getInEdges(this);
		for(BayesianEdge inEdge : inEdges)
			inStates.put(inEdge.getSourceNode(), inEdge.getSourceNode().getState());
		
		return inStates;
	}

	private Set<BayesianNode> getInfluencingNodes()
	{
		List<BayesianEdge> inEdges = this.network.getInEdges(this);
		Set<BayesianNode> inNodes = new HashSet<BayesianNode>();
		for(BayesianEdge inEdge : inEdges)
			inNodes.add( (inEdge.getLeftNode().equals(this) ? inEdge.getRightNode() : inEdge.getLeftNode()) );
		return Collections.unmodifiableSet( inNodes );
	}

	private void updateInfluence()
	{
		Set<BayesianNode> currentInfluences = this.getInfluencingNodes();
		if((this.evidence == null)&&(currentInfluences.size() > 0))
			this.evidence = new EvidenceMap(currentInfluences);
		else
			if( !currentInfluences.equals(this.evidence.getInfluencingNodes()) )
				this.evidence = new EvidenceMap(currentInfluences);
	}
}
