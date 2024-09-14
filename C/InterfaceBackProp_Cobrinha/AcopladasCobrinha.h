#ifndef ACOPLADAS_COBRINHA_H
#define ACOPLADAS_COBRINHA_H

#include <stdlib.h>
#include <stdio.h>
#include "defines.h"

void ResetGame(int **G, int **t, int *P, int *score, int *Moves, int N);
void PutSurroundings(float *M);
void GetSurroundings(float *M, int **G, int *P, int N, int Nc);

#endif