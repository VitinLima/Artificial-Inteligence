all: main.out

# HomeFolder := $(shell pwd)/..
main_folder := $(shell pwd)
kb_listener_folder := $(main_folder)/kb-listener

main.o: main.c
	gcc -c main.c -o main.o -I $(kb_listener_folder)/

AcopladasCobrinha.o: AcopladasCobrinha.c
	gcc -c AcopladasCobrinha.c -o AcopladasCobrinha.o

AcopladasBackPropagation.o: AcopladasBackPropagation.c
	gcc -c AcopladasBackPropagation.c -o AcopladasBackPropagation.o

Cobrinha.o: Cobrinha.c
	gcc -c Cobrinha.c -o Cobrinha.o

functions.o: functions.c
	gcc -c functions.c -o functions.o

BackPropagation.o: BackPropagation.c
	gcc -c BackPropagation.c -o BackPropagation.o

main.out: main.o Cobrinha.o functions.o BackPropagation.o AcopladasCobrinha.o AcopladasBackPropagation.o defines.h
	gcc -o main.out main.o $(kb_listener_folder)/KBManager.o Cobrinha.o functions.o BackPropagation.o AcopladasCobrinha.o AcopladasBackPropagation.o

clean:
	rm *.o *.out

run: main.out
	./main.out