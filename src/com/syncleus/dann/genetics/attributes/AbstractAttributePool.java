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
package com.syncleus.dann.genetics.attributes;

import java.util.*;

public abstract class AbstractAttributePool<T> implements AttributePool<T>
{
	private final Set<AttributeChangeListener<T>> globalListeners = new HashSet<AttributeChangeListener<T>>();
	private final Map<Attribute<?, ? extends T>, Set<AttributeChangeListener<?>>> listeners = new HashMap<Attribute<?, ? extends T>, Set<AttributeChangeListener<?>>>();

	public final <C extends T> boolean listen(AttributeChangeListener<C> listener, Attribute<?, ? extends C> attribute)
	{
		if( attribute == null )
			throw new IllegalArgumentException("attribute can not be null");
		if(listener == null)
			throw new IllegalArgumentException("listener can not be null");

		//if its already a global listener forget about it
		if( this.globalListeners.contains(listener) )
			return false;

		Set<AttributeChangeListener<?>> attributesListeners = listeners.get(attribute);
		if( attributesListeners == null )
		{
			attributesListeners = new HashSet<AttributeChangeListener<?>>();
			listeners.put(attribute, attributesListeners);
		}
		return attributesListeners.add(listener);
	}

	public final boolean listenAll(AttributeChangeListener<T> listener)
	{
		if(listener == null)
			throw new IllegalArgumentException("listener can not be null");

		if( this.globalListeners.contains(listener) )
			return false;

		//since its global now lets make sure it isnt on a specific attribute
		this.removeListener(listener);

		return this.globalListeners.add(listener);
	}

	public final boolean removeListener(AttributeChangeListener<?> listener)
	{
		boolean removed = false;
		for(Map.Entry<Attribute<?, ? extends T>, Set<AttributeChangeListener<?>>> listenerEntry : this.listeners.entrySet())
		{
			assert (listenerEntry.getValue() != null) && (listenerEntry.getValue().size() > 0);

			if( listenerEntry.getValue().remove(listener) )
			{
				removed = true;
				if(listenerEntry.getValue().size() <= 0)
					listeners.remove(listenerEntry.getKey());
			}
		}

		if( this.globalListeners.remove(listener) )
			return true;

		return removed;
	}

	public final <C extends T> boolean removeListener(AttributeChangeListener<C> listener, Attribute<?, ? extends C> attribute)
	{
		//if this listener is global we cant remove it despite still listening so throw an exception
		if( this.globalListeners.contains(listener) )
			throw new IllegalArgumentException("listener is a global listener so we can not remove it for a single attribute");
		
		final Set<AttributeChangeListener<?>> attributeListeners = this.listeners.get(attribute);

		if( attributeListeners == null )
			return false;

		assert attributeListeners.size() > 0;

		if( attributeListeners.remove(listener) )
		{
			if(attributeListeners.size() <= 0)
				listeners.remove(attribute);
			return true;
		}

		return false;
	}

	protected final <C extends T> void notify(Attribute<?, C> attribute, C attributeValue)
	{
		Set<AttributeChangeListener<?>> attributeListeners = this.listeners.get(attribute);

		//lets notify everyone who is subscribed
		for( AttributeChangeListener listener : attributeListeners )
		{
			listener.attributeChanged(attribute, attributeValue);
		}

		//now lets hangle global listeners
		for( AttributeChangeListener<T> listener : this.globalListeners )
		{
			listener.attributeChanged(attribute, attributeValue);
		}
	}

	public abstract <I extends T> I getAttributeValue(Attribute<?, I> attribute);
	public abstract <C extends T> C setAttributeValue(Attribute<?, C> attribute, C value);
}
