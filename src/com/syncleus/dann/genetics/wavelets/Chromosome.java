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


import java.util.Random;
import java.util.TreeSet;

public class Chromosome implements Cloneable
{
    private TreeSet<Gene> genes = new TreeSet<Gene>();
    private Cell cell = null;
    private static Random random = new Random();
    
    private Chromosome(Chromosome originalCell)
    {
        this.cell = originalCell.cell;
        for(Gene oldGene : originalCell.genes)
            this.genes.add(oldGene);
    }
    
    public Chromosome clone()
    {
        Chromosome copy = new Chromosome(this);
        return copy;
    }
    
    
    void preTick()
    {
        for(Gene gene : this.genes)
            gene.preTick();
    }
    
    void tick()
    {
        for(Gene gene : this.genes)
            gene.tick();
    }



    public Chromosome clone(Cell cell)
    {
        Chromosome copy = this.clone();
        copy.setCell(cell);
        return copy;
    }



    void setCell(Cell cell)
    {
        this.cell = cell;
        for(Gene gene : this.genes)
            gene.setCell(cell);
    }
    
    Chromosome mutate()
    {
        return null;
    }
    
    Chromosome mutate(Gene newGene)
    {
        return null;
    }
    
    Chromosome mutate(Signal newSignal)
    {
        return null;
    }
    
    TreeSet<Gene> crossover(TreeSet<Gene> sourceGenes)
    {
        //pull off the genes to be replaced
        TreeSet<Gene> destGenes = new TreeSet<Gene>();
        for(int destSize = 0 ; destSize < this.genes.size(); destSize++)
        {
            if( this.genes.last().getGeneId().compareTo(sourceGenes.first().getGeneId()) < 0)
                break;
            
            destGenes.add(this.genes.last());
            this.genes.remove(this.genes.last());
        }
        
        //make sure the destination
        this.genes.addAll(sourceGenes);
        
        return destGenes;
    }
    
    TreeSet<Gene> getCrossoverSet()
    {
        TreeSet<Gene> crossoverSet = new TreeSet<Gene>();
        for(int crossoverSize = 0; crossoverSize < random.nextInt(this.genes.size()); crossoverSize++)
        {
            crossoverSet.add(this.genes.last());
        }
        return crossoverSet;
    }
}
