#! /usr/bin/python

import os
import time
import numpy
from operator import itemgetter

#This function reads the sensor data file and outputs the center of all the files
#In this case the center would be a mean of all the accelerometer data in the file
def compute():

    work = os.getcwd()
    filepath = work+ '/sensordata.txt'
    print(filepath)

    f = open(filepath,'r')
    #This opens the goodsquat or badsquat file to compute the centers for the data
    #f2 = open("goodsquats.txt",'a+')

    lines = f.read().split(' ')
    data = []
    tuples = []
    tuples2 = []
    
    x = []
    y = []
    z = []

    #Converts tuple in the text file to real tuples.
    for line in lines:
       # line =  line.rstrip('\n')
        line = line.strip()
        if not line.isdigit():
            if not line == "STOP":
                data.append(line)
    for i in range(0,len(data),3):
        tuples.append(data[i]+data[i+1]+data[i+2])
    #print(tuples)
    

    for t in tuples:
        t = t.replace('(','')
        t = t.replace(')','')
        tupleList = t.split(',')
        try:
            tuple1 = (float(tupleList[0]),float(tupleList[1]),float(tupleList[2]))
            tuples2.append(tuple1)
        except:
            print("")
        #print(tuple1)

    for a,b,c in tuples2:
        x.append(a)
        y.append(b)
        z.append(c)

    #Computes mean for tuples
    mean = tuple(numpy.mean(tuples2,axis=0))
    
    print("X MAX ",max(x))
    print("Y MAX ",max(y))
    print("Z MAX ",max(z))
    print("X MIN ",min(x))
    print("Y MIN ",min(y))
    print("Z MIN ",min(z))
    
    print('mean: ',mean)
    #The averages for the good and bad squat data are written into
    #goodsquat.txt or badsquat.txt
   # f2.write(str(mean))

    return (mean)

#compute()


