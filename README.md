
<!-- Variables -->

[resourceId]: 94096

[banner]: https://i.imgur.com/TN49lsh.png
[ratingImage]: https://img.shields.io/badge/dynamic/json.svg?color=brightgreen&label=rating&query=%24.rating.average&suffix=%20%2F%205&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F99128
[buildImage]: https://github.com/M0diis/VenturaCalendar/actions/workflows/gradle.yml/badge.svg
[downloadsImage]: https://img.shields.io/badge/dynamic/json.svg?color=brightgreen&label=downloads%20%28spigotmc.org%29&query=%24.downloads&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F99128
[updatedImage]: https://badges.pufler.dev/updated/M0diis/VenturaCalendar
[licenseImage]: https://img.shields.io/github/license/M0diis/VenturaCalendar.svg

<!-- End of variables block -->

![Banner][banner]

![build][buildImage] ![license][licenseImage]  
![downloads][downloadsImage] ![rating][ratingImage]

## VenturaCalendar
A simple calendar plugin for RPG and other servers.

### Development
Building is quite simple.

To build VenturaCalendar, you need JDK 16 or higher and Gradle installed on your system.

Clone the repository or download the source code from releases.  
Run `gradlew shadowjar` to build the jar.  
The jar will be found created in `/build/libs/` folder. 

**Building**
```
git clone https://github.com/M0diis/VenturaCalendar.git
cd VenturaCalendar
gradlew shadowjar
```

### API

You can find the API documentation in the [wiki](https://github.com/M0diis/VenturaCalendar/wiki/API).

### Dev-builds

All the development builds can be found on actions page.
Open the workflow and get the artifact from there.

https://github.com/M0diis/VenturaCalendar/actions

#### Links

- [Spigot Page](https://www.spigotmc.org/resources/venturacalendar-your-own-custom-calendar.94096/)
- [Issues](https://github.com/M0diis/VenturaCalendar/issues)
  - [Bug report](https://github.com/M0diis/VenturaCalendar/issues)
  - [Feature request](https://github.com/M0diis/VenturaCalendar/issues)
- [Pull requests](https://github.com/M0diis/VenturaCalendar/pulls)

##### APIs
- [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)
- [bStats](https://github.com/Bastian/bStats)

