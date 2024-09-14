#ifndef BACK_PROPAGATION_H
#define BACK_PROPAGATION_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "functions.h"
#include "defines.h"
#include "AcopladasBackPropagation.h"

extern float ***W, ***WC;
extern float **B, **BC;
extern float **L, **LC;

int Inicializa_BackPropagation(int **I, int *N);

#endif