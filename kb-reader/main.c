#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "KBManager.h"

int main(){
    int running = 1;
    printf("Listening\n");
    pthread_t thread_id;
    pthread_create(&thread_id, NULL, thread_run, NULL);
    while(running){
        while(available()>0){
            char c = kbmanager_read();
            if(c != '\n' && c != '\r'){
                printf("Char received: %c\n", c);
            }
        }
        if(!is_alive() && is_dead()){
            running = 0;
            printf("Exiting\n");
        } else{
            printf("Running\n");
            sleep(1);
        }
    }
    return 0;
}