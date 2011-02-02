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

	@Override
	public final A getAttribute()
	{
		return this.attributeId;
	}

	@Override
	public final boolean isAttributeValue(final Object attributeValue)
	{
		return this.attributeValueType.isInstance(attributeValue);
	}

	@Override
	public final Class<T> getAttributeValueType()
	{
		return this.attributeValueType;
	}

	@Override
	public final boolean equals(final Object object)
	{
		return this.attributeId.equals(object);
	}

	@Override
	public final int hashCode()
	{
		return this.attributeId.hashCode();
	}
}
