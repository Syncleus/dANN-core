package com.syncleus.dann.genetics.attributes;

public class StringAttribute<T> extends SimpleAttribute<String, T>
{
	public StringAttribute(final String attributeId, final Class<T> attributeValueType)
	{
		super(attributeId, attributeValueType);
	}
}
