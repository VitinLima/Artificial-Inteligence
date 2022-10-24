# -*- coding: utf-8 -*-
"""
Created on Sat Oct 15 19:25:02 2022

@author: 160047412
"""

import tkinter as tk
from math import sqrt
from NeuralNetwork import Sigmoid,NeuralNetwork,NeuralGroup,Neuron,Connection

def rgb_to_hex(color_rgb):
    color_red = hex(int(255*color_rgb[0])).removeprefix('0x').zfill(2)
    color_green = hex(int(255*color_rgb[1])).removeprefix('0x').zfill(2)
    color_blue = hex(int(255*color_rgb[2])).removeprefix('0x').zfill(2)
    return '#' + color_red + color_green + color_blue

class NeuralNetworkCanvas(tk.Canvas):
    def __init__(self, network=[], master=None, cnf={}, **kw):
        tk.Canvas.__init__(self, master, cnf, **kw)
        self.title = "Neural Network Canvas"
        self.name = 'Neural Network'
        
        if network==[]:
            self.network=NeuralNetwork()
        else:
            self.network=network
            
        self.neuralGroups=[]
        self.neurons=[]
        # for neuron in self.network.neurons:
        #     DrawableNeuron(self, neuron=neuron)
        # for neuralGroup in self.neuralGroups:
        #     DrawableNeuralGroup(self, neuralGroup)
        
        self.selected=[]
        self.optPanel = []
        
        self.bind('<ButtonPress>', self.buttonPressCallback, add='+')
        self.bind('<ButtonRelease>', self.buttonReleaseCallback, add='+')
        self.bind('<Motion>', self.buttonMotionCallback, add='+')
        
        self.layers = []
    
    def getRoot(self, child):
        if child.master==None:
            return child
        else:
            return self.getRoot(child.master)
    
    def buttonPressCallback(self, event):
        selected_id = list(self.find_overlapping(event.x, event.y, event.x+1, event.y+1))
        if len(selected_id)==0:
            self.selected = []
            return
        closest = self.find_closest(event.x, event.y)[0]
        for ng in self.neuralGroups:
            if ng.on_canvas.count(closest)>0:
                self.selected = ng
                self.selected.dx = event.x-ng.x
                self.selected.dy = event.y-ng.y
                self.selected.setLayer(1)
                return
            for n in ng.neurons:
                if n.on_canvas.count(closest)>0:
                    self.selected = n
                    self.selected.dx = event.x-n.x
                    self.selected.dy = event.y-n.y
                    self.selected.setLayer(1)
                    return
                for d in n.dentrites:
                    if d.on_canvas.count(closest)>0:
                        self.selected = d
                        self.selected.setLayer(1)
                        return
        for n in self.neurons:
            if n.on_canvas.count(closest)>0:
                self.selected = n
                self.selected.dx = event.x-n.x
                self.selected.dy = event.y-n.y
                self.selected.setLayer(1)
                return
            for d in n.dentrites:
                if d.on_canvas.count(closest)>0:
                    self.selected = d
                    self.selected.setLayer(1)
                    return
        # for g in self.neuralGroups:
        #     dx = event.x-g.x
        #     dy = event.y-g.y
        #     w = g.width
        #     h = g.height
        #     if dx>0 and dy>0 and dx<w and dy<h:
        #         self.selected = g
        #         self.selected.dx = dx
        #         self.selected.dy = dy
        #         return
        #     # for n in g.neurons:
        #     #     for c in n.dentrites:
        #     #         px1 = c.dentrite.x
        #     #         py1 = c.dentrite.y
        #     #         px2 = c.axon.x
        #     #         py2 = c.axon.y
        # for e in self.neurons:
        #     dx = event.x-e.x
        #     dy = event.y-e.y
        #     r = e.radius
        #     if dx*dx+dy*dy<r*r:
        #         self.selected = e
        #         self.selected.dx = dx
        #         self.selected.dy = dy
        #         return
    
    def buttonReleaseCallback(self, event):
        if self.selected==[]:
            pass
        else:
            self.selected.setLayer(0)
        self.selected=[]
    
    def buttonMotionCallback(self, event):
        if self.selected==[]:
            return
        elif self.selected.__class__ is not DrawableConnection:
            self.selected.coords(self, event.x, event.y)
    
    def updateNetwork(self):
        self.neurons = []
        self.neuralGroups = []
        for neuron in self.network.neurons:
            DrawableNeuron(self, neuron=neuron)
        for neuralGroup in self.neuralGroups:
            DrawableNeuralGroup(self, neuralGroup)
    
    # def updateNeuralGroups(self):
    #     self.neuralGroups = [DrawableNeuralGroup(self.network.neuralGroups[i]) for i in range(len(self.network.neuralGroups))]
    
    # def updateNeurons(self):
    #     self.neurons = [DrawableNeuron(self.network.neurons[i]) for i in range(len(self.network.neurons))]
    
    # def updateConnections(self):
    #     for neuralGroup in self.neuralGroups:
    #         for neuron in neuralGroup.neurons:
    #             for connection in neuron.neuron.dentrites:
    #                 axon = self.findDrawableNeuron(connection.axon)
    #                 neuron.dentrites.append(DrawableConnection(connection, neuron, axon))
    #     for neuron in self.neurons:
    #         for connection in neuron.neuron.dentrites:
    #             axon = self.findDrawableNeuron(connection.axon)
    #             neuron.dentrites.append(DrawableConnection(connection, neuron, axon))
    
    def newNetwork(self, network=[]):
        if network==[]:
            self.network = NeuralNetwork()
        else:
            self.network = network
        # self.neuralGroups = [DrawableNeuralGroup(self.network, network.neuralGroups[i]) for i in range(len(network.neuralGroups))]
        # self.neurons = [DrawableNeuron(self.network, network.neurons[i]) for i in range(len(network.neurons))]
    
    def findDrawableNeuron(self, neuron):
        for i in range(len(self.neurons)):
            if self.neurons[i].neuron==neuron:
                return self.neurons[i]
        return []
    
    def draw(self):
        layers = [[] for i in range(4)]
        # tags = [[] for i in range(4)]
        
        for neuralGroup in self.neuralGroups.copy():
            if neuralGroup.toDraw:
                neuralGroup.drawSelf(self)
            layers[neuralGroup.layer].append(neuralGroup.on_canvas)
                    
            for neuron in neuralGroup.neurons.copy():
                if neuron.toDraw:
                    neuron.drawSelf(self)
                layers[neuron.layer].append(neuron.on_canvas)
                        
                for dentrite in neuron.dentrites.copy():
                    if dentrite.toDraw:
                        dentrite.drawSelf(self)
                    layers[dentrite.layer].append(dentrite.on_canvas)
                            
                # for axon in neuron.axons.copy():
                #     if axon.toDraw:
                #         axon.drawSelf(self)
                #     layers[axon.layer].append(axon.on_canvas)
                            
        for neuron in self.neurons.copy():
            if neuron.toDraw:
                neuron.drawSelf(self)
            layers[neuron.layer].append(neuron.on_canvas)
                    
            for dentrite in neuron.dentrites.copy():
                if dentrite.toDraw:
                    dentrite.drawSelf(self)
                layers[dentrite.layer].append(dentrite.on_canvas)
                        
            # for axon in neuron.axons.copy():
            #     if axon.toDraw:
            #         axon.drawSelf(self)
            #     layers[axon.layer].append(axon.on_canvas)
                        
        
        order = self.find_all()
        layers = self.__union(self.__flatten(layers), order)
        # union = [order.count(L) for L in layers]
        # counter = [layers.count(L) for L in layers]
        # print(len(order))
        # print(len(layers))
        # print(union)
        # print(counter)
        # while True:
        #     pass
        
        for i in range(len(layers)-1):
            self.tag_raise(layers[i+1], layers[i])
            
    
    def __union(self, l1, l2):
        u = []
        for e in l1:
            if l2.count(e)>0:
                u += [e]
        return u
    
    def __flatten(self, l):
        if l.__class__==list or l.__class__==tuple:
            flattened = []
            for e in l:
                flattened += self.__flatten(e)
            return flattened
        else:
            return [l]

class DrawableNeuralGroup:
    def __init__(self, network, neuralGroup=[], x=0, y=0, width=30, height=50):
        self.network = network
        if neuralGroup==[]:
            self.neuralGroup = NeuralGroup(self.network.network)
        else:
            self.neuralGroup = neuralGroup
        
        self.state = 'alive'
        self.x=x
        self.y=y
        self.dx=0
        self.dy=0
        self.height=height
        self.width=width
        self.on_canvas = []
        self.name = 'NeuralGroup'
        self.tag = str(self).split()[-1]
        self.neurons = [DrawableNeuron(self, self.neuralGroup.neurons[i]) for i in range(len(self.neuralGroup.neurons))]
        
        self.network.neuralGroups.append(self)
        self.layer = 1
        self.toDraw = True
    
    def setLayer(self, layer):
        if layer==1:
            self.layer = 3
        elif layer==0:
            self.layer = 2
        for n in self.neurons:
            n.setLayer(layer)
    
    def ungroup(self):
        self.state = 'toKill'
        for neuron in self.neurons:
            neuron.network = self.network
            self.network.neurons.append(neuron)
            neuron.neuron.network = self.network.network
            self.network.network.neurons.append(neuron.neuron)
        self.neuralGroup.ungroup()
        self.toDraw = True
    
    def kill(self, childSupport=True):
        for neuron in self.neurons:
            neuron.kill(childSupport=False)
        if childSupport==True:
            self.neuralGroup.kill()
        self.state = 'toKill'
        self.toDraw = True
    
    def fitToContent(self):
        self.height = 35*len(self.neurons)
    
    def coords(self, canvas, x, y):
        self.x = x-self.dx
        self.y = y-self.dy
        
        points = [self.x,
                  self.y,
                  self.x+self.width,
                  self.y+self.height]
        canvas.coords(self.on_canvas[0], points)
        
        N = len(self.neurons)
        px = self.x+self.width/2
        for i in range(N):
            self.neurons[i].x=px
            py=self.y + (i+0.5)*self.height/(N)
            self.neurons[i].dx=0
            self.neurons[i].dy=0
            self.neurons[i].coords(canvas, px, py)
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.on_canvas:
            canvas.delete(d)
        self.on_canvas = []
        if self.state=='toKill':
            self.network.neuralGroups.remove(self)
            for neuron in self.neurons.copy():
                neuron.drawSelf(canvas)
            self.state='dead'
            return
        
        p1 = (self.x,self.y)
        p2 = (self.x+self.width,self.y+self.height)
        self.on_canvas.append(canvas.create_rectangle(p1, p2, fill='blue', tag=self.tag))
        
        N = len(self.neurons)
        px = self.x+self.width/2
        for i in range(N):
            self.neurons[i].x=px
            self.neurons[i].y=self.y + (i+0.5)*self.height/(N)
            self.neurons[i].dx=0
            self.neurons[i].dy=0
            self.neurons[i].drawSelf(canvas)
        
        # while canvas.findAbove(self.tag):
        #     tk.Canvas.

class DrawableNeuron:
    def __init__(self, network, neuron=[], x=0, y=0, radius=10):
        self.network = network
        if neuron==[]:
            if self.network.__class__==NeuralNetworkCanvas:
                self.neuron = Neuron(self.network.network)
            elif self.network.__class__==DrawableNeuralGroup:
                self.neuron = Neuron(self.network.neuralGroup)
        else:
            self.neuron = neuron
        self.dentrites = []
        self.axons = []
        
        self.state = 'alive'
        self.x = x
        self.y = y
        self.dx = 0
        self.dy = 0
        self.radius = radius
        self.on_canvas = []
        self.name = 'Neuron'
        self.tag = str(self).split()[-1]
        
        self.network.neurons.append(self)
        self.layer = 1
        self.toDraw = True
    
    def setLayer(self, layer):
        if layer==1:
            self.layer = 3
        elif layer==0:
            self.layer = 2
        for d in self.dentrites:
            d.setLayer(layer)

    def kill(self, childSupport=True):
        for dentrite in self.dentrites:
            dentrite.kill(childSupport=False)
        for axon in self.axons:
            axon.kill(childSupport=False)
        if childSupport==True:
            self.neuron.kill()
        self.state = 'toKill'
        self.toDraw = True
    
    def coords(self, canvas, x, y):
        self.x = x-self.dx
        self.y = y-self.dy
        
        for i in range(len(self.dentrites)):
            self.dentrites[i].coords(canvas)
        for i in range(len(self.axons)):
            self.axons[i].coords(canvas)
        
        points = [self.x-self.radius*1.2,
                  self.y-self.radius*1.2,
                  self.x+self.radius*1.2,
                  self.y+self.radius*1.2]
        canvas.coords(self.on_canvas[0], points)
        points = [self.x-self.radius,
                  self.y-self.radius,
                  self.x+self.radius,
                  self.y+self.radius]
        canvas.coords(self.on_canvas[1], points)
        points = [self.x-self.radius*0.7,
                  self.y-self.radius*0.7,
                  self.x+self.radius*0.7,
                  self.y+self.radius*0.7]
        canvas.coords(self.on_canvas[2], points)
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.on_canvas:
            canvas.delete(d)
        self.on_canvas = []
        if self.state=='toKill':
            self.network.neurons.remove(self)
            for dentrite in self.dentrites.copy():
                dentrite.drawSelf(canvas)
            for axon in self.axons.copy():
                axon.drawSelf(canvas)
            self.state='dead'
            del self
            return
        
        for i in range(len(self.dentrites)):
            self.dentrites[i].drawSelf(canvas)
        for i in range(len(self.axons)):
            self.axons[i].drawSelf(canvas)
        
        points = (
                (self.x-self.radius*1.2,self.y-self.radius*1.2),
                (self.x+self.radius*1.2,self.y+self.radius*1.2)
            )
        self.on_canvas.append(canvas.create_oval(points, fill='white', tag=self.tag))
        
        if self.neuron.bias > 0:
            color_hex = rgb_to_hex((0,self.neuron.bias,0))
        else:
            color_hex = rgb_to_hex((self.neuron.bias,0,0))
        points = (
                (self.x-self.radius,self.y-self.radius),
                (self.x+self.radius,self.y+self.radius)
            )
        self.on_canvas.append(canvas.create_oval(points, fill=color_hex, tag=self.tag))
    
        color_hex = rgb_to_hex((0,self.neuron.activation,0))
        points = (
                (self.x-self.radius*0.7,self.y-self.radius*0.7),
                (self.x+self.radius*0.7,self.y+self.radius*0.7)
            )
        self.on_canvas.append(canvas.create_oval(points, fill=color_hex, tag=self.tag))

class DrawableConnection:
    def __init__(self, dentrite, axon, connection=[], width=3):
        if dentrite.__class__==DrawableNeuron and axon.__class__==DrawableNeuron:
            pass
        elif dentrite.__class__==DrawableNeuralGroup and axon.__class__==DrawableNeuron:
            for neuron in dentrite.neurons:
                DrawableConnection(neuron, axon)
            return
        elif dentrite.__class__==DrawableNeuron and axon.__class__==DrawableNeuralGroup:
            for neuron in axon.neurons:
                DrawableConnection(dentrite, neuron)
            return
        elif dentrite.__class__==DrawableNeuralGroup and axon.__class__==DrawableNeuralGroup:
            for neuron1 in dentrite.neurons:
                for neuron2 in axon.neurons:
                    DrawableConnection(neuron1, neuron2)
            return
        else:
            return
        
        if connection==[]:
            self.connection = Connection(dentrite.neuron, axon.neuron)
        else:
            self.connection = connection
        self.dentrite = dentrite
        self.axon = axon
        self.dentrite.dentrites.append(self)
        self.axon.axons.append(self)
        
        self.state = 'alive'
        self.width=width
        self.on_canvas = []
        self.name = 'Connection'
        self.tag = str(self).split()[-1]
        self.layer = 0
        self.toDraw = True
    
    def setLayer(self, layer):
        if layer==1:
            self.layer = 1
        elif layer==0:
            self.layer = 0

    def kill(self, childSupport=True):
        if childSupport==True:
            self.connection.kill()
        self.state = 'toKill'
        self.toDraw = True
    
    def coords(self, canvas):
        x0 = self.dentrite.x
        y0 = self.dentrite.y
        x1 = self.axon.x
        y1 = self.axon.y
        w = self.width
        
        meanX = (x0+x1)/2
        meanY = (y0+y1)/2
        
        points = [x0-w,y0-w,
                  x0+w,y0+w,
                  meanX,meanY]
        canvas.coords(self.on_canvas[0], points)
        points = [x0-w,y0+w,
                  x0+w,y0-w,
                  meanX,meanY]
        canvas.coords(self.on_canvas[1], points)
        points = [x1-w,y1-w,
                  x1+w,y1+w,
                  meanX,meanY]
        canvas.coords(self.on_canvas[2], points)
        points = [x1-w,y1+w,
                  x1+w,y1-w,
                  meanX,meanY]
        canvas.coords(self.on_canvas[3], points)
        
        points = [x0-w/2,y0-w/2,
                  x0+w/2,y0+w/2,
                  meanX,meanY]
        canvas.coords(self.on_canvas[4], points)
        points = [x0-w/2,y0+w/2,
                  x0+w/2,y0-w/2,
                  meanX,meanY]
        canvas.coords(self.on_canvas[5], points)
        points = [x1-w/2,y1-w/2,
                  x1+w/2,y1+w/2,
                  meanX,meanY]
        canvas.coords(self.on_canvas[6], points)
        points = [x1-w/2,y1+w/2,
                  x1+w/2,y1-w/2,
                  meanX,meanY]
        canvas.coords(self.on_canvas[7], points)
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.on_canvas:
            canvas.delete(d)
        self.on_canvas = []
        if self.state=='toKill':
            self.dentrite.dentrites.remove(self)
            self.axon.axons.remove(self)
            self.state='dead'
            del self
            return
        
        x0 = self.dentrite.x
        y0 = self.dentrite.y
        x1 = self.axon.x
        y1 = self.axon.y
        w = self.width
        
        meanX = (x0+x1)/2
        meanY = (y0+y1)/2
        
        points = [x0-w,y0-w,
                  x0+w,y0+w,
                  meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill='blue', tag=self.tag))
        points = [x0-w,y0+w,
                  x0+w,y0-w,
                  meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill='blue', tag=self.tag))
        points = [x1-w,y1-w,
                  x1+w,y1+w,
                    meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill='red', tag=self.tag))
        points = [x1-w,y1+w,
                  x1+w,y1-w,
                    meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill='red', tag=self.tag))
        
        if self.connection.weight > 0:
            color_hex = rgb_to_hex((0,Sigmoid(self.connection.weight),0))
        else:
            color_hex = rgb_to_hex((Sigmoid(-self.connection.weight),0,0))
        points = [x0-w/2,y0-w/2,
                  x0+w/2,y0+w/2,
                    meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill=color_hex, tag=self.tag))
        points = [x0-w/2,y0+w/2,
                  x0+w/2,y0-w/2,
                    meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill=color_hex, tag=self.tag))
        points = [x1-w/2,y1-w/2,
                  x1+w/2,y1+w/2,
                    meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill=color_hex, tag=self.tag))
        points = [x1-w/2,y1+w/2,
                  x1+w/2,y1-w/2,
                  meanX,meanY]
        self.on_canvas.append(canvas.create_polygon(points, fill=color_hex, tag=self.tag))

class OptionsPanel(tk.Frame):
    def __init__(self, canvas=[], master=None, cnf={}, **kw):
        tk.Frame.__init__(self, master, cnf, **kw)
        self.canvas=canvas
        if self.canvas==[]:
            pass
        else:
            self.canvas.bind('<ButtonPress>', self.pressedButtonCallback, add='+')
        
        self.neuronOptions = self.NeuronOptions(master=self, width=100, bg='black')
        self.neuronOptions.pack_forget()
        self.neuronOptions.canvas = self.canvas
        self.neuralGroupOptions = self.NeuralGroupOptions(master=self, width=100, bg='black')
        self.neuralGroupOptions.pack_forget()
        self.neuralGroupOptions.canvas = self.canvas
        self.connectionOptions = self.ConnectionOptions(master=self, width=100, bg='black')
        self.connectionOptions.pack_forget()
        self.connectionOptions.canvas = self.canvas
        self.otherOptions = self.OtherOptions(master=self, width=100, bg='black')
        self.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        self.otherOptions.canvas = self.canvas
        self.placeHolder= tk.Frame(master=self, width=0, height=0)
        self.placeHolder.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        self.currentPanel = self.otherOptions
    
    def attachCanvas(self, canvas):
        if self.canvas==[]:
            pass
        else:
            self.canvas.unbind(self.pressedButtonCallback)
        if canvas==[]:
            pass
        else:
            self.canvas=canvas
            self.canvas.bind('<ButtonPress>', self.pressedButtonCallback, add='+')
            self.neuronOptions.canvas=self.canvas
            self.neuralGroupOptions.canvas = self.canvas
            self.connectionOptions.canvas = self.canvas
            self.otherOptions.canvas = self.canvas
    
    def setCurrentPanel(self, panel=[]):
        if panel==[]:
            self.neuronOptions.pack_forget()
            self.neuralGroupOptions.pack_forget()
            self.connectionOptions.pack_forget()
            self.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.currentPanel = self.otherOptions
        elif panel==self.NeuronOptions:
            self.neuronOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.currentPanel = self.neuronOptions
            self.neuralGroupOptions.pack_forget()
            self.connectionOptions.pack_forget()
            self.otherOptions.pack_forget()
        elif panel==self.NeuralGroupOptions:
            self.neuronOptions.pack_forget()
            self.neuralGroupOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.currentPanel = self.neuralGroupOptions
            self.connectionOptions.pack_forget()
            self.otherOptions.pack_forget()
        elif panel==self.ConnectionOptions:
            self.neuronOptions.pack_forget()
            self.neuralGroupOptions.pack_forget()
            self.connectionOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.currentPanel = self.connectionOptions
            self.otherOptions.pack_forget()
        self.currentPanel.select()
        
    def pressedButtonCallback(self, event):
        self.event = event
        self.selected = self.canvas.selected
        if self.currentPanel.waitingForInput==True:
            self.currentPanel.select()
        else:
            if self.selected.__class__==DrawableNeuralGroup:
                self.setCurrentPanel(panel=self.NeuralGroupOptions)
            elif self.selected.__class__==DrawableNeuron:
                self.setCurrentPanel(panel=self.NeuronOptions)
            elif self.selected.__class__==DrawableConnection:
                self.setCurrentPanel(panel=self.ConnectionOptions)
            else:
                self.setCurrentPanel()
    
    class NeuronOptions(tk.Frame):
        def __init__(self, master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Neuron', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='name', command=self.nameBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.nameTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=20,height=1)
            self.nameTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='connect with', command=self.connectWithBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='activation', command=self.activationBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.activationTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=5,height=1)
            self.activationTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='bias', command=self.biasBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.biasTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=5,height=1)
            self.biasTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
        
        def select(self):
            if self.waitingForInput==True:
                self.waitingForInput = False
                self.selectionFunction()
            elif self.master.selected is not []:
                self.nameTb.delete('1.0', 'end')
                self.nameTb.insert('end', self.master.selected.name)
                self.activationTb.delete('1.0', 'end')
                self.activationTb.insert('end', str(self.master.selected.neuron.activation))
                self.biasTb.delete('1.0', 'end')
                self.biasTb.insert('end', str(self.master.selected.neuron.bias))
        
        def selectionFunction(self):
            DrawableConnection(self.selection, self.master.selected)
            self.master.selected = self.selection
        
        def nameBtCb(self):
            self.master.selected.name = self.nameTb.get('1.0', 'end')
        
        def connectWithBtCb(self):
            self.waitingForInput = True
            self.selection = self.master.selected
        
        def activationBtCb(self):
            self.master.selected.neuron.activation = float(self.activationTb.get('1.0', 'end'))
            self.master.selected.toDraw = True
        
        def biasBtCb(self):
            self.master.selected.neuron.bias = float(self.biasTb.get('1.0', 'end'))
            self.master.selected.toDraw = True
        
        def killBtCb(self):
            self.master.selected.kill()
            self.master.setCurrentPanel()
    
    class NeuralGroupOptions(tk.Frame):
        def __init__(self, master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Neural Group', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='name', command=self.nameBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.nameTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=20,height=1)
            self.nameTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='connect with', command=self.connectWithBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='ungroup', command=self.ungroupBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
        
        def select(self):
            if self.waitingForInput==True:
                self.waitingForInput = False
                self.selectionFunction()
            elif self.master.selected is not []:
                self.nameTb.delete('1.0', 'end')
                self.nameTb.insert('end', self.master.selected.name)
        
        def selectionFunction(self):
            DrawableConnection(self.selection, self.master.selected)
            self.master.selected = self.selection
        
        def nameBtCb(self):
            self.master.selected.name = self.nameTb.get('1.0', 'end')
        
        def ungroupBtCb(self):
            self.master.selected.ungroup()
            self.master.setCurrentPanel()
        
        def connectWithBtCb(self):
            self.waitingForInput = True
            self.selection = self.master.selected
        
        def killBtCb(self):
            self.master.selected.kill()
            self.master.setCurrentPanel()
    
    class ConnectionOptions(tk.Frame):
        def __init__(self, master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Connection', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='name', command=self.nameBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.nameTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=20,height=1)
            self.nameTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self)
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=fr, text='weight', command=self.weightBtCb, fg='white', bg='black')
            bt.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.weightTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=5,height=1)
            self.weightTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
        
        def select(self):
            if self.waitingForInput==True:
                self.waitingForInput = False
                self.selectionFunction()
            elif self.master.selected is not []:
                self.nameTb.delete('1.0', 'end')
                self.nameTb.insert('end', self.master.selected.name)
                self.weightTb.delete('1.0', 'end')
                self.weightTb.insert('end', str(self.master.selected.connection.weight))
        
        def nameBtCb(self):
            self.master.selected.name = self.nameTb.get('1.0', 'end')
        
        def weightBtCb(self):
            self.master.selected.connection.weight = float(self.weightTb.get('1.0', 'end'))
            self.master.selected.toDraw = True
        
        def killBtCb(self):
            self.master.selected.kill()
            self.master.setCurrentPanel()
    
    class OtherOptions(tk.Frame):
        def __init__(self, master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.mainPanel = tk.Frame(master=self, bg='black')
            self.mainPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.titleLb = tk.Label(master=self.mainPanel, text='Options', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.mainPanel, text='birth', command=self.birthBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.mainPanel, text='group together', command=self.groupTogetherBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self.mainPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.birthPanel = tk.Frame(master=self, bg='black')
            self.birthPanel.pack_forget()
            lb = tk.Label(master=self.birthPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.birthPanel, text='Neuron', command=self.birth_neuronBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.birthPanel, text='Neural Group', command=self.birth_neuralGroupBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.birthPanel, text='Back', command=self.birth_backBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self.birthPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.birth_neuralGroupPanel = tk.Frame(master=self, bg='black')
            self.birth_neuralGroupPanel.pack_forget()
            lb = tk.Label(master=self.birth_neuralGroupPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            fr = tk.Frame(master=self.birth_neuralGroupPanel, bg='black')
            fr.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=fr, text='size:', fg='white', bg='black')
            lb.pack(side=tk.LEFT, fill=tk.Y, expand=True)
            self.birth_neuralGroup_sizeTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=5,height=1)
            self.birth_neuralGroup_sizeTb.insert('end', '3')
            self.birth_neuralGroup_sizeTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.birth_neuralGroupPanel, text='Back', command=self.birth_neuralGroup_backBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self.birth_neuralGroupPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.groupTogetherPanel = tk.Frame(master=self, bg='black')
            self.groupTogetherPanel.pack_forget()
            lb = tk.Label(master=self.birth_neuralGroupPanel, text='Group neurons', fg='white', bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.groupTogetherPanel, text='back', command=self.groupTogether_backBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
        
        def select(self):
            if self.waitingForInput==True:
                self.selectionFunction()
        
        def birthBtCb(self):
            self.mainPanel.pack_forget()
            self.birthPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.birth_neuralGroupPanel.pack_forget()
        
        def birth_neuronBtCb(self):
            self.selectionFunction = self.birth_neuronSelectionFunction
            self.waitingForInput = True
        
        def birth_neuronSelectionFunction(self):
            DrawableNeuron(self.canvas, x=self.master.event.x, y=self.master.event.y)
        
        def birth_neuralGroupBtCb(self):
            self.mainPanel.pack_forget()
            self.birth_neuralGroupPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.selectionFunction = self.birth_neuralGroupSelectionFunction
            self.waitingForInput = True
        
        def birth_neuralGroupSelectionFunction(self):
            N = int(self.birth_neuralGroup_sizeTb.get('1.0','end'))
            neuralGroup = DrawableNeuralGroup(self.canvas, x=self.master.event.x, y=self.master.event.y, height=30*N)
            for i in range(N):
                DrawableNeuron(neuralGroup)
        
        def birth_neuralGroup_backBtCb(self):
            self.mainPanel.pack_forget()
            self.birthPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.birth_neuralGroupPanel.pack_forget()
            self.waitingForInput = False
        
        def birth_backBtCb(self):
            self.birthPanel.pack_forget()
            self.mainPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.birth_neuralGroupPanel.pack_forget()
            self.waitingForInput = False
        
        def backBtCb(self):
            self.master.setCurrentPanel()
        
        def groupTogetherBtCb(self):
            self.mainPanel.pack_forget()
            self.groupTogetherPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.currentGrouping = DrawableNeuralGroup(self.master.canvas)
            self.selectionFunction = self.groupTogetherSelectionFunction
            self.waitingForInput=True
        
        def groupTogetherSelectionFunction(self):
            if self.master.selected.__class__ is not DrawableNeuron:
                return
            self.master.selected.network = self.currentGrouping
            self.master.selected.neuron.network = self.currentGrouping.neuralGroup.network
            self.currentGrouping.neurons.append(self.master.selected)
            self.currentGrouping.neuralGroup.neurons.append(self.master.selected.neuron)
            self.currentGrouping.height = 30*len(self.currentGrouping.neurons)
            self.currentGrouping.toDraw=True
        
        def groupTogether_backBtCb(self):
            self.groupTogetherPanel.pack_forget()
            self.mainPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.waitingForInput=False