package com.syncleus.dann.attributes;

public class SimpleAttribute<A, T> implements Attribute<A, T>
{
	private final A attributeId;
	private final Class<T> attributeValueType;

	public SimpleAttribute(final A attributeId, final Class<T> attributeValueType)
	{
		this.attributeId = attributeId;
		this.attributeValueType = attributeValueType;
	}

	public final A getAttribute()
	{
		return this.attributeId;
	}

	public final boolean isAttributeValue(Object attributeValue)
	{
		return this.attributeValueType.isInstance(attributeValue);
	}

	
	public final Class<T> getAttributeValueType()
	{
		return this.attributeValueType;
	}

	public final boolean equals(Object object)
	{
		return this.attributeId.equals(object);
	}

	public final int hashCode()
	{
		return this.attributeId.hashCode();
	}
}
