#ifndef COBRINHA_H
#define COBRINHA_H

#include <stdio.h>
#include <stdlib.h>
// #include <conio.h>
// #include <windows.h>
#include "defines.h"

int Game(int **G, int **t, int *score, int *p, int D, int *Moves, int N);
void Inicializa_Game(int ***G, int ***t, int *score, int *Moves, int *N, int *Pos);

#endif