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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EvidenceMap<S> extends HashMap<Map<BayesianNode,Object>,StateEvidence<S>>
{
	private final Set<BayesianNode> influencingNodes;

	public EvidenceMap(final Set<BayesianNode> influencingNodes)
	{
		this.influencingNodes = Collections.unmodifiableSet(new HashSet<BayesianNode>(influencingNodes));
	}

	public EvidenceMap(final BayesianNode influencingNode)
	{
		Set<BayesianNode> newInfluences = new HashSet<BayesianNode>();
		newInfluences.add(influencingNode);
		this.influencingNodes = Collections.unmodifiableSet(newInfluences);
	}

	public Set<BayesianNode> getInfluencingNodes()
	{
		return this.influencingNodes;
	}

	public int incrementState(Set<BayesianNode> influences, S state)
	{
		Map<BayesianNode,Object> influenceMap = new HashMap<BayesianNode,Object>();
		for(BayesianNode influence : influences)
			influenceMap.put(influence, influence.getState());
		
		return this.incrementState(influenceMap, state);
	}

	public int incrementState(Map<BayesianNode,Object> influence, S state)
	{
		this.verifyInfluencingStates(influence);

		StateEvidence<S> stateEvidence = this.get(influence);
		if(stateEvidence == null)
		{
			stateEvidence = new StateEvidence<S>();
			this.put(influence, stateEvidence);
		}

		Integer evidence = stateEvidence.get(state);
		if(evidence == null)
			evidence = Integer.valueOf(1);
		else
			evidence = Integer.valueOf(evidence + 1);

		stateEvidence.put(state, evidence);

		return evidence;
	}

	private void verifyInfluencingStates(Map<BayesianNode,Object> influencingStates)
	{
		if(! influencingStates.keySet().equals(this.influencingNodes))
			throw new IllegalArgumentException ("wrong number of influencing nodes");
	}

	@Override
	public boolean containsKey(Object keyObj)
	{
		if(keyObj instanceof Set)
		{
			Set key = (Set) keyObj;

			Map<BayesianNode,Object> newKey = new HashMap<BayesianNode,Object>();
			for(Object nodeObj : key)
			{
				if(nodeObj instanceof BayesianNode)
				{
					BayesianNode node = (BayesianNode) nodeObj;
					newKey.put(node, node.getState());
				}
				else
					return super.containsKey(keyObj);
			}
			return super.containsKey(newKey);
		}

		return super.containsKey(keyObj);
	}

	@Override
	public StateEvidence<S> get(Object keyObj)
	{
		if(keyObj instanceof Set)
		{
			Set key = (Set) keyObj;

			Map<BayesianNode,Object> newKey = new HashMap<BayesianNode,Object>();
			for(Object nodeObj : key)
			{
				if(nodeObj instanceof BayesianNode)
				{
					BayesianNode node = (BayesianNode) nodeObj;
					newKey.put(node, node.getState());
				}
				else
					return super.get(keyObj);
			}
			return super.get(newKey);
		}

		return super.get(keyObj);
	}

	@Override
	public StateEvidence<S> put(Map<BayesianNode,Object> key, StateEvidence<S> value)
	{
		this.verifyInfluencingStates(key);

		return super.put(key,value);
	}

	@Override
	public void putAll(Map<? extends Map<BayesianNode,Object>,? extends StateEvidence<S>> map)
	{
		for(Map<BayesianNode,Object> inputStates : map.keySet())
			this.verifyInfluencingStates(inputStates);
		super.putAll(map);
	}
}
