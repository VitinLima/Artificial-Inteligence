# -*- coding: utf-8 -*-
"""
Created on Sat Oct 15 02:06:49 2022

@author: 160047412
"""

import tkinter as tk
from tkinter import ttk
from NeuralNetworkCanvas import NeuralNetworkCanvas,DrawableNeuralGroup,DrawableNeuron,DrawableConnection,OptionsPanel

class App(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)
        self.title = "Main Root Window"
        
        self.isAlive = True
        self.protocol("WM_DELETE_WINDOW", self.on_closing)
    
    # def handleEvent(self, event):
    #     self.last_event=event
    #     if self.last_event==[]:
    #         self.optPanel.destroy()
    #     else:
    #         self.optPanel = OptionsPanel(master=app, width=100, height=200, bg='black')
    #         # optPanel.grid(row=0,column=1)
    #         self.optPanel.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
    
    def getRoot(self):
        return self
        
    def on_closing(self):
        self.isAlive = False
        self.destroy()

class WPanel(tk.Frame):
    def __init__(self, master=None, cnf={}, **kw):
        tk.Frame.__init__(self, master, cnf, **kw)
    
    def bt1Cb(self):
        pass
    
    def bt2Cb(self):
        pass
    
    def getRoot(self):
        if self.master==None:
            return None
        return self.master.getRoot()

def foo():
    global app, frame1, optPanel
    print(optPanel.pack(side=tk.LEFT, fill=tk.BOTH, expand=True))

if __name__=="__main__":
    try:
        app=App()
        
        tabs = ttk.Notebook(master=app)
        tabs.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        frame = tk.Frame(master=tabs, bg='black', borderwidth=0, padx=0, pady=0)
        frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        tabs.add(frame, text='Network')
        
        canvas = NeuralNetworkCanvas(master=frame, width=500, height=200, bg='black', borderwidth=0)
        canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        optPanel = OptionsPanel(canvas=canvas, master=frame, bg='black', borderwidth=0)
        optPanel.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        n1=DrawableNeuron(x=100,y=50)
        n2=DrawableNeuron(x=50,y=100)
        n3=DrawableNeuron(x=20,y=30)
        n4=DrawableNeuron(x=170,y=50)
        
        canvas.addNeuron(n1)
        canvas.addNeuron(n2)
        canvas.addNeuron(n3)
        canvas.addNeuron(n4)
        
        canvas.neurons[0].addConnection(DrawableConnection(n1,n2))
        canvas.neurons[1].addConnection(DrawableConnection(n2,n3))
        canvas.neurons[3].addConnection(DrawableConnection(n4,n1))
        
        neuralGroup1 = DrawableNeuralGroup(x=250,y=100)
        neuralGroup1.addNeuron(DrawableNeuron())
        neuralGroup1.addNeuron(DrawableNeuron())
        neuralGroup1.addConnection(n1)
        
        neuralGroup2 = DrawableNeuralGroup(x=350,y=50)
        neuralGroup2.addNeuron(DrawableNeuron())
        neuralGroup2.addNeuron(DrawableNeuron())
        neuralGroup2.addNeuron(DrawableNeuron())
        neuralGroup2.addConnection(neuralGroup1)
        
        canvas.addNeuralGroup(neuralGroup1)
        canvas.addNeuralGroup(neuralGroup2)
        
        canvas.draw()
        
        while app.isAlive==True:
            # canvas.draw()
            app.update_idletasks()
            app.update()
    finally:
        if app.isAlive:
            app.destroy()