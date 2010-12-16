package com.syncleus.dann.attributes;

public class IntegerAttribute<T> extends SimpleAttribute<Integer, T>
{
	public IntegerAttribute(final Integer attributeId, final Class<T> attributeValueType)
	{
		super(attributeId, attributeValueType);
	}
}
