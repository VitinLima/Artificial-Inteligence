#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <conio.h>
#include <windows.h>
#include "Funcoes.h"
#include "GlobalVariables.h"
#include "Displays.h"
#include "Cobrinha.h"
#include "BackPropagation.h"
#include "Integrado.h"

int main(){
	srand(time(0));
	Inicializa_Game();
	if(Inicializa_BackPropagation()){
		Display_Error_In_Loading();
		DESALOCAGAME();
		return 0;
	}
	
	int BatchSize, GamesMove;
	boolean Convergence = FALSE, Convergence_Parameter;
	boolean RandomBehaviourFlag = Display_Initialize_Simulation(&BatchSize, &GamesMove, &Convergence_Parameter);
	
	int i, j, k;
	
	int Choice1, Choice2, flag1, flag2 = 0, flag3 = 0, Game_Id = 0;
	
	float temp1, temp2;
	
	float RandomBehaviour = 0.0f, DeltaRandomBehaviour = 0.0f, Decision[4], z;
	
	//PrintaSistema();
	//PrintWCBC();
	
	printf("\nStarting game\n");
	while(!kbhit() && !Convergence){
		if(Game_Id > 1000000)
			Game_Id = 0;
		GetSurroundings();
		Processa();
		
		Choice1 = 0;
		temp1 = 0;
		for(i = 0; i < 4; i++)
			if(L[Nc-1][i] > temp1){
				Choice1 = i+1;
				temp1 = L[Nc-1][i];
			}
		if(RandomBehaviourFlag)
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
		
		if(Game_Id%GamesMove == 0 && Game_Id > 0){
			PrintInformation(Game_Id, flag2, Choice2, RandomBehaviourFlag, RandomBehaviour);
			Sleep(100);
		}
		
		flag1 = Game(Choice2);
		if(flag1 == 2){
			//flag3++;
			//BackPropagation(FALSE, 1.0f/(float)Max_Moves, Choice2, BatchSize);
			//if(Choice1==Choice2 && RandomBehaviour < 100)
			//	RandomBehaviour += DeltaRandomBehaviour/(float)Max_Moves;
		} else{
			if(flag1 == 0){
				flag3++;
				BackPropagation(FALSE, 1.0f, Choice2, BatchSize);
				Game_Id++;
				Reset_Game(Game_Id, GamesMove, RandomBehaviourFlag, RandomBehaviour, flag2);
				if(RandomBehaviourFlag && Choice1==Choice2 && RandomBehaviour < 100)
					RandomBehaviour += DeltaRandomBehaviour;
			} else{
				if(flag1 == 1){
					flag2++;
					flag3++;
					BackPropagation(TRUE, 1.0f, Choice2, BatchSize);
					if(RandomBehaviourFlag && Choice1==Choice2 && RandomBehaviour >= -10.0f)
						RandomBehaviour -= DeltaRandomBehaviour;
				}
			}
		}
		if(flag3 == BatchSize){
			Corrige();
			flag3 = 0;
			ZeraWCBC();
		}
		if(RandomBehaviour < -10.0f && Convergence_Parameter){
			Convergence = TRUE;
			Display_Convergence_Achieved();
		}
	}
	char c;
	if(!Convergence)
		c = getch();
	
	saveWB();
	
	DESALOCAWBWCBCLLCIM();
	DESALOCAGAME();
	
	printf("Returning 0 successfully! :D\n\n");
	
	return 0;
}