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
        return SIGMOID(int(z))

def d_Sigmoid(z):
    global D_SIGMOID
    if z<-8:
        return D_SIGMOID[0]
    elif z>8:
        return D_SIGMOID[-1]
    else:
        return D_SIGMOID(int(z))

class NeuralNetwork:
    def __init__(self):
        self.neuralGroups = []
        self.neurons = []
    
    def addNeuron(self, neuron):
        self.neurons.append(neuron)

class NeuralGroup:
    def __init__(self, neurons=[]):
        self.neurons = neurons
    
    def addNeuron(self, neuron):
        self.neurons.append(neuron)
    
    def kill(self):
        pass

class Neuron:
    def __init__(self, bias=0, connections=[]):
        self.excitement = 0.5
        self.z_value = 0
        self.bias = bias
        self.connections = connections
    
    def requestExcitement(self):
        return self.excitement
    
    def updateExcitement(self):
        self.z_value = self.bias+sum([self.connections[i].requestExcitement() for i in range(len(self.connections))])
        self.excitement = Sigmoid(self.z_value)
    
    def addConnection(self, connection):
        self.connections.append(connection)
    
    def setExcitement(self, excitement):
        self.excitement = excitement
    
    def learn(self, dc_dy):
        dc_dz = dc_dy*d_Sigmoid(self.z_value)
        self.bias -= dc_dz
        for i in range(len(self.connections)):
            self.connection[i].learn(dc_dz)
    
    def kill(self):
        pass

class Connection:
    def __init__(self, owner, connected, weight=0):
        self.owner = owner
        self.connected = connected
        self.weight = weight
    
    def requestExcitement(self):
        return self.connected.requestExcitement()*self.weight
    
    def learn(self, dc_dz):
        self.weight -= dc_dz*self.neuron.requestExcitement()
        self.neuron.learn(dc_dz*self.weight)
    
    def kill(self):
        pass