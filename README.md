Android-Bitly-API
=================

Simple Class to implement a Bitly URL in a Android project.

For implemnt, copy this class into your project and in your activity or function call this way

  BitlyAndroid bitly = new BitlyAndroid("bitlyapidemo", "R_0da49e0a9118ff35f52f629d2d71bf07");
  
  String shortUrl = bitly.getShortUrl("http://johnsenf.blogspot.com/2009/12/android-sources-and-javadoc-in-eclipse.html");
