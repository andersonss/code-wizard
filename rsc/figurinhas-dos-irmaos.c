#include <stdio.h>
#include <stdlib.h>

int main()

{

    int g=0,d=0,j=0,m=0,n,b,k,i,a[100000],p=0,f;
    a[100000]=0;
    scanf("%d",&n);
    i=n-1;
    for(n=10; n>0;n--)
    {
        k=0;
        scanf("%d", &b);
        if(b==0)
        {
            n++;
        }

        if(b%2==0 && b!=0)
        {
            g++;
            j=j+b;
        }
        if(b%2!=0 && b!=0)
        {
            d++;
            m=m+b;
        }

        while(k<=i)
        {
            if(a[k]==b)
            {
              k=10000000;
                if(b%2==0 && b!=0 )
                {
                    j=j-b;
                }
                else if (b%2!=0 && b!=0)
                {
                    m=m-b;

                }

            }

            k++;
        }



    a[p]=b;
    p++;
    }
    if(j>m)
    {
      f=j;
    }
    else
    {
        f=m;
    }

printf("%d\n%d\n%d",g,d,f);
}


