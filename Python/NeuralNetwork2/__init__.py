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

class MyClass:
    def __init__(self, canvas, color, layer, tag, x=0, y=0, w=50, h=50):
        self.tag = str(self).split()[-1]
        self.onCanvas = []
        self.onCanvas.append(canvas.create_rectangle([x, y, x+w, y+h], fill=color, tag=self.tag))
        self.onCanvas.append(canvas.create_rectangle([x+200, y, x+w+200, y+h], fill=color, tag=self.tag))
        self.layer = layer

if __name__=="__main__":
    try:
        app=App()
        # canvas = tk.Canvas(master=app)
        # W = 100
        # H = 100
        # rect1 = MyClass(canvas, 'red', 3, 'rect1', x=50, y=50, w=W, h=H)
        # # rect2 = MyClass(canvas, 'green', 2, 'rect2', x=75, y=50, w=W, h=H)
        # # rect3 = MyClass(canvas, 'blue', 1, 'rect3', x=100, y=50, w=W, h=H)
        # canvas.pack()
        # # order = canvas.find_all()
        # # layers = [3,2,1]
        
        # # for i in range(len(layers)-1):
        # #     canvas.tag_raise(layers[i+1], layers[i])
        
        # print(canvas.find_all())
        
        tabs = ttk.Notebook(master=app)
        tabs.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        frame = tk.Frame(master=tabs, bg='black', borderwidth=0, padx=0, pady=0)
        frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        tabs.add(frame, text='Network')
        
        canvas = NeuralNetworkCanvas(master=frame, width=500, height=200, bg='black', borderwidth=0)
        canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        optPanel = OptionsPanel(canvas=canvas, master=frame, bg='black', borderwidth=0)
        optPanel.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        n1=DrawableNeuron(canvas, x=100,y=50)
        n2=DrawableNeuron(canvas, x=50,y=100)
        n3=DrawableNeuron(canvas, x=20,y=30)
        n4=DrawableNeuron(canvas, x=170,y=50)
        
        DrawableConnection(n1,n2)
        DrawableConnection(n2,n3)
        DrawableConnection(n4,n1)
        
        neuralGroup1 = DrawableNeuralGroup(canvas, x=250,y=100)
        DrawableNeuron(neuralGroup1)
        DrawableNeuron(neuralGroup1)
        DrawableNeuron(neuralGroup1)
        neuralGroup1.fitToContent()
        DrawableConnection((neuralGroup1), n1)
        
        neuralGroup2 = DrawableNeuralGroup(canvas, x=350,y=50)
        DrawableNeuron(neuralGroup2)
        DrawableNeuron(neuralGroup2)
        DrawableNeuron(neuralGroup2)
        DrawableNeuron(neuralGroup2)
        DrawableNeuron(neuralGroup2)
        neuralGroup2.fitToContent()
        DrawableConnection(neuralGroup2, neuralGroup1)
        
        while app.isAlive==True:
            canvas.draw()
            app.update_idletasks()
            app.update()
    finally:
        if app.isAlive:
            app.destroy()