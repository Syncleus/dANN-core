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
package com.syncleus.dann.graph;

import com.syncleus.dann.graph.xml.*;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

import java.util.*;

public abstract class AbstractBidirectedEdge<N> extends AbstractEdge<N> implements BidirectedEdge<N>
{
	private final N leftNode;
	private final N rightNode;
	private final EndState leftEndState;
	private final EndState rightEndState;

    protected AbstractBidirectedEdge()
    {
        this.leftNode = null;
        this.rightNode= null;
        this.leftEndState = null;
        this.rightEndState = null;
    }

	protected AbstractBidirectedEdge(final N leftNode, final EndState leftEndState, final N rightNode, final EndState rightEndState)
	{
		super(packNodes(leftNode, rightNode));

		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.leftEndState = leftEndState;
		this.rightEndState = rightEndState;
	}

	private static <N> List<N> packNodes(final N leftNode, final N rightNode)
	{
		final List<N> pack = new ArrayList<N>();
		pack.add(leftNode);
		pack.add(rightNode);
		return pack;
	}

	public final N getLeftNode()
	{
		return this.leftNode;
	}

	public final N getRightNode()
	{
		return this.rightNode;
	}

	public final EndState getLeftEndState()
	{
		return this.leftEndState;
	}

	public final EndState getRightEndState()
	{
		return this.rightEndState;
	}

	public boolean isIntroverted()
	{
		return (this.rightEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.INWARD) && (this.leftEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.INWARD);
	}

	public boolean isExtraverted()
	{
		return (this.rightEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.OUTWARD) && (this.leftEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.OUTWARD);
	}

	public boolean isDirected()
	{
		if( (this.rightEndState == EndState.INWARD) && (this.leftEndState == EndState.OUTWARD) )
			return true;
		else if( (this.rightEndState == EndState.OUTWARD) && (this.leftEndState == EndState.INWARD) )
			return true;
		return false;
	}

	public boolean isHalfEdge()
	{
		if( (this.rightEndState == EndState.NONE) && (this.leftEndState != EndState.NONE) )
			return true;
		else if( (this.rightEndState != EndState.NONE) && (this.leftEndState == EndState.NONE) )
			return true;
		return false;
	}

	public boolean isLooseEdge()
	{
		return (this.rightEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.NONE) && (this.leftEndState == com.syncleus.dann.graph.BidirectedEdge.EndState.NONE);
	}

	public boolean isOrdinaryEdge()
	{
		return (!this.isHalfEdge()) && (!this.isLooseEdge());
	}

	public boolean isLoop()
	{
		return this.leftEndState.equals(this.rightEndState);
	}

	@Override
	public String toString()
	{
		return this.leftNode.toString() +
				endStateToString(this.leftEndState, true) +
				'-' +
				endStateToString(this.rightEndState, false) +
				this.rightNode;
	}

	private static String endStateToString(final EndState state, final boolean isLeft)
	{
		switch(state)
		{
		case INWARD:
			return (isLeft ? ">" : "<");
		case OUTWARD:
			return (isLeft ? "<" : ">");
		default:
			return "";
		}
	}

	@Override
	public AbstractBidirectedEdge<N> clone()
	{
		return (AbstractBidirectedEdge<N>) super.clone();
	}

    @Override
    public BidirectedEdgeXml toXml()
    {
        Namer namer = new Namer();
        BidirectedEdgeElementXml xml = new BidirectedEdgeElementXml();

        xml.setNodeInstances(new BidirectedEdgeElementXml.NodeInstances());
        Set<N> writtenNodes = new HashSet<N>();
        for(N node : this.getNodes())
        {
            if( writtenNodes.add(node) )
            {
                NamedValueXml named = new NamedValueXml();
                named.setName(namer.getNameOrCreate(node));
                if(node instanceof XmlSerializable)
                    named.setValue(((XmlSerializable)node).toXml(namer));
                else
                    named.setValue(node);
                xml.getNodeInstances().getNodes().add(named);
            }
        }

        return xml;
    }

    @Override
    public BidirectedEdgeXml toXml(Namer nodeNames)
    {
        if(nodeNames == null)
            throw new IllegalArgumentException("nodeNames can not be null");

        BidirectedEdgeXml xml = new BidirectedEdgeXml();
        this.toXml(xml, nodeNames);
        return xml;
    }

    @Override
    public void toXml(EdgeXml jaxbObject, Namer nodeNames)
    {
        if(nodeNames == null)
            throw new IllegalArgumentException("nodeNames can not be null");
        if(jaxbObject == null)
            throw new IllegalArgumentException("jaxbObject can not be null");

        super.toXml(jaxbObject, nodeNames);

        if( jaxbObject instanceof BidirectedEdgeXml )
        {
            ((BidirectedEdgeXml)jaxbObject).setLeftNode(nodeNames.getNameOrCreate(this.leftNode));
            ((BidirectedEdgeXml)jaxbObject).setRightNode(nodeNames.getNameOrCreate(this.rightNode));
            ((BidirectedEdgeXml)jaxbObject).setLeftDirection(this.leftEndState.toString().toLowerCase());
            ((BidirectedEdgeXml)jaxbObject).setRightDirection(this.rightEndState.toString().toLowerCase());
        }
    }
}
