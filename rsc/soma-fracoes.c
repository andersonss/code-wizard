#include <stdio.h>
#include <stdlib.h>

main()
{
      int a,b,c,d;
      int multiplo,resp,frac1,frac2,total;
      scanf("%d/%d + %d/%d",&a,&b,&c,&d);
      if((b==0) || (d==0))
      {
          printf("entrada invalida!");
      }
      else if(b==d)
      {
            resp = (a+c);
            printf("%d/%d\n",resp,b);
    }
        else if(b>d && b%d==0)
        {
            multiplo = b;
            frac1 = a+((b/d)*c);
            printf("%d/%d\n",frac1,b);
        }
        else
        {
            multiplo = b*d;
            frac1 = (multiplo/b)*a;
            frac2 = (multiplo/d)*c;
            total = frac1 + frac2;
            printf("%d/%d\n",total,multiplo);
        }

     }
