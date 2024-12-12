#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <memory>


static char the_day[10] = "";

#define EXECUTE_JOURNEY(date) do \
    { \
        extern int journey##date(int argc, char *argv[]); \
        if(strlen(the_day) > 0) { \
            if(strncmp(#date, the_day, 8) ==0) { \
                printf("-----"#date"-----\n");\
                journey##date(argc, argv);  \
            } \
        }\
        else {\
            printf("-----"#date"-----\n");\
            journey##date(argc, argv) ; \
        } \
    } while(0)



int main(int argc, char *argv[])
{
    
    if(argc > 1 ) {
        if(strlen(argv[1]) == 8) {
            strncpy(the_day, argv[1], 8);
            the_day[8] = '\0';
        }
    } else {
        printf("--launch jouneries--\n");
    }
    EXECUTE_JOURNEY(20181010);
    return 0;
}
