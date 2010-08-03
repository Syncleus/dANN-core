package com.syncleus.dann.xml;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Namer<K>
{
    private final AtomicLong currentNodeName = new AtomicLong(0);
    private final WeakHashMap<K, Long> nodeNames = new WeakHashMap<K, Long>();

    public String getNameOrCreate(K node)
    {
        Long nodeName = nodeNames.get(node);
        if (nodeName == null)
        {
            nodeName = currentNodeName.incrementAndGet();
            nodeNames.put(node, nodeName);
        }
        return nodeName.toString();
    }

    public String getName(K node)
    {
        return nodeNames.get(node).toString();
    }

    public boolean hasName(K node)
    {
        return nodeNames.containsKey(node);
    }

    public void reset()
    {
        this.nodeNames.clear();
        this.currentNodeName.set(0);
    }
}
