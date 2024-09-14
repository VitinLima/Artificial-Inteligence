#include <stdio.h>
#include <stdlib.h>
#include <time.h>
// #include <conio.h>
// #include <windows.h>
#include "defines.h"

int main(){
	srand(time(0));
	int i;
	while(!kbhit())
		if((int)((float)rand()/(float)RAND_MAX*27) == 28){
			printf("Error");
			break;
		}
}