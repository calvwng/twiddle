# Twiddle
CPE 409 Twiddle App

# Vision And Scope:

Twiddle is an Android mobile application that enables users to discover fun and entertaining nearby activities. Upon entering the app, the user's GPS location will be detected and he/she will be presented a list of nearby activities that have been crowdsource by the nearby community. Additionally, the user will also be able to contribute to the community by submitting their ideas in mind. Our target audience includes anyone who is looking for something to do. Our application will not be a review tool but more for discovery and exploring new opportunities.


# Team:

Christine Pham (Team Member)

Gabriel Silva (Team Member)

Calvin Wong (ScrumMaster)

Michael Wong (Team Member)

# Low-Fidelity Prototype
![](https://cpslo-csc.mybalsamiq.com/projects/treecounting/Twiddle-Browse.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)
![](https://cpslo-csc.mybalsamiq.com/mockups/2717168.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)
![](https://cpslo-csc.mybalsamiq.com/projects/treecounting/Activity-Page.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)

# REST API
We will be using the following APIs:

<b>Parse:</b>

Parse is a great platform to use for back-end mobile applications. Because Parse has a native Android SDK library, we eliminate the need to create a Rest API by simplying making SDK calls to the Parse server. Parse also handles push notifications for you, eliminating the need to setup GCM on a in-house back-end API. We are planning to use Parse to store most of the data such as user credentials, each actvitiy submitted, starred activities, etc. 

<b>Google Maps:</b>

Google Maps API will be used to display the location of where each activity is happening. 

<b>Amazon S3:</b>

We are considering using Amazon S3 to store any photos/media for each activity submitted. Parse will capture the URL for the photo and link it to each activity accordingly. Reasons we are considering using Amazon S3 is because it will be a great learning experience to learn how to use another platform and additionally if we need to scale off Parse, our media will already be on Amazon AWS.

