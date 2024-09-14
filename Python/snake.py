# -*- coding: utf-8 -*-
"""
Created on Thu Oct 20 19:24:12 2022

@author: 160047412
"""

import tkinter as tk
from NeuralNetwork import NeuralNetwork, Neuron, Connection
import random

def rgb_to_hex(color_rgb):
    color_red = hex(int(255*color_rgb[0])).removeprefix('0x').zfill(2)
    color_green = hex(int(255*color_rgb[1])).removeprefix('0x').zfill(2)
    color_blue = hex(int(255*color_rgb[2])).removeprefix('0x').zfill(2)
    return '#' + color_red + color_green + color_blue

class SnakeCanvas(tk.Canvas):
    def __init__(self, width=64, height=64, master=None, cnf={}, **kw):
        tk.Canvas.__init__(self, master, cnf, **kw)
        self.snakes = []
        self.foods = []
        self.walls = []
        self.field = [[[] for w in range(width)] for h in range(height)]
    
    def cycle(self):
        for snake in snakes:
            snake.think()
            
    
    def draw(self):
        for snake in self.snakes:
            if snake.toDraw is True:
                snake.drawSelf(self)
        
        for food in self.foods:
            if food.toDraw is True:
                food.drawSelf(self)
        
        for wall in self.walls:
            if wall.toDraw is True:
                wall.drawSelf(self)

class Snake:
    def __init__(self, field, brain=[]):
        self.field = field
        self.body = []
        self.head = []
        if brain is []:
            self.brain = NeuralNetwork()
            [Neuron(self.brain) for i in range(12)]
            for i in range(4):
                for j in range(8):
                    Connection(self.brain.neurons[8+i], self.brain.neurons[j])
        else:
            self.brain = brain
        self.state = 'unborn'
        self.color = rgb_to_hex(random.random(), random.random(), random.random())
        self.onCanvas = []
        self.toDraw = True
    
    def think(self):
        for i in range(8):
            self.brain.neurons[i].activation = 0.0
        
        if self.head[1] == 0:
            self.brain.neurons[0].activation = 1.0
        elif self.field[self.head[0]][self.head[1]-1] is []:
            self.brain.neurons[0].activation = 1.0
        
        # if self.head[1] == len(field)-1:
        #     pass
    
    def birth(self, x_head, y_head):
        self.body = [[x_head, y_head]]
        self.head = [x_head, y_head]
        self.state = 'alive'
    
    def kill(self, x_head, y_head):
        self.brain.learn()
        self.birth(x_head, y_head)
    
    def drawMove(self, canvas):
        if self.onCanvas is []:
            return
        canvas.delete(self.onCanvas[0])
        x, y, w, h = self.head[0], self.head[1], 10, 10
        canvas.append(canvas.create_rectangle(w*x, h*y, w*x+w, h*y+h, fill=self.color))
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.onCanvas:
            canvas.delete(d)
        
        for b in self.body:
            x, y, w, h = self.head[0], self.head[1], 10, 10
            self.on_canvas.append(canvas.create_rectangle(w*x, h*y, w*x+w, h*y+h, fill=self.color))

class Food:
    def __init__(self, field):
        self.field = field
        self.position = []
        self.state = 'unborn'
        self.color = rgb_to_hex(random.random(), random.random(), random.random())
        self.onCanvas = []
        self.toDraw = True
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.onCanvas:
            canvas.delete(d)
        
        x, y, w, h = self.position[0], self.position[1], 10, 10
        self.on_canvas.append(canvas.create_rectangle(w*x, h*y, w*x+w, h*y+h, fill=self.color))
        self.on_canvas.append(canvas.create_text((w*x, h*y), text='f', fill='white', font='tkDefaultFont 10'))

class Wall:
    def __init__(self, field):
        self.field = field
        self.position = []
        self.state = 'unborn'
        self.color = rgb_to_hex(random.random(), random.random(), random.random())
        self.onCanvas = []
        self.toDraw = True
    
    def drawSelf(self, canvas):
        self.toDraw = False
        for d in self.onCanvas:
            canvas.delete(d)
        
        x, y, w, h = self.position[0], self.position[1], 10, 10
        self.on_canvas.append(canvas.create_rectangle(w*x, h*y, w*x+w, h*y+h, fill=self.color))
        self.on_canvas.append(canvas.create_text((w*x, h*y), text='w', fill='white', font='tkDefaultFont 10'))