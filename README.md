Android-Bitly-API
=================

Simple Class to implement a Bitly URL in a Android project.

For implemnt, copy this class ( BitlyAndroid.java ) into your project and in your activity or function call this way:


  BitlyAndroid bitly = new BitlyAndroid("bitlyapi", "000000000000");
  
  String shortUrl = bitly.getShortUrl("http://www.google.com.br");
