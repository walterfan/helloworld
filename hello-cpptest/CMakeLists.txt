project(HelloCppTest)
cmake_minimum_required(VERSION 2.6)

include_directories($ENV{GMOCK_HOME}/include $ENV{GMOCK_HOME}/gtest/include ./h)
link_directories($ENV{GMOCK_HOME}/bld $ENV{GMOCK_HOME}/gtest/bld)
add_definitions(-std=c++11)
set(CMAKE_CXX_FLAGS "${CMAXE_CXX_FLAGS} -Wall -g")

set(sources src/StringUtil.cpp src/Point.cpp src/journey.cpp)
set(test_sources test/StringUtilTest.cpp test/VTableTest.cpp)

add_executable(main src/main.cpp ${sources})
add_executable(unittest test/UnitTest.cpp ${sources} ${test_sources})
target_link_libraries(unittest pthread)
target_link_libraries(unittest gmock)
target_link_libraries(unittest gtest)


