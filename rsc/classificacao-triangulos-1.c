#include <stdio.h>
int main () {
    int comp,larg,altura;
	
    scanf("%d%d%d", &comp,&larg,&altura);   

    if((comp == larg) && ( larg == altura)) {
        printf("equilatero");
    }
    else if((comp != larg) && (comp != altura) && (larg != altura))
    {
        printf("escaleno");
    }
    else {
        printf("isosceles");
    }
}
