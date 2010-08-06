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

import com.syncleus.dann.graphicalmodel.bayesian.xml.EvidenceMapElementXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.EvidenceMapXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.StateEvidenceXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

import java.util.*;

public class EvidenceMap<S> extends HashMap<Map<BayesianNode, Object>, StateEvidence<S>> implements XmlSerializable<EvidenceMapXml, Object>
{
	private static final long serialVersionUID = 5956089319330421885L;
	private final Set<BayesianNode> influencingNodes;

	public EvidenceMap(final Set<BayesianNode> influencingNodes)
	{
		this.influencingNodes = Collections.unmodifiableSet(new HashSet<BayesianNode>(influencingNodes));
	}

	public EvidenceMap(final BayesianNode influencingNode)
	{
		final Set<BayesianNode> newInfluences = new HashSet<BayesianNode>();
		newInfluences.add(influencingNode);
		this.influencingNodes = Collections.unmodifiableSet(newInfluences);
	}

	public Set<BayesianNode> getInfluencingNodes()
	{
		return this.influencingNodes;
	}

	public int incrementState(final Set<BayesianNode> influences, final S state)
	{
		final Map<BayesianNode, Object> influenceMap = new HashMap<BayesianNode, Object>();
		for(final BayesianNode influence : influences)
			influenceMap.put(influence, influence.getState());
		return this.incrementState(influenceMap, state);
	}

	public int incrementState(final Map<BayesianNode, Object> influence, final S state)
	{
		this.verifyInfluencingStates(influence);
		StateEvidence<S> stateEvidence = this.get(influence);
		if( stateEvidence == null )
		{
			stateEvidence = new StateEvidence<S>();
			this.put(influence, stateEvidence);
		}
		Integer evidence = stateEvidence.get(state);
		if( evidence == null )
			evidence = 1;
		else
			evidence = evidence + 1;
		stateEvidence.put(state, evidence);
		return evidence;
	}

	private void verifyInfluencingStates(final Map<BayesianNode, Object> influencingStates)
	{
		if( !influencingStates.keySet().equals(this.influencingNodes) )
			throw new IllegalArgumentException("wrong number of influencing nodes");
	}

	@Override
	public boolean containsKey(final Object keyObj)
	{
		if( keyObj instanceof Set )
		{
			final Set key = (Set) keyObj;
			final Map<BayesianNode, Object> newKey = new HashMap<BayesianNode, Object>();
			for(final Object nodeObj : key)
			{
				if( nodeObj instanceof BayesianNode )
				{
					final BayesianNode node = (BayesianNode) nodeObj;
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
	public StateEvidence<S> get(final Object keyObj)
	{
		if( keyObj instanceof Set )
		{
			final Set key = (Set) keyObj;

			final Map<BayesianNode, Object> newKey = new HashMap<BayesianNode, Object>();
			for(final Object nodeObj : key)
			{
				if( nodeObj instanceof BayesianNode )
				{
					final BayesianNode node = (BayesianNode) nodeObj;
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
	public StateEvidence<S> put(final Map<BayesianNode, Object> key, final StateEvidence<S> value)
	{
		this.verifyInfluencingStates(key);

		return super.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends Map<BayesianNode, Object>, ? extends StateEvidence<S>> map)
	{
		for(final Map<BayesianNode, Object> inputStates : map.keySet())
			this.verifyInfluencingStates(inputStates);
		super.putAll(map);
	}

    public EvidenceMapXml toXml()
    {
        EvidenceMapElementXml xml = new EvidenceMapElementXml();
        Namer<Object> namer = new Namer<Object>();

        final Set<BayesianNode> seenNodes = new HashSet<BayesianNode>();
        final Set<Object> seenStates = new HashSet<Object>();

        xml.setNodeInstances(new EvidenceMapElementXml.NodeInstances());
        xml.setStateInstances(new EvidenceMapElementXml.StateInstances());
        for(Map.Entry<Map<BayesianNode, Object>, StateEvidence<S>> entry : this.entrySet())
        {
            final Map<BayesianNode, Object> influences = entry.getKey();
            final StateEvidence<S> evidence = entry.getValue();

            //add instances for all the nodes and states from the influences
            for(Map.Entry<BayesianNode, Object> influenceEntry : influences.entrySet())
            {
                BayesianNode node = influenceEntry.getKey();
                Object state = influenceEntry.getValue();

                if( seenStates.add(state) )
                {
                    final Object stateXml;
                    if(state instanceof XmlSerializable)
                        stateXml = ((XmlSerializable)state).toXml(namer);
                    else
                        stateXml = state;

                    NamedValueXml encapsulation = new NamedValueXml();
                    encapsulation.setName( namer.getNameOrCreate(state) );
                    encapsulation.setValue(stateXml);
                    xml.getStateInstances().getStates().add(encapsulation);
                }

                if( seenNodes.add(node) )
                {
                    final Object nodeXml;
                    if(node instanceof XmlSerializable)
                        nodeXml = ((XmlSerializable)node).toXml(namer);
                    else
                        nodeXml = node;

                    NamedValueXml encapsulation = new NamedValueXml();
                    encapsulation.setName( namer.getNameOrCreate(node) );
                    encapsulation.setValue(nodeXml);
                    xml.getNodeInstances().getNodes().add(encapsulation);
                }
            }

            //add instances for all states from the evidence
            for(S state : evidence.keySet())
            {
                if( seenStates.add(state) )
                {
                    final Object stateXml;
                    if(state instanceof XmlSerializable)
                        stateXml = ((XmlSerializable)state).toXml(namer);
                    else
                        stateXml = state;

                    NamedValueXml encapsulation = new NamedValueXml();
                    encapsulation.setName( namer.getNameOrCreate(state) );
                    encapsulation.setValue(stateXml);
                    xml.getStateInstances().getStates().add(encapsulation);
                }
            }
        }

        this.toXml(xml, namer);
        return xml;
    }

    public EvidenceMapXml toXml(Namer<Object> namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("namer can not be null");

        EvidenceMapXml xml = new EvidenceMapXml();
        this.toXml(xml, namer);
        return xml;
    }

    public void toXml(EvidenceMapXml jaxbObject, Namer<Object> namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("nodeNames can not be null");
        if(jaxbObject == null)
            throw new IllegalArgumentException("jaxbObject can not be null");

        if( jaxbObject.getInfluencedEvidences() == null)
            jaxbObject.setInfluencedEvidences(new EvidenceMapXml.InfluencedEvidences());
        for(Map.Entry<Map<BayesianNode, Object>, StateEvidence<S>> entry : this.entrySet())
        {
            EvidenceMapXml.InfluencedEvidences.InfluencedEvidence influencedEvidence = new EvidenceMapXml.InfluencedEvidences.InfluencedEvidence();

            //add the influences to the xml
            influencedEvidence.setInfluences(new EvidenceMapXml.InfluencedEvidences.InfluencedEvidence.Influences());
            for(Map.Entry<BayesianNode, Object> influenceEntry : entry.getKey().entrySet())
            {
                final EvidenceMapXml.InfluencedEvidences.InfluencedEvidence.Influences.Influence influenceXml = new EvidenceMapXml.InfluencedEvidences.InfluencedEvidence.Influences.Influence();
                influenceXml.setNode(namer.getNameOrCreate(influenceEntry.getKey()));
                influenceXml.setState(namer.getNameOrCreate(influenceEntry.getValue()));
                influencedEvidence.getInfluences().getInfluences().add(influenceXml);
            }

            //add the state evidence to the xml
            influencedEvidence.setStateEvidence(entry.getValue().toXml(namer));

            jaxbObject.getInfluencedEvidences().getInfluencedEvidences().add(influencedEvidence);
        }
    }
}
