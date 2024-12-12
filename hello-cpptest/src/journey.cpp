#include "Point.h"
#include <stdio.h>
#include <iostream>

using namespace std;
using namespace wfutil;


typedef string(*NoParamFun)(void);

int journey20181010(int argc, char *argv[])
{


    Point2d* pPoint = new Point2d(3.4, 4.0);
    NoParamFun pFun = NULL;


    //vtable pointer
    size_t** pVtab = *(size_t***)pPoint;

    cout << "vtable address：" << pVtab << endl;

    cout << "vtable — first func addr：" << *((size_t*)pVtab[0])<< endl;

    cout << pPoint->toString() <<endl;
    //pFun = (NoParamFun)pVtab;
	//pFun();

    delete pPoint;
    printf("--journey20181010 done --\n");

    return 0;
}
