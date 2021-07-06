<!-- Variables -->

[resourceId]: 86813

[banner]: https://i.imgur.com/IufJw5D.png
[ratingImage]: https://img.shields.io/badge/dynamic/json.svg?color=brightgreen&label=rating&query=%24.rating.average&suffix=%20%2F%205&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F86813
[buildImage]: https://github.com/M0diis/M0-OnlinePlayersGUI/workflows/Java%20CI%20with%20Gradle/badge.svg
[releaseImage]: https://img.shields.io/github/v/release/M0diis/M0-OnlinePlayersGUI.svg?label=github%20release
[downloadsImage]: https://img.shields.io/badge/dynamic/json.svg?color=brightgreen&label=downloads%20%28spigotmc.org%29&query=%24.downloads&url=https%3A%2F%2Fapi.spiget.org%2Fv2%2Fresources%2F86813
[licenseImage]: https://img.shields.io/github/license/M0diis/M0-OnlinePlayersGUI.svg

<!-- End of variables block -->

![build][buildImage] ![release][releaseImage] ![license][licenseImage]  
![downloads][downloadsImage] ![rating][ratingImage]

![Banner][banner]

## M0-OnlinePlayersGUI
A simple minecraft Online Player GUI plugin.  

Commands and permissions can be found in [/src/main/resources/plugin.yml](https://github.com/M0diis/M0-OnlinePlayersGUI/blob/main/src/main/resources/plugin.yml).

### Building

To build the plugin you need JDK 8 or higher and Gradle installed on your system.

Clone the repository or download the source code from releases.
Run `gradlew shadowjar` to build the jar.
The jar will be created in `/build/libs/` folder.

```
git clone https://github.com/M0diis/M0-OnlinePlayersGUI.git
cd M0-OnlinePlayersGUI
gradlew shadowjar
```

### Configuration

```yaml
M0-OnlinePlayersGUI:
  ReloadMessage: '&2Configuration has been reloaded.'
  NoPermission: '&2You do not have permission to this command.'
  ToggleMessage: '&2You have toggled your visibility in Online GUI.'
  # Whether to hide buttons if there are not enough players
  # to fill up more than one page
  HideButtonsOnSinglePage: false
  # Hook to EssentialsX which will hide players
  # in the GUI if they are vanished
  EssentialsXHook: false
  # Conditional display
  # It will check whether the placeholder applies (is true) for the
  # player and if not, it will exclude the player from the GUI
  # https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders
  Condition:
    Required: false
    Placeholder: "%checkitem_mat:DIRT%"
  PreviousButton:
    Material: ENCHANTED_BOOK
    Name: '&cPrevious Page'
    Lore:
      - '&7Click to open previous page.'
  NextButton:
    Material: ENCHANTED_BOOK
    Name: '&aNext Page'
    Lore:
      - '&7Click to open next page.'
  # Size should be in increments of 9 and not
  # lower than 9 or higher than 54
  # 9 slots is one row
  GUI:
    Size: 18
    Title: '&2Online Players'
    UpdateOn:
      Join: true
      Leave: true
  # Player display configuration
  # Every section supports placeholders
  PlayerDisplay:
    Name: '&6%player_name%'
    Lore:
      - '&2Right click to &ateleport&2 to the player.'
      - '&2Left click to &asay hi&2.'
    Commands:
      Left-Click:
        - '[PLAYER] msg %player_name% Hello, my name is %sender_name%.'
      Right-Click:
        - '[CONSOLE] msg %player_name% Hello! '
  # Custom Items
  # These items will be displayed in the same row as
  # the previous and next buttons are
  CustomItems:
    1:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    2:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    3:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    5:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    7:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    8:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []
    9:
      Material: AIR
      Name: ''
      Lore: []
      Commands:
        CloseOnLeftClick: false
        Left-Click: []
        CloseOnRightClick: false
        Right-Click: []

```

### Spigot

You can find the resource on spigot:  
https://www.spigotmc.org/resources/m0-onlineplayersgui.86813

### Dev Builds

You can find all the developer builds [here](https://github.com/M0diis/M0-OnlinePlayersGUI/actions) under the artifacts section.

### Links:

- [Spigot](https://www.spigotmc.org/)
- [PaperMC](https://papermc.io/)
