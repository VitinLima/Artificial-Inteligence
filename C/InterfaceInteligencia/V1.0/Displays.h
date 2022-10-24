#include <windows.h>

void PrintaSistema(){
	//printf("\nPrintaSistema\n");
	int i, j, k;
	
	for(i = 0; i < Nc-1; i++){
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
	
	for(i = 0; i < Nc; i++){
		printf("L %d ", i);
		for(j = 0; j < I[i]; j++)
			printf("%.2f ", L[i][j]);
		printf("\n");
	}
}

void PrintWCBC(){
	//printf("\nPrintWCBC\n");
	int i, j, k;
	for(i = 0; i < Nc-1; i++){
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

void PrintLayers(){
	//printf("\nPrintLayers\n");
	int i, j;
	for(i = 0; i < Nc; i++){
		for(j = 0; j < I[i]; j++)
			printf("%.2f ", L[i][j]);
		printf("\n");
	}
}

void Display_Percentage(float Percent){
	system("@cls||clear");
	printf("Percentage until next visualization: %.1f%%\n", Percent);
}

void PrintInformation(int Game_Id, int flag2, int D, float RandomBehaviour){
	system("@cls||clear");
	printf("PrintInformation\n");
	int i, j;
	printf("\n\nGame number %d\t\tRandom Behaviour %.3f\n", Game_Id, RandomBehaviour);
	printf("\nScore: %d\t\tMovements left: %d\t\tScored so far: %d\n\n", score, Moves, flag2);
	for(i = 0; i < Ng; i++){
		for(j = 0; j < Ng; j++){
			printf("%d", G[i][j]);
		printf("%d", G[i][j]);
		}
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
	printf("Arquive to load:\n\n");
	scanf("%s", Name);
	*A = fopen(Name, "r");
	while(*A == NULL){
		fclose(*A);
		system("@cls||clear");
		printf("Arquive Not Found\n\n");
		printf("Arquive to load:\n\n");
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
	I[0] = 20;
	I[Nc-1] = 4;
}

void Display_Error_In_Loading(){
	//printf("Display_Error_In_Loading\n");
	int i;
	system("@cls||clear");
	printf("An Error Occured During WB Initialization\n\nReturning\n\n");
	printf("Liberating Cobrinha\n\n");
	for(i = 0; i < Ng; i++){
		free(G[i]);
		free(t[i]);
	}
	free(G);
	free(t);
	
	printf("Code is closing\n\nReturning 0! :D\n\n");
	}

boolean Display_Initialize_Simulation(int *BatchSize, int *GamesMove){
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
	
	system("@cls||clear");
	int opt;
	printf("Utilize forced random behaviour?\n\n1. TRUE\n2. FALSE\n");
	scanf("%d", &opt);
	if(opt == 1)
		return TRUE;
	else
		return FALSE;
}

void MontaJogo(){
	//printf("Montajogo\n");
	system("@cls||clear");
	printf("Digite o tamanho do mapa:\n\n");
	scanf("%d", &Ng);
	system("@cls||clear");
	printf("Digite o numero maximo de movimentos:\n\n");
	scanf("%d", &MaxMoves);
}