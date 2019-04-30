#cmake版本
cmake_minimum_required(VERSION 3.4.1)

add_library(SerialPort
        #库的共享的权限
        SHARED
        #文件所在目录
        SerialPort.c)

# Include libraries needed for libserial_port lib
target_link_libraries(SerialPort
        android
        log)