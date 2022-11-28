# 📸 AutoPackIcon
Easily generate Minecraft pack icons from within your workspace.

## ℹ️ Information
This program was made for a specific use case, so it probably
won't be useful in your situation. When executed inside of
a Minecraft pack workspace, it scans for an image file matching the
regex provided at startup. That image is copied into the first 
directory found, which is assumed to be the directory of the 
pack. Then, it is resized and renamed to "pack.png."

## ❓ How to use
The app requires a Java version of 17 or newer.
You should have a workspace with a folder, an image, and the program.
Then, run `java -Dautopackicon.iconimagepattern={image regex} -jar {app file name}`
in a terminal/command prompt. When the `autopackicon.iconimagepattern` property
is omitted, the program defaults to `(?i)_Back\.bmp` as the regex.

Enjoy! 😀
