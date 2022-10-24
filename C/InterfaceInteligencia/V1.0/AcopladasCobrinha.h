void ResetGame(int Game_Id, int GamesMove){
	//printf("ResetGame\n");
	score = 0;
	Moves = MaxMoves;
	int i, j;
	for(i = 0; i < Ng; i++)
		for(j = 0; j < Ng; j++){
			G[i][j] = 0;
			t[i][j] = -1;
		}
	i = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
	j = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
	Pos[0] = j;
	Pos[1] = j;
	G[Pos[0]][Pos[1]] = 1;
	t[Pos[0]][Pos[1]] = 0;
	boolean flag = TRUE;
	while(flag){
		i = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
		j = (int)((float)rand()/(float)RAND_MAX*(float)(Ng-1));
		if(G[i][j] == 0){
			G[i][j] = 2;
			flag = FALSE;
		}
	}
	float Percent;
	int casas_decimais;
	if((1000*Game_Id)%GamesMove == 0){
		Percent = (float)(Game_Id%GamesMove);
		casas_decimais= 0;
		while(Percent>1){
			Percent /= 10.0f;
			casas_decimais++;
		}
		Percent *= 100.0f;
		if(GamesMove > 1000){
			if(casas_decimais < 4){
				Percent /= 10.0f;
				if(casas_decimais < 3){
					Percent /= 10.0f;
						if(casas_decimais < 2)
						Percent /= 10.0f;
				}
			}
		} else{
			if(GamesMove > 100){
				if(casas_decimais < 3){
					Percent /= 10.0f;
					if(casas_decimais < 2)
						Percent /= 10.0f;
				}
			} else
				if(GamesMove > 10 && casas_decimais < 2)
					Percent /= 10.0f;
		}
		Display_Percentage(Percent);
	}
}

void PutSurroundings(){
	//printf("PutSurroudings\n");
	int i;
	for(i = 0; i < I[0]; i++)
		L[0][i] = M[i];
}

void GetSurroundings(){
	//printf("getSurroudings");
	int i, j;
	for(i = 0; i < I[0]; i++)
		M[i] = 0;
	//CIMA
	for(i = 1; Pos[0]+i < Ng; i++){
		if(G[Pos[0]+i][Pos[1]] == 1)
			break;
		M[0]++;
	}
	//BAIXO
	for(i = 1; Pos[0]-i >= 0; i++){
		if(G[Pos[0]-i][Pos[1]] == 1)
			break;
		M[1]++;
	}
	//DIREITA
	for(i = 1; Pos[1]+i < Ng; i++){
		if(G[Pos[0]][Pos[1]+i] == 1)
			break;
		M[2]++;
	}
	//ESQUERDA
	for(i = 1; Pos[1]-i >= 0; i++){
		if(G[Pos[0]][Pos[1]-i] == 1)
			break;
		M[3]++;
	}
	//BAIXOESQUERDA
	for(i = 1; Pos[0]+i < Ng && Pos[1]+i < Ng; i++){
		if(G[Pos[0]+i][Pos[1]+i] == 1)
			break;
		M[4]++;
	}
	//BAIXOESQUERDA
	for(i = 1; Pos[0]+i < Ng && Pos[1]-i >= 0; i++){
		if(G[Pos[0]+i][Pos[1]-i] == 1)
			break;
		M[5]++;
	}
	//CIMADIREITA
	for(i = 1; Pos[0]-i >= 0 && Pos[1]+i < Ng; i++){
		if(G[Pos[0]-i][Pos[1]+i] == 1)
			break;
		M[6]++;
	}
	//CIMADIREITA
	for(i = 1; Pos[0]-i >= 0 && Pos[1]-i >= 0; i++){
		if(G[Pos[0]-i][Pos[1]-i] == 1)
			break;
		M[7]++;
	}
	for(i = 0; i < 8; i++)
		M[i] = 1.0f/(1.0f + M[i]);
	
	for(i = 0; i < I[Nc-1]; i++)
		M[16+i] = L[Nc-1][i];
	//BAIXO
	for(i = 1; Pos[0]+i < Ng; i++){
		if(G[Pos[0]+i][Pos[1]] == 1)
			break;
		if(G[Pos[0]+i][Pos[1]] == 2){
			M[8]++;
			return;
		}
	}
	//CIMA
	for(i = 1; Pos[0]-i >= 0; i++){
		if(G[Pos[0]-i][Pos[1]] == 1)
			break;
		if(G[Pos[0]-i][Pos[1]] == 2){
			M[9]++;
			return;
		}
	}
	//DIREITA
	for(i = 1; Pos[1]+i < Ng; i++){
		if(G[Pos[0]][Pos[1]+i] == 1)
			break;
		if(G[Pos[0]][Pos[1]+i] == 2){
			M[10]++;
			return;
		}
	}
	//ESQUERDA
	for(i = 1; Pos[1]-i >= 0; i++){
		if(G[Pos[0]][Pos[1]-i] == 1)
			break;
		if(G[Pos[0]][Pos[1]-i] == 2){
			M[11]++;
			return;
		}
	}
	//BAIXODIREITA
	for(i = 1; Pos[0]+i < Ng && Pos[1]+i < Ng; i++){
		if(G[Pos[0]+i][Pos[1]+i] == 1)
			break;
		if(G[Pos[0]+i][Pos[1]+i] == 2){
			M[12]++;
			return;
		}
	}
	//BAIXOESQUERDA
	for(i = 1; Pos[0]+i < Ng && Pos[1]-i >= 0; i++){
		if(G[Pos[0]+i][Pos[1]-i] == 1)
			break;
		if(G[Pos[0]+i][Pos[1]-i] == 2){
			M[13]++;
			return;
		}
	}
	//CIMADIREITA
	for(i = 1; Pos[0]-i >= 0 && Pos[1]+i < Ng; i++){
		if(G[Pos[0]-i][Pos[1]+i] == 1)
			break;
		if(G[Pos[0]-i][Pos[1]+i] == 2){
			M[14]++;
			return;
		}
	}
	//CIMAESQUERDA
	for(i = 1; Pos[0]-i >= 0 && Pos[1]-i >= 0; i++){
		if(G[Pos[0]-i][Pos[1]-i] == 1)
			break;
		if(G[Pos[0]-i][Pos[1]-i] == 2){
			M[15]++;
			return;
		}
	}
}