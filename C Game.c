#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <stdlib.h>

int rep = 40;
struct creature *cre;
struct room *rooms;
struct creature* PC; //pointer to PC

struct creature{
    int name;
    int location;
    int type;
};
struct room{
    int name;
    int creatures;
    int creNameIndex[10]; //stores the index from cre of the creatures in the room
    int direc[4];
    int cleanliness;
};

bool moveCheck(int loc, int newLoc){ //This checks if a creature/PC can move or not
    if(newLoc == -1){
        return false;
    }else if(rooms[newLoc].creatures >= 10){
        return false;
    }else {
        for(int i = 0; i < 4; i++){
            if(rooms[loc].direc[i] == newLoc){ return true;}
        }
    }
    printf("Error\n");
    return false;    
}

void cleanlinessCheck(int type, int Loc){ //This allows creatures that are forced to move clean rooms with out reaction
    if((type == 1) & (rooms[Loc].cleanliness == 2)){
        rooms[Loc].cleanliness--;
    }else if((type == 2) & (rooms[Loc].cleanliness == 0)){
        rooms[Loc].cleanliness++;
    }
}

void moveprint(int loc, int newloc,char direc,int name){ //prints the direction the PC goes north
    if(moveCheck(loc,newloc)){
        if(name == PC->name){
            switch(direc){
            case'n':
                printf("You go North!\n");
                break;
            case's':
                printf("You go South!\n");
                break;
            case'e':
                printf("You go East!\n");
                break;
            case'w':
                printf("You go West!\n");
                break;
            }
        } else{
            switch(direc){
            case'n':
                printf("Creature %d goes North!\n",name);
                break;
            case's':
                printf("Creature %d goes South!\n",name);
                break;
            case'e':
                printf("Creature %d goes East!\n",name);
                break;
            case'w':
                printf("Creature %d goes West!\n",name);
                break;
            }
        }
    }
}

void mover(int loc,int name,char direc){ //This moves the creature
    //name is the creature name and not always it's index
    int pos,newloc;
    //getting new location
    switch (direc){ //gets the direction the creature moved
    case'n':
        newloc = rooms[loc].direc[0];
        break;
    case's':
        newloc = rooms[loc].direc[1];
        break;
    case'e':
        newloc = rooms[loc].direc[2];
        break;
    case'w':
        newloc = rooms[loc].direc[3];
        break;
    default:
        return;
        break;
    }
    if(!moveCheck(loc, newloc)){
        if(name == PC->name){
            printf("You cannot move that way\n");
        }else{
            printf("Creature can not go that way\n");
        }
        return;
    }else{ //if it can then move creature
        for(int i = 0; i < 10; i++){
            if(cre[rooms[loc].creNameIndex[i]].name == name){
                rooms[newloc].creNameIndex[rooms[newloc].creatures] = rooms[loc].creNameIndex[i]; //copying over the index
                rooms[newloc].creatures++;
                if(name == PC->name){ PC->location = newloc;}
                cre[rooms[loc].creNameIndex[i]].location = newloc; //updating creature location
                pos = i;
                break;
            }
        }
        for(int i = pos; i < 9; i++){ //left shift array after deletion of the creature in old location
            rooms[loc].creNameIndex[i] = rooms[loc].creNameIndex[i+1];
        }
    rooms[loc].creatures--;
    moveprint(loc,newloc,direc,name);
    }
}

void removecre(int name, int loc){ //removing creature from the game
    int pos; //index of creature to be removed
    for(int k = 0; k < rooms[loc].creatures; k++){ //finding postion of element to be deleted
        if(name == cre[rooms[loc].creNameIndex[k]].name){
            pos = k;
        }
    }
    for(int k = pos; k < 9; k++){ //left shift array after deletion of the creature in old location
        rooms[loc].creNameIndex[k] = rooms[loc].creNameIndex[k+1];
    }
    rooms[loc].creatures--;
}

void aiCheck(int loc, bool flag, int index){  //Checks if a AI moves or not
    if(rooms[loc].cleanliness == 1){
        return;
    }
    int type1,name1;
    name1 = cre[index].name;
    type1 = cre[index].type;
    if(((flag == true) & (type1 == 2)) | ((flag == false) & (type1 == 1))){ //true = clean false = dirty
        for(int i = 0; i < 4; i++){
            if(moveCheck(loc,rooms[loc].direc[i])){
                switch (i)
                {
                case(0):
                    mover(loc,name1,'n');
                    cleanlinessCheck(type1,cre[index].location);
                    printf("%d leaves to the North\n",name1);
                    return;
                    break;
                case(1):
                    mover(loc,name1,'s');
                    cleanlinessCheck(type1,cre[index].location);
                    printf("%d leaves to the South\n",name1);
                    return;
                    break;
                case(2):
                    mover(loc,name1,'e');
                    cleanlinessCheck(type1,cre[index].location);
                    printf("%d leaves to the East\n",name1);
                    return;
                    break;
                case(3):
                    mover(loc,name1,'w');
                    cleanlinessCheck(type1,cre[index].location);
                    printf("%d leaves to the West\n",name1);
                    return;
                    break;
                }
            }
        }
       removecre(name1,loc); //if it cant go anywhere it burrows out
       printf("%d leaves the park\n",name1);
       return; 
    }
}
//New and improved reaction
void superReaction(int loc, bool flag, int nameA){ //This makes creatures react, more to being forced to do stuff, less if not
    for(int j = 0; j < 100; j++){
        int type = 0;
        int name = 0;
        if(cre[j].location == loc){
            type = cre[j].type; //evaluates type
            name = cre[j].name;
            if(name != nameA){
                if(type == 1 && flag == true){ //looks at type and cleanliness and prints a reaction
                    rep++;
                    printf("Animal %d licks your face! Your respect is now %d\n",name,rep);                   
                }else if(type == 1 && flag == false){
                    rep--;
                    printf("Animal %d growls at you! Your respect is now %d\n",name,rep);
                    aiCheck(loc,flag,j);                  
                }else if(type == 2 && flag == true){
                    rep--;
                    printf("NPC %d yells at you! Your respect is now %d\n",name,rep);
                    aiCheck(loc,flag,j);               
                }else if(type == 2 && flag == false){
                    rep++;
                    printf("NPC %d smiles at you! Your respect is now %d\n",name,rep);
                                
                }
            }else{
                if(type == 1 && flag == true){ //looks at type and cleanliness and prints a reaction
                    rep+=3;
                    printf("Animal %d licks your face a lot! Your respect is now %d\n",name,rep);                   
                }else if(type == 1 && flag == false){
                    rep-=3;
                    printf("Animal %d growls a lot at you! Your respect is now %d\n",name,rep);
                    aiCheck(loc,flag,j); //it may move                 
                }else if(type == 2 && flag == true){
                    rep-=3;
                    printf("NPC %d yells at you a lot! Your respect is now %d\n",name,rep);
                    aiCheck(loc,flag,j); //it may move              
                }else if(type == 2 && flag == false){
                    rep+=3;
                    printf("NPC %d smiles a lot at you! Your respect is now %d\n",name,rep);             
                }
            }
        }

    }
}

void clean(int loc,int name){ //cleans the room and starts the ai / reaction chain
    if(name == PC->name){ //if pc cleans
        if(rooms[loc].cleanliness == 0){
            printf("This room is already Clean\n");
            return;
        }else {
            rooms[loc].cleanliness--;
        }
        superReaction(loc,true,name);
    }else{ // creature cleaning
        if(rooms[loc].cleanliness == 0){
            printf("This room is already Clean\n");
            return;
        }else {
            rooms[loc].cleanliness--;
        }
        superReaction(loc,true,name); //start of the chain
    }
}

void dirty(int loc,int name){ //dirtys the room and starts the ai / reaction chain
    if(name == PC->name){ //if pc dirtys
        if(rooms[loc].cleanliness == 2){
            printf("This room is already Dirty\n");
            return;
        }else {
            rooms[loc].cleanliness++;
        }
        superReaction(loc,false,name);
    }else{ //if creature dirtys
        if(rooms[loc].cleanliness == 2){
            printf("This room is already Dirty\n");
            return;
        }else {
            rooms[loc].cleanliness++;
        }
        superReaction(loc,false,name);
    }
}

void look(int loc){ //prints description of the room
//Made up of three parts, printing clean value, its neighbors and what is in it

    char cleanval[10]; //string for clean val
    if(rooms[loc].cleanliness == 0){
        strcpy(cleanval, "Clean");
    }else if(rooms[loc].cleanliness == 1){
        strcpy(cleanval, "HalfDirty");
    }else{
        strcpy(cleanval, "Dirty");
    }

    printf("Room %d, %s, neighbors ", rooms[loc].name, cleanval);

    for(int i = 0; i < 4; i++){ //printing neighbors
        if(rooms[loc].direc[i] != -1 && i == 0){
            printf("%d to the north",rooms[loc].direc[i]);
        }
        if(rooms[loc].direc[i] != -1 && i == 1){
            printf(" %d to the south",rooms[loc].direc[i]);
        }
        if(rooms[loc].direc[i] != -1 && i == 2){
            printf(" %d to the east",rooms[loc].direc[i]);
        }
        if(rooms[loc].direc[i] != -1 && i == 3){
            printf(" %d to the west",rooms[loc].direc[i]);
        }
    }

    printf(", contains\n"); //printing what is in the room
    for(int i = 0; i < rooms[loc].creatures; i++){
        switch (cre[rooms[loc].creNameIndex[i]].type){ //name safe
        case 0:
            printf("PC\n");
             break;
        case 1:
            printf("Animal %d\n",cre[rooms[loc].creNameIndex[i]].name);
            break;
        case 2:
            printf("Human %d\n",cre[rooms[loc].creNameIndex[i]].name);
            break;
        }
    }
}

void command(int name, int loc, char input[6]){ //this makes the : actually work
    if(strcmp(input,"clean") == 0){
        clean(loc,name);
    }else if(strcmp(input,"dirty") == 0){
        dirty(loc,name);
    }else if(strlen(input) == 1){
        mover(loc,name,input[0]);
        for(int i = 0; i < 100; i++){ // gets info of creature after it moves
            if(cre[i].name == name){
                cleanlinessCheck(cre[i].type,cre[i].location);
                break; //no need to go over a 100 times
            }
        }
    }
}

static void help(){
    printf("Commands\n");
    printf(" 1. Look - Gives you a description of the room.\n");
    printf(" 2. N,n,S,s,E,e,W,w- are all move commands that move you in a north, south, east and west rooms if possible\n");
    printf(" 3. clean- cleans the room you are in\n");
    printf(" 4. dirty- dirties the room you are in\n");
    printf(" 5. name of creature:move|clean|dirty- makes the creature in that said room do the given action. Move takes the same command as moving your character\n");
}

int main(){
    int numTotRooms, numTotCreature;
    printf("Hello Welcome to Animal Park Cleaner 2000 Your goal is to keep the animals and visitors of the park happy.\n");
    printf("Every time you make a visitor or animal happy they might smile at you or lick your face and raise your reputation.\n");
    printf("You can navigate rooms by entering N,n,W,w,E,e,S or s, you can also use the clean command to clean a room and dirty\n");
    printf("command to dirty a room. The look command will give you all relative info about the room. Animals like clean rooms, while\n");
    printf("visitors(NPC) like dirty spaces. If you make too many NPC or Animals upset you will get fried and kick out of the park. If\n");
    printf("you ever get confused or need help type help command into the terminal to get a quick refresher on the rules. Before we get\n");
    printf("started thought, please build the park, by entering how many rooms are at your park between 1 and 1oo\n");
    scanf("%d",&numTotRooms);
    rooms = malloc(numTotRooms * sizeof(struct room)); //adding array of struts to the heap

    printf("Next Please describe each room, its cleanliness and neighbors given the system bellow\n");
    printf("For example For room 2 you would enter 0 -1 1 -1 2 The first number is the cleanliness with 0 = clean 1 = half-dirty and 2 = dirty\n");
    printf("The next four numbers are the neighbors going from North,South,East,West -1 meaning no neighbor and any other positive integer being the name of the neighbor\n");
    
    for(int i=0; i < numTotRooms; i++){
        int clean,N,S,E,W;
        printf("Please enter the cleanliness and number of neighboring rooms to room %d\n",i);
        scanf("%d %d %d %d %d",&clean,&N,&S,&E,&W);
        rooms[i].name = i; //name of room
        rooms[i].creatures = 0; //setting creatures
        rooms[i].cleanliness = clean;
        rooms[i].direc[0] = N;
        rooms[i].direc[1] = S;
        rooms[i].direc[2] = E;
        rooms[i].direc[3] = W;
    }
    printf("Great! now enter a number between 1 - 100 for the number of creatures\n");
    scanf("%d",&numTotCreature);

    cre = malloc(numTotCreature * sizeof(struct creature)); //adding array of struts to the heap

    printf("For each Creature please describe what it is animal, PC or NPC and what room it is in, using the system described bellow\n");
    printf("For example for creature 1 we would give the input 0 1 saying its a player in room 1, 0 = player 1 = animal and 2 = NPC\n");
   PC = malloc(sizeof(struct creature));
   for(int i = 0; i < numTotCreature; i++){
        int type, location;
        printf("Please enter the type and location for creature %d\n",i);
        scanf("%d %d", &type, &location);
        cre[i].name = i; //name and index match, but we should not assume that
        cre[i].type = type;
        cre[i].location = location;
        if(cre[i].type == 0){*PC = cre[i];} //creates a pointer to the PC;
        rooms[location].creNameIndex[rooms[location].creatures] = i;
        rooms[location].creatures++;
    }

    printf("Please Enter a Command (Hint type help to get help)\n");
    char input[8];
    while(rep < 80 && rep > 0){
        scanf("%s",input);
        for(int i = 0; i < 8; i++){
            input[i] = tolower(input[i]);
        }
        if(strcmp(input,"look") == 0){
            look(PC->location);
        }else if(strcmp(input,"clean") == 0){
            clean(PC->location,PC->name);
        }else if(strcmp(input,"dirty") == 0){
            dirty(PC->location,PC->name);
        }else if(strlen(input) == 1){
            mover(PC->location, PC->name, input[0]);
        }else if(strcmp(input,"help") == 0){
            help();
        }else if(strcmp(input,"exit") == 0){
            printf("Thank you for playing!\n");
            break;
        }else if(strstr(input,":") != NULL){
            char *pt = input;
            char temp[6];
            char t[2];
            int n;
            strcpy(temp,&input[2]);
            strncpy(t,pt+0,1-0);
            sscanf(t,"%d", &n); 
            command(n,PC->location,temp);
        }else{
            printf("Command not recognized please try again!\n");
        }
    }
    if(rep <= 0){
        printf("Oh no you got fired from the park!\n");
    }else if (rep >= 80){
        printf("Congrats you got promoted and a raise!\n");
    }
    free(cre);
    free(rooms);
    free(PC);
}
// look, clean, dirty, n, s, e, w, exit, help
// test inputs one room no exits, one room with all exits to it self, the example input
// valgrind --leak-check=full ./a.out (pi command)
