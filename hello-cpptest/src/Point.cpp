/*
 * Point.cpp
 *
 *  Created on: 21 Oct 2018
 *      Author: yafan
 */

#include <iostream>
#include <sstream>
#include "Point.h"

using namespace std;

namespace wfutil {

 std::string Point::toString() {
	cout << "Point::toString" <<endl;
	ostringstream os;
	os << "x=" << _x;
	return os.str();
}

 std::string Point2d::toString() {
	cout << "Point2d::toString" <<endl;
	ostringstream os;
	os << "x=" << _x;
	os << ", y=" << _y;
	return os.str();
}

 std::string Point3d::toString() {
	cout << "Point3d::toString" <<endl;
	ostringstream os;
	os << "x=" << _x;
	os << ", y=" << _y;
	os << ", z=" << _z;
	return os.str();
}

} /* namespace end */
