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

import com.syncleus.dann.graph.context.AbstractContextNode;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNodeXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.SimpleBayesianNodeElementXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.SimpleBayesianNodeXml;
import com.syncleus.dann.xml.NameXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

import java.util.*;

public class SimpleBayesianNode<S> extends AbstractContextNode<BayesianNode<S>, BayesianEdge<BayesianNode<S>>, BayesianNetwork<BayesianNode<S>, BayesianEdge<BayesianNode<S>>>> implements BayesianNode<S>
{
	private EvidenceMap<S> evidence;
	private S state;
	private final SortedSet<S> learnedStates;

	public SimpleBayesianNode(final S initialState)
	{
		super(false);
		
		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");

		this.state = initialState;
		this.learnedStates = new TreeSet<S>();
	}

	//if we leave a networks lets clear the states
	@Override
	public boolean leavingGraph(BayesianNetwork<BayesianNode<S>, BayesianEdge<BayesianNode<S>>> graph)
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

		this.evidence.incrementState(this.getInputStates(), this.state);
		this.learnedStates.add(this.state);
	}

    @Override
	public double stateProbability()
	{
		this.updateInfluence();

		final StateEvidence<S> stateEvidence = this.evidence.get(this.getInputStates());
		return (stateEvidence != null ? stateEvidence.getPercentage(this.state) : 0.0);
	}

	private Map<BayesianNode, Object> getInputStates()
	{
		//TODO change this so it only cares if it has edges to work with and doesnt care what networks its a part of
		if( !this.isGraphMember() )
			throw new IllegalStateException("This bayesian node is not currently a member of any network");

		final Map<BayesianNode, Object> inStates = new HashMap<BayesianNode, Object>();

		final Set<BayesianEdge< BayesianNode<S>>> inEdges = this.getJoinedGraphs().iterator().next().getInEdges(this);
		for(final BayesianEdge<? extends BayesianNode> inEdge : inEdges)
			inStates.put(inEdge.getSourceNode(), inEdge.getSourceNode().getState());

		return inStates;
	}

	protected Set<BayesianNode> getInfluencingNodes()
	{
		//TODO change this so it only cares if it has edges to work with and doesnt care what networks its a part of
		if( !this.isGraphMember() )
			throw new IllegalStateException("This bayesian node is not currently a member of any network");

		final Set<BayesianEdge<BayesianNode<S>>> inEdges = this.getJoinedGraphs().iterator().next().getInEdges(this);
		final Set<BayesianNode> inNodes = new HashSet<BayesianNode>();
		for(final BayesianEdge<? extends BayesianNode> inEdge : inEdges)
			inNodes.add((inEdge.getLeftNode().equals(this) ? inEdge.getRightNode() : inEdge.getLeftNode()));
		return Collections.unmodifiableSet(inNodes);
	}

	private boolean updateInfluence()
	{
		final Set<BayesianNode> currentInfluences = this.getInfluencingNodes();
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
    public SimpleBayesianNodeXml toXml()
    {
        Namer<Object> namer = new Namer<Object>();
        SimpleBayesianNodeElementXml xml = new SimpleBayesianNodeElementXml();

        xml.setStateInstances(new SimpleBayesianNodeElementXml.StateInstances());
        Set<S> writtenStates = new HashSet<S>();
        for(S learnedState : this.learnedStates)
        {
            if( writtenStates.add(learnedState) )
            {
                NamedValueXml named = new NamedValueXml();
                named.setName(namer.getNameOrCreate(learnedState));
                if(learnedState instanceof XmlSerializable)
                    named.setValue(((XmlSerializable)learnedState).toXml(namer));
                else
                    named.setValue(learnedState);
                xml.getStateInstances().getStates().add(named);
            }
        }

        if( writtenStates.add(this.state) )
        {
            NamedValueXml named = new NamedValueXml();
            named.setName(namer.getNameOrCreate(this.state));
            if(this.state instanceof XmlSerializable)
                named.setValue(((XmlSerializable)this.state).toXml(namer));
            else
                named.setValue(this.state);
            xml.getStateInstances().getStates().add(named);
        }
        
        this.toXml(xml, namer);

        return xml;
    }

    @Override
    public SimpleBayesianNodeXml toXml(Namer<Object> namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("namer can not be null");

        SimpleBayesianNodeXml xml = new SimpleBayesianNodeXml();
        this.toXml(xml, namer);
        return xml;
    }

    @Override
    public void toXml(BayesianNodeXml jaxbObject, Namer<Object> namer)
    {
        //set learned states
        if(jaxbObject.getLearnedStates() == null)
            jaxbObject.setLearnedStates(new SimpleBayesianNodeXml.LearnedStates());
        for(S learnedState : learnedStates)
        {
            NameXml stateXml = new NameXml();
            stateXml.setName(namer.getNameOrCreate(learnedState));
            jaxbObject.getLearnedStates().getStates().add(stateXml);
        }

        //set current state
        if(jaxbObject.getState() == null)
            jaxbObject.setState(new NameXml());
        jaxbObject.getState().setName(namer.getNameOrCreate(this.state));

        //set evidence map
        if((jaxbObject instanceof SimpleBayesianNodeXml) && (this.evidence != null) )
            ((SimpleBayesianNodeXml)jaxbObject).setEvidence(this.evidence.toXml(namer));
    }
}
