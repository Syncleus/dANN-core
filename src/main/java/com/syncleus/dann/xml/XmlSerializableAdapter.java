package com.syncleus.dann.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlSerializableAdapter<X, V extends XmlSerializable<X, ?>> extends XmlAdapter<X, V>
{
	@Override
	public final X marshal(final V graph)
	{
		return graph.toXml();
	}

	@Override
	public V unmarshal(final X graphXml)
	{
		throw new IllegalStateException("You can not unmarshal XML through this adapter");
	}
}
