/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackmodulator;

import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.Random;
import org.jaudiolibs.jnajack.util.SimpleAudioClient;

/**
 *
 * @author daph hiebert <cepha.fish@gmail.com>
 */
public class JackModulator implements SimpleAudioClient.Processor
{

    @Override
    public void setup(float samplerate, int buffersize)
    {
    }

    @Override
    public void process(FloatBuffer[] inputs, FloatBuffer[] outputs)
    {
    }

    @Override
    public void shutdown()
    {
        System.out.println("Audio modulator shutdown");
    }

}
