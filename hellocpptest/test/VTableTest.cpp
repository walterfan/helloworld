#include "gtest/gtest.h"
#include "gmock/gmock.h"
#include <stdio.h>

#include <string.h>
#include <stdint.h>
#include <iostream>

using namespace std;

class Point {
public:
    virtual ~Point();
    virtual Point& mult(float) = 0;
    float x() const{return _x;}
    virtual float y() const{return 0;}
    virtual float z() const{return 0;}

protected:
    Point(float x=0.0): _x(0) {};
    float _x;
};

class Point2d : public Point{
public:
    Point2d(float x=0.0, float y=0.0) : Point(x), _y(y){};
    virtual ~Point2d();
    virtual Point2d& mult(float);
    virtual float y() const{return _y;}
protected:
    float _y;
};

class Point3d : public Point2d{
public:
    Point3d(float x=0.0, float y=0.0, float z=0.0) : Point2d(x,y), _z(z){};
    virtual ~Point3d();
    virtual Point3d& mult(float);
    virtual float z() const{return _z;}
protected:
    float _z;
};


class Base {
     public:
            virtual void f() { cout << "Base::f" << endl; }
            virtual void g() { cout << "Base::g" << endl; }
            virtual void h() { cout << "Base::h" << endl; }
            virtual ~Base() {}

};

typedef void(*Fun)(void);

class Base1 {
public:
            virtual void f() { cout << "Base1::f" << endl; }
            virtual void g() { cout << "Base1::g" << endl; }
            virtual void h() { cout << "Base1::h" << endl; }
            virtual ~Base1() {}
};

class Base2 {
public:
            virtual void f() { cout << "Base2::f" << endl; }
            virtual void g() { cout << "Base2::g" << endl; }
            virtual void h() { cout << "Base2::h" << endl; }
            virtual ~Base2() {}
};

class Base3 {
public:
            virtual void f() { cout << "Base3::f" << endl; }
            virtual void g() { cout << "Base3::g" << endl; }
            virtual void h() { cout << "Base3::h" << endl; }
            virtual ~Base3() {}
};


class Derive : public Base1, public Base2, public Base3 {
public:
            virtual void f() { cout << "Derive::f" << endl; }
            virtual void g1() { cout << "Derive::g1" << endl; }
};


TEST(VTableTest, testVTableAddr)
{
    Fun pFun = NULL;

    Derive d;
    size_t** pVtab = (size_t**)&d;

    //Base1's vtable
    //pFun = (Fun)*((int*)*(int*)((int*)&d+0)+0);
    pFun = (Fun)pVtab[0][0];
    pFun();

    //pFun = (Fun)*((int*)*(int*)((int*)&d+0)+1);
    pFun = (Fun)pVtab[0][1];
    pFun();

    //pFun = (Fun)*((int*)*(int*)((int*)&d+0)+2);
    pFun = (Fun)pVtab[0][2];
    pFun();

    //Derive's vtable
    //pFun = (Fun)*((int*)*(int*)((int*)&d+0)+3);
    pFun = (Fun)pVtab[0][3];
    pFun();

    //The tail of the vtable
    pFun = (Fun)pVtab[0][4];
    cout<<pFun<<endl;


    //Base2's vtable
    //pFun = (Fun)*((int*)*(int*)((int*)&d+1)+0);
    pFun = (Fun)pVtab[1][0];
    pFun();

    //pFun = (Fun)*((int*)*(int*)((int*)&d+1)+1);
    pFun = (Fun)pVtab[1][1];
    pFun();

    pFun = (Fun)pVtab[1][2];
    pFun();

    //The tail of the vtable
    pFun = (Fun)pVtab[1][3];
    cout<<pFun<<endl;



    //Base3's vtable
    //pFun = (Fun)*((int*)*(int*)((int*)&d+1)+0);
    pFun = (Fun)pVtab[2][0];
    pFun();

    //pFun = (Fun)*((int*)*(int*)((int*)&d+1)+1);
    pFun = (Fun)pVtab[2][1];
    pFun();

    pFun = (Fun)pVtab[2][2];
    pFun();

    //The tail of the vtable
    pFun = (Fun)pVtab[2][3];
    cout<<pFun<<endl;

}

TEST(VTableTest, testFirstFuncAddr)
{
    printf("--journey20140617 Hello Walter on 20140617--\n");

    Base b;

    Fun pFun = NULL;

    cout << "vtable address：" << (size_t*)(&b) << endl;
    cout << "vtable — first func addr：" << (size_t)*(size_t*)(&b) << endl;

    // Invoke the first virtual function
    pFun = (Fun)*((size_t*)*(size_t*)(&b));
    pFun();


}
