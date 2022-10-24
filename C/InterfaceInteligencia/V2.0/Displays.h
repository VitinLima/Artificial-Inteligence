void Display_Percentage(float Percent, float RandomBehaviour, int flag2){
	system("@cls||clear");
	printf("Percentage until next visualization: %.0f%%\nScored so far: %d\n\nMax Score: %d\tRandom Behaviour: %.2f\n", 100.0f - Percent, flag2, Max_Score, RandomBehaviour);
}

void Display_Layers(){
	int i, j;
	for(i = 0; i < Nc; i++){
		for(j = 0; j < I[i]; j++)
			printf("%.2f ", L[i][j]);
		printf("\n");
	}
}

void PrintInformation(int Game_Id, int flag2, int D, float RandomBehaviour){
	system("@cls||clear");
	printf("PrintInformation\n");
	printf("\n\n");
	Display_Layers();
	printf("\n\n");
	int i, j;
	printf("\n\nGame number %d\t\tRandom Behaviour %.3f\n", Game_Id, RandomBehaviour);
	printf("\nScore: %d\t\tMax Score: %d\t\tMovements left: %d\t\tScored so far: %d\n\nx: %d\ty: %d\txc: %d\tyc: %d\n\n", Score, Max_Score, Moves, flag2, x[0], y[0], xc, yc);
	for(i = 0; i < Ngy; i++){
		for(j = 0; j < Ngx; j++){
			printf("%d%d", Map[i][j], Map[i][j]);
		}
		/*printf("\t");
		for(j = 0; j < Ngx; j++){
			printf("%d%d", (int)L[0][i*Ngx+j], (int)L[0][i*Ngx+j]);
		}
		printf("\t");
		for(j = 0; j < Ngx; j++){
			printf("%d%d", (int)L[0][Ngx*Ngy + i*Ngx+j], (int)L[0][Ngx*Ngy + i*Ngx+j]);
		}
		printf("\t");
		for(j = 0; j < Ngx; j++){
			printf("%d%d", (int)L[0][2*Ngx*Ngy + i*Ngx+j], (int)L[0][2*Ngx*Ngy + i*Ngx+j]);
		}
		printf("\t");
		for(j = 0; j < Ngx; j++){
			printf("%d%d", (int)L[0][3*Ngx*Ngy + i*Ngx+j], (int)L[0][3*Ngx*Ngy + i*Ngx+j]);
		}*/
		printf("\n");
	}
}

boolean Display_BackPropagation_Initialization(){
	//printf("Display_BackPropagation_Initialization\n");
	system("@cls||clear");
	int opt;
	printf("Initialization of the Back Propagation:\n\n");
	printf("1. Create WB\n2. Load WB\n\n");
	scanf("%d", &opt);
	while(opt != 1 && opt != 2){
		printf("Invalid Option\n");
		printf("1. Create WB\n2. Load WB");
		scanf("%d", &opt);
	}
	if(opt == 2)
		return TRUE;
	return FALSE;
}

void Display_BackPropagation_Creation(){
	//printf("Display_BackPropagation_Creation\n");
	system("@cls||clear");
	printf("Hidden Layers\n\n");
	scanf("%d", &Nc);
	Nc+=2;
}

boolean Display_BackPropagation_Loading(FILE **A){
	//printf("Display_BackPropagation_Loading\n");
	char Name[20];
	system("@cls||clear");
	printf("Archive to load:\n\n");
	scanf("%s", Name);
	*A = fopen(Name, "r");
	while(*A == NULL){
		fclose(*A);
		system("@cls||clear");
		printf("Archive Not Found\n\n");
		printf("Archive to load:\n\n");
		scanf("%s", Name);
		*A = fopen(Name, "r");
	}
	fscanf(*A, "%d", &Nc);
	return FALSE;
}

void AlocaI(boolean opt, FILE *A){
	//printf("AlocaI\n");
	int i;
	I = malloc(sizeof(int)*Nc);
	if(opt){
		for(i = 0; i < Nc; i++)
			fscanf(A, "%d", &I[i]);
		return;
	}
	for(i = 1; i < Nc-1; i++){
		system("@cls||clear");
		printf("Number of Neurons of Layer %d\n\n", i);
		scanf("%d", &I[i]);
		printf("\n");
	}
	I[0] = 8;
	I[Nc-1] = 4;
}

void Display_Error_In_Loading(){
	//printf("Display_Error_In_Loading\n");
	int i;
	system("@cls||clear");
	printf("An Error Occured During WB Initialization\n\nReturning\n\n");
	printf("Liberating Cobrinha\n\n");
	
	printf("Code is closing\n\nReturning 0! :D\n\n");
	}

boolean Display_Initialize_Simulation(int *BatchSize, int *GamesMove, boolean *Convergence_Parameter){
	//printf("Display_Initialize_Simulation\n");
	system("@cls||clear");
	printf("Please, enter the size of each batch for this training\n\n");
	scanf("%d", BatchSize);
	while(*BatchSize < 1){
		printf("\n\nInvalid value\n\nValue must be greater than 1\n\n");
		printf("Please, enter the size of each batch for this training\n\n");
		scanf("%d", BatchSize);
	}
	
	system("@cls||clear");
	printf("Please, enter the number of games between each visualization for this training\n\n");
	scanf("%d", GamesMove);
	while(*GamesMove < 1){
		printf("\n\nInvalid value\n\nValue must be greater than 1\n\n");
		printf("Please, enter the number of games between each visualization for this training\n\n");
		scanf("%d", GamesMove);
	}
	
	int opt;
	system("@cls||clear");
	printf("Stop at Convergence?\n\n1. TRUE\n2. FALSE\n");
	scanf("%d", &opt);
	if(opt == 1)
		*Convergence_Parameter = TRUE;
	else
		*Convergence_Parameter = FALSE;
	system("@cls||clear");
	printf("Utilize forced random behaviour?\n\n1. TRUE\n2. FALSE\n");
	scanf("%d", &opt);
	if(opt == 1)
		return TRUE;
	else
		return FALSE;
}

void Display_Convergence_Achieved(){
	system("@cls||clear");
	printf("Convergence Achieved\n\nExiting Training\n");
}

void Map_Saving(){
	char Name[20];
	system("@cls||clear");
	printf("Digite o nome do arquivo\n\n");
	scanf("%s", Name);
	system("@cls||clear");
	printf("Saving\n");
	FILE *A;
	A = fopen(Name, "w");
	int i;
	fprintf(A, "%d %d %d\n", Ngx, Ngy, Np);
	for(i = 0; i < Np; i++)
		fprintf(A, "%d %d ", xp[i], yp[i]);
	fclose(A);
}

void Map_Creation(){
	system("@cls||clear");
	printf("Digite o tamanho x do mapa:\n\n");
	scanf("%d", &Ngx);
	system("@cls||clear");
	printf("Digite o tamanho y do mapa:\n\n");
	scanf("%d", &Ngy);
	system("@cls||clear");
	printf("Digite o numero de paredes:\n\n");
	scanf("%d", &Np);
	
	xp = malloc(sizeof(int)*Np);
	yp = malloc(sizeof(int)*Np);
	
	int i;
	int opt;
	for(i = 0; i < Np; i++){
		system("@cls||clear");
		printf("Posicao x da parede numero %d:\n\n", i+1);
		scanf("%d", &xp[i]);
		xp[i]--;
		system("@cls||clear");
		printf("Posicao y da parede numero %d:\n\n", i+1);
		scanf("%d", &yp[i]);
		yp[i]--;
	}
	system("@cls||clear");
	printf("Map Created. Would you like to save it?\n\n1. Yes\n2. No\n\n");
	scanf("%d", &opt);
	if(opt == 1)
		Map_Saving();
}

void Map_Loading(){
	int i;
	FILE *A;
	//printf("Display_Map_Loading\n");
	char Name[20];
	system("@cls||clear");
	printf("Archive to load:\n\n");
	scanf("%s", Name);
	A = fopen(Name, "r");
	while(A == NULL){
		fclose(A);
		system("@cls||clear");
		printf("Archive Not Found\n\n");
		printf("Archive to load:\n\n");
		scanf("%s", Name);
		A = fopen(Name, "r");
	}
	fscanf(A, "%d", &Ngx);
	fscanf(A, "%d", &Ngy);
	fscanf(A, "%d", &Np);
	xp = malloc(sizeof(int)*Np);
	yp = malloc(sizeof(int)*Np);
	for(i = 0; i < Np; i++)
		fscanf(A, "%d %d", &xp[i], &yp[i]);
	fclose(A);
}

void Monta_Game(){
	//printf("Montajogo\n");
	int opt;
	
	system("@cls||clear");
	printf("1. Create map\n2. Load map\n\n");
	scanf("%d", &opt);
	opt--;
	if(opt)
		Map_Loading();
	else
		Map_Creation();
	
	system("@cls||clear");
	printf("Habilitar morte na parede?\n\n1. Sim\n2. Nao\n\n");
	scanf("%d", &opt);
	if(opt == 1)
		Board_Walls_Kill = TRUE;
	else
		Board_Walls_Kill = FALSE;
	system("@cls||clear");
	printf("Digite o numero maximo de movimentos:\n\n");
	scanf("%d", &Max_Moves);
	system("@cls||clear");
	printf("Digite o tamanho inicial\n\n");
	scanf("%d", &Tamanho_Inicial);
}