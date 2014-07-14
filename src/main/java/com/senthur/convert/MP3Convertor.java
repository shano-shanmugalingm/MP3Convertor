package com.senthur.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.io.XugglerIO;

public class MP3Convertor {

	private static final String MIME_TYPE = "audio/mpeg3";

	private IContainer iContainer;
	
	public MP3Convertor() {
		iContainer = IContainer.make();
	}
	
	public ByteArrayOutputStream convert(byte[] stream, String url) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = new ByteArrayInputStream(stream);
		
		if (iContainer.open(inputStream, IContainerFormat.make()) >= 0) {
			IMediaReader mediaReader = ToolFactory.makeReader(iContainer);
			IMediaWriter mediaWriter = ToolFactory.makeWriter(XugglerIO.map(outputStream), mediaReader);
			
			setMediaFormat(mediaWriter, url);
			assignSampler(mediaReader, mediaWriter);
			
			while (mediaReader.readPacket() == null);
			iContainer.close();
		}
		return outputStream;
	}

	private void setMediaFormat(IMediaWriter mediaWriter, String url) {
		IContainerFormat containerFormat = IContainerFormat.make();
		containerFormat.setOutputFormat(null, url, MIME_TYPE);
		mediaWriter.getContainer().setFormat(containerFormat);
	}
	
	private void assignSampler(IMediaReader mediaReader, IMediaWriter mediaWriter) {
		MP3ChimeSampler mp3Sampler = new MP3ChimeSampler(mediaWriter);
		mediaReader.addListener(mp3Sampler);
		mp3Sampler.addListener(mediaWriter);
	}

}
