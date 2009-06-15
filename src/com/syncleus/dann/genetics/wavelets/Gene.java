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


import com.syncleus.dann.util.UniqueId;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class Gene implements Cloneable
{
    private TreeSet<SignalProcessingWavelet> processors = new TreeSet<SignalProcessingWavelet>();
    private Cell cell = null;
    private UniqueId geneId = null;
    //private UniqueId allelId = null;
    private static Random random = new Random();
    private LocalSignal outputMultiplier;



    private Gene(Gene originalGene)
    {
        this.cell = originalGene.cell;
        for(SignalProcessingWavelet oldProcessor:originalGene.processors)
        {
            this.processors.add(oldProcessor.clone());
        }
    }



    public TreeSet<Signal> getSignals()
    {
        TreeSet<Signal> retVal = new TreeSet<Signal>();
        for(SignalProcessingWavelet processor:this.processors)
        {
            retVal.addAll(processor.getSignals());
        }
        retVal.add(this.outputMultiplier);

        return retVal;
    }



    private Signal[] getRandomizedSignals()
    {
        TreeSet<Signal> signalSet = this.getSignals();

        ArrayList<Signal> signalList = new ArrayList<Signal>(signalSet);

        Signal[] retVal = new Signal[signalSet.size()];
        for(int retIndex = 0;retIndex < retVal.length;retIndex++)
        {
            Signal currentSignal = signalList.get(random.nextInt(signalList.size()));
            signalList.remove(currentSignal);

            retVal[retIndex] = currentSignal;
        }

        return retVal;
    }



    private Signal getRandomizedSignal()
    {
        TreeSet<Signal> signalSet = this.getSignals();

        ArrayList<Signal> signalList = new ArrayList<Signal>(signalSet);

        return signalList.get(random.nextInt(signalList.size()));
    }



    private SignalProcessingWavelet[] getRandomizedProcessors()
    {
        TreeSet<SignalProcessingWavelet> processorSet = new TreeSet<SignalProcessingWavelet>(this.processors);

        ArrayList<SignalProcessingWavelet> processorList = new ArrayList<SignalProcessingWavelet>(processorSet);

        SignalProcessingWavelet[] retVal = new SignalProcessingWavelet[processorSet.size()];
        for(int retIndex = 0;retIndex < retVal.length;retIndex++)
        {
            SignalProcessingWavelet currentProcessor = processorList.get(random.nextInt(processorList.size()));
            processorList.remove(currentProcessor);

            retVal[retIndex] = currentProcessor;
        }

        return retVal;
    }



    private SignalProcessingWavelet getRandomizedProcessor()
    {
        TreeSet<SignalProcessingWavelet> processorSet = new TreeSet<SignalProcessingWavelet>(this.processors);

        ArrayList<SignalProcessingWavelet> processorList = new ArrayList<SignalProcessingWavelet>(processorSet);

        return processorList.get(random.nextInt(processorList.size()));
    }



    private Signal getRandomNewSignal()
    {
        Signal newSignal = null;

        if(random.nextBoolean())
        {
            newSignal = new GlobalSignal();
        }
        else
        {
            newSignal = new LocalSignal();
        }

        newSignal = this.cell.updateSignal(newSignal);

        return newSignal;
    }



    void preTick()
    {
        for(SignalProcessingWavelet processor:this.processors)
        {
            processor.preTick();
        }
    }



    void tick()
    {
        for(SignalProcessingWavelet processor:this.processors)
        {
            processor.tick();
        }
    }



    /**
     * Internally mutates and initiates internal mutation in all children.<br/>
     * <br/>
     * May change in any of the following ways:<br/>
     * <ul>
     * <li>a new wavelet is created using existing signals</li>
     * <li>a new wavelet is created using a new signal for the output</li>
     * <li>a wavelet is copied then internally mutate</li>
     * <li>a wavelet is deleted</li>
     * <li>a signal used in one wavelet is added to another wavelet</li>
     * <li>a wavelet internally mutates</li>
     * <!--li>new allelId is assigned</li-->
     * <li>new geneId is assigned</li>
     * </ul>
     * 
     * @return the new mutated gene
     */
    Gene mutate()
    {
        Gene copy = this.clone();
        //it always allows the children to attempt internal mutation
        copy.processors.clear();
        for(SignalProcessingWavelet processor:this.processors)
        {
            copy.processors.add(processor.mutate());
        }

        while(random.nextFloat() < 0.1)
        {

            //possibly create a new wavelet from existing signals
            if(random.nextFloat() < 0.1)
            {
                Signal[] randomSignals = copy.getRandomizedSignals();
                int randomSignalsIndex = 0;

                if(randomSignals.length >= 2)
                {
                    Signal outSignal = randomSignals[randomSignalsIndex++];
                    Signal inSignal = randomSignals[randomSignalsIndex++];

                    SignalProcessingWavelet newProcessor = new SignalProcessingWavelet(copy.cell, inSignal, outSignal);
                    //give the new processor a change to internally mutate
                    newProcessor.mutate();

                    copy.processors.add(newProcessor);
                }
            }

            //possibly create a new wavelet with a new output signal
            if(random.nextFloat() < 0.1)
            {
                if(copy.processors.size() > 0)
                {
                    Signal inSignal = copy.getRandomizedSignal();
                    Signal outSignal = copy.getRandomNewSignal();
                    SignalProcessingWavelet linkedWave = copy.getRandomizedProcessor();

                    SignalProcessingWavelet newProcessor = new SignalProcessingWavelet(copy.cell, inSignal, outSignal);
                    //give the new processor a change to internally mutate
                    newProcessor.mutate();

                    copy.processors.remove(linkedWave);
                    copy.processors.add(linkedWave.mutate(outSignal));


                    copy.processors.add(newProcessor);
                }
            }

            //possibly copy and mutate a wavelet
            if(random.nextFloat() < 0.1)
            {
                if(copy.processors.size() > 0)
                {
                    copy.processors.add(copy.getRandomizedProcessor().mutate());
                }
            }

            //possibly delete a wavelet
            if(random.nextFloat() < 0.1)
            {
                if(copy.processors.size() > 1)
                {
                    copy.processors.remove(copy.getRandomizedProcessor());
                }
            }

            //possibly use a signal from one wavelet in another
            if(random.nextFloat() < 0.1)
            {
                if(copy.processors.size() >= 2)
                {
                    SignalProcessingWavelet[] randomProcessors = copy.getRandomizedProcessors();
                    SignalProcessingWavelet source = randomProcessors[0];
                    SignalProcessingWavelet destination = randomProcessors[1];

                    TreeSet<Signal> sourceSignals = source.getSignals();
                    TreeSet<Signal> destSignals = destination.getSignals();

                    TreeSet<Signal> uniqueSourceSignals = new TreeSet<Signal>();
                    for(Signal sourceSignal:sourceSignals)
                    {
                        if(destSignals.contains(sourceSignal) == false)
                        {
                            uniqueSourceSignals.add(sourceSignal);
                        }
                    }

                    Signal[] uniqueArray = new Signal[uniqueSourceSignals.size()];
                    uniqueSourceSignals.toArray(uniqueArray);
                    Signal sourceCopy = uniqueArray[random.nextInt(uniqueArray.length)];

                    copy.processors.remove(destination);
                    copy.processors.add(destination.mutate(sourceCopy));
                }
            }
        }

        //possibly change the geneId
        if(random.nextFloat() < 0.1)
        {
            copy.geneId = new UniqueId(64);
        }

        return copy;
    }



    /**
     * Incorperates the new signal into the gene.<br/>
     * <!--br/>
     * <Will change in all of the following ways:<br/>
     * <ul>
     * <li>Obtains a new allelId</li>
     * </ul-->
     * <br/>
     * May change in any of the following ways:<br/>
     * <ul>
     * <li>It internally mutates</li>
     * <li>The signal is added to a wavelet</li>
     * <li>A new wavelet is created using the signal as an output</li>
     * </ul>
     * 
     * @param newSignal signal to incorperate
     * @return the new mutated gene
     */
    Gene mutate(Signal newSignal)
    {
        Gene copy = this.clone();

        //possibly create a new wavelet using the signal as an output
        if(random.nextFloat() < 0.05)
        {
            Signal randomSignal = copy.getRandomNewSignal();

            SignalProcessingWavelet newProcessor = new SignalProcessingWavelet(copy.cell, randomSignal, newSignal);
            //give the new processor a change to internally mutate
            newProcessor.mutate();

            copy.processors.add(newProcessor);
        }
        //possible add the signal to an existing wavelet
        else
        {
            SignalProcessingWavelet wavelet = copy.getRandomizedProcessor();
            
            copy.processors.remove(wavelet);
            copy.processors.add(wavelet.mutate(newSignal));
        }

        return copy.mutate();
    }



    /**
     * Incorperates the new wavelet into the gene<br/>
     * <!--br/>
     * Will change in all of the following ways:<br/>
     * <ul>
     * <li>Obtains a new allelId</li>
     * </ul-->
     * <br/>
     * May change in any of the following ways:<br/>
     * <ul>
     * <li>The new wavelet is added to the gene</li>
     * <li>internally mutates</li>
     * </ul>
     * 
     * @param wavelet the wavelet to incorperate
     * @return the new mutated gene
     */
    Gene mutate(SignalProcessingWavelet wavelet)
    {
        Gene copy = this.clone();
        copy.processors.add(wavelet);
        return copy.mutate();
    }



    /**
     * Uses the new geneId in place of its current one.
     * @param geneId new geneId to use
     * @return new mutated gene that uses the new geneId
     */
    Gene mutate(UniqueId geneId)
    {
        Gene copy = this.clone();
        copy.geneId = geneId;
        return copy;
    }



    public Gene clone()
    {
        Gene copy = new Gene(this);
        return copy;
    }



    public Gene clone(Cell cell)
    {
        Gene copy = this.clone();
        copy.setCell(cell);
        return copy;
    }



    void setCell(Cell cell)
    {
        this.cell = cell;
        for(SignalProcessingWavelet processor:this.processors)
        {
            processor.setCell(cell);
        }
    }



    public
    UniqueId getGeneId()
    {
        return geneId;
    }
}
