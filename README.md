# JarQFarming
Farming plugin for the JarQ minehut server
## How To Use
### Plugin Setup
1. Download the latest release and put it in your plugins folder
2. Reload/Restart the server (The plugin automatically disables. This is normal)
3. Open `<plugins folder>/JarQFarming/config.yml`
4. Fill out any missing values and reload/restart the server
5. Start Farming!
### API [![](https://jitpack.io/v/RedPlayer1/JarQFarming.svg)](https://jitpack.io/#RedPlayer1/JarQFarming)
The [`FarmingAPI.kt`](https://github.com/RedPlayer1/JarQFarming/blob/master/src/main/java/me/redplayer_1/jarqfarming/FarmingAPI.kt) object should contain everything you need.
#### Maven
Add the repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Add the dependency
```xml
<dependency>
    <groupId>com.github.RedPlayer1</groupId>
    <artifactId>JarQFarming</artifactId>
    <version>Tag</version>
</dependency>
```
#### Gradle
Add the repository
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
Add the dependency
```gradle
dependencies {
    implementation 'com.github.RedPlayer1:JarQFarming:v1.1.0'
}
```
