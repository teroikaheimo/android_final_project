# android_final_project
Simple app that uses open [API]( https://dev.hel.fi/apis/linked-events/) by city of Helsinki.
The app fetches event data and allow the user to filter the data by search words, starting date, ending date, and event places.

Here is a demonstration of the app in use. And no, the _cats on synthesizers in space_ are not served from the API. I had to add demo pictures because I could not find an event with more than one picture and the grading demanded that the app must be able to show all pictures related to the event(Which it can do. Kittys are removed from the final version)
<p align="center">
  <img src="demonstration.gif"></p>

## Grading
In this course you get base grade from doing exercises 2-3(3 = 100%) and grade bump(1-4) by doing this final project to specs. I made my project to get a +3 grade bumb.

### Grade bump +1
 - Application must show splash screen when the application starts. In that screen show your name and your class id for few seconds.
 - User must be able to search events based on event time and place. At least one place is mandatory. Show the data ListView or other view capable of showing such information. Show at least 4 fields of information about the event in the list. Use /event/ call to fetch events.
 - All adapters used must be custom adapters (new class inheriting any adapter-class)

### Grade bump +2
 - User must be able to search for places to filter the search of events. User probably doesn’t know the places beforehand, that’s why this feature is needed. User must be able to select the place to use when searching for events.
 - When user selects the event, they will be shown detailed information about the event. Show only textual fields, that seems to contain sensible, human readable information.
 
 ### Grade bump +3
  - All visible items on screen are fragments or inside fragments. No user interface-components are in directly in activities’ layouts.
  - Make it possible to somehow show the user all pictures related to a selected event.

