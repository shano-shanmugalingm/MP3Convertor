package com.senthur.convert;

import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class MP3ConvertorTestCase {
	
	private MP3Convertor mp3Convertor;
	
	@BeforeTest
	public void setup() {
		mp3Convertor = new MP3Convertor();
	}

	@Test
	public void wavFileShouldBeConvertedToMP3() {
		byte[] waveByteStream = getWavFileByteStream();
		String url = "beepbeep.mp3";
		ByteArrayOutputStream mp3ByteStream = mp3Convertor.convert(waveByteStream, url);
		writeMP3StreamToFile(mp3ByteStream, url);
	}

	private byte[] getWavFileByteStream() {
		byte[] byteStream = null;
		File waveFile = null;
		URL waveFileURL = URLClassLoader.getSystemResource("beepbeep.wav");
		try {
			waveFile = new File(waveFileURL.toURI());
			byteStream = Files.toByteArray(waveFile);
		} catch (URISyntaxException e) {
			fail("Failed to load Wav File.");
		} catch (IOException e) {
			fail("Failed to load Wav File.");
		}
		return byteStream;
	}

	private void writeMP3StreamToFile(ByteArrayOutputStream mp3ByteStream, String url) {
		File mp3File = new File(url);
		FileOutputStream mp3OutputStream = null;
		try {
			mp3OutputStream = new FileOutputStream(mp3File);
			mp3ByteStream.writeTo(mp3OutputStream);
		} catch (FileNotFoundException e) {
			fail("Failed to write MP3 to File");
		} catch (IOException e) {
			fail("Failed to write MP3 to File");
		} finally {
			if (mp3OutputStream != null)
				try {
					mp3OutputStream.close();
				} catch (IOException e) {
				}
		}
	}

}
