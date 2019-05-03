
#This is act as the main file for the system.
#It is a bluetooth server that also triggers the collection of data
#It also calculates the distance between the recent squat vs the good and the bad squat averages
#It then sends the data back to the android app saying whether it be good or bad

from bluetooth import *
import subprocess
import sys
import threading
from bno055_simpletest import Bexample
from classify import compute
import numpy
import math

#This functions opens up goodsquat.txt to find the center point among the good squats data
def goodSquat():
    f = open("goodsquats.txt",'r+')
    data = f.read().split(',')
    return(float(data[0].strip('(')),float(data[1]),float(data[2].strip(')\n')))\

#This functions opens up goodsquat.txt to find the center point among the bad squats data
def badSquat():
    f = open("badsquats.txt","r+")
    data = f.read().split(',')
    return(float(data[0].strip('(')),float(data[1]),float(data[2].strip(')\n')))

#This function starts the bluetooth server and listens for input from the android app.
def randomfunction():
    #Bluetooth connection
    cmd = 'sudo hciconfig hci0 piscan'
    subprocess.check_output(cmd,shell = True)


    server_sock=BluetoothSocket( RFCOMM )
    server_sock.bind(("",PORT_ANY))
    server_sock.listen(1)

    port = server_sock.getsockname()[1]
    uuid = "00012412-0000-1000-8000-0080ABCD1234"


    advertise_service( server_sock, "SampleServer",
                       service_id = uuid,
                       service_classes = [ uuid, SERIAL_PORT_CLASS ],
                       profiles = [ SERIAL_PORT_PROFILE ], 
    #                   protocols = [ OBEX_UUID ] 
                        )

                       
    print("Waiting for connection on RFCOMM channel %d" % port)

    client_sock, client_info = server_sock.accept()
    print("Accepted connection from ", client_info)

    cmd = 'sudo hciconfig hciX noscan'
    subprocess.check_output(cmd,shell = True)
    proc = None

    #Once connected listening for incoming messages
    try:
        while True:
            data = client_sock.recv(1024)
            print("received [%s]" % data)
            
            timestamp1 = 0
            timestamp2 = 0

            data = data.decode("utf-8")
            print(data)
            # If s is sent from bluetooth then it starts bno055_simpletest in a new thread
            if "s" in data:
                message = "Squat Status"
                client_sock.sendall(message.encode())
                if ',' in data:
                    timestamp1 = data.split(',')[0]
                    print("getting timestamp")

                print("TimeStamp is ",timestamp1)
                try:
                    #New thread start
                    proc = Bexample()
                    proc.start()
                    print("Data Collection Started")
                except:
                    print("Error ",sys.exc_info())
            #If P recieved in the message then the data collection stops and the distance for that certain
            #and calculates the squat distances.   
            if ("p" in data):
                if "," in data:
                    timestamp2 = data.split(',')[0]
                print("Timestamp is ",timestamp2)

                try:
                    proc.kill()
                    print("Data Collection Stopped")

                
                    #Takes the suqat average and compares it to the averages of the good and bad squats
                    good = numpy.array(goodSquat())
                    bad = numpy.array(badSquat())
                    current = numpy.array(compute())
                
                    goodDist = numpy.absolute(numpy.linalg.norm(current-good))
                    badDist = numpy.absolute(numpy.linalg.norm(current-bad))

                    #The lower the distance the closer it is to the good or bad
                    print("good ",goodDist," Bad ", badDist)
                    if math.floor(goodDist) == math.floor(badDist):
                        message = "Bad Squat"
                    if goodDist < badDist:
                        message = "Good Squat"
                    elif badDist < goodDist:
                        message = "Bad Squat"
    
                    client_sock.sendall(message.encode())
                except:
                    print("Ooops something went wrong try again", sys.exc_info())
    except IOError:
        print("disconnected")
        client_sock.close()
        server_sock.close()
        randomfunction()

randomfunction()




