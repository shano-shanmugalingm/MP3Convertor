package com.senthur.convert;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.AudioSamplesEvent;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioResampler;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IStreamCoder;

public class MP3ChimeSampler extends MediaToolAdapter {
	private static final int OUTPUT_CHANNEL = 1;
	private static final int OUTPUT_SAMPLE_RATE = 44100;
	
	private IAudioResampler audioResampler = null;
	private IMediaWriter mediaWriter;
	
	public MP3ChimeSampler(IMediaWriter mediaWriter) {
		this.mediaWriter = mediaWriter;
	}
	
	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {
	  IAudioSamples samples = event.getAudioSamples();
	  if (audioResampler == null) {
	    audioResampler = IAudioResampler.make(OUTPUT_CHANNEL, samples.getChannels(), OUTPUT_SAMPLE_RATE, samples.getSampleRate());
	  }
	  if (event.getAudioSamples().getNumSamples() > 0) {
	    IAudioSamples out = IAudioSamples.make(samples.getNumSamples(), OUTPUT_CHANNEL);
	    audioResampler.resample(out, samples, samples.getNumSamples());
	 
	    AudioSamplesEvent asc = new AudioSamplesEvent(event.getSource(), out, event.getStreamIndex());
	    super.onAudioSamples(asc);
	    out.delete();
	  }
	}
	@Override
	public void onAddStream(IAddStreamEvent event) {
	  int streamIndex = event.getStreamIndex();
	  IStreamCoder streamCoder = event.getSource().getContainer().getStream(streamIndex).getStreamCoder();
	  if (streamCoder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
		  mediaWriter.addAudioStream(streamIndex, streamIndex, OUTPUT_CHANNEL, OUTPUT_SAMPLE_RATE);			  
	  }
	  super.onAddStream(event);
	}
}
