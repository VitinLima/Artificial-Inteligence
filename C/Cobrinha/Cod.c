#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <windows.h>

int Game(int **G, int **t, int *score, int *p, int *D, int *Moves){
	char c;
	int b = 1;
	if(kbhit()){
		c = getch();
		switch(c){
			case 'd':
				if(*D != 2)
					*D = 1;
				//else	if(*score > 0)	return 0;
				break;
			case 'a':
				if(*D != 1)
					*D = 2;
				//else	if(*score > 0)	return 0;
				break;
			case 's':
				if(*D != 4)
					*D = 3;
				//else	if(*score > 0)	return 0;
				break;
			case 'w':
				if(*D != 3)
					*D = 4;
				//else	if(*score > 0)	return 0;
				break;
			case 'p':
				while(!kbhit());
				return 1;
				break;
			case 'e':
				return 0;
				break;
		}
	}
	int i, j;
	switch(*D){
		case 1:
			p[1]+=1;
			if(p[1] == 28 || *Moves == 0 || G[p[0]][p[1]] == 1)
				return 0;
			if(G[p[0]][p[1]] == 2){
				*score += 1;
				*Moves = 60;
				while(b){
					i = (int)((float)rand()/(float)RAND_MAX*27);
					j = (int)((float)rand()/(float)RAND_MAX*27);
					if(G[i][j] == 0){
						G[i][j] = 2;
						b = 0;
					}
				}
			}
			G[p[0]][p[1]] = 1;
			t[p[0]][p[1]] += *score+2;
			break;
		case 2:
			p[1]-=1;
			if(p[1] == -1 || *Moves == 0 || G[p[0]][p[1]] == 1)
				return 0;
			if(G[p[0]][p[1]] == 2){
				*score += 1;
				*Moves = 60;
				while(b){
					i = (int)((float)rand()/(float)RAND_MAX*27);
					j = (int)((float)rand()/(float)RAND_MAX*27);
					if(G[i][j] == 0){
						G[i][j] = 2;
						b = 0;
					}
				}
			}
			G[p[0]][p[1]] = 1;
			t[p[0]][p[1]] += *score+2;
			break;
		case 3:
			p[0]+=1;
			if(p[0] == 28 || *Moves == 0 || G[p[0]][p[1]] == 1)
				return 0;
			if(G[p[0]][p[1]] == 2){
				*score += 1;
				*Moves = 60;
				while(b){
					i = (int)((float)rand()/(float)RAND_MAX*27);
					j = (int)((float)rand()/(float)RAND_MAX*27);
					if(G[i][j] == 0){
						G[i][j] = 2;
						b = 0;
					}
				}
			}
			G[p[0]][p[1]] = 1;
			t[p[0]][p[1]] += *score+2;
			break;
		case 4:
			p[0]-=1;
			if(p[0] == -1 || *Moves == 0 || G[p[0]][p[1]] == 1)
				return 0;
			if(G[p[0]][p[1]] == 2){
				*score += 1;
				*Moves = 60;
				while(b){
					i = (int)((float)rand()/(float)RAND_MAX*27);
					j = (int)((float)rand()/(float)RAND_MAX*27);
					if(G[i][j] == 0){
						G[i][j] = 2;
						b = 0;
					}
				}
			}
			G[p[0]][p[1]] = 1;
			t[p[0]][p[1]] += *score+2;
			break;
	}
	for(i = 0; i < 28; i++)
		for(j = 0; j < 28; j++)
			if(t[i][j] == 0)
				G[i][j] = 0;
	for(i = 0; i < 28; i++)
		for(j = 0; j < 28; j++)
			if(t[i][j] > -1)
				t[i][j] -= 1;
	*Moves-=1;
	
	system("@cls||clear");
	for(i = 0; i < 17; i++)
		printf("\n");
	printf("w. Go up\n");
	printf("s. Go down\n");
	printf("a. Go left\n");
	printf("d. Go right\n");
	printf("p. Pause game\n");
	printf("e. Finish game\n");
	printf("\nScore: %d\t\tMovements left: %d\n\n", *score, *Moves);
	for(i = 0; i < 28; i++){
		for(j = 0; j < 28; j++){
			printf("%d", G[i][j]);
			printf("%d", G[i][j]);
		}
		printf("\n");
	}
	Sleep(100);
	return 1;
}

int main(){
	int N = 28;
	int **Grid, **t;
	int i, j;
	Grid = malloc(sizeof(int*)*N);
	t = malloc(sizeof(int*)*N);
	for(i = 0; i < N; i++){
		Grid[i] = malloc(sizeof(int)*N);
		t[i] = malloc(sizeof(int)*N);
	}
	
	for(i = 0; i < N; i++)
		for(j = 0; j < N ; j++){
			Grid[i][j] = 0;
			t[i][j] = -1;
		}
	
	int score = 0, Moves = 60;
	int Direction = 1, Pos[2];
	Pos[0] = 13;
	Pos[1] = 13;
	
	int b = 1;
	while(b){
		i = (int)((float)rand()/(float)RAND_MAX*27);
		j = (int)((float)rand()/(float)RAND_MAX*27);
		if(Grid[i][j] == 0){
			Grid[i][j] = 2;
			b = 0;
		}
	}
	
	while(Game(Grid, t, &score, Pos, &Direction, &Moves));
	
	printf("\nGame Over\n\nYour score: %d", score);
	
	for(i = 0; i < N; i++)
		free(t[i]);
	free(Grid);
	for(i = 0; i < N; i++)
		free(t[i]);
	free(Grid);
	return 0;
}