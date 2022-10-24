#include <stdio.h>
#include <stdlib.h>
#include "Funcoes.h"
#include "AcopladasBackPropagation.h"

void AlocaWBWCBCLM(){
	//printf("AlocaWBWCBCLM\n");
	int i, j;
	
	W = malloc(sizeof(float**)*(Nc-1));
	B = malloc(sizeof(float*)*(Nc-1));
	WC = malloc(sizeof(float**)*(Nc-1));
	BC = malloc(sizeof(float*)*(Nc-1));
	L = malloc(sizeof(float*)*Nc);
	LC = malloc(sizeof(float*)*(Nc-1));
	
	for(i = 0; i < Nc-1; i++){
		W[i] = malloc(sizeof(float*)*I[i+1]);
		B[i] = malloc(sizeof(float)*I[i+1]);
		WC[i] = malloc(sizeof(float)*I[i+1]);
		BC[i] = malloc(sizeof(float)*I[i+1]);
		LC[i] = malloc(sizeof(float)*I[i+1]);
		for(j = 0; j < I[i+1]; j++){
			W[i][j] = malloc(sizeof(float)*I[i]);
			WC[i][j] = malloc(sizeof(float)*I[i]);
		}
	}
	for(i = 0; i < Nc; i++)
		L[i] = malloc(sizeof(float*)*I[i]);
	
	M = malloc(sizeof(float)*I[0]);
}

void DESALOCAWBWCBCLLCIM(){
	//printf("DESALOCAWBWCBCLLCIM\n");
	int i, j;
	for(i = 0; i < Nc-1; i++){
		free(B[i]);
		free(BC[i]);
		free(LC[i]);
		for(j = 0; j < I[i+1]; j++){
			free(W[i][j]);
			free(WC[i][j]);
		}
		free(W[i]);
		free(WC[i]);
	}
	for(i = 0; i < Nc; i++)
		free(L[i]);
	free(W);
	free(B);
	free(L);
	free(LC);
	free(I);
	free(M);
	
	printf("Liberating Cobrinha\n\n");
	for(i = 0; i < Ng; i++){
		free(G[i]);
		free(t[i]);
	}
	free(G);
	free(t);
}

void saveWB(){
	//printf("saveWB\n");
	FILE *A;
	int i, j, k;
	printf("Saving\n\n");
	char Name[20] = "WBddHidden.dat";
	*(Name+2) = (Nc/10)%10+48;
	*(Name+3) = Nc%10+48;
	A = fopen(Name, "r");
	int flag;
	flag = 0;
	while(A != NULL){
		flag++;
		fclose(A);
		*(Name+10) = '[';
		*(Name+11) = flag+48;
		*(Name+12) = ']';
		*(Name+13) = '.';
		*(Name+14) = 'd';
		*(Name+15) = 'a';
		*(Name+16) = 't';
		*(Name+17) = '\0';
		A = fopen(Name, "r");
	}
	fclose(A);
	A = fopen(Name, "w");
	fprintf(A, "%d\n", Nc);
	for(i = 0; i < Nc; i++)
		fprintf(A, "%d ", I[i]);
	fprintf(A, "\n");
	for(i = 0; i < Nc-1; i++)
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				fprintf(A, "%.5f ", W[i][j][k]);
			fprintf(A, "\n");
		}
	for(i = 0; i < Nc-1; i++){
		for(j = 0; j < I[i+1]; j++)
			fprintf(A, "%.5f ", B[i][j]);
		fprintf(A, "\n");
	}
	fclose(A);
}

boolean Inicializa_BackPropagation(){
	//printf("Inicializa_BackPropagation\n");
	boolean opt;
	FILE *A = NULL;
	opt = Display_BackPropagation_Initialization();
	if(opt){
		if(Display_BackPropagation_Loading(&A))
			return TRUE;
	}
	else
		Display_BackPropagation_Creation();
	
	AlocaI(opt, A);
	
	AlocaWBWCBCLM();
	
	ZeraWCBC();
	
	IniciaWB(opt, A);
	
	fclose(A);
	
	return FALSE;
}