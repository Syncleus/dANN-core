package com.syncleus.dann.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlSerializableAdapter<X, V extends XmlSerializable<X,?>> extends XmlAdapter<X, V>
{
    public final X marshal(V graph)
    {
        return graph.toXml();
    }

    public V unmarshal(X graphXml)
    {
        throw new IllegalStateException("You can not unmarshal XML through this adapter");
    }
}