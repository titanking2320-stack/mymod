VanillaToggle for Minecraft 26.2 (Fabric)

BUILD
  Requirements: JDK 25, internet connection.
  1. Put the Gradle wrapper in this folder (see below) OR install Gradle 9.6+.
  2. Run:   gradlew build        (Windows)   |   ./gradlew build   (Mac/Linux)
  3. Your mod: build/libs/vanillatoggle-1.1-26.2.jar  (NOT the -sources jar)
  4. Put it in .minecraft/mods together with Fabric API 26.2 (Mod Menu optional).

GRADLE WRAPPER
  If there is no gradlew file: generate a project at https://fabricmc.net/develop/template
  for 26.2, copy its gradlew, gradlew.bat and gradle/ folder into this folder.

BEHAVIOUR (keybind default F7)
  Vanilla ON : view bobbing ON,  FOV effects 100% (config value), damage tilt 100%, particles allowed
  Vanilla OFF: view bobbing OFF, FOV effects 0%,   damage tilt 0%,   listed particles suppressed
