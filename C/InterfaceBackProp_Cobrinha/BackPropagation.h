#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "Funcoes.h"

float ***W, ***WC;
float **B, **BC;
float **L, **LC;
#include "AcopladasBackPropagation.h"

int Inicializa_BackPropagation(int **I, int *N){
	printf("Inicia Back Propagation\n\n");
	int opt;
	FILE *A;
	char Name[20];
	printf("1. Load WB\n2. Create WB\n\n");
	scanf("%d", &opt);
	while(opt != 2 && opt != 1){
		printf("Invalid Option\n");
		printf("1. Load WB\n2. Create WB");
		scanf("%d", &opt);
	}
	if(opt == 2){
		printf("Hidden Layers\n\n");
		scanf("%d", N);
		printf("\n");
		*N+=2;
	}
	else{
		printf("Arquive to load:\n\n");
		scanf("%s", Name);
		A = fopen(Name, "r");
		while(A == NULL){
			fclose(A);
			printf("Arquive Not Found\n\n");
			return 1;
		}
		printf("\n");
		fscanf(A, "%d", N);
		printf("\n%s\n\n%d\n", Name, *N);
	}
	
	int i, j;
	
	*I = malloc(sizeof(int)*(*N));
	if(opt == 2){
		for(i = 1; i < *N-1; i++){
			printf("\nNumber of Neurons of Layer %d\n\n", i);
			scanf("%d", &(*I)[i]);
			printf("\n");
		}
		(*I)[0] = 20;
		(*I)[*N-1] = 4;
	}
	else
		for(i = 0; i < *N; i++)
			fscanf(A, "%d", &(*I)[i]);
	
	W = malloc(sizeof(float**)*(*N-1));
	B = malloc(sizeof(float*)*(*N-1));
	WC = malloc(sizeof(float**)*(*N-1));
	BC = malloc(sizeof(float*)*(*N-1));
	L = malloc(sizeof(float*)*(*N));
	LC = malloc(sizeof(float*)*(*N-1));
	
	for(i = 0; i < *N-1; i++){
		W[i] = malloc(sizeof(float*)*(*I)[i+1]);
		B[i] = malloc(sizeof(float)*(*I)[i+1]);
		WC[i] = malloc(sizeof(float*)*(*I)[i+1]);
		BC[i] = malloc(sizeof(float)*(*I)[i+1]);
		LC[i] = malloc(sizeof(float)*(*I)[i+1]);
		for(j = 0; j < (*I)[i+1]; j++){
			W[i][j] = malloc(sizeof(float)*(*I)[i]);
			WC[i][j] = malloc(sizeof(float)*(*I)[i]);
		}
	}
	for(i = 0; i < *N; i++)
		L[i] = malloc(sizeof(float*)*(*I)[i]);
	
	int n = *N;
	IniciaWB(n, *I, opt, A);
	
	//PrintaSistema(N, *I);
	return 0;
}