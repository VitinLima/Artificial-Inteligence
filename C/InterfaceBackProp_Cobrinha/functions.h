#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#include <stdlib.h>
#include <stdio.h>

#include "defines.h"

float Det(float **Matrix, int N);
float ProdutoEscalar();
void ProdutoInterno();
void ProdutoMatrixVetor(float **M, float *N, float *O, int Nm, int Nn);
void ProdutoVetorMatrix(float *N, float **M, float *O, int Nn, int Nm);
void ProdutoMatrixMatrix();
void SomaVetor();
void SomaMatrix();
int faci(int a);
float powi(float x, int a);
float expj(float a, int i);
float expi(float a);
float Sigmoid(float P);

#endif