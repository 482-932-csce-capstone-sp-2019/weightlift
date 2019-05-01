# file: rfcomm-server.py
# auth: Albert Huang <albert@csail.mit.edu>
# desc: simple demonstration of a server application that uses RFCOMM sockets
#
# $Id: rfcomm-server.py 518 2007-08-10 07:20:07Z albert $

from bluetooth import *
import subprocess
import sys
import threading
from bno055_simpletest import Bexample
from classify import compute
import numpy
import math

def goodSquat():
    f = open("goodsquats.txt",'r+')
    data = f.read().split(',')
    return(float(data[0].strip('(')),float(data[1]),float(data[2].strip(')\n')))

def badSquat():
    f = open("badsquats.txt","r+")
    data = f.read().split(',')
    return(float(data[0].strip('(')),float(data[1]),float(data[2].strip(')\n')))

def randomfunction():

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

    try:
        while True:
            data = client_sock.recv(1024)
            print("received [%s]" % data)
            
            timestamp1 = 0
            timestamp2 = 0

            data = data.decode("utf-8")
            print(data)

            if "s" in data:
                message = "Squat Status"
                client_sock.sendall(message.encode())
                if ',' in data:
                    timestamp1 = data.split(',')[0]
                    print("getting timestamp")

                print("TimeStamp is ",timestamp1)
                try:
                    #Proc = subprocess.Popen(['sudo',"python3", "/home/pi/sensor/examples/bno055_simpletest.py"])
                    #print(Proc)
                    proc = Bexample()
                    proc.start()
                    print("Data Collection Started")
                except:
                    print("Error ",sys.exc_info())
                
            if ("p" in data):
                if "," in data:
                    timestamp2 = data.split(',')[0]
                print("Timestamp is ",timestamp2)

                try:
                    proc.kill()
                   #subprocess.check_call(["sudo","kill",str(Proc.pid)])
                    print("Data Collection Stopped")

                
                    #TRY TO DETERMINE GOOD OR BAD
                    good = numpy.array(goodSquat())
                    bad = numpy.array(badSquat())
                    current = numpy.array(compute())
                
                    goodDist = numpy.absolute(numpy.linalg.norm(current-good))
                    badDist = numpy.absolute(numpy.linalg.norm(current-bad))
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

#print(goodSquat())
#print(badSquat())
randomfunction()




