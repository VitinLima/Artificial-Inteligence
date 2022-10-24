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

void Display_Game_Over(){

}

int main(){
	Inicializa_Game();
	int i, j, D = 1;
	boolean stop = FALSE;
	char c;
	while(Game(D) != 0 && !stop){
		system("@cls||clear");
		printf("x: %d y: %d\nxc: %d yc: %d\nMoves Left: %d\n\n", x[0], y[0], xc, yc, Moves);
		for(i = 0; i < Ng; i++){
			for(j = 0; j < Ng; j++)
				printf("%d%d", Map[i][j], Map[i][j]);
			printf("\n");
		}
		Sleep(600);
		
		if(kbhit()){
			c = getch();
			switch(c){
				case 'w':
					D = 4;
					break;
				case 's':
					D = 3;
					break;
				case 'd':
					D = 1;
					break;
				case 'a':
					D = 2;
					break;
				case 'p':
					stop = TRUE;
					break;
			}
		}
	}
	Display_Game_Over();
	
	return 0;
}