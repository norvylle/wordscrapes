#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <time.h>

#define BALANCED 0
#define LEFT_LEANING 1
#define RIGHT_LEANING 2
#define MAX_SIZE 500000


int current = 0;

void checkInWords(char *genwords[], char *words[], int size){ 
    int windex = 0;
    int i;
    for(i = 0; words[i]; i++){
        while(windex != MAX_SIZE){
            // printf("%s %s\n",words[i],genwords[windex]);
            // printf("%i || %i\n", i,windex);
            if(strcmp(words[i], genwords[windex]) == 0){
                printf("%s \n", genwords[windex]);
                break;
            }

            if(strcmp(words[i], genwords[windex]) > 0){
                windex++;
                continue;
            }

            if(strcmp(words[i], genwords[windex]) < 0){
                break;
            }
        }
        
    }
} 


int cmpstr(const void *p1, const void *p2){
    return (strcmp((char *)p1,(char *)p2));
}

// merge sort
void genmerge(char **a, int lower, int mid, int upper,int size) {
    int i, j, k;
    char **temp;

    temp = (char **) malloc((upper-lower+1)*sizeof(char *));

    for (int i = 0; i < (upper-lower+1); ++i){
        temp[i] = (char *) malloc(size);
    }


    for(i=0, j=lower,k=mid+1; j<=mid || k<=upper; i++){
        // TO DO: implement this
        if(j<= mid && (k == upper + 1 || cmpstr(a[j],a[k]) <= 0)){
            strcpy(temp[i],a[j++]);
        }else{
            strcpy(temp[i],a[k++]);
        }
    }
    for(i=0, j=lower; j<=upper; i++, j++){
        strcpy(a[j],temp[i]); 
    }
    
    for (int i = 0; i < (upper-lower+1); ++i){
        free(temp[i]);
    }
    free(temp);
}

void genmsort(char **a, int lower, int upper) {
    int mid;

    if(upper-lower > 0) {
        mid = (lower+upper)/2;
        genmsort(a, lower, mid);
        genmsort(a, mid+1, upper);
        genmerge(a, lower, mid, upper,sizeof(char)*100);        
    }
}


char** read(int *n){
    FILE *fp;
    int i,j;
    char** items;

    fp = fopen("words.txt","r");

    if(fp == NULL) return NULL;
    else{
        fscanf(fp,"%d\n",n);
        items = (char **) malloc(sizeof(char *)*(*n));
        for(i=0;i<(*n);i++){
            items[i] = (char *) malloc(sizeof(char)*100);
        }
        for(i=0;i<(*n);i++){
            fscanf(fp,"%s\n",items[i]);
            for(j = 0; items[i][j]; j++){
                items[i][j] = tolower(items[i][j]);
            }
        }
    }
    return items;
}

int main(int argc, char **argv[]){
    
    clock_t t1, t2;
    int n;
    int avl_size = 0;
	char **words = read(&n);
    char **genwords;
    char letters[30];
    char toFind[30];
    char temp[30];
    strcpy(letters, argv[1]);
    strcpy(toFind, argv[2]);
    int wordLength = strlen(letters);
    int toFindLength  = strlen(toFind);

    int nopts[toFindLength + 2];
	int option[wordLength + 2][wordLength + 2];
	int start,move,i,candidate,c;

    genwords = (char **) malloc(sizeof(char *)*MAX_SIZE);
    for(i=0;i<MAX_SIZE;i++){
        genwords[i] = (char *) malloc(sizeof(char)*100);
    }

	move=start=0;
	nopts[start]=1;
    t1 = clock();
    

    while (nopts [start] > 0) {
        
        if (nopts [move] > 0) {
            nopts [++ move] = 0;
            if (move == toFindLength + 1) {
                strcpy(temp,"");
                for (i = 1; i <= toFindLength; i ++){
                    char letter[2];
                    letter[0] = letters[option [i][nopts [i]] - 1];
                    letter[1] = '\0';
                    strcat(temp, letter);
                }
                int checker = 1;
                for(i = 0; toFind[i]; i++){ //check if generated word satisfy search criteria
                    if(toFind[i] != '_'){
                        if(toFind[i] == temp[i]){
                            continue;
                        }else{
                            checker = 0;
                            break;
                        }
                    }
                      
                }
                
                if(checker == 1) strcpy(genwords[current++], temp);
                
                if(current == MAX_SIZE){
                   
                    genmsort(genwords,0,MAX_SIZE - 1);
                    checkInWords(genwords, words, n);
                    current = 0;
                }
            } else {
                for (c = wordLength; c >= 1; c --) {
                    for (i = move - 1; i > 0; i --)             //
                        if (c == option [i][nopts[i]])          //  permutation
                        break;                                  //
                    if (!(i > 0))                               //
                        option [move] [++ nopts [move]] = c;
                    }
                }
        } else nopts [-- move] --;     //backtrack
    }

    t2 = clock();
    genmsort(genwords,0,current - 1);
    checkInWords(genwords, words, n);
    
    printf("time elapsed: %0.4f\n", (double)(t2-t1)/(double)CLOCKS_PER_SEC);
    

    for(i = 0; i < n; i++){
        free(words[i]);
    }
    free(words);

    return 0;

}