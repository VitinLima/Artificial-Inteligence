# -*- coding: utf-8 -*-
"""
Created on Sat Oct 15 14:42:08 2022

@author: 160047412
"""

SIGMOID = [0.0025, 0.0067, 0.018, 0.047, 0.12, 0.27, 0.5, 0.73, 0.88, 0.95, 0.98, 0.99, 1]
D_SIGMOID = [0.0025, 0.0066, 0.018, 0.045, 0.1, 0.2, 0.25, 0.2, 0.1, 0.045, 0.018, 0.0066, 0.0025]

def Sigmoid(z):
    global SIGMOID
    if z<-8:
        return SIGMOID[0]
    elif z>8:
        return SIGMOID[-1]
    else:
        return SIGMOID[int(z)]

def d_Sigmoid(z):
    global D_SIGMOID
    if z<-8:
        return D_SIGMOID[0]
    elif z>8:
        return D_SIGMOID[-1]
    else:
        return D_SIGMOID[int(z)]

class NeuralNetwork:
    def __init__(self):
        self.neuralGroups = []
        self.neurons = []

class NeuralGroup:
    def __init__(self, network):
        self.network = network
        self.neurons = []
        
        self.network.neuralGroups.append(self)
    
    def ungroup(self):
        for neuron in self.neurons:
            neuron.network = self.network
    
    def kill(self):
        for neuron in self.neurons:
            neuron.kill()
        del self

class Neuron:
    def __init__(self, network, bias=0):
        self.network = network
        self.activation = 0.5
        self.z_activation = 0
        self.bias = bias
        self.dentrites = []
        self.axons = []
        
        self.network.neurons.append(self)
    
    def requestActivation(self):
        return self.activation
    
    def updateActivation(self):
        self.z_activation = self.bias+sum([self.dentrites[i].requestActivation() for i in range(len(self.dentrites))])
        self.activation = Sigmoid(self.z_value)
    
    def setActivation(self, activation):
        self.activation = activation
    
    def learn(self, dc_dy):
        dc_dz = dc_dy*d_Sigmoid(self.z_value)
        self.bias -= dc_dz
        for i in range(len(self.dentrites)):
            self.dentrites[i].learn(dc_dz)
    
    def kill(self):
        for dentrite in self.dentrites:
            dentrite.kill()
        for axon in self.axons:
            axon.kill()
        del self

class Connection:
    def __init__(self, dentrite, axon, weight=0):
        if dentrite.__class__==Neuron and axon.__class__==Neuron:
            self.dentrite = dentrite
            self.axon = axon
            self.weight = weight
            self.dentrite.dentrites.append(self)
            self.axon.axons.append(self)
        elif dentrite.__class__==NeuralGroup and axon.__class__==Neuron:
            for neuron in dentrite.neurons:
                Connection(neuron, axon, weight)
        elif dentrite.__class__==Neuron and axon.__class__==NeuralGroup:
            for neuron in axon.neurons:
                Connection(dentrite, neuron, weight)
        elif dentrite.__class__==NeuralGroup and axon.__class__==NeuralGroup:
            for neuron1 in dentrite.neurons:
                for neuron2 in axon.neurons:
                    Connection(neuron1, neuron2, weight)
    
    def requestActivation(self):
        return self.axon.requestactivation()*self.weight
    
    def learn(self, dc_dz):
        self.weight -= dc_dz*self.axon.requestActivation()
        self.neuron.learn(dc_dz*self.weight)
    
    def kill(self):
        self.dentrite.dentrites.remove(self)
        self.axon.axons.remove(self)
        del self