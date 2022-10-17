# -*- coding: utf-8 -*-
"""
Created on Sat Oct 15 19:25:02 2022

@author: 160047412
"""

import tkinter as tk
from NeuralNetwork import NeuralNetwork,NeuralGroup,Neuron,Connection

class NeuralNetworkCanvas(tk.Canvas):
    def __init__(self, network=[], master=None, cnf={}, **kw):
        tk.Canvas.__init__(self, master, cnf, **kw)
        self.title = "Neural Network Canvas"
        
        self.neuralGroups=[]
        self.neurons=[]
        
        if network==[]:
            self.network=NeuralNetwork()
        else:
            self.network=network
            self.updateNeuralGroups()
            self.updateNeurons()
            self.updateConnections()
        
        self.selected=[]
        self.optPanel = []
        
        self.bind('<ButtonPress>', self.buttonPressCallback, add='+')
        self.bind('<ButtonRelease>', self.buttonReleaseCallback, add='+')
        self.bind('<Motion>', self.buttonMotionCallback, add='+')
    
    def getRoot(self, child):
        if child.master==None:
            return child
        else:
            return self.getRoot(child.master)
    
    def buttonPressCallback(self, event):
        self.selected = []
        for g in self.neuralGroups:
            dx = event.x-g.x
            dy = event.y-g.y
            w = g.width
            h = g.height
            if dx>0 and dy>0 and dx<w and dy<h:
                self.selected = g
                self.selected.dx = dx
                self.selected.dy = dy
                return
            # for n in g.neurons:
            #     for c in n.connections:
            #         px1 = c.owner.x
            #         py1 = c.owner.y
            #         px2 = c.connected.x
            #         py2 = c.connected.y
        for e in self.neurons:
            dx = event.x-e.x
            dy = event.y-e.y
            r = e.radius
            if dx*dx+dy*dy<r*r:
                self.selected = e
                self.selected.dx = dx
                self.selected.dy = dy
                return
    
    def buttonReleaseCallback(self, event):
        self.selected=[]
    
    def buttonMotionCallback(self, event):
        if self.selected==[]:
            return
        self.selected.coords(self, event.x, event.y)
    
    def addNeuron(self, neuron):
        self.neurons.append(neuron)
        self.network.addNeuron(neuron.neuron)
        self.draw()
    
    def addNeuralGroup(self, neuralGroup):
        self.neuralGroups.append(neuralGroup)
        self.network.addNeuron(neuralGroup.neuralGroup)
        self.draw()
    
    def updateNeuralGroups(self):
        self.neuralGroups = [DrawableNeuralGroup(self.network.neuralGroups[i]) for i in range(len(self.network.neuralGroups))]
    
    def updateNeurons(self):
        self.neurons = [DrawableNeuron(self.network.neurons[i]) for i in range(len(self.network.neurons))]
    
    def updateConnections(self):
        for neuralGroup in self.neuralGroups:
            for neuron in neuralGroup.neurons:
                for connection in neuron.neuron.connections:
                    connected = self.findDrawableNeuron(connection.connected)
                    neuron.connections.append(DrawableConnection(connection, neuron, connected))
        for neuron in self.neurons:
            for connection in neuron.neuron.connections:
                connected = self.findDrawableNeuron(connection.connected)
                neuron.connections.append(DrawableConnection(connection, neuron, connected))
    
    def newNetwork(self, network=[]):
        self.network = network
        self.neuralGroups = [DrawableNeuralGroup(network.neuralGroups[i]) for i in range(len(network.neuralGroups))]
        self.neurons = [DrawableNeuron(network.neurons[i]) for i in range(len(network.neurons))]
    
    def findDrawableNeuron(self, neuron):
        for i in range(len(self.neurons)):
            if self.neurons[i].neuron==neuron:
                return self.neurons[i]
        return []
    
    def draw(self):
        self.delete('all')
        for neuralGroup in self.neuralGroups:
            neuralGroup.drawSelf(self)
        for neuron in self.neurons:
            neuron.drawSelf(self)

class DrawableNeuralGroup:
    def __init__(self, neuralGroup=[], x=0, y=0, width=30, height=50):
        if neuralGroup==[]:
            self.neuralGroup = NeuralGroup()
        else:
            self.neuralGroup = neuralGroup
        
        self.x=x
        self.y=y
        self.dx=0
        self.dy=0
        self.height=height
        self.width=width
        self.on_canvas = []
        self.name = 'Neural Group'
        self.neurons = [DrawableNeuron(self.neuralGroup.neurons[i]) for i in range(len(self.neuralGroup.neurons))]
    
    def addNeuron(self, neuron):
        self.neurons.append(neuron)
        self.neuralGroup.addNeuron(neuron.neuron)
        self.height = 30*len(self.neurons)
    
    def addConnection(self, n):
        if n.__class__==DrawableNeuralGroup:
            for neuron in self.neurons:
                for connected in n.neurons:
                    neuron.addConnection(DrawableConnection(neuron, connected))
        elif n.__class__==DrawableNeuron:
            for neuron in self.neurons:
                neuron.addConnection(DrawableConnection(neuron, n))
    
    def kill(self):
        pass
    
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
            self.neurons[i].coords(canvas, px, py)
    
    def drawSelf(self, canvas):
        canvas.delete(self.on_canvas)
        self.on_canvas = []
        
        p1 = (self.x,self.y)
        p2 = (self.x+self.width,self.y+self.height)
        self.on_canvas.append(canvas.create_rectangle(p1, p2, fill='blue', tag=self.name))
        
        N = len(self.neurons)
        px = self.x+self.width/2
        for i in range(N):
            self.neurons[i].x=px
            self.neurons[i].y=self.y + (i+0.5)*self.height/(N)
            self.neurons[i].drawSelf(canvas)

class DrawableNeuron:
    def __init__(self, neuron=[], x=0, y=0, radius=10):
        if neuron==[]:
            self.neuron = Neuron()
        else:
            self.neuron = neuron
        self.connections = []
        self.connected_by = []
        
        self.x = x
        self.y = y
        self.dx = 0
        self.dy = 0
        self.radius = radius
        self.excitement_color = 'gray'
        self.bias_color='blue'
        self.on_canvas = []
        self.name = 'Neuron'
    
    def addConnection(self, connection):
        self.connections.append(connection)
        self.neuron.addConnection(connection.connection)

    def kill(self):
        pass
    
    def coords(self, canvas, x, y):
        self.x = x-self.dx
        self.y = y-self.dy
        
        for i in range(len(self.connections)):
            self.connections[i].coords(canvas)
        for i in range(len(self.connected_by)):
            self.connected_by[i].coords(canvas)
        
        points = [self.x-self.radius,
                  self.y-self.radius,
                  self.x+self.radius,
                  self.y+self.radius]
        canvas.coords(self.on_canvas[0], points)
        points = [self.x-self.radius*0.7,
                  self.y-self.radius*0.7,
                  self.x+self.radius*0.7,
                  self.y+self.radius*0.7]
        canvas.coords(self.on_canvas[1], points)
    
    def drawSelf(self, canvas):
        canvas.delete(self.on_canvas)
        self.on_canvas = []
        for i in range(len(self.connections)):
            self.connections[i].drawSelf(canvas)
        
        points = (
                (self.x-self.radius,self.y-self.radius),
                (self.x+self.radius,self.y+self.radius)
            )
        self.on_canvas.append(canvas.create_oval(points, fill=self.bias_color, tag=self.name))
        points = (
                (self.x-self.radius*0.7,self.y-self.radius*0.7),
                (self.x+self.radius*0.7,self.y+self.radius*0.7)
            )
        self.on_canvas.append(canvas.create_oval(points, fill=self.excitement_color, tag=self.name))

class DrawableConnection:
    def __init__(self, owner, connected, connection=[], width=3):
        if connection==[]:
            self.connection = Connection(owner, connected)
        else:
            self.connection = connection
        self.owner = owner
        self.connected = connected
        connected.connected_by.append(self)
        
        self.width=width
        self.weight_color='gray'
        self.on_canvas = []
        self.name = 'Connection'

    def kill(self):
        pass
    
    def coords(self, canvas):
        x0 = self.owner.x
        y0 = self.owner.y
        x1 = self.connected.x
        y1 = self.connected.y
        w = self.width
        points = [x0-w,y0-w,
                  x0+w,y0+w,
                  x1-w,y1-w,
                  x1+w,y1+w]
        canvas.coords(self.on_canvas[0], points)
        points = [x0-w,y0+w,
                  x0+w,y0-w,
                  x1-w,y1+w,
                  x1+w,y1-w]
        canvas.coords(self.on_canvas[1], points)
    
    def drawSelf(self, canvas):
        canvas.delete(self.on_canvas)
        self.on_canvas = []
        x0 = self.owner.x
        y0 = self.owner.y
        x1 = self.connected.x
        y1 = self.connected.y
        w = self.width
        points = (
                (x0-w,y0-w),
                (x0+w,y0+w),
                (x1-w,y1-w),
                (x1+w,y1+w)
            )
        self.on_canvas.append(canvas.create_polygon(points, fill=self.weight_color, tag=self.name))
        points = (
                (x0-w,y0+w),
                (x0+w,y0-w),
                (x1-w,y1+w),
                (x1+w,y1-w)
            )
        self.on_canvas.append(canvas.create_polygon(points, fill=self.weight_color, tag=self.name))
    
    def getRoot(self):
        if self.master==None:
            return None
        return self.master.getRoot()

class OptionsPanel(tk.Frame):
    def __init__(self, canvas=[], master=None, cnf={}, **kw):
        tk.Frame.__init__(self, master, cnf, **kw)
        self.canvas=canvas
        if self.canvas==[]:
            pass
        else:
            self.canvas.bind('<ButtonPress>', self.pressedButtonCallback, add='+')
        
        self.neuronOptions = self.NeuronOptions(master=self, bg='black')
        self.neuronOptions.pack_forget()
        self.neuronOptions.canvas = self.canvas
        self.neuralGroupOptions = self.NeuralGroupOptions(master=self, bg='black')
        self.neuralGroupOptions.pack_forget()
        self.neuralGroupOptions.canvas = self.canvas
        self.connectionOptions = self.ConnectionOptions(master=self, bg='black')
        self.connectionOptions.pack_forget()
        self.connectionOptions.canvas = self.canvas
        self.otherOptions = self.OtherOptions(master=self, bg='black')
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
        self.canvas=canvas
        if self.canvas==[]:
            pass
        else:
            self.canvas.bind('<ButtonPress>', self.pressedButtonCallback, add='+')
            self.neuronOptions.canvas=self.canvas
            self.neuralGroupOptions.canvas = self.canvas
            self.connectionOptions.canvas = self.canvas
            self.otherOptions.canvas = self.canvas
        
    def pressedButtonCallback(self, event):
        if self.currentPanel.waitingForInput==True:
            self.currentPanel.select(self.canvas.selected, event)
        else:
            if self.canvas.selected.__class__==DrawableNeuron:
                self.neuronOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
                self.neuralGroupOptions.pack_forget()
                self.connectionOptions.pack_forget()
                self.otherOptions.pack_forget()
                self.neuronOptions.select(self.canvas.selected, event)
            elif self.canvas.selected.__class__==DrawableNeuralGroup:
                self.neuronOptions.pack_forget()
                self.neuralGroupOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
                self.connectionOptions.pack_forget()
                self.otherOptions.pack_forget()
                self.neuralGroupOptions.select(self.canvas.selected, event)
            elif self.canvas.selected.__class__==DrawableConnection:
                self.neuronOptions.pack_forget()
                self.neuralGroupOptions.pack_forget()
                self.connectionOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
                self.otherOptions.pack_forget()
                self.connectionOptions.select(self.canvas.selected, event)
            else:
                self.neuronOptions.pack_forget()
                self.neuralGroupOptions.pack_forget()
                self.connectionOptions.pack_forget()
                self.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
                self.otherOptions.select(self.canvas.selected, event)
    
    class NeuronOptions(tk.Frame):
        def __init__(self, neuron=[], master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Neuron', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='name', command=self.nameBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='connect with', command=self.connectWithBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='activation', command=self.activationBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='bias', command=self.biasBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
            self.selected=[]
        
        def select(self, selection, event):
            pass
        
        def nameBtCb(self):
            pass
        
        def connectWithBtCb(self):
            pass
        
        def activationBtCb(self):
            pass
        
        def biasBtCb(self):
            pass
        
        def killBtCb(self):
            for s in self.selected:
                s.kill()
            self.master.neuronOptions.pack_forget()
            self.master.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
    
    class NeuralGroupOptions(tk.Frame):
        def __init__(self, neuronGroup=[], master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Neural Group', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='name', command=self.nameBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='connect with', command=self.connectWithBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='ungroup', command=self.ungroupBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
            self.selected=[]
        
        def select(self, selection, event):
            pass
        
        def nameBtCb(self):
            pass
        
        def ungroupBtCb(self):
            pass
        
        def connectWithBtCb(self):
            pass
        
        def killBtCb(self):
            for s in self.selected:
                s.kill()
            self.master.neuralGroupOptions.pack_forget()
            self.master.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
    
    class ConnectionOptions(tk.Frame):
        def __init__(self, master=None, cnf={}, **kw):
            tk.Frame.__init__(self, master, cnf, **kw)
            
            self.titleLb = tk.Label(master=self, text='Connection', fg='white', bg='black')
            self.titleLb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='weight', command=self.weightBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self, text='kill', command=self.killBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
            self.selected=[]
        
        def select(self, selection, event):
            pass
        
        def weightBtCb(self):
            pass
        
        def killBtCb(self):
            for s in self.selected:
                s.kill()
            self.master.connectionOptions.pack_forget()
            self.master.otherOptions.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
    
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
            lb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            self.birth_neuralGroup_sizeTb = tk.Text(master=fr, state='normal', fg='white', bg='black', width=2,height=1)
            self.birth_neuralGroup_sizeTb.insert('end', '3')
            self.birth_neuralGroup_sizeTb.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
            bt = tk.Button(master=self.birth_neuralGroupPanel, text='Back', command=self.birth_neuralGroup_backBtCb, fg='white', bg='black')
            bt.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            lb = tk.Label(master=self.birth_neuralGroupPanel, bg='black')
            lb.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.waitingForInput = False
            self.selected=[]
        
        def select(self, selection, event):
            if self.waitingForInput==True:
                self.selectionFunction(selection, event)
        
        def birthBtCb(self):
            self.mainPanel.pack_forget()
            self.birthPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            self.birth_neuralGroupPanel.pack_forget()
        
        def birth_neuronBtCb(self):
            self.selectionFunction = self.birth_neuronSelectionFunction
            self.waitingForInput = True
        
        def birth_neuronSelectionFunction(self, selection, event):
            print('Creating new neuron')
            self.canvas.addNeuron(DrawableNeuron(x=event.x, y=event.y))
        
        def birth_neuralGroupBtCb(self):
            self.mainPanel.pack_forget()
            self.birthPanel.pack_forget()
            self.birth_neuralGroupPanel.pack(side=tk.TOP, fill=tk.BOTH, expand=True)
            
            self.selectionFunction = self.birth_neuralGroupSelectionFunction
            self.waitingForInput = True
        
        def birth_neuralGroupSelectionFunction(self, selection, event):
            print('Creating new neural group')
            N = int(self.birth_neuralGroup_sizeTb.get('1.0','end'))
            neurons = [Neuron() for i in range(N)]
            neuralGroup = NeuralGroup(neurons)
            self.canvas.addNeuralGroup(DrawableNeuralGroup(neuralGroup, x=event.x, y=event.y, height=30*N))
        
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
        
        def groupTogetherBtCb(self):
            pass