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

import com.syncleus.dann.graphicalmodel.bayesian.xml.StateEvidenceElementXml;
import com.syncleus.dann.graphicalmodel.bayesian.xml.StateEvidenceXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

import java.util.*;

public class StateEvidence<S> extends HashMap<S, Integer> implements XmlSerializable<StateEvidenceXml, Object>
{
	private static final long serialVersionUID = 4276706788994272957L;
	private long totalEvidence;

	public long getTotalEvidence()
	{
		return this.totalEvidence;
	}

	public double getPercentage(final S key)
	{
		final Integer stateEvidence = this.get(key);
		if( stateEvidence != null )
			return this.get(key).doubleValue() / ((double) this.totalEvidence);
		else
			return 0.0;
	}

	@Override
	public Integer put(final S key, final Integer value)
	{
		final Integer old = super.put(key, value);
		if( old != null )
			this.totalEvidence -= old;
		this.totalEvidence += value;

		return old;
	}

	@Override
	public void putAll(final Map<? extends S, ? extends Integer> map)
	{
		final Map<S, Integer> oldMap = new HashMap<S, Integer>(this);
		super.putAll(map);

		for(final Map.Entry<? extends S, ? extends Integer> entry : map.entrySet())
		{
			final Integer oldEvidence = oldMap.get(entry.getKey());
			final Integer newEvidence = this.get(entry.getKey());
			this.totalEvidence = (this.totalEvidence - oldEvidence) + newEvidence;
		}
	}

    public StateEvidenceXml toXml()
    {
        StateEvidenceElementXml xml = new StateEvidenceElementXml();
        Namer<Object> namer = new Namer<Object>();

        xml.setStates(new StateEvidenceXml.States());
        for(S state : this.keySet())
        {
            final String name = namer.getNameOrCreate(state);

            final Object stateXml;
            if(state instanceof XmlSerializable)
                stateXml = ((XmlSerializable)state).toXml(namer);
            else
                stateXml = state;

            NamedValueXml encapsulation = new NamedValueXml();
            encapsulation.setName(name);
            encapsulation.setValue(stateXml);

            xml.getStateInstances().getStates().add(encapsulation);
        }

        this.toXml(xml, namer);
        return xml;
    }

    public StateEvidenceXml toXml(Namer<Object> namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("namer can not be null");

        StateEvidenceXml xml = new StateEvidenceXml();
        this.toXml(xml, namer);
        return xml;
    }

    public void toXml(StateEvidenceXml jaxbObject, Namer<Object> namer)
    {
        if(namer == null)
            throw new IllegalArgumentException("nodeNames can not be null");
        if(jaxbObject == null)
            throw new IllegalArgumentException("jaxbObject can not be null");

        if( jaxbObject.getStates() == null)
            jaxbObject.setStates(new StateEvidenceXml.States());
        for(Map.Entry<S,Integer> entry : this.entrySet())
        {
            StateEvidenceXml.States.State stateXml = new StateEvidenceXml.States.State();
            stateXml.setName(namer.getNameOrCreate(entry.getKey()));
            stateXml.setCount(entry.getValue());
            jaxbObject.getStates().getStates().add(stateXml);
        }
    }
}
