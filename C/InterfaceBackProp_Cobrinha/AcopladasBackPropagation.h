#ifndef ACOPLADAS_BACK_PROPAGATION_H
#define ACOPLADAS_BACK_PROPAGATION_H

#include <stdlib.h>
#include <stdio.h>
#include "defines.h"

void PrintaSistema(int N, int *I);
float DSigmoid(float P);
void Corrige(int N, int *I);
void PrintWCBC(int N, int *I, float ***WC, float **BC);
void BackPropagation(int N, int *I, boolean C, int Choice, int BatchSize);
void PrintLayers(int N, int *I);
void Processa(int N, int *I);
void IniciaWB(int N, int *I, int opt, FILE *A);
void ZeraWCBC(int N, int *I);
void NivelaWB(int N, int *I);
void PrintInformation(int Nc, int *I, int Ng, int **G, int *Pos, int Game_Id, int score, int flag2, int D, int Moves, float RandomBehaviour);
void Mutation(int N, int *I);

#endif;