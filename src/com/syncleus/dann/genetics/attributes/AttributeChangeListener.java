package com.syncleus.dann.genetics.attributes;

public interface AttributeChangeListener<T>
{
	<V extends T> void attributeChanged(Attribute<?, V> attribute, V attributeValue);
}
