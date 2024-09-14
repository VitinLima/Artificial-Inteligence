all: main.out

HomeFolder := $(shell pwd)/..
BuildFolder := $(HomeFolder)/build

KBManager.o: KBManager.c KBManager.h
	gcc -c KBManager.c -o KBManager.o

main.o: main.c
	gcc -c main.c -o main.o

main.out: main.o KBManager.o
	gcc -o main.out main.o KBManager.o

clean:
	rm *.o *.out

run: main.out
	./main.out