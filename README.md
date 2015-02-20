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

# Technical Overview
For the Twiddle app, we used a standard Java back-end with front-end layouts defined through XML because this is the standard setup for building Android apps and using the Android API.

For our cloud platform/database, we chose Parse because most of the team was familiar with it already and its documentation is clear and easy to search through.

In our current vertical prototype, we are using the Parse API for database management and the Google Maps API for retrieving/searching for locations.

We are using a Git repository for version control because it allows our team to easily work independently when needed and track our code revisions on GitHub. 

Our Twiddle repository can be found at: https://github.com/calvwng/twiddle

In addition, we are using Facebook's authentication for our login/user system.

# Vertical Prototype
Our Milestone 2 vertical prototype is available for download in apk form at: https://drive.google.com/file/d/0B9gofKm21tHgZnRsazVzTWdSekE/view?usp=sharing

(Make sure your Android device is set to allow installation of apks with unknown sources.)

Once we decide on a logo and add more functionality, we will create a beta store page for releasing updates.
Our Milestone 2 vertical prototype app includes the ability to log in via Facebook and use our navigation drawer, which allows the user to log out or navigate between an Adventures feed (implemented), a Favorite adventures list (placeholder), and a Settings menu (placeholder). Adventures displayed on the Adventures feed can be liked or unliked by the user, and the logged in user can create a new Adventure with the floating button on the feed (currently the new Adventure won't appear until the app is restarted). For this milestone we just wanted to show our successful calls to the Facebook, Parse, and Google Maps APIs, so the location input for new Adventures is simply based on coordinates for now. Images to be captured/uploaded for an Adventure are not yet used within the app, but this will likely be the next task we address.
