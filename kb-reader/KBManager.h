#ifndef KB_MANAGER_H
#define KB_MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define exit_char 'e'

void* thread_run();
int start_listening();
int is_alive();
int is_dead();
int is_empty();
unsigned int available();
char kbmanager_read();
int is_overflow();
void clear_overflow_flag();

#endif