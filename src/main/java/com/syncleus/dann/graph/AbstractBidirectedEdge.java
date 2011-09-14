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

import java.util.*;
import com.syncleus.dann.graph.xml.*;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

public abstract class AbstractBidirectedEdge<N, LN extends N, RN extends N> extends AbstractEdge<N> implements MixableBidirectedEdge<N, LN,RN>
{
	@Override
	public abstract AbstractEndpoint<LN,RN> getLeftEndPoint();

	@Override
	public abstract AbstractEndpoint<RN,LN> getRightEndPoint();

	@Override
	public final Set<Endpoint<N, N>> getEndPoints()
	{
		return new EndPointsSet();
	}

	private static <N> List<N> packNodes(final N leftNode, final N rightNode)
	{
		final List<N> pack = new ArrayList<N>();
		pack.add(leftNode);
		pack.add(rightNode);
		return pack;
	}

	@Override
	public boolean isIntroverted()
	{
		return (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD);
	}

	@Override
	public boolean isExtroverted()
	{
		return (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD);
	}

	@Override
	public boolean isDirected()
	{
		if( (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) )
			return true;
		else if( (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) )
			return true;
		return false;
	}

	@Override
	public boolean isHalfEdge()
	{
		if( (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndPoint().getDirection() != MixableBidirectedEdge.Endpoint.Direction.NONE) )
			return true;
		else if( (this.getRightEndPoint().getDirection() != MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) )
			return true;
		return false;
	}

	@Override
	public boolean isLooseEdge()
	{
		return (this.getRightEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndPoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE);
	}

	@Override
	public boolean isOrdinaryEdge()
	{
		return (!this.isHalfEdge()) && (!this.isLooseEdge());
	}

	@Override
	public boolean isLoop()
	{
		return this.getLeftEndPoint().getDirection().equals(this.getRightEndPoint().getDirection());
	}

	@Override
	public String toString()
	{
		return this.getLeftEndPoint().getTarget().toString()
				+ endStateToString(this.getLeftEndPoint().getDirection(), true)
				+ '-'
				+ endStateToString(this.getRightEndPoint().getDirection(), false)
				+ this.getRightEndPoint().getTarget();
	}

	private static String endStateToString(final MixableBidirectedEdge.Endpoint.Direction state, final boolean isLeft)
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
	protected AbstractBidirectedEdge<N, LN,RN> clone()
	{
		return (AbstractBidirectedEdge<N, LN,RN>) super.clone();
	}

	@Override
	public BidirectedEdgeXml toXml()
	{
		final Namer namer = new Namer();
		final BidirectedEdgeElementXml xml = new BidirectedEdgeElementXml();

		xml.setNodeInstances(new BidirectedEdgeElementXml.NodeInstances());
		final Set<N> writtenNodes = new HashSet<N>();
		for (N node : this.getTargets())
		{
			if (writtenNodes.add(node))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(node));
				if (node instanceof XmlSerializable)
				{
					named.setValue(((XmlSerializable) node).toXml(namer));
				}
				else
				{
					named.setValue(node);
				}
				xml.getNodeInstances().getNodes().add(named);
			}
		}

		return xml;
	}

	@Override
	public BidirectedEdgeXml toXml(final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}

		final BidirectedEdgeXml xml = new BidirectedEdgeXml();
		this.toXml(xml, nodeNames);
		return xml;
	}

	@Override
	public void toXml(final EdgeXml jaxbObject, final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}
		if (jaxbObject == null)
		{
			throw new IllegalArgumentException("jaxbObject can not be null");
		}

		super.toXml(jaxbObject, nodeNames);

		if (jaxbObject instanceof BidirectedEdgeXml)
		{
			((BidirectedEdgeXml) jaxbObject).setLeftNode(nodeNames.getNameOrCreate(this.getLeftEndPoint().getTarget()));
			((BidirectedEdgeXml) jaxbObject).setRightNode(nodeNames.getNameOrCreate(this.getRightEndPoint().getTarget()));
			((BidirectedEdgeXml) jaxbObject).setLeftDirection(this.getLeftEndPoint().getDirection().toString().toLowerCase());
			((BidirectedEdgeXml) jaxbObject).setRightDirection(this.getRightEndPoint().getDirection().toString().toLowerCase());
		}
	}

	private final class EndPointsSet implements Set<Endpoint<N, N>>
	{
		public EndPointsSet()
		{
		}

		@Override
		public int size()
		{
			return 2;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean contains(Object o)
		{
			if( (getLeftEndPoint().equals(o)) || (getRightEndPoint().equals(o)) )
				return true;
			return false;
		}

		@Override
		public Iterator<Endpoint<N, N>> iterator()
		{
			return new EndPointIterator();
		}

		@Override
		public Endpoint<N, N>[] toArray()
		{
			return new Endpoint[]{getLeftEndPoint(),getRightEndPoint()};
		}

		@Override
		public <T> T[] toArray(T[] a)
		{
			a[0] = (T) getLeftEndPoint();
			a[1] = (T) getRightEndPoint();
			return a;
		}

		@Override
		public boolean add(Endpoint<N, N> nnEndpoint)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			for(Object object : c)
				if( !this.contains(object) )
					return false;
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends Endpoint<N, N>> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public void clear()
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		private class EndPointIterator implements Iterator<Endpoint<N, N>>
		{
			private Boolean stage = Boolean.TRUE;

			public EndPointIterator()
			{
			}

			@Override
			public boolean hasNext()
			{
				if(stage == null)
					return false;
				return true;
			}

			@Override
			public Endpoint<N, N> next()
			{
				if(stage == Boolean.TRUE)
				{
					stage = false;
					return getLeftEndPoint();
				}
				else if(stage == Boolean.FALSE)
				{
					stage = null;
					return getRightEndPoint();
				}

				throw new NoSuchElementException("no elements left!");
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("This iterator is read-only!");
			}
		};
	}

	protected abstract class AbstractEndpoint<EN extends N, ON extends N> extends AbstractEdge<N>.AbstractEndpoint<EN> implements MixableBidirectedEdge.Endpoint<N, EN,ON>
	{
		private EN node = null;
		private Direction direction;

		protected AbstractEndpoint(Direction direction)
		{
			super();

			if( direction == null )
				throw new IllegalArgumentException("direction can not be null!");
			this.direction = direction;
		}

		protected AbstractEndpoint(EN node, Direction direction)
		{
			this(direction);

			this.node = node;
		}

		@Override
		public EN getTarget()
		{
			return this.node;
		}

		public void setTarget(final EN node)
		{
			this.node = node;
		}

		@Override
		public Direction getDirection()
		{
			return this.direction;
		}

		public void setDirection(final Direction direction)
		{
			this.direction = direction;
		}
	};
}
