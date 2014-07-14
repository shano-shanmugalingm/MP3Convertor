MP3Convertor
============

Sample Project to convert wave file to mp3

This project uses Xuggler - http://www.xuggle.com/xuggler/ to perform the actual conversion.

I have added a wrapper to convert a provided wav file to mp3.
When we pass in the .WAV file as a byte stream (byte[]), the program will convert it to a ByteArrayOutputStream of MP3.

Please see src\test\java\com\senthur\convert\MP3ConvertorTestCase.java to see how to use this API.
