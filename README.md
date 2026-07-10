# ![icon](https://github.com/Jemaroo/MagicalMap/blob/main/src/icons/magicalMap1.png "icon") MagicalMap
Magical Map contains a variety of modding tools that allow you to modify TTYD.

**Note:** This tool is built around the US version of TTYD, support for JP and PAL is untested.

<br/>

## Additional Mentions

Huge thanks to **Jdaster64** for [documentation](https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt) on TTYD's structures their [sheet](https://docs.google.com/spreadsheets/d/15hTm80MaefXxEuWorJOSBD3e6lvw2CCAQTtKUVRhbf4/edit?gid=0#gid=0) on the Switch remake's registry values which helped obtain some of the flag names!

Huge thanks to **NWPlayer123** for their [decomped item tables](https://github.com/doldecomp/ttyd/blob/f3ce61550f927cfd08b1e97ff1079f9f476f4d9f/include/evt/evt_badgeshop.h)!

Huge thanks to **Silver** for their work on writing the tooltips and research on [various flags and values](https://www.youtube.com/@SilverGames136/videos)!

Shoutouts to **hirothetraveler**, **Reed**, **Diagamma**, **cursed**, and others for their suggestions and feedback!

<br/>

## Latest Usage

**Requirements: A recent installation of Java**

1: Download the latest release, it should contain a zip file.

2: Unzip the file, and in the root of the folder should be MagicalMap.exe

3: Dump the contents of your game to a root folder using Dolphin or another dumper, and open that directory with Magical Map

4: Make changes and export file to save

5: After exporting a file with the changes made, replace the dol/rel you modified with the new file in your game's filesystem

<br/>

## Planned Future Features/Additions
### Magical Map
- Implement Text Tool
### Battle Unit Tool
- Attack Property Tabs
- Enemy Formations?
- Randomizer Option
- BattleUnitSetup Structs
- Stage Object Weapons
### Item Data Tool
- More Field Objects?
- Tooltips
### Miscellaneous Edits
- Audience SP Multipliers
- Merlee Effect Probability
- Tooltips

<br/>

## Building
### Required Libraries
- [jdk-25.0.2](https://www.oracle.com/java/technologies/downloads/#jdk25-windows)
- [javafx-sdk-24.0.1](https://gluonhq.com/products/javafx/)
- [UpdateCheckerJava-2.5.1](https://github.com/TechnicJelle/UpdateCheckerJava/releases/v2.5.1)
- [gson-2.10.1](https://github.com/google/gson/releases/tag/gson-parent-2.10.1)
- [commons-collections4-4.4](https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/)
- [commons-compress-1.21](https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.21/)
- [commons-io-2.11.0](https://repo1.maven.org/maven2/commons-io/commons-io/2.11.0/)
- [jakarta.xml.bind-api-3.0.1](https://repo1.maven.org/maven2/jakarta/xml/bind/jakarta.xml.bind-api/3.0.1/)
- [log4j-api-2.18.0](https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-api/2.18.0/)
- [log4j-core-2.20.0](https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.20.0/)
- [poi-5.2.3](https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.3/)
- [poi-excelant-5.2.3](https://repo1.maven.org/maven2/org/apache/poi/poi-excelant/5.2.3/)
- [poi-ooxml-5.2.3](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.3/)
- [poi-ooxml-full-5.2.3](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-full/5.2.3/)
- [poi-scratchpad-5.2.3](https://repo1.maven.org/maven2/org/apache/poi/poi-scratchpad/5.2.3/)
- [slf4j-api-1.7.36](https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/)
- [SparseBitSet-1.2](https://repo1.maven.org/maven2/com/zaxxer/SparseBitSet/1.2/)
- [xmlbeans-5.1.1](https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.1.1/)

### Packing
Compile the .jar file, then Command Line:
```
jlink --module-path "PATH_TO_JMODS_FOLDER;PATH_TO_JAVAFX_LIB_FOLDER" --add-modules java.base,javafx.controls,javafx.fxml --strip-debug --no-man-pages --no-header-files --compress=2 --output runtime
```
Add the dll folder of the javaFX dll's to the runtime folder, then Command Line:
```
jpackage --input . --name MagicalMap --main-jar "PATH_TO_JAR" --runtime-image runtime --app-content "runtime/dll" --java-options "--enable-native-access=ALL-UNNAMED" --java-options "--enable-preview" --java-options "-Dprism.order=sw" --java-options "-Dprism.verbose=true" --java-options "-Djava.library.path=runtime/dll" --icon "PATH_TO_.ICO_ICON" --type app-image --verbose
```
