# GUARANA ANDROID EVALUATION #

This test consists in building a simple application that will allow users
to find currently open coffee shops around them.

There is no account creation needed in this project. You have 5 hours maximum to complete the project.

All the elements provided below are sufficient and no answers will be given during this test by Guarana’s team. If you are facing a technical choice, you will have to make your own decision keeping in mind:

## Screens

The application contains 4 screens:

![screens.png](http://guarana-research.com/Developer_test/screens.png)

**1. Splash screen**

Simple screen implementation.

![Splash Screen.png](http://guarana-research.com/Developer_test/Splash%20Screen.png)

**2. Home**

A navigation bar shows the logo of Guarana and a heart icon on the right side, allowing the user to go to a favourites list view.

Right below, a map view will be showing all the coffee shops currently open using Foursquare API. The coffee shop location will be marked with a red rounded coffee mug icon. If the user of the application marks a place as a favorite, the icon would become green.

When touching a pin a detail view will open.

At the bottom of the view, a view will display the same locations but as a list that is scrollable (bottom sheet for example). When taping on a location, a detail view of the place will open.

![Home View.png](http://guarana-research.com/Developer_test/Home%20View.png)


**3. Favorites View**

The user should be able to view places saved as favourite. This is just a list with no further actions.

![Favorite View.png](http://guarana-research.com/Developer_test/Favorite%20View.png)

**4. Details**

A detail view shows the details about the place chosen.

A large image will be used at the top of the screen. A telephone icon will prompt the user to call the place. The user should be allowed to tap on the image and see it full screen.

The address will be displayed below the image.

A static map view shows the location centred on the map.

A description below explains the place.

A button at the bottom allows the user to open a web view loading the web link of the place.

![Detail View.png](http://guarana-research.com/Developer_test/Detail%20View.png)

![Image Full Screen.png](http://guarana-research.com/Developer_test/Image%20Full%20Screen.png)


## Technical Requirements

To be able to proceed, you will need the following software:

* Android Studio with Android Lollipop SDK
* Git (BitBucket)
* Optional: Sketch (30 days free trial available)

As mentioned, the Foursquare API must be used to search places. Below you can find the credentials to request data from them.

**Documentation**  
For Google Places API, it should be used the "Nearby Search requests", available on the link https://developers.google.com/places/web-service/search. Although it's deprecated, it's enough for this purpose.

Google Places API Key:
AIzaSyDeoYwCj2qHPUO2G2hnUc67xtG6xs-9I6o

## Assets

By cloning this repository, you have access to all the assets:

* Icons.zip - all the assets as PNG images
* Screens.zip - screenshots of all screens. They are based on iOS interface but you can adapt them to Android's equivalents.

*A Sketch file is also supplied so you can get more information about assets locations on the screen (also available in this repository).*
http://guarana-research.com/Developer_test/Developer%20test.sketch

## Evaluation

The purpose of this test is to evaluation your proficiency on Android.

These are some aspects that will be evaluated, among others:

* General quality of the code
* Respect of the project instructions
* Time used to complete the evaluation
* Network layer implementation
* Data persistence
* Accordance to proposed interface
* Use of external libraries

## Delivering Your Solution

You should deliver the results through BitBucket, push them to the repository we will provide at the beginning of the test.
Please do not make any pull requests.

Remember that you are applying for a selection process, and the challenge is very open and vague for a reason. We want to see your full potential. Implement things that may surprise us, and if you want, describe what those things are and why they should have be recognised. Sell yourself ;)

## Property of this test

Please note that your test and its instructions are purely condidential. The code produced during your test cannot be made public and shall remain the property of Guarana.  

