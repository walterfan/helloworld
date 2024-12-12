/*
 * Point.h
 *
 *  Created on: 21 Oct 2018
 *      Author: yafan
 */

#ifndef H_POINT_H_
#define H_POINT_H_
#include <string>

namespace wfutil {

class Point{
public:

    virtual std::string toString();
    float x() const{return _x;}
    virtual float y() const{return 0;}
    virtual float z() const{return 0;}
public:
	Point(float x=0.0):_x(x) {};
    virtual ~Point() {};
protected:

    float _x;
};

class Point2d : public Point{
public:
    virtual std::string toString();
    virtual float y() const{return _y;}
public:
    Point2d(float x=0.0, float y=0.0) : Point(x), _y(y){};
    virtual ~Point2d() {};
protected:
    float _y;
};

class Point3d : public Point2d{
public:
    virtual std::string toString();
    virtual float z() const{return _z;}
public:
    Point3d(float x=0.0, float y=0.0, float z=0.0) : Point2d(x,y), _z(z){};
    virtual ~Point3d() {};
protected:
    float _z;
};

} /* namespace end */

#endif /* H_POINT_H_ */
