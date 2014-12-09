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
package com.syncleus.dann.backprop;

import com.syncleus.grail.graph.unit.action.ActionTrigger;
import com.syncleus.grail.graph.unit.action.PrioritySerialTriggerEdge;
import com.syncleus.grail.graph.unit.action.AbstractPriorityTrigger;
import com.syncleus.grail.graph.unit.action.SerialPriorityTrigger;
import com.syncleus.grail.graph.unit.SignalMultiplyingEdge;
import com.syncleus.dann.ActivationNeuron;
import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.dann.backprop.AbstractBackpropNeuron;
import com.syncleus.dann.backprop.BackpropNeuron;
import com.syncleus.ferma.*;
import com.syncleus.grail.graph.*;
import java.util.*;

// TODO : What happens if nestedGraphName changes after there are already vertexes?
public abstract class SimpleNestedLayerVertex extends AbstractGrailVertexFrame implements NestedLayerVertex {

    private final static String PROPAGATE_ACTION_TRIGGER_ID_PREFIX = "propagateAction-";
    private final static String BACKPROPAGATE_ACTION_TRIGGER_ID_PREFIX = "backpropagateAction-";
    
    @Override
    protected void init() {
        super.init();
    }
    
    @Override
    public void propagate() {
        this.getPropagateActionTrigger().trigger();
    }
    
    @Override
    public void backpropagate() {
        this.getBackPropagateActionTrigger().trigger();
    }
    
    @Override
    public long getVertexCount() {
        return this.getNestedGraph().v().has(LAYER_KEY, this.getId()).count();
    }
    
    @Override
    public void setLearningRate(final double learningRate) {
        this.setProperty("learningRate", learningRate);
        
        for( BackpropNeuron neuron : this.getNestedNeurons() )
            neuron.setLearningRate(learningRate);
    }
    
    @Override
    public void setActivationFunctionClass(final Class<? extends ActivationFunction> activationFunctionClass) {
        this.setProperty("activationFunction", activationFunctionClass);
        
        for( BackpropNeuron neuron : this.getNestedNeurons() )
            neuron.setActivationFunctionClass(activationFunctionClass);
    }
    
    @Override
    public void makeBias() {
        for( BackpropNeuron neuron : this.getNestedNeurons() )
            neuron.setSignal(1.0);
    }

    @Override
    public void setVertexCount(final long newVertexCount, final Class<? extends ActivationNeuron> neuronType, final Class<? extends SignalMultiplyingEdge> edgeType) {
        if( newVertexCount < 0 )
            throw new IllegalArgumentException("newVertexCount can not be negative.");
        
        // TODO : this should probably be initalized sooner?
        final AbstractPriorityTrigger propagateTrigger = this.getNestedGraph().addFramedVertex(SerialPriorityTrigger.class);
        propagateTrigger.setProperty(LAYER_KEY, PROPAGATE_ACTION_TRIGGER_ID_PREFIX + this.getId());
        final AbstractPriorityTrigger backpropagateTrigger = this.getNestedGraph().addFramedVertex(SerialPriorityTrigger.class);
        backpropagateTrigger.setProperty(LAYER_KEY, BACKPROPAGATE_ACTION_TRIGGER_ID_PREFIX + this.getId());
        
        final long currentCount = this.getVertexCount();
        
        //the vertex count isnt changing so nothing to do
        if( currentCount == newVertexCount)
            return; //do nothing
        
        //we are removing vertices
        if( currentCount > newVertexCount ) {
            final long removeCount = currentCount - newVertexCount;
            
            //it doesnt matter which ones we remove
            final Iterator<? extends BackpropNeuron> allVertices = this.getNestedNeurons().iterator();
            for(int removalIndex = 0; removalIndex < removeCount; removalIndex++ )
                allVertices.next().remove();
        }
        //we are adding vertices
        else {
            final long addCount = newVertexCount - currentCount;
            final Set<ActivationNeuron> newNeurons = new HashSet<>();
            for(int addIndex = 0; addIndex < addCount; addIndex++) {
                final ActivationNeuron newNeuron = this.getNestedGraph().addFramedVertex(neuronType);
                newNeuron.setProperty(LAYER_KEY, this.getId());
                
                final PrioritySerialTriggerEdge propagateTriggerEdge = this.getGraph().addFramedEdge(propagateTrigger, newNeuron, "triggers", PrioritySerialTriggerEdge.class);
                propagateTriggerEdge.setTriggerAction("propagate");
                propagateTriggerEdge.setTriggerPriority(0);
                
                final PrioritySerialTriggerEdge backPropagateTriggerEdge = this.getGraph().addFramedEdge(backpropagateTrigger, newNeuron, "triggers", PrioritySerialTriggerEdge.class);
                backPropagateTriggerEdge.setTriggerAction("backpropagate");
                backPropagateTriggerEdge.setTriggerPriority(0);
                
                newNeurons.add(newNeuron);
            }
            
            //connect all the source layer verticies to our new neurons
            for(NestedLayerVertex sourceLayer : this.getSourceLayers(NestedLayerVertex.class)) {
                //make sure the connected layer has the same layer name
                if(!this.getNestedGraphName().equals(sourceLayer.getNestedGraphName()))
                    throw new IllegalStateException("One of the source layers uses a different nested graph");
                
                for( final ActivationNeuron sourceNeuron : this.getNestedGraph().v().has(LAYER_KEY, sourceLayer.getId()).frame(ActivationNeuron.class) )
                    for(final ActivationNeuron newNeuron : newNeurons )
                        this.getNestedGraph().addFramedEdge(sourceNeuron, newNeuron, "signals", edgeType);
            }
            
            //connect all the new neurons to the destination layers
            for(NestedLayerVertex targetLayer : this.getTargetLayers(NestedLayerVertex.class)) {
                //make sure the connected layer has the same layer name
                if(!this.getNestedGraphName().equals(targetLayer.getNestedGraphName()))
                    throw new IllegalStateException("One of the source layers uses a different nested graph");
                
                for( final ActivationNeuron targetNeuron : this.getNestedGraph().v().has(LAYER_KEY, targetLayer.getId()).frame(ActivationNeuron.class) )
                    for(final ActivationNeuron newNeuron : newNeurons )
                        this.getNestedGraph().addFramedEdge(newNeuron, targetNeuron, "signals", edgeType);
            }
        }
    }
    
    @Override
    public void removeVertexCount(final long removeVertexCount) {
        this.setVertexCount(this.getVertexCount() - removeVertexCount, null, null);
    }
    
    @Override
    public void addVertexCount(final long newVertexCount, final Class<? extends ActivationNeuron> neuronType, final Class<? extends SignalMultiplyingEdge> edgeType) {
        this.setVertexCount(this.getVertexCount() + newVertexCount, neuronType, edgeType);
    }

    @Override
    public GrailGraph getNestedGraph() {
        return this.getGraph().subgraph(this.getNestedGraphName());
    }
    
    @Override
    public Iterable<? extends BackpropNeuron> getNestedNeurons() {
        return this.getNestedGraph().v().has(LAYER_KEY, this.getId()).frame(AbstractBackpropNeuron.class);
    }
    
    private VertexTraversal<?,?,?> getNestedActionTrigger() {
        return this.getNestedGraph().v().has(LAYER_KEY, this.getId());
    }
    
    private ActionTrigger getPropagateActionTrigger(){
//        System.out.println("getting propagate property: " + (PROPAGATE_ACTION_TRIGGER_ID_PREFIX + this.getId()) + " on graph: " + this.getNestedGraph().toString());
        return this.getNestedGraph().getFramedVertices(LAYER_KEY, PROPAGATE_ACTION_TRIGGER_ID_PREFIX + this.getId(), SerialPriorityTrigger.class).iterator().next();
    }
    
    private ActionTrigger getBackPropagateActionTrigger(){
        return this.getNestedGraph().v().has(LAYER_KEY, BACKPROPAGATE_ACTION_TRIGGER_ID_PREFIX + this.getId()).next(SerialPriorityTrigger.class);
    }
}
