void GetSurroundings(){
	//printf("getSurroudings");
	int Distance_x, Distance_y;
	int i, j;
	for(i = 0; i < I[0]; i++)
		L[0][i] = 0.0f;
	Distance_x = xc - x[0];
	Distance_y = yc - y[0];
	if(Distance_x*Distance_x > Distance_y*Distance_y){
		if(Distance_x > 0)
			L[0][0] = 1.0f;
		else
			L[0][1] = 1.0f;
	} else{
		if(Distance_y > 0)
			L[0][2] = 1.0f;
		else
			L[0][3] = 1.0f;
	}
	
	//BAIXO
	for(i = 1; y[0]+i < Ngx; i++){
		if(Map[y[0]+i][x[0]] == 1)
			break;
		L[0][4]++;
	}
	//CIMA
	for(i = 1; y[0]-i >= 0; i++){
		if(Map[y[0]-i][x[0]] == 1)
			break;
		L[0][5]++;
	}
	//DIREITA
	for(i = 1; x[0]+i < Ngx; i++){
		if(Map[y[0]][x[0]+i] == 1)
			break;
		L[0][6]++;
	}
	//ESQUERDA
	for(i = 1; x[0]-i >= 0; i++){
		if(Map[y[0]][x[0]-i] == 1)
			break;
		L[0][7]++;
	}
	
	L[0][4] = 1.0f/(1.0f+L[0][4]);
	L[0][5] = 1.0f/(1.0f+L[0][5]);
	L[0][6] = 1.0f/(1.0f+L[0][6]);
	L[0][7] = 1.0f/(1.0f+L[0][7]);
}