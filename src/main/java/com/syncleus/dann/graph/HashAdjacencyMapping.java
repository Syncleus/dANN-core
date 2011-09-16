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

public class HashAdjacencyMapping<LK,RK,V> extends AbstractSet<AdjacencyMapping.Adjacency<LK,RK>> implements AdjacencyMapping<LK,RK,V>
{
	private Integer size = 0;
	private final Map<LK, WeakRightKeySet> leftAdjacency = new HashMap<LK, WeakRightKeySet>();
	private final Map<RK, WeakLeftKeySet> rightAdjacency = new HashMap<RK, WeakLeftKeySet>();
	private final Map<LK,Map<RK,V>> valueMapping = new HashMap<LK, Map<RK,V>>();

	@Override
	public boolean contains(Object o)
	{
		if(!(o instanceof Adjacency))
			return false;
		Adjacency<?,?> adjacency = (Adjacency<?,?>) o;

		return this.contains((LK)adjacency.getLeftKey(),(RK)adjacency.getRightKey());
	}

	@Override
	public boolean contains(Object leftKey, Object rightKey)
	{
		if( leftKey == null && rightKey == null)
			throw new IllegalArgumentException("leftKey and rightKey can not both be null");

		if(leftKey == null)
		{
			WeakLeftKeySet adjacentLeft;
			if( (adjacentLeft = this.rightAdjacency.get(rightKey)) != null )
			{
				if( adjacentLeft.isEmpty() )
				{
					this.rightAdjacency.put((RK)rightKey,null);
					return true;
				}
				else
					return false;
			}
			else
				return true;
		}
		else if(rightKey == null)
		{
			WeakRightKeySet adjacentRight;
			if( (adjacentRight = this.leftAdjacency.get(leftKey)) != null )
			{
				if( adjacentRight.isEmpty() )
				{
					this.leftAdjacency.put((LK)leftKey,null);
					return true;
				}
				else
					return false;
			}
			else
				return true;
		}

		final Set<RK> adjacentRightKeys = leftAdjacency.get(leftKey);
		if( adjacentRightKeys == null )
			return false;
		else if( adjacentRightKeys.isEmpty() )
		{
			leftAdjacency.put((LK)leftKey,null);
			return false;
		}

		if( adjacentRightKeys.contains(rightKey) )
			return true;
		else
			return false;
	}

	@Override
	public Set<RK> getRightKeys()
	{
		return new Set<RK>()
		{
			@Override
			public int size()
			{
				return rightAdjacency.keySet().size();
			}

			@Override
			public boolean isEmpty()
			{
				return rightAdjacency.keySet().isEmpty();
			}

			@Override
			public boolean contains(Object o)
			{
				return rightAdjacency.keySet().contains(o);
			}

			@Override
			public Iterator<RK> iterator()
			{
				return new Iterator<RK>()
				{
					private final Iterator<RK> iterator = rightAdjacency.keySet().iterator();

					@Override
					public boolean hasNext()
					{
						return this.iterator.hasNext();
					}

					@Override
					public RK next()
					{
						return this.iterator.next();
					}

					@Override
					public void remove()
					{
						this.iterator.remove();
						size = null;
					}
				};
			}

			@Override
			public Object[] toArray()
			{
				return rightAdjacency.keySet().toArray();
			}

			@Override
			public <T> T[] toArray(T[] a)
			{
				return rightAdjacency.keySet().toArray(a);
			}

			@Override
			public boolean add(RK rk)
			{
				return rightAdjacency.keySet().add(rk);
			}

			@Override
			public boolean remove(Object o)
			{
				size = null;
				return rightAdjacency.keySet().remove(o);
			}

			@Override
			public boolean containsAll(Collection<?> c)
			{
				return rightAdjacency.keySet().containsAll(c);
			}

			@Override
			public boolean addAll(Collection<? extends RK> c)
			{
				return rightAdjacency.keySet().addAll(c);
			}

			@Override
			public boolean retainAll(Collection<?> c)
			{
				size = null;
				return rightAdjacency.keySet().retainAll(c);
			}

			@Override
			public boolean removeAll(Collection<?> c)
			{
				size = null;
				return rightAdjacency.keySet().removeAll(c);
			}

			@Override
			public void clear()
			{
				size = null;
				rightAdjacency.keySet().clear();
			}
		};
	}

	@Override
	public Set<LK> getLeftKeys()
	{
		return new Set<LK>()
		{
			@Override
			public int size()
			{
				return leftAdjacency.keySet().size();
			}

			@Override
			public boolean isEmpty()
			{
				return leftAdjacency.keySet().isEmpty();
			}

			@Override
			public boolean contains(Object o)
			{
				return leftAdjacency.keySet().contains(o);
			}

			@Override
			public Iterator<LK> iterator()
			{
				return new Iterator<LK>()
				{
					private final Iterator<LK> iterator = leftAdjacency.keySet().iterator();

					@Override
					public boolean hasNext()
					{
						return this.iterator.hasNext();
					}

					@Override
					public LK next()
					{
						return this.iterator.next();
					}

					@Override
					public void remove()
					{
						this.iterator.remove();
						size = null;
					}
				};
			}

			@Override
			public Object[] toArray()
			{
				return leftAdjacency.keySet().toArray();
			}

			@Override
			public <T> T[] toArray(T[] a)
			{
				return leftAdjacency.keySet().toArray(a);
			}

			@Override
			public boolean add(LK rk)
			{
				return leftAdjacency.keySet().add(rk);
			}

			@Override
			public boolean remove(Object o)
			{
				size = null;
				return leftAdjacency.keySet().remove(o);
			}

			@Override
			public boolean containsAll(Collection<?> c)
			{
				return leftAdjacency.keySet().containsAll(c);
			}

			@Override
			public boolean addAll(Collection<? extends LK> c)
			{
				return leftAdjacency.keySet().addAll(c);
			}

			@Override
			public boolean retainAll(Collection<?> c)
			{
				size = null;
				return leftAdjacency.keySet().retainAll(c);
			}

			@Override
			public boolean removeAll(Collection<?> c)
			{
				size = null;
				return leftAdjacency.keySet().removeAll(c);
			}

			@Override
			public void clear()
			{
				size = null;
				leftAdjacency.keySet().clear();
			}
		};
	}

	@Override
	public Set<LK> getLeftAdjacency(Object leftKey)
	{
		return Collections.unmodifiableSet(this.leftAdjacency.get(leftKey));
	}

	@Override
	public Set<RK> getRightAdjacency(Object rightKey)
	{
		return Collections.unmodifiableSet(this.rightAdjacency.get(rightKey));
	}

	@Override
	public boolean putLeftKey(LK leftKey)
	{
		if(leftKey == null)
			throw new IllegalArgumentException("leftKey can not be null");

		if( leftAdjacency.containsKey(leftKey))
			return false;

		this.leftAdjacency.put(leftKey,null);

		if(this.size != null)
			this.size++;
		return true;
	}

	@Override
	public boolean putRightKey(RK rightKey)
	{
		if(rightKey == null)
			throw new IllegalArgumentException("rightKey can not be null");

		if( rightAdjacency.containsKey(rightKey))
			return false;

		this.rightAdjacency.put(rightKey,null);
		if( this.size != null)
			this.size++;

		return true;
	}

	private boolean put(LK leftKey, RK rightKey, boolean eraseValue)
	{
		if(leftKey == null)
		{
			if(rightKey == null)
				throw new IllegalArgumentException("both rightKey and leftKey can not be null");
			return this.putRightKey(rightKey);
		}
		else if(rightKey == null)
			return this.putLeftKey(leftKey);

		final boolean wasLeftOrphaned;
		WeakRightKeySet adjacentRightKeys = leftAdjacency.get(leftKey);
		if(adjacentRightKeys == null)
		{
			if(leftAdjacency.containsKey(leftKey))
				wasLeftOrphaned = true;
			else
				wasLeftOrphaned = false;
			adjacentRightKeys = new WeakRightKeySet();
			leftAdjacency.put(leftKey, adjacentRightKeys);
		}
		else
			wasLeftOrphaned = false;

		if( !adjacentRightKeys.add(rightKey) )
			return false;

		//if this node already had existing adjacency then it would add 1 to the size, otherwise the size doesnt change
		//since your removing an entry due to the orphan that used to exist
		if( this.size != null && (adjacentRightKeys.size() > 1 || !wasLeftOrphaned) )
			this.size++;

		final boolean wasRightOrphaned;
		WeakLeftKeySet adjacentLeftKeys = rightAdjacency.get(rightKey);
		if(adjacentLeftKeys == null)
		{
			if(rightAdjacency.containsKey(rightKey))
				wasRightOrphaned = true;
			else
				wasRightOrphaned = false;
			adjacentLeftKeys = new WeakLeftKeySet();
			rightAdjacency.put(rightKey, adjacentLeftKeys );
		}
		else
			wasRightOrphaned = false;

		assert !adjacentLeftKeys.contains(leftKey);
		adjacentLeftKeys.add(leftKey);

		//adding this adjacency got removed an additional orphan from the entry list further reducing the size, an
		//adjacency added to 2 orphans will reduce the size by 2, adding to 1 orphan will not change the size, if
		//neither is an orphan the size will go up by one.
		if( this.size != null && adjacentLeftKeys.size() == 1 && !wasRightOrphaned)
		{
			this.size--;
			assert size >= 1;
		}

		if( eraseValue )
		{
			final Map<RK,V> mappings = this.valueMapping.get(leftKey);
			if( mappings == null )
				return true;
			mappings.remove(rightKey);
			if( mappings.isEmpty() )
				this.valueMapping.remove(leftKey);
		}

		return true;
	}

	@Override
	public boolean put(LK leftKey, RK rightKey)
	{
		return this.put(leftKey, rightKey, true);
	}

	@Override
	public boolean put(LK leftKey, RK rightKey, V value)
	{
		if( (leftKey == null || rightKey == null) && value != null)
			throw new IllegalArgumentException("Can not associated a value with an orphaned key");

		final KeyPairing keyPair = new KeyPairing(leftKey, rightKey);
		final boolean valueChanged = this.put(leftKey, rightKey, false);

		Map<RK,V> mappings = this.valueMapping.get(leftKey);
		if( mappings == null )
		{
			mappings = new WeakHashMap<RK, V>();
			this.valueMapping.put(leftKey,mappings);
		}

		if( mappings.put(rightKey, value) == value )
			return valueChanged;
		else
			return true;
	}

	@Override
	public V get(Object leftKey, Object rightKey)
	{
		if(leftKey == null || rightKey == null)
			throw new IllegalArgumentException("Can not associated a value with an orphaned key");

		final Map<RK,V> mappings = this.valueMapping.get(leftKey);
		if( mappings == null )
			return null;

		final V value = mappings.get(rightKey);
		if( !this.contains(leftKey,rightKey) )
		{
			mappings.remove(rightKey);
			return null;
		}
		return value;
	}

	@Override
	public boolean add(AdjacencyMapping.Adjacency<LK, RK> adjacency)
	{
		return put(adjacency.getLeftKey(), adjacency.getRightKey());
	}

	@Override
	public boolean removeLeftKey(Object leftKey)
	{
		if(leftKey == null)
			throw new IllegalArgumentException("leftKey can not be null");

		if( !leftAdjacency.containsKey(leftKey))
			return false;

		WeakRightKeySet adjacentRight = this.leftAdjacency.remove(leftKey);

		if( adjacentRight == null && this.size != null)
			this.size--;
		else
		{
			if( adjacentRight.isEmpty() )
			{
				if( this.size != null )
					this.size--;
				this.leftAdjacency.put((LK)leftKey,null);
			}
			assert adjacentRight.size() > 0;
			this.size = null;

			//cleanup our weak refrences
			adjacentRight = null;
		}
		assert this.size == null || this.size >= 0;

		return true;
	}

	@Override
	public boolean removeRightKey(Object rightKey)
	{
		if(rightKey == null)
			throw new IllegalArgumentException("rightKey can not be null");

		if( !rightAdjacency.containsKey(rightKey))
			return false;

		WeakLeftKeySet adjacentLeft = this.rightAdjacency.remove(rightKey);

		if( this.size != null && adjacentLeft == null )
			this.size--;
		else
		{
			if( adjacentLeft.isEmpty() )
			{
				if( this.size != null)
					this.size--;
				this.rightAdjacency.put((RK)rightKey, null);
			}
			assert adjacentLeft.size() > 0;
			this.size = null;

			//cleanup our weak refrences
			adjacentLeft = null;
		}
		assert this.size == null || this.size >= 0;

		return true;
	}

	@Override
	public boolean remove(Object leftKey, Object rightKey)
	{
		if(leftKey == null)
		{
			if(rightKey == null)
				throw new IllegalArgumentException("both rightKey and leftKey can not be null");
			return this.removeRightKey(rightKey);
		}
		else if(rightKey == null)
			return this.removeLeftKey(leftKey);

		WeakRightKeySet adjacentRightKeys = leftAdjacency.get(leftKey);
		if(adjacentRightKeys == null)
			return false;
		else if(adjacentRightKeys.isEmpty())
		{
			this.leftAdjacency.put((LK)leftKey,null);
			return false;
		}

		if( !adjacentRightKeys.remove(rightKey) )
			return false;

		//if the removal of the link creates an orphan the size wont change (the orphan adds an entry), however
		//if no orphan is created, since there is one less link the size goes down
		if(this.size != null)
			this.size--;

		WeakLeftKeySet adjacentLeftKeys = rightAdjacency.get(rightKey);
		assert (adjacentLeftKeys != null);

		adjacentLeftKeys.remove(leftKey);

		//if removal of the adjacency results in two orphanded keys the size of the entry set actually goes up by one,
		//one orphaned key and the size stays the same, no orphaned keys and the size goes down by one.
		if(adjacentLeftKeys.isEmpty())
		{
			if(this.size != null)
				this.size++;
			this.rightAdjacency.put((RK)rightKey, null);
		}
		assert this.size == null || this.size >= 2;

		return true;
	}

	@Override
	public boolean remove(Object o)
	{
		if(!(o instanceof Adjacency))
			return false;

		final Adjacency<?,?> adjacency = (Adjacency<?,?>) o;

		return this.remove(adjacency.getLeftKey(), adjacency.getRightKey());
	}

	@Override
	public void clear()
	{
		leftAdjacency.clear();
		rightAdjacency.clear();
		this.size = 0;
	}

	@Override
	public Iterator<Adjacency<LK, RK>> iterator()
	{
		return new AdjacencyIterator();
	}

	@Override
	public int size()
	{
		if(this.size != null)
			return this.size;

		System.gc();

		int newSize = 0;
		for(final Map.Entry<LK,WeakRightKeySet> entry : this.leftAdjacency.entrySet())
		{
			if( entry.getValue() != null )
			{
				if( entry.getValue().isEmpty() )
				{
					this.leftAdjacency.put(entry.getKey(),null);
					newSize++;
				}
				else
					newSize += entry.getValue().size();
			}
			else
				newSize++;
		}
		for(final Map.Entry<RK,WeakLeftKeySet> entry : this.rightAdjacency.entrySet())
		{
			if( entry.getValue() != null )
			{
				if( entry.getValue().isEmpty() )
				{
					this.rightAdjacency.put(entry.getKey(),null);
					newSize++;
				}
			}
			else
				newSize++;
		}

		this.size = newSize;
		return newSize;
	}

	private class AdjacencyIterator implements Iterator<Adjacency<LK,RK>>
	{
		private int remaining;
		private final Iterator<Map.Entry<LK,WeakRightKeySet>> leftIterator;
		private final Iterator<Map.Entry<RK,WeakLeftKeySet>> rightIterator;
		private LK currentLeftKey = null;
		private Iterator<RK> adjacentIterator = null;

		public AdjacencyIterator()
		{
			this.remaining = size();
			this.leftIterator = leftAdjacency.entrySet().iterator();
			this.rightIterator = rightAdjacency.entrySet().iterator();
		}

		@Override
		public boolean hasNext()
		{
			if(this.adjacentIterator != null && this.adjacentIterator.hasNext())
				return true;
			if(this.remaining > 0 && (this.leftIterator.hasNext() || this.rightIterator.hasNext()))
				return true;
			return false;
		}

		@Override
		public Adjacency<LK, RK> next()
		{
			if(this.remaining <= 0)
				throw new NoSuchElementException("no more elements!");

			if( (this.currentLeftKey != null) && (this.adjacentIterator != null) )
			{
				if(this.adjacentIterator.hasNext())
				{
					this.remaining--;
					return new SimpleAdjacency(currentLeftKey,adjacentIterator.next());
				}
				else
					this.adjacentIterator = null;
			}

			if(this.leftIterator.hasNext())
			{
				Map.Entry<LK,WeakRightKeySet> nextEntry = this.leftIterator.next();
				this.currentLeftKey = nextEntry.getKey();
				assert this.currentLeftKey != null;
				this.remaining--;
				if(nextEntry.getValue() != null && !nextEntry.getValue().isEmpty())
				{
					this.adjacentIterator = nextEntry.getValue().iterator();

					return new SimpleAdjacency(this.currentLeftKey, this.adjacentIterator.next());
				}
				else
					return new SimpleAdjacency(this.currentLeftKey,null);
			}
			else
				this.currentLeftKey = null;

			Map.Entry<RK,WeakLeftKeySet> nextEntry = this.rightIterator.next();
			while( nextEntry.getValue() != null && !nextEntry.getValue().isEmpty() )
			{
				nextEntry = this.rightIterator.next();
			}
			this.remaining--;
			return new SimpleAdjacency(null, nextEntry.getKey());
		}

		@Override
		public void remove()
		{
			if( this.currentLeftKey != null )
				this.leftIterator.remove();
			else
				this.rightIterator.remove();
		}
	}

	public class SimpleAdjacency implements Adjacency<LK,RK>
	{
		private final LK leftKey;
		private final RK rightKey;

		public SimpleAdjacency(LK leftKey, RK rightKey)
		{
			this.leftKey = leftKey;
			this.rightKey = rightKey;
		}

		@Override
		public LK getLeftKey()
		{
			return this.leftKey;
		}

		@Override
		public RK getRightKey()
		{
			return this.rightKey;
		}
	}

	private static class WeakHashSet<E> implements Set<E>
	{
		private final Map<E, Object> references = new WeakHashMap<E, Object>();
//		private final Set<WeakReference<E>> references = new HashSet<WeakReference<E>>();
//		private final ReferenceQueue<E> queue = new ReferenceQueue<E>();
/*
		private final class ElementReference
		{
			private final WeakReference<E> reference;

			public ElementReference(E element)
			{
				if( element != null )
					this.reference = new WeakReference<E>(element, queue);
				else
					this.reference = null;
			}

			public E get()
			{
				if( !isClearTag() )
					return this.reference.get();
				return
					  null;
			}

			private boolean isClearTag()
			{
				return reference == null;
			}

			@Override
			public int hashCode()
			{
				if( this.isClearTag() )
					return 0;

				final E element = reference.get();
				if( element == null )
					return 0;
				else
					return element.hashCode();
			}

			@Override
			public boolean equals(Object obj)
			{
				if(obj == null)
					return false;
				if( !(obj instanceof ElementReference) )
					return false;
				final ElementReference otherElementReference = (ElementReference) obj;

				final E otherElement = otherElementReference.get();
				if( otherElement == null )
					return false;

				final E element = reference.get();
				if( element == null )
					return super.;

				return element.equals(otherElement);
			}
		};
*/

		@Override
		public int size()
		{
			return this.references.size();
		}

		@Override
		public boolean isEmpty()
		{
			return this.references.isEmpty();
		}

		@Override
		public boolean contains(Object o)
		{
			return this.references.containsKey(o);
		}

		@Override
		public Iterator<E> iterator()
		{
			return this.references.keySet().iterator();
		}

		@Override
		public Object[] toArray()
		{
			return this.references.keySet().toArray();
		}

		@Override
		public <T> T[] toArray(T[] a)
		{
			return this.references.keySet().toArray(a);
		}

		@Override
		public boolean add(E ee)
		{
			if( this.references.put(ee, null) == null )
				return true;
			return false;
		}

		@Override
		public boolean remove(Object o)
		{
			return this.references.keySet().remove(0);
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			return this.references.keySet().containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends E> c)
		{
			boolean added = false;
			for( E elementToAdd : c )
				if( this.add(elementToAdd) )
					added = true;
			return added;
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			return this.references.keySet().retainAll(c);
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			return this.references.keySet().removeAll(c);
		}

		@Override
		public void clear()
		{
			this.references.clear();
		}
	};

	private static abstract class WeakGraphElementSet<E> extends WeakHashSet<E>
	{
		protected abstract void clean();

		@Override
		public int size()
		{
			this.clean();
			return super.size();	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean isEmpty()
		{
			this.clean();
			return super.isEmpty();	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean contains(Object o)
		{
			this.clean();
			return super.contains(o);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public Iterator<E> iterator()
		{
			this.clean();
			return super.iterator();	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public Object[] toArray()
		{
			this.clean();
			return super.toArray();	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public <T> T[] toArray(T[] a)
		{
			this.clean();
			return super.toArray(a);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean add(E n)
		{
			assert n != null;
			this.clean();
			return super.add(n);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean remove(Object o)
		{
			this.clean();
			return super.remove(o);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			this.clean();
			return super.containsAll(c);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean addAll(Collection<? extends E> c)
		{
			assert (c != null ? !c.contains(null) : true);
			this.clean();
			return super.addAll(c);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			this.clean();
			return super.retainAll(c);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			this.clean();
			return super.removeAll(c);	//To change body of overridden methods use File | Settings | File Templates.
		}
	};

	private final class WeakLeftKeySet extends WeakGraphElementSet<LK>
	{
		protected void clean()
		{
			if(size != null)
				return;
			this.retainAll(leftAdjacency.keySet());
		}
	};

	private final class WeakRightKeySet extends WeakGraphElementSet<RK>
	{
		protected void clean()
		{
			if(size != null)
				return;
			this.retainAll(rightAdjacency.keySet());
		}
	};

	private final class KeyPairing implements AdjacencyMapping.Adjacency<LK,RK>
	{
		private final LK leftKey;
		private final RK rightKey;

		public KeyPairing(LK leftKey, RK rightKey)
		{
			this.leftKey = leftKey;
			this.rightKey = rightKey;
		}

		public LK getLeftKey()
		{
			return this.leftKey;
		}

		public RK getRightKey()
		{
			return this.rightKey;
		}

		@Override
		public int hashCode()
		{
			return (leftKey == null ? 0 : leftKey.hashCode()) + (rightKey == null ? 0 : rightKey.hashCode());
		}

		@Override
		public boolean equals(Object obj)
		{
			if( obj == null )
				return false;
			else if( !(obj instanceof KeyPairing) )
				return false;

			final KeyPairing keyPair = (KeyPairing) obj;
			if(
				  (
				  	(this.leftKey != null && (this.leftKey.equals(keyPair.getLeftKey()))) ||
					(this.leftKey == null && keyPair.getLeftKey() == null)
				  ) &&
				  (
				  	(this.rightKey != null && (this.rightKey.equals(keyPair.getRightKey()))) ||
					(this.rightKey == null && keyPair.getRightKey() == null)
				  )
				)
				return true;

			return false;
		}
	};
}
