/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.genetics.wavelets;


import java.util.TreeSet;

public class Nucleus implements Cloneable
{
    private TreeSet<Chromosome> chromosomes = new TreeSet<Chromosome>();
    private Cell cell = null;
    
    private Nucleus(Nucleus originalNucleus)
    {
        this.cell = originalNucleus.cell;
        for(Chromosome oldChromosome : originalNucleus.chromosomes)
            this.chromosomes.add(oldChromosome);
    }
    
    public Nucleus clone()
    {
        Nucleus copy = new Nucleus(this);
        return copy;
    }



    public Nucleus clone(Cell cell)
    {
        Nucleus copy = this.clone();
        copy.setCell(cell);
        return copy;
    }
    
    
    void preTick()
    {
        for(Chromosome chromosome : this.chromosomes)
            chromosome.preTick();
    }
    
    void tick()
    {
        for(Chromosome chromosome : this.chromosomes)
            chromosome.preTick();
    }



    private void setCell(Cell cell)
    {
        this.cell = cell;
        for(Chromosome chromosome : this.chromosomes )
            chromosome.setCell(cell);
    }
}
