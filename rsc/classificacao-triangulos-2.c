#include<stdio.h>
main()
{
   float a,b,c;
   scanf("%f%f%f",&a,&b,&c) ;
   if(a==b && b==c)
   {
       printf("equilatero");
   }
   if(a!=c && b==a)
   {
       printf("isosceles");
   }
   if(a!=b && b==c)
    {

      printf("isosceles");
    }
   if(a==c && b!=c)
    {
      printf("isosceles");
    }
   if(a!=b && b!=c && c!=a)
   {
       printf("escaleno");
   }


}
