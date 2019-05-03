Weightlifting Wearable - Identifying Squats
==============
:exclamation: **For Design Documentation, API Specification, and Contribution Guide go to this github WIKI** :exclamation:

<img src="https://i.imgur.com/yTDPUf9.jpg" alt="Phone App" align="right" />

The weightlifting wearable aims to track and correct form for the common squat exercise. This is done by a system of IMU's, Raspberry PI's, and an android application.

It is build exclusively using commercial products and is marketed to be a very low cost wearable with little intrusion into the users experience.

Uses include: squat recognition, self help, training.


*This project was completed as part of Senior Capstone at Texas A&M University.*
Weightlifting wearable designed and implemented by Daniel Esparza, Jose Elizondo, and Jason Zhuang.

If you have any questions about the Weightlifting Wearable, please go to one of
the following places:

* [Wearable Wiki](https://github.com/482-932-csce-capstone-sp-2019/weightlift/wiki)
* [API Specification](https://github.com/482-932-csce-capstone-sp-2019/weightlift/wiki/API-Specification)
* [Contribution Guide]( https://github.com/482-932-csce-capstone-sp-2019/weightlift/wiki/Contribution-Guide)
* [Design Documentation](https://github.com/482-932-csce-capstone-sp-2019/weightlift/wiki/Design-Documentation)

## Features

* Made from cheap commercial parts
* Potential impact on weightlifting communities
* Java implementation is modifiable for many raspberry pi's

## Images

Images of the implementation.

* [App](https://i.imgur.com/yTDPUf9.jpg)
* [Pi and Adafruit IMU](https://i.imgur.com/fNFPYA4.jpg)
* [Prototype](https://i.imgur.com/JvnI4mx.jpg)
* [Waffleboard](https://i.imgur.com/WGIHlH8.png)


## Usage

Weightlifting Wearable is run from the phone applciation. Please ensure the raspberry pi has previously been configured to automatically start the bluetooth server.

Once it has been powered, start the phone application which will try and connect to the configured UUID + MAC address configured in MainActivity.java

### Start

Simply press start, perform your movement and then stop. The display will output your result.


## Development / Contributing

See the [Contribution Guide](https://github.com/482-932-csce-capstone-sp-2019/weightlift/wiki/Contribution-Guide)

### Author

[Daniel Esparza](https://github.com/d-Esparza19) 

### Core Team

* [Daniel Esparza](https://github.com/d-Esparza19) 
* [Jose Elizondo](https://github.com/JElizondo033).
* [Jason Zhuang](https://github.com/JasonZuang).
* Dawson Deere
