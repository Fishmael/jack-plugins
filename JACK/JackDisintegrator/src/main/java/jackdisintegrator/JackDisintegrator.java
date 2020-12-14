/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jackdisintegrator;

import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.Random;
import org.jaudiolibs.jnajack.util.SimpleAudioClient;

/**
 *
 * @author daph hiebert <cepha.fish@gmail.com>
 */
public class JackDisintegrator implements SimpleAudioClient.Processor
{

    private static final boolean DEBUG = true;
    private static final int WINDOW_SIZE = 1000;
    private static Random r;
    /**
     * Sets the amplitude of the disintegrated samples
     */
    private static final double LOW_MULTIPLIER = 0.2;
    /**
     * All random values generated above this value will set the bit in the carrier sequence
     */
    private static final double CUTOFF = .5;
    /**
     * Controls the average length of a run in the carrier sequence
     */
    private static final double RUN_MULT = 5;
    /**
     * The carrier sequence
     */
    private static BitSet bs;

    public static void main(String[] args) throws Exception
    {
        r = new Random();
        bs = new BitSet();

        SimpleAudioClient client = SimpleAudioClient.create("disintegrator", new String[]
        {
            "input-L", "input-R"
        },
                new String[]
                {
                    "output-L", "output-R"
                }, true, true, new JackDisintegrator());
        client.activate();

        while (true)
        {
            Thread.sleep(1000);
        }
    }

    @Override
    public void setup(float samplerate, int buffersize)
    {
        if (DEBUG)
        {
            System.err.println("setup called...");
        }

    }

    @Override
    public void process(FloatBuffer[] inputs, FloatBuffer[] outputs)
    {
        if (DEBUG)
        {
            System.err.println("process called...");
        }
        
        // Populate bitset
        int i = 0;
        while (i < WINDOW_SIZE)
        {   
            boolean isSet = r.nextDouble() > CUTOFF;
            long runLength = Math.round(r.nextGaussian() * RUN_MULT);
            for (int j = 0; j < runLength; j++)
            {
                bs.set(i, isSet);
                i++;
            }
        }
        
//        if (DEBUG)
//        {
//            System.out.printf("BitSet:%s", bs.toString());
//        }

        for (int channel = 0; channel < 2; channel++)
        {
            int size = inputs[channel].capacity();
            for (int x = 0; x < size; x++)
            {

            }
        }
    }

    @Override
    public void shutdown()
    {
        System.out.println("Audio Source shutdown");
    }
}
