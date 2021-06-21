# WorkOutTracker

An Android app designed to keep track of common workouts or own custom ones. Users will not need to log in to make use of the app, as they will be able to watch the list of global exercises, the Guest Mode. The list of global exercises can only be edited by an admin user. 
Stopwatch and timer features are available no login needed, and it can be minimized.
Users that have signed in will be able to access the same as guest ones. Also, for them, there will be available a list of custom exercises in which the users will be able to add new ones just for themselves. Those will be uploaded to the cloud on Firebase, and only own exercises will be able to be removed.
Exercises will have a description and picture stored in Firebase. 


## Technologies

Native Android with Java.
Firebase and realtime database for login and storage of workouts.

## Mockup

![Mockup](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/Mockup.png)

## Usage

![Main Window](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/01_main.png)

At the main window we have the logo, and the options to log in and sign up.

![Register](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/02_reg.png)

There is a simple form for signin up, which if successful will redirect us to the next activity.

![Public Workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/03_pworks.png)

Here we have all workouts accessible by everybody. They can only be added by the admin user, as the floating button will only appear if the user is tagged as admin.

![Custom Workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/04_cworks.png)

On the other side, we have custom workouts, which will be added by everyone, but you can only see the ones you have created.

![Adding new workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/05_newex.png)

Adding new exercises is done via this Alert Dialog, which will create a new entry at Firebase, in a different path depending if the workout is public or not.

![Login](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/06_login.png)

The login activity. Using an admin account, will let create more exercises at the public side, though only already tagged as admin users will be able to. All new users are created as non admin as default.
