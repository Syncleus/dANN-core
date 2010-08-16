package com.syncleus.dann.genetics.attributes;

public interface AttributePool<T>
{
	<C extends T> boolean listen(AttributeChangeListener<C> listener, Attribute<?, ? extends C> attribute);
	boolean listenAll(AttributeChangeListener<T> listener);
	boolean removeListener(AttributeChangeListener<?> listener);
	<C extends T> boolean removeListener(AttributeChangeListener<C> listener, Attribute<?, ? extends C> attribute);
	<C extends T> C getAttributeValue(Attribute<?, C> attribute);
	<C extends T> C setAttributeValue(Attribute<?, C> attribute, C value);
}
