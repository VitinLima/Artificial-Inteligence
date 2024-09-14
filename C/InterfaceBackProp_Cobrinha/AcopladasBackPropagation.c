#include "AcopladasBackPropagation.h"

void PrintaSistema(int N, int *I){
	printf("\nPrintaSistema\n");
	int i, j, k;
	
	for(i = 0; i < N-1; i++){
		printf("W %d\n", i);
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				printf("%.2f ", W[i][j][k]);
			printf("\n");
		}
		printf("B ");
		for(j = 0; j < I[i+1]; j++)
			printf("%.2f ", B[i][j]);
		printf("\n");
	}
	
	/*for(i = 0; i < N-1; i++){
		printf("WC %d\n", i);
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				printf("%.2f ", WC[i][j][k]);
			printf("\n");
		}
		printf("BC ");
		for(j = 0; j < I[i+1]; j++)
			printf("%.2f ", BC[i][j]);
		printf("\n");
		printf("LC ");
		for(j = 0; j < I[i+1]; j++)
			printf("%.2f ", LC[i][j]);
		printf("\n");
	}*/
	
	for(i = 0; i < N; i++){
		printf("L %d ", i);
		for(j = 0; j < I[i]; j++)
			printf("%.2f ", L[i][j]);
		printf("\n");
	}
}

float DSigmoid(float P){
	if(P*P > 16){
		return 0.0f;
	}
	float Z = 1/(1+expi(-P));
	Z *= Z;
	Z *= expi(-P);
	return Z;
}

void Corrige(int N, int *I){
	//printf("\nCorrige\n");
	int i, j, k;
	for(i = 0; i < N-1; i++)
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				W[i][j][k] -= WC[i][j][k];
			B[i][j] -= BC[i][j];
		}
}

void PrintWCBC(int N, int *I, float ***WC, float **BC){
	printf("\nPrintWCBC\n");
	int i, j, k;
	for(i = 0; i < N-1; i++){
			printf("\nWC %d\n", i);
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				printf("%.2f ", WC[i][j][k]);
			printf("\n");
		}
		printf("\nBC\n");
		for(j = 0; j < I[i+1]; j++)
			printf("%.2f ", BC[i][j]);
		printf("\n");
	}
}

void BackPropagation(int N, int *I, boolean C, int Choice, int BatchSize){
	int i, j, k;
	float z;
	float Answer[4];
	
	for(i = 0; i < N-1; i++)
		for(j = 0; j < I[i+1]; j++)
			LC[i][j] = 0;
	
	if(C){
		for(i = 0; i < 4; i++)
			Answer[i] = 0;
		Answer[Choice-1] = 1;
	}
	else{
		for(i = 0; i < 4; i++)
			Answer[i] = 1;
		Answer[Choice-1] = 0;
	}
	
	for(i = 0; i < I[N-1]; i++)
		LC[N-2][i] = 2*(L[N-1][i]-Answer[i]);
	//if(C)
	//	for(i = 0; i < I[N-1]; i++)
	//		LC[N-2][i] *= 10;
	
	for(i = N-2; i >= 0; i--){
		for(j = 0; j < I[i+1]; j++){
			z = 0.0f;
			for(k = 0; k < I[i]; k++)
				z += L[i][k]*W[i][j][k];
			z += B[i][j];
			BC[i][j] += LC[i][j]*DSigmoid(z)/(float)BatchSize;
			for(k = 0; k < I[i]; k++)
				WC[i][j][k] += LC[i][j]*L[i][k]*DSigmoid(z)/(float)BatchSize;
		}
		if(i > 0)
			for(j = 0; j < I[i+1]; j++)
				for(k = 0; k < I[i]; k++)
					LC[i-1][k] += LC[i][j]*W[i][j][k]*DSigmoid(z);
	}
}

void PrintLayers(int N, int *I){
	printf("\nPrintLayers\n");
	int i, j;
	for(i = 0; i < N; i++){
		for(j = 0; j < I[i]; j++)
			printf("%.2f ", L[i][j]);
		printf("\n");
	}
}

void Processa(int N, int *I){
	//printf("\nProcessa\n");
	int i, j, k;
	
	for(i = 0; i < N-1; i++)
		for(j = 0; j < I[i+1]; j++){
			L[i+1][j] = 0;
			for(k = 0; k < I[i]; k++)
				L[i+1][j] += L[i][k]*W[i][j][k];
			L[i+1][j] += B[i][j];
			L[i+1][j] = Sigmoid(L[i+1][j]);
		}
	//PrintLayers(N, I);
}

void IniciaWB(int N, int *I, int opt, FILE *A){
	printf("\nIniciaWB\n\n");
	int i, j, k;
	if(opt == 2){
		for(i = 0; i < N-1; i++)
			for(j = 0; j < I[i+1]; j++){
				B[i][j] = ((float)rand()/(float)RAND_MAX-0.5f)*2;
				for(k = 0; k < I[i]; k++)
					W[i][j][k] = ((float)rand()/(float)RAND_MAX-0.5f)*2;
			}
	}
	else{
		for(i = 0; i < N-1; i++)
			for(j = 0; j < I[i+1]; j++)
				for(k = 0; k < I[i]; k++)
					fscanf(A, "%f", &W[i][j][k]);
		for(i = 0; i < N-1; i++)
			for(j = 0; j < I[i+1]; j++)
				fscanf(A, "%f", &B[i][j]);
	}
}



void ZeraWCBC(int N, int *I){
	int i, j, k;
	for(i = 0; i < N-1; i++)
		for(j = 0; j < I[i+1]; j++){
			BC[i][j] = 0;
			for(k = 0; k < I[i]; k++)
				WC[i][j][k] = 0;
		}
}

void NivelaWB(int N, int *I){
	int i, j, k;
	float S;
	for(i = 0; i < N-1; i++){
		S = 0;
		for(j = 0; j < I[i+1]; j++)
			S += B[i][j];
		S /= I[i+1];
		for(j = 0; j < I[i+1]; j++)
			B[i][j] -= S;
		
		for(j = 0; j < I[i+1]; j++){
			S = 0;
			for(k = 0; k < I[i]; k++)
				S += W[i][j][k];
			S /= I[i];
			for(k = 0; k < I[i]; k++)
				W[i][j][k] -= S;
		}
	}
}

void PrintInformation(int Nc, int *I, int Ng, int **G, int *Pos, int Game_Id, int score, int flag2, int D, int Moves, float RandomBehaviour){
	int i, j;
	system("@cls||clear");
	//PrintaSistema(Nc, I);
	//for(i = 0; i < 13; i++)
	//	printf("\n");
	//printf("w. Go up\n");
	//printf("s. Go down\n");
	//printf("a. Go left\n");
	//printf("d. Go right\n");
	//printf("p. Pause game\n");
	//printf("e. Finish game\n");
	//printf("\nY position: %d\nX position: %d\nInputs: ", Pos[0], Pos[1]);
	//for(i = 0; i < I[0]; i++)
	//	printf("%.2f ", L[0][i]);
	//printf("\n\nOutputs: ");
	//for(i = 0; i < I[Nc-1]; i++)
	//	printf("%.2f ", L[Nc-1][i]);
	printf("\n\nGame number %d\t\tRandom Behaviour %.3f\n", Game_Id, RandomBehaviour);
	printf("\nScore: %d\t\tMovements left: %d\t\tScored so far: %d\n\n", score, Moves, flag2);
	for(i = 0; i < Ng; i++){
		for(j = 0; j < Ng; j++){
			printf("%d", G[i][j]);
		printf("%d", G[i][j]);
		}
		printf("\n");
	}
	//printf("Move: %d\n", D);
	//printf("Gaming\n");
}

void Mutation(int N, int *I){
	int R[4];
	R[0] = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
	R[1] = (int)((float)rand()/(float)RAND_MAX*(float)(I[R[0]+1]-1));
	R[2] = (int)((float)rand()/(float)RAND_MAX);
	if(R[2] == 0){
		R[3] = (int)((float)rand()/(float)RAND_MAX*(float)(I[R[0]]-1));
		W[R[0]][R[1]][R[3]] *= ((float)rand()/(float)RAND_MAX*2.0f);
		W[R[0]][R[1]][R[3]] += ((float)rand()/(float)RAND_MAX*2.0f);
	}
	else{
		B[R[0]][R[1]] *= ((float)rand()/(float)RAND_MAX*2.0f);
		B[R[0]][R[1]] += ((float)rand()/(float)RAND_MAX*2.0f);
	}
}