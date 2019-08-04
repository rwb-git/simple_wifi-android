To create an icon, in Android Studio click File - New - Image Asset, then click the Path folder and select simple_wifi_icon.png. If it looks ok, click Next, or if the size is wrong click Trim and adjust the slider, then Next and Finish.

The layout_files go in Res Layout.

You need to correct the package in AndroidManifest.xml and each of the java source files. 

Also, you need the IP for your esp8266 device. One way is to hard code the IP in led_bt.java in the variable ipb if you have configured your router to assign a particular IP. You should be able to find your IP in your router configuration. Or, you can see it in the Serial monitor in Arduino IDE if you have enabled serial printing in the source code. And, the app can scan for it, but that seems to not work so well if other devices (phones, tablets, etc.) are in the range that you scan, so the main use for that (for me, anyway) is to find the device in a range like 10.0.0.198 to 10.0.0.205 which is where I have set up my router to automatically assign particular IPs to my esp devices. It's easier to just scan that small range than to look up which IP that a particular esp is using. 
