# WorkOutTracker

An Android app designed to keep track of common workouts or create own custom exercises, and use a timer to know how long should you keep working out or resting.
Users will need to log in to make use of the app; once logged in normal users will be able to watch the list of global exercises but not edit it, while admins will be able to edit it, that's the "Workout List" screen. The list of custom exercises will be editable by everyone, and every user will have their own. 
Stopwatch and timer features will be accessed from a button on the top right corner.
The exercise and user database is stored in Firebase. An admin account can only be created through access to the database itself.
Users will be able to update their profile, changing their profile picture, and password. Also they will be able to delete their account.
A timer is also available from the top right corner, so the user can track how long have them worked out that exercise, or to track for how long do they rest between workouts.

A [video of the app](https://youtu.be/VSRFnWqmVY8)

## Technologies

Native Android with Java.
Firebase and realtime database for login and storage of workouts. Also Firebase Storage to store workout pictures.

## Mockup

![Mockup](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/Mockup.png)

## Usage

![Main Window](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/01_main.png)

At the main window we have the logo, and the options to log in and sign up.

![Register](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/02_reg.png)

There is a simple form for signin up, which if successful will redirect us to the next activity.

![Public Workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/03_pworks.png)

Here we have all workouts accessible by everybody. They are only able to be edited by the admin user, as the floating button will only appear if the user is tagged as admin and also the dots attached to every of the exercises to remove or edit them.

![Custom Workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/04_cworks.png)

On the other side, we have custom workouts, which will be added by everyone, but you will only see the ones created by yourself.

![Adding new workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/05_newex.png)

Adding new exercises is done via this activity, in which optionally we will add a new image to the exercise. If pressed the button to set the image, external storage access will be requested, and only if accepted you'll be able to add the picture.

![Login](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/06_login.png)

The login activity. Using an admin account, will let create more exercises at the public side, though only already tagged as admin users will be able to. All new users are created as non admin as default.

![Update existing workouts](https://github.com/javier-l0pez/WorkOutTracker/blob/main/Screenshots/07_updatex.png)

If one of the items in the recyclerview is to be edited, the same activity to add new ones is used. While uploading a picture, the user will get a toast message if pressing the button again that will avoid the user from creating a duplicate picture.


## Goals

* [X] Mockup\*
* [X] User database
* [X] Workouts database
* [X] Workouts management
* [X] Workout pictures
* [X] Debug APK
* [X] Video demonstration
* [ ] Landing page

