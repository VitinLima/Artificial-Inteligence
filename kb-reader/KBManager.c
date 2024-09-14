#include "KBManager.h"

int buffer_empty = 0;
char buffer[256];
unsigned int buffer_writer_pointer = 0;
unsigned int buffer_reader_pointer = 0;
unsigned int buffer_size = 0;
int is_alive_flag = 0;
int is_dead_flag = 0;
int overflow_flag = 0;

void* thread_run(){
    is_alive_flag = 1;
    char c;
    while(is_alive_flag){
        scanf("%c", &c);
        if(c==exit_char){
            is_alive_flag = 0;
        } else{
            buffer[buffer_writer_pointer++] = c;
            buffer_size++;
            if(buffer_writer_pointer==256){
                buffer_writer_pointer = 0;
            }
            if(buffer_size==256){
                overflow_flag = 1;
            }
        }
    }
    is_dead_flag = 1;
}

int start_listening(){
}
int is_alive(){
    return is_alive_flag;
}
int is_dead(){
    return is_dead_flag;
}

int is_empty(){
    return buffer_empty;
}

unsigned int available(){
    return buffer_size;
}

char kbmanager_read(){
    if(buffer_size>0){
        buffer_size--;
        char c = buffer[buffer_reader_pointer++];
        if(buffer_reader_pointer==256){
            buffer_reader_pointer = 0;
        }
        return c;
    }
    return 'e';
}

int is_overflow(){
    return overflow_flag;
}

void clear_overflow_flag(){
    overflow_flag = 0;
}