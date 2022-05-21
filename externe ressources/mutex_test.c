#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


struct example{
    int parameter;
    //pthread_mutex_t lock;
};


void *dosomething(void *args);

int main(){
    /*struct example *ex1 = (struct example *) malloc(sizeof(struct example));
    struct example *ex2 = (struct example *) malloc(sizeof(struct example));

    ex1->parameter = 1;
    ex2->parameter = 1;
    //pthread_mutex_init(&(ex1->lock),NULL);
    //pthread_mutex_init(&(ex2->lock),NULL);

    pthread_t thread1;
    pthread_t thread2;
    //pthread_t thread3;
    //pthread_t thread4;    

    pthread_create(&thread1,NULL,dosomething,(void *) ex1);
    pthread_create(&thread2,NULL,dosomething,(void *) ex1);
    pthread_join(thread1,NULL);
    //pthread_join(thread2,NULL);*/
    char *msg = "RIMOV 055***";
    char buf[20];
    int x = sprintf(buf,"%s",msg);
    sprintf(buf + x, "%s", "hi");
    printf("%s",buf);
    return 0;
}


void *dosomething(void *args){
    struct example *ex = (struct example *) args;
    for(int i = 0; i < 10; i++){
        //pthread_mutex_lock(&(ex->lock));
        if(ex->parameter == 1){
            ex->parameter--;
        } else {
            ex->parameter++;
        }
        printf("%d\n",ex->parameter);
        //pthread_mutex_unlock(&(ex->lock));
    }
    pthread_exit(NULL);
}
