#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <windows.h>

int Game(int D){
	//printf("Game\n");
	int flag = 1;
	int i, j;
	boolean oPost = TRUE, oPostMoves = TRUE;
	switch(D){
		case 1:
			Pos[1]+=1;
			if(Pos[1] == Ng && oPost)
				return 1;
			if(Pos[1] == Ng)
				Pos[1] -= Ng;
			if(G[Pos[0]][Pos[1]] == 1)
				return 1;
			break;
		case 2:
			Pos[1]-=1;
			if(Pos[1] == -1 && oPost)
				return 1;
			if(Pos[1] == -1)
				Pos[1] += Ng;
			if(G[Pos[0]][Pos[1]] == 1)
				return 1;
			break;
		case 3:
			Pos[0]+=1;
			if(Pos[0] == Ng && oPost)
				return 1;
			if(Pos[0] == Ng)
				Pos[0] -= Ng;
			if(G[Pos[0]][Pos[1]] == 1)
				return 1;
			break;
		case 4:
			Pos[0]-=1;
			if(Pos[0] == -1 && oPost)
				return 1;
			if(Pos[0] == -1)
				Pos[0] += Ng;
			if(G[Pos[0]][Pos[1]] == 1)
				return 1;
			break;
	}
	for(i = 0; i < Ng; i++)
		for(j = 0; j < Ng; j++)
			if(t[i][j] == 0)
				G[i][j] = 0;
	for(i = 0; i < Ng; i++)
		for(j = 0; j < Ng; j++)
			if(t[i][j] > -1)
			t[i][j] -= 1;

	if(oPostMoves)
		Moves-=1;
	if(G[Pos[0]][Pos[1]] == 1)
		return 1;
	if(G[Pos[0]][Pos[1]] == 2){
		score += 1;
		t[Pos[0]][Pos[1]] += 1;
		Moves = MaxMoves;
		while(flag){
			i = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
			j = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
			if(G[i][j] == 0){
				G[i][j] = 2;
				flag = 0;
			}
		}
		G[Pos[0]][Pos[1]] = 1;
		t[Pos[0]][Pos[1]] += score+1;
		return 2;
	}
	if(Moves == 0)
		return 3;
	G[Pos[0]][Pos[1]] = 1;
	t[Pos[0]][Pos[1]] += score+3;
	return 0;
}

void Inicializa_Game(){
	//printf("Inicializa_Game\n");
	int i, j;
	MontaJogo();
	G = malloc(sizeof(int*)*Ng);
	t = malloc(sizeof(int*)*Ng);
	for(i = 0; i < Ng; i++){
		G[i] = malloc(sizeof(int)*Ng);
		t[i] = malloc(sizeof(int)*Ng);
	}
	
	for(i = 0; i < Ng; i++)
		for(j = 0; j < Ng ; j++){
			G[i][j] = 0;
			t[i][j] = -1;
		}
	
	Moves = MaxMoves;
	Pos[0] = (int)((float)Ng/2.0f);
	Pos[1] = (int)((float)Ng/2.0f);
	G[Pos[0]][Pos[1]] = 1;
	t[Pos[0]][Pos[1]] = score+3;
	
	int flag = 1;
	while(flag){
		i = (int)((float)rand()/(float)RAND_MAX*(float)Ng);
		j = (int)((float)rand()/(float)RAND_MAX*(float)Ng);
		if(G[i][j] == 0){
			G[i][j] = 2;
			flag = 0;
		}
	}
}