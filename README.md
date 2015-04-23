Playing
=======
Download the latest build here: [duck.jar](https://github.com/SweatyReptile/loser/releases)

If the game doesn't start when you double-click `duck.jar`, run the following command
```
java -jar path/to/file/duck.jar
```

If you just want to see gameplay, scroll down for some video links

Building
========
[![Build Status](https://travis-ci.org/SweatyReptile/loser.svg?branch=develop)](https://travis-ci.org/SweatyReptile/loser)

Unfortunately, you probably need the android SDK set up somewhere, and that directory needs to be in the `ANDROID_HOME` environment variable.

```bash
git clone https://github.com/SweatyReptile/loser.git
cd loser
# If you have a bash environment,
./gradlew :desktop:run
# On windows,
gradlew.bat :desktop:run
```

Videos
======
Gameplay demo video:

<a href="https://youtu.be/tGQSMRsMdI8" target="_blank">![Gameplay](assetsrc/images/screenshots/gameplay.png?raw=true)</a>

In-game console demo video:
<a href="https://youtu.be/7PblE93kbjA" target="_blank">![Console Demo](assetsrc/images/screenshots/console.png?raw=true)</a>

License
=======

All software code is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl.html).

All art assets are licensed under the [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-nc-sa/4.0/)

[![Art licensed under CC-BY-NC-SA](http://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png)](http://creativecommons.org/licenses/by-nc-sa/4.0/)
