# Twiddle
CPE 409 Twiddle App

# Vision And Scope:

Twiddle is an Android mobile application that enables users to discover entertaining local activities curated by the local communities themselves. Upon launching the app, the user's GPS location will be detected and he/she will be presented a list of nearby activities that have been crowdsourced by that area's local community. The user will be able to browse this list to view, like, and favorite notable activities. Additionally, the user will be able to post new activities to contribute to the local activity listing. Our target audience includes anyone who seeks adventure or entertainment, especially tourists and visitors from out of town. Our application will not be a review tool but more of an opportunity discovery and exploration tool.


# Team:

Christine Pham (Team Member)

Gabriel Silva (Team Member)

Calvin Wong (ScrumMaster)

Michael Wong (Team Member)

# Low-Fidelity Prototype
![](https://cpslo-csc.mybalsamiq.com/projects/treecounting/Twiddle-Browse.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)
![](https://cpslo-csc.mybalsamiq.com/projects/treecounting/Activity-Page.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)
![](https://cpslo-csc.mybalsamiq.com/mockups/2717168.png?key=ac26bbbdb6ed099bbfc48ff75806c48735f53bd9)

# REST API
We will be using the following APIs:

<b>Parse:</b>

Parse is a great platform to use for back-end mobile applications. Because Parse has a native Android SDK library, we eliminate the need to create a Rest API by simplying making SDK calls to the Parse server. Parse also handles push notifications for you, eliminating the need to setup GCM on a in-house back-end API. We are planning to use Parse to store most of the data such as user credentials, each actvitiy submitted, starred activities, etc. 

<b>Google Maps:</b>

Google Maps API will be used to display the location of where each activity is happening. 

<b>Amazon S3:</b>

We are considering using Amazon S3 to store any photos/media for each activity submitted. Parse will capture the URL for the photo and link it to each activity accordingly. Reasons we are considering using Amazon S3 is because it will be a great learning experience to learn how to use another platform and additionally if we need to scale off Parse, our media will already be on Amazon AWS.

# Initial UML Class Diagram
![](https://40.media.tumblr.com/f8330d858d6cb3fc061395fd164f7c1d/tumblr_njp715jAwt1u7eu2po1_500.png)

# Initial Use Case Diagram
![](https://36.media.tumblr.com/219aa792792cf47d220b0d79892bea72/tumblr_njp715jAwt1u7eu2po2_540.png)

