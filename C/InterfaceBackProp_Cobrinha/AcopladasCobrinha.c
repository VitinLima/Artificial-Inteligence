#include "AcopladasCobrinha.h"

void ResetGame(int **G, int **t, int *P, int *score, int *Moves, int N){
	*score = 0;
	*Moves = 200;
	int i, j;
	for(i = 0; i < N; i++)
		for(j = 0; j < N; j++){
			G[i][j] = 0;
			t[i][j] = -1;
		}
	i = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
	j = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
	P[0] = j;
	P[1] = j;
	G[P[0]][P[1]] = 1;
	t[P[0]][P[1]] = 0;
	boolean flag = TRUE;
	while(flag){
		i = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
		j = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
		if(G[i][j] == 0){
			G[i][j] = 2;
			flag = FALSE;
		}
	}
}

void PutSurroundings(float *M){
	int i;
	for(i = 0; i < 20; i++)
		L[0][i] = M[i];
}

void GetSurroundings(float *M, int **G, int *P, int N, int Nc){
	int i, j;
	for(i = 0; i < 20; i++)
		M[i] = 0;
	//CIMA
	for(i = 1; P[0]+i < N; i++){
		if(G[P[0]+i][P[1]] == 1)
			break;
		M[0]++;
	}
	//BAIXO
	for(i = 1; P[0]-i >= 0; i++){
		if(G[P[0]-i][P[1]] == 1)
			break;
		M[1]++;
	}
	//DIREITA
	for(i = 1; P[1]+i < N; i++){
		if(G[P[0]][P[1]+i] == 1)
			break;
		M[2]++;
	}
	//ESQUERDA
	for(i = 1; P[1]-i >= 0; i++){
		if(G[P[0]][P[1]-i] == 1)
			break;
		M[3]++;
	}
	//BAIXOESQUERDA
	for(i = 1; P[0]+i < N && P[1]+i < N; i++){
		if(G[P[0]+i][P[1]+i] == 1)
			break;
		M[4]++;
	}
	//BAIXOESQUERDA
	for(i = 1; P[0]+i < N && P[1]-i >= 0; i++){
		if(G[P[0]+i][P[1]-i] == 1)
			break;
		M[5]++;
	}
	//CIMADIREITA
	for(i = 1; P[0]-i >= 0 && P[1]+i < N; i++){
		if(G[P[0]-i][P[1]+i] == 1)
			break;
		M[6]++;
	}
	//CIMADIREITA
	for(i = 1; P[0]-i >= 0 && P[1]-i >= 0; i++){
		if(G[P[0]-i][P[1]-i] == 1)
			break;
		M[7]++;
	}
	for(i = 0; i < 8; i++)
		M[i] = 1.0f/(1.0f + M[i]);
	M[16] = L[Nc-1][0];
	M[17] = L[Nc-1][1];
	M[18] = L[Nc-1][2];
	M[19] = L[Nc-1][3];
	//BAIXO
	for(i = 1; P[0]+i < N; i++){
		if(G[P[0]+i][P[1]] == 1)
			break;
		if(G[P[0]+i][P[1]] == 2){
			M[8]++;
			return;
		}
	}
	//CIMA
	for(i = 1; P[0]-i >= 0; i++){
		if(G[P[0]-i][P[1]] == 1)
			break;
		if(G[P[0]-i][P[1]] == 2){
			M[9]++;
			return;
		}
	}
	//DIREITA
	for(i = 1; P[1]+i < N; i++){
		if(G[P[0]][P[1]+i] == 1)
			break;
		if(G[P[0]][P[1]+i] == 2){
			M[10]++;
			return;
		}
	}
	//ESQUERDA
	for(i = 1; P[1]-i >= 0; i++){
		if(G[P[0]][P[1]-i] == 1)
			break;
		if(G[P[0]][P[1]-i] == 2){
			M[11]++;
			return;
		}
	}
	//BAIXODIREITA
	for(i = 1; P[0]+i < N && P[1]+i < N; i++){
		if(G[P[0]+i][P[1]+i] == 1)
			break;
		if(G[P[0]+i][P[1]+i] == 2){
			M[12]++;
			return;
		}
	}
	//BAIXOESQUERDA
	for(i = 1; P[0]+i < N && P[1]-i >= 0; i++){
		if(G[P[0]+i][P[1]-i] == 1)
			break;
		if(G[P[0]+i][P[1]-i] == 2){
			M[13]++;
			return;
		}
	}
	//CIMADIREITA
	for(i = 1; P[0]-i >= 0 && P[1]+i < N; i++){
		if(G[P[0]-i][P[1]+i] == 1)
			break;
		if(G[P[0]-i][P[1]+i] == 2){
			M[14]++;
			return;
		}
	}
	//CIMAESQUERDA
	for(i = 1; P[0]-i >= 0 && P[1]-i >= 0; i++){
		if(G[P[0]-i][P[1]-i] == 1)
			break;
		if(G[P[0]-i][P[1]-i] == 2){
			M[15]++;
			return;
		}
	}
}