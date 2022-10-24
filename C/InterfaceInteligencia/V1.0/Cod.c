#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "GlobalVariables.h"
#include "Displays.h"
#include "Cobrinha.h"
#include "BackPropagation.h"
#include "AcopladasCobrinha.h"

int main(){
	srand(time(0));
	int Pos[2];
	Inicializa_Game();
	if(Inicializa_BackPropagation()){
		Display_Error_In_Loading();
		return 0;
	}
	
	int BatchSize, GamesMove;
	boolean opt = Display_Initialize_Simulation(&BatchSize, &GamesMove);
	
	int i, j, k;
	
	int Choice1, Choice2, flag1, flag2 = 0, flag3 = 0, Game_Id = 0;
	
	float temp1, temp2;
	
	float RandomBehaviour = 4.0f, DeltaRandomBehaviour = 0.01f, Decision[4], z;
	
	//PrintaSistema();
	//PrintWCBC();
	
	printf("\nStarting game\n");
	while(!kbhit()){
		GetSurroundings();
		PutSurroundings();
		Processa();
		
		Choice1 = 0;
		temp1 = 0;
		for(i = 0; i < 4; i++)
			if(L[Nc-1][i] > temp1){
				Choice1 = i+1;
				temp1 = L[Nc-1][i];
			}
		if(opt)
			z = Sigmoid(RandomBehaviour);
		else
			z = 0.0f;
		for(i = 0; i < 4; i++)
			Decision[i] = z*(float)rand()/(float)RAND_MAX + (1.0f - z)*L[Nc-1][i];
		
		temp1 = 0;
		Choice2 = 0;
		for(i = 0; i < 4; i++)
			if(Decision[i] > temp1){
				temp1 = Decision[i];
				Choice2 = i+1;
			}
		
		if(Game_Id%GamesMove == 0){
			PrintInformation(Game_Id, flag2, Choice2, RandomBehaviour);
			Sleep(300);
		}
		
		flag1 = Game(Choice2);
		if(flag1 == 0){
			flag3++;
			BackPropagation(FALSE, 1.0f/(float)MaxMoves, Choice2, BatchSize);
			if(Choice1==Choice2 && RandomBehaviour < 100)
				RandomBehaviour += DeltaRandomBehaviour/(float)MaxMoves;
		} else{
			if(flag1 == 1){
				flag3++;
				BackPropagation(FALSE, 1.0f, Choice2, BatchSize);
				Game_Id++;
				ResetGame(Game_Id, GamesMove);
				if(Choice1==Choice2 && RandomBehaviour < 100)
					RandomBehaviour += DeltaRandomBehaviour;
			} else{
				if(flag1 == 2){
					flag2++;
					flag3++;
					BackPropagation(TRUE, 1.0f, Choice2, BatchSize);
					if(Choice1==Choice2 && RandomBehaviour > -4)
						RandomBehaviour -= DeltaRandomBehaviour;
				} else{
					if(flag1 == 3){
						Game_Id++;
						ResetGame(Game_Id, GamesMove);
					}
				}
			}
		}
		if(flag3 == BatchSize){
			Corrige();
			flag3 = 0;
			ZeraWCBC();
		}
	}
	char c = getch();
	
	saveWB();
	
	DESALOCAWBWCBCLLCIM();
	
	printf("Returning 0 successfully! :D\n\n");
	
	return 0;
}