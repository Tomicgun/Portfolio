//
// Created by thoma on 11/6/2023.
//
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <math.h>


//Globals
int S; //# of cache sets
int s; //# of bits in the address
int E; //# of cache lines
int B; //block size in bytes
int b; //# of bits in an offset
int m; //size of the address in bits
int h; //hit time cycles
int p; //miss penalty in cycles
char policy[3]; //replacement policy
int totCycles = 0;
int misses = 0;
int t;


//The actual Structure of my cache
struct setLine{ //sets are made up of lines
    int uses;
    bool valid;
    int lastUsed;
    int tag;
};

struct cacheSet{ //there are multiple lines per set
    struct setLine* lines;
};

struct cache{ //a cache is made up of sets
    struct cacheSet* sets;
};

struct setLine setLine(int tag){ //setting the line to valid and inp inputting its tag and other bookmarking stuff
    struct setLine line;
    line.uses++;
    line.valid = true;
    line.tag = tag;
    line.lastUsed = totCycles;
    return line;
}

int toInt(const char* key) { //converting hex to int
    int val[64];
    int size = 0;
    for (int i = 0; key[i] != '\0'; i++) {
        if ('0' == key[i]) {
            val[i] = 0;
        } else if ('1' == key[i]) {
            val[i] = 1;
        } else if ('2' == key[i]) {
            val[i] = 2;
        } else if ('3' == key[i]) {
            val[i] = 3;
        } else if ('4' == key[i]) {
            val[i] = 4;
        } else if ('5' == key[i]) {
            val[i] = 5;
        } else if ('6' == key[i]) {
            val[i] = 6;
        } else if ('7' == key[i]) {
            val[i] = 7;
        } else if ('8' == key[i]) {
            val[i] = 8;
        } else if ('9' == key[i]) {
            val[i] = 9;
        } else if ('a' == key[i] || 'A' == key[i]) {
            val[i] = 10;
        } else if ('b' == key[i] || 'B' == key[i]) {
            val[i] = 11;
        } else if ('c' == key[i] || 'C' == key[i]) {
            val[i] = 12;
        } else if ('d' == key[i] || 'D' == key[i]) {
            val[i] = 13;
        } else if ('e' == key[i] || 'E' == key[i]) {
            val[i] = 14;
        } else if ('f' == key[i] || 'F' == key[i]) {
            val[i] = 15;
        }
        size++;
    }
    int sum = 0;
    for (int i = 0; key[i] != '\0'; i++) {
        sum += val[i] * (int) pow(16, size - 1);
        size--;
    }
    return sum;
}

int getTag(int key){ //getting the tag bits of the address
    return (int) ((unsigned int) key >> (s+b));
}

int getSet(int key){ //getting the tag bits of the address (now works for all address no matter the amount of set bits)
    unsigned long long int set = key;
    unsigned long long int mask = (~0x0);
    mask = mask >> (64-s);
    set = set >> (b);
    set &= mask;
    return (int) set;
}

bool contains(struct cacheSet set, int tag){ //check if the set contains the line that is valid and with the tag
    for(int i =0; i<E;i++){
        if(set.lines[i].tag == tag){
            set.lines[i].uses++;
            set.lines[i].lastUsed = totCycles+h; //updates time last accessed
            return true;
        }
    }
    return false;
}

//least frequently used
int LFU(struct cacheSet set){
    int max = 2147483647; //this is int max

    //this loop finds the least number of uses
    for(int i=0; i<E;i++){
        if(set.lines[i].uses < max){
            max = set.lines[i].uses;
        }
    }

    //this loop finds the line with the least number of uses
    for(int i=0;i<E;i++){
        if(set.lines[i].uses == max){
            return i;
        }
    }
    return -1;
}

//least recently used
int LRU(struct cacheSet set){
    int minTime = 2147483647; //this is the max int number

    //this loop finds the oldest time in the set
    for(int i=0; i<E;i++){
        if(set.lines[i].lastUsed < minTime){
            minTime = set.lines[i].lastUsed;
        }
    }

    //this loop finds the line that was least recently accessed
    for(int i=0;i<E;i++){
        if(set.lines[i].lastUsed == minTime){
            return i;
        }
    }
    return -1;
}

//check if the set is full, basically if it comes across a non-valid line it returns false, otherwise returns true.
bool setFull(struct cacheSet set){
    for(int i = 0; i<E;i++){
        if(set.lines[i].valid != true){return false;}
    }
    return true;
}

//check if the set is empty, does the opposite of setFull
bool setEmpty(struct cacheSet set){
    for(int i = 0; i<E;i++){
        if(set.lines[i].valid != false){return false;}
    }
    return true;
}

//checking if hit, if it does hit update bookkeeping
bool hitCheck(struct cacheSet set,int tag){
    if(contains(set,tag)){ //there a set that already contains the tag
        totCycles+=h;
        return true;
    }else if(setFull(set)){ //the set is full, must call ether LFU or LRU
        if(strcmp("LFU",policy)==0){
            int index = LFU(set);
            totCycles+=p+h;
            set.lines[index] = setLine(tag); //updating the replaced line
            misses++;
            return false;
        }else{
            int index = LRU(set);
            totCycles+=p+h;
            set.lines[index] = setLine(tag); //updating the replaced line
            misses++;
            return false;
        }
    }else if(setEmpty(set)){ //if set is empty just set the first line
        totCycles += p+h;
        set.lines[0] = setLine(tag);
        misses++;
        return false;
    }else{
        for(int j = 0;j<E;j++){ //if the set is not empty not full and does not contain the tag, then find empty line and set it
            if(!set.lines[j].valid){
                totCycles+=p+h;
                set.lines[j] = setLine(tag);
                misses++;
                return false;
            }
        }
    }
    return false;
}
int main(){
    int address;
    int set;
    //input
    setbuf(stdout, 0);
    printf("Welcome to Cache simulator, Please enter\n");
    printf("    1. Number of cache sets\n");
    printf("    2. Number of cache lines\n");
    printf("    3. block size in bytes\n");
    printf("    4. size of the address in bits\n");
    printf("    5. Replacement policy express it ether as LFU or LRU\n");
    printf("    5. hit time in cycles\n");
    printf("    6. miss penalty in cycles\n");
    printf("----------------------------------------\n");
    printf("Please enter Number of cache sets\n");
    scanf("%d",&S);
    printf("Please enter Number of cache lines\n");
    scanf("%d",&E);
    printf("Please enter block size in bytes\n");
    scanf("%d",&B);
    printf("Please enter size of the address in bits\n");
    scanf("%d",&m);
    printf("Please enter the replacement policy\n");
    scanf("%s",policy);
    printf("Please enter hit time in cycles\n");
    scanf("%d",&h);
    printf("miss penalty in cycles\n");
    scanf("%d",&p);

    //some math
    s = (int) log2(S); //set bits
    b = (int) log2(B); //off set bits
    t = (int) m-(s+b);

    //local global variables
    char* input = malloc(64*sizeof(char));
    int count = 0;

    //creating the cache
    struct cache cacheSim;
    struct setLine line;

    //allocating space for sets
    cacheSim.sets = (struct cacheSet*)malloc(S * sizeof(struct cacheSet));

    for(int i = 0; i < S; i++){

        //allocating space for lines
        cacheSim.sets[i].lines = (struct setLine*)malloc(E * sizeof(struct setLine));

        //set lines
        for(int j = 0; j < E; j++){
            line.valid = false;
            line.uses = 0;
            line.tag = 0;
            line.lastUsed = totCycles;
            cacheSim.sets[i].lines[j] = line;
        }
    }

    printf("Please enter the address you want to read in Hexadecimal numbers\n");
    //main loop
    while(true){
        scanf("%s",input);
        if(strstr(input,"-1")){
            break;
        }else{
            count++;
            address = toInt(input); //get address as an int
            int tag = getTag(address); //get the tag as an int
            if(s == 0){ //if the # of set bits is zero then just set the set to zero
                set =0;
            }else{
                set = getSet(address); //get set as an int
            }
            //print output
            if(hitCheck(cacheSim.sets[set],tag)){
                printf("%s %s",input,"h\n");
            }else{
                printf("%s %s",input,"m\n");
            }
        }
    }
    //ending bookkeeping
    int rate = (int) (100* ((double) misses/ (double) count));
    printf("Miss rate %d%s",rate,"%");
    printf(" %d",totCycles);
    printf("\n");

    //my freeing statements, I free much more than malloc
    for(int i =0; i<S; i++) {
        free(cacheSim.sets[i].lines);
    }
        free(cacheSim.sets);
        free(input); //dont forget input
}