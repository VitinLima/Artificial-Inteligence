#include <stdio.h>
#include <stdlib.h>
// #include <conio.h>
// #include <windows.h>
#include "defines.h"

int Game(int **G, int **t, int *score, int *p, int D, int *Moves, int N){
	int b = 1;
	int i, j;
	boolean opt = TRUE, optMoves = TRUE;
	switch(D){
		case 1:
			p[1]+=1;
			if(p[1] == N && opt)
				return 1;
			if(p[1] == N)
				p[1] -= N;
			if(G[p[0]][p[1]] == 1)
				return 1;
			break;
		case 2:
			p[1]-=1;
			if(p[1] == -1 && opt)
				return 1;
			if(p[1] == -1)
				p[1] += N;
			if(G[p[0]][p[1]] == 1)
				return 1;
			break;
		case 3:
			p[0]+=1;
			if(p[0] == N && opt)
				return 1;
			if(p[0] == N)
				p[0] -= N;
			if(G[p[0]][p[1]] == 1)
				return 1;
			break;
		case 4:
			p[0]-=1;
			if(p[0] == -1 && opt)
				return 1;
			if(p[0] == -1)
				p[0] += N;
			if(G[p[0]][p[1]] == 1)
				return 1;
			break;
	}
	for(i = 0; i < N; i++)
		for(j = 0; j < N; j++)
			if(t[i][j] == 0)
				G[i][j] = 0;
	for(i = 0; i < N; i++)
		for(j = 0; j < N; j++)
			if(t[i][j] > -1)
			t[i][j] -= 1;

	if(optMoves)
		*Moves-=1;
	if(G[p[0]][p[1]] == 1)
		return 1;
	if(G[p[0]][p[1]] == 2){
		*score += 1;
		t[p[0]][p[1]] += 1;
		*Moves = 200;
		while(b){
			i = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
			j = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
			if(G[i][j] == 0){
				G[i][j] = 2;
				b = 0;
			}
		}
		G[p[0]][p[1]] = 1;
		t[p[0]][p[1]] += *score+1;
		return 2;
	}
	if(*Moves == 0)
		return 3;
	G[p[0]][p[1]] = 1;
	t[p[0]][p[1]] += *score+3;
	return 0;
}

void Inicializa_Game(int ***G, int ***t, int *score, int *Moves, int *N, int *Pos){
	printf("Inicia Game\n\n");
	int i, j;
	//printf("Tamanho do campo da Cobrinha\n\n");
	//scanf("%d", N);
	//if(*N < 28)
	//	*N = 28;
	//printf("\n\n");
	*N = 10;
	*G = (int**)malloc(sizeof(int*)*(*N));
	*t = (int**)malloc(sizeof(int*)*(*N));
	for(i = 0; i < *N; i++){
		(*G)[i] = (int*)malloc(sizeof(int)*(*N));
		(*t)[i] = (int*)malloc(sizeof(int)*(*N));
	}
	
	for(i = 0; i < *N; i++)
		for(j = 0; j < *N ; j++){
			(*G)[i][j] = 0;
			(*t)[i][j] = -1;
		}
	
	*Moves = 200;
	Pos[0] = (int)((float)*N/2.0f);
	Pos[1] = (int)((float)*N/2.0f);
	(*G)[Pos[0]][Pos[1]] = 1;
	(*t)[Pos[0]][Pos[1]] = *score+3;
	
	int b = 1;
	while(b){
		i = (int)((float)rand()/(float)RAND_MAX*(*N));
		j = (int)((float)rand()/(float)RAND_MAX*(*N));
		if((*G)[i][j] == 0){
			(*G)[i][j] = 2;
			b = 0;
		}
	}
}