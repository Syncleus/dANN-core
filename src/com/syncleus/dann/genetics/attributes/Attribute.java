package com.syncleus.dann.genetics.attributes;

public interface Attribute<A, T>
{
	A getAttribute();
	Class<T> getAttributeValueType();
	boolean isAttributeValue(Object attributeValue);
}
