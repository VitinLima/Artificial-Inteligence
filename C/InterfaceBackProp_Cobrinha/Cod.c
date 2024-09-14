#include <stdio.h>
#include <stdlib.h>
#include "Cobrinha.h"
#include "BackPropagation.h"
#include "AcopladasCobrinha.h"
#include <time.h>
#include <unistd.h>
#include "defines.h"

int main(){
	srand(time(0));
	int **G, **t, Moves, score = 0, Ng;
	int Pos[2];
	Inicializa_Game(&G, &t, &score, &Moves, &Ng, Pos);
	int *I, Nc;
	int i, j, k;
	if(Inicializa_BackPropagation(&I, &Nc)){
		printf("An Error Occured During WB Initialization\n\nReturning\n\n");
	
		printf("Liberating Cobrinha\n\n");
		for(i = 0; i < Ng; i++){
			free(G[i]);
			free(t[i]);
		}
		free(G);
		free(t);
		
		printf("Code is closing\n\nReturning 0! :D\n\n");
		
		return 0;
	}
	
	int Choice1, Choice2, b = 0, flag, flag2 = 0, flag3 = 0, Game_Id = 0;
	float temp1, temp2;
	float M[20];
	float C;
	
	int BatchSize;
	printf("Please, enter the size of each batch for this training\n\n");
	scanf("%d", &BatchSize);
	while(BatchSize < 1){
		printf("\n\nInvalid value\n\nValue must be greater than 1\n\n");
		printf("Please, enter the size of each batch for this training\n\n");
		scanf("%d", &BatchSize);
	}
	
	int GamesMove;
	printf("Please, enter the number of games between each visualization for this training\n\n");
	scanf("%d", &GamesMove);
	while(GamesMove < 1){
		printf("\n\nInvalid value\n\nValue must be greater than 1\n\n");
		printf("Please, enter the number of games between each visualization for this training\n\n");
		scanf("%d", &GamesMove);
	}
	
	float RandomBehaviour = 4.0f, DeltaRandomBehaviour = 0.01f, Decision[4], z;
	boolean opt = FALSE;
	
	printf("\nStarting game\n");
	while(TRUE){
		GetSurroundings(M, G, Pos, Ng, Nc);
		PutSurroundings(M);
		Processa(Nc, I);
		
		Choice1 = 0;
		temp1 = 0;
		for(i = 0; i < 4; i++)
			if(L[Nc-1][i] > temp1){
				Choice1 = i+1;
				temp1 = L[Nc-1][i];
			}
		if(opt)
			z = Sigmoid(RandomBehaviour);
		else
			z = 0.0f;
		for(i = 0; i < 4; i++)
			Decision[i] = z*(float)rand()/(float)RAND_MAX + (1.0f - z)*L[Nc-1][i];
		temp1 = 0;
		Choice2 = 0;
		for(i = 0; i < 4; i++)
			if(Decision[i] > temp1){
				temp1 = Decision[i];
				Choice2 = i+1;
			}
		
		
		
		if(Game_Id%GamesMove == 0 && Game_Id > 0){
			PrintInformation(Nc, I, Ng, G, Pos, Game_Id, score, flag2, Choice2, Moves, RandomBehaviour);
			usleep(100000);
		}
		
		flag = Game(G, t, &score, Pos, Choice2, &Moves, Ng);
		if(flag == 1){
			flag3++;
			BackPropagation(Nc, I, FALSE, Choice2, BatchSize);
			ResetGame(G, t, Pos, &score, &Moves, Ng);
			Game_Id++;
			if(Choice1==Choice2 && RandomBehaviour < 100)
				RandomBehaviour += DeltaRandomBehaviour;
		}
		else{
			if(flag == 2){
				flag2++;
				flag3++;
				BackPropagation(Nc, I, TRUE, Choice2, BatchSize);
				if(Choice1==Choice2 && RandomBehaviour > -4)
					RandomBehaviour -= DeltaRandomBehaviour;
			}
			else
				if(flag == 3){
					//flag3++;
					BackPropagation(Nc, I, FALSE, Choice2, BatchSize);
					ResetGame(G, t, Pos, &score, &Moves, Ng);
					Game_Id++;
				}
		}
		if(flag3 == BatchSize){
			Corrige(Nc, I);
			flag3 = 0;
			ZeraWCBC(Nc, I);
		}
	}
	// char c = getch();
	
	FILE *A;
	printf("Saving\n\n");
	char Name[20] = "WBddHidden.dat";
	*(Name+2) = (Nc/10)%10+48;
	*(Name+3) = Nc%10+48;
	A = fopen(Name, "r");
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
	printf("Liberating Back Propagation\n\n");
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
	
	printf("Liberating Cobrinha\n\n");
	for(i = 0; i < Ng; i++){
		free(G[i]);
		free(t[i]);
	}
	free(G);
	free(t);
	
	printf("Returning 0 successfully! :D\n\n");
	
	return 0;
}