# weather

This application has been developed during my Master Degree Mobile Application class.

This has been designed for a 5.2" inches Android Device running Marshmallow (Android 6.0).

The weather data is retrieved from https://openweathermap.org/ - you should define your APP_KEY, once you've registered, in the ../weather/utils/AppData.java file.

Some functionalities have been taken from https://github.com/martykan/forecastie project. In particular, the section which shows the world map with rain, wind and temperature situation.

This application exploits an Alarm object in order to receive a constant update and changing the data without specific user request. Furthermore, a notification system has been provided so that the user will be advised of weather changes.

Last but not least, the background of the application will change according to the current weather state. I decided to implement a function which let the app change the wallpaper of the smartphone as soon as its background changes (This works even when the application is not active - visible).

Also have a look at a chosen design strategy: those could not fit very well the Android policy, but I tried to put all my knowledge to make the code the most modular possible (especially in development phase).
