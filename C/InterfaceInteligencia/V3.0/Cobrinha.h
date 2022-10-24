void Check_Display_Info(int Game_Id, int Id_Game_Display, float RandomBehaviour, int flag2){
	//printf("Check Display Info\n");
	if((100*Game_Id)%Id_Game_Display == 0){
		float Percent = (float)Game_Id/(float)Id_Game_Display;
		Percent *= 100.0f;
		while(Percent > 100.0f)
			Percent -= 100.0f;
		Display_Percentage(Percent, RandomBehaviour, flag2);
		
		
		/*int casas_decimais;
		
		Percent = (float)(Game_Id%Id_Game_Display);
		casas_decimais= 0;
		while(Percent>1){
			Percent /= 10.0f;
			casas_decimais++;
		}
		Percent *= 100.0f;
		if(Id_Game_Display > 1000){
			if(casas_decimais < 4){
				Percent /= 10.0f;
				if(casas_decimais < 3){
					Percent /= 10.0f;
						if(casas_decimais < 2)
						Percent /= 10.0f;
				}
			}
		} else{
			if(Id_Game_Display > 100){
				if(casas_decimais < 3){
					Percent /= 10.0f;
					if(casas_decimais < 2)
						Percent /= 10.0f;
				}
			} else
				if(Id_Game_Display > 10 && casas_decimais < 2)
					Percent /= 10.0f;
		}*/
	}
}

void New_Food(){
	//printf("New Food\n");
	int i;
	xc = (int)((float)rand()/(float)RAND_MAX*(float)(Ngx-1));
	yc = (int)((float)rand()/(float)RAND_MAX*(float)(Ngy-1));
	for(i = 0; i < Tamanho; i++)
		if(xc == x[i] && yc == y[i])
			New_Food();
	for(i = 0; i < Np; i++)
		if(xc == xp[i] && yc== yp[i])
			New_Food();
}

void Reset_Game(int Game_Id, int Id_Game_Display, float RandomBehaviour, int flag2){
	//printf("ResetGame\n");
	
	if(Score > Max_Score)
		Max_Score = Score;
	Score = 0;
	Moves = Max_Moves;
	Tamanho = Tamanho_Inicial;
	
	int i;
	
	for(i = 1; i < Ngx*Ngy; i++){
		x[i] = -1;
		y[i] = -1;
	}
	
	x[0] = (int)((float)rand()/(float)RAND_MAX*(float)(Ngx-1));
	y[0] = (int)((float)rand()/(float)RAND_MAX*(float)(Ngy-1));
	
	New_Food();
	
	Update_Map();
	
	Check_Display_Info(Game_Id, Id_Game_Display, RandomBehaviour, flag2);
}

boolean Check_Food(){
	//printf("Check Food\n");
	if(x[0] == xc && y[0] == yc){
		Score++;
		Tamanho++;
		New_Food();
		return TRUE;
	}
	return FALSE;
}

boolean Check_Game_Over(){
	//printf("Check Game Over\n");
	int i;
	if(Board_Walls_Kill){
		if(x[0] == Ngx || y[0] == Ngy || x[0] == -1 || y[0] == -1){
			if(x[0] == Ngx)	x[0] --;
			else	if(x[0] == -1)	x[0] ++;
			if(y[0] == Ngy)	y[0] --;
			else	if(y[0] == -1)	y[0] ++;
			return TRUE;
		}
	}
	else{
		if(x[0] == Ngx)	x[0] -= Ngx;
		else	if(x[0] == -1)	x[0] += Ngx;
		if(y[0] == Ngy)	y[0] -= Ngy;
		else	if(y[0] == -1)	y[0] += Ngy;
	}
	for(i = 1; i < Tamanho; i++)
		if(x[0] == x[i] && y[0] == y[i])
			return TRUE;
	for(i = 0; i < Np; i++)
		if(x[0] == xp[i] && y[0] == yp[i])
			return TRUE;
	if(Moves == 0)
		return TRUE;
	return FALSE;
}

void Update_Map(){
	//printf("Update Map\n");
	int i, j;
	for(i = 0; i < Ngy; i++)
		for(j = 0; j < Ngx; j++)
			Map[i][j] = 0;
	for(i = 0; i < Tamanho; i++)
		if(x[i] != -1 && y[i] != -1)
			Map[y[i]][x[i]] = 1;
	
	for(i = 0; i < Np ; i++)
		Map[yp[i]][xp[i]] = 1;
	Map[yc][xc] = 2;
}

int Game(int Direction){
	//printf("Game\n");
	int i;
	for(i = 1; i < Tamanho; i++){
		x[Tamanho-i] = x[Tamanho-i-1];
		y[Tamanho-i] = y[Tamanho-i-1];
	}
	switch(Direction){
		case 1:
			x[0]++;
			break;
		case 2:
			x[0]--;
			break;
		case 3:
			y[0]++;
			break;
		case 4:
			y[0]--;
			break;
	}
	Moves--;
	if(Check_Food()){
		Update_Map();
		Moves = Max_Moves;
		return 1;
	}
	if(Check_Game_Over()){
		Update_Map();
		return 0;
	}
	Update_Map();
	return 2;
}

void DESALOCAGAME(){
	//printf("Desaloca Game\n");
	free(x);
	free(y);
	free(xp);
	free(yp);
}

void Inicializa_Game(){
	//printf("Inicializa_Game\n");
	
	Monta_Game();
	
	x = malloc(sizeof(int)*Ngx*Ngy);
	y = malloc(sizeof(int)*Ngx*Ngy);
	
	int i, j;
	for(i = 1; i < Ngx*Ngy; i++){
		x[i] = -1;
		y[i] = -1;
	}
	
	Map = malloc(sizeof(int*)*Ngy);
	for(i = 0; i < Ngy; i++){
		Map[i] = malloc(sizeof(int)*Ngx);
		for(j = 0; j < Ngx; j++)
			Map[i][j] = 0;
	}
	
	Moves = Max_Moves;
	Tamanho = Tamanho_Inicial;
	Score = 0;
	
	x[0] = (int)((float)rand()/(float)RAND_MAX*(float)(Ngx-1));
	y[0] = (int)((float)rand()/(float)RAND_MAX*(float)(Ngy-1));
	
	New_Food();
}