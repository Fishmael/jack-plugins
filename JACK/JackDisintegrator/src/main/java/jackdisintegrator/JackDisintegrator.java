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
 * A simple audio disintegrator plugin
 *
 * @author daph hiebert <cepha.fish@gmail.com>
 */
public class JackDisintegrator implements SimpleAudioClient.Processor
{

    private static final boolean DEBUG = true;
    /**
     *
     */
    private static final int WINDOW_SIZE = 1000;
    private static Random r;
    /**
     * Sets the amplitude of the disintegrated samples
     */
    private static final float LOW_MULTIPLIER = 0.5f;
    /**
     * All random values generated above this value will set the bit in the
     * carrier sequence
     */
    private static final double CUTOFF = .3;
    /**
     * Controls the average length of a run in the carrier sequence
     */
    private static final double RUN_MULT = 100000;

    private static final boolean SMOOTH = true;

    private static final int SMOOTH_LENGTH = 200;
    /**
     * The carrier sequence
     */
    private static BitSet bs;
    private static int smoothIndex;
//    private static boolean isSmoothing;
    private static boolean smoothDown;
    private static boolean isSmoothing;
    private static int bitIndex;
    private static int windowIndex;
    private static int bufferSize;

    public static void main(String[] args) throws Exception
    {

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

        populate();

        r = new Random();
        bs = new BitSet();
        smoothIndex = 0;
        smoothDown = false;
        isSmoothing = false;
        bitIndex = 0;
        windowIndex = 0;
        bufferSize = buffersize;
    }

    @Override
    public void process(FloatBuffer[] inputs, FloatBuffer[] outputs)
    {
        for (int channel = 0; channel < 2; channel++)
        {
            for (int x = 0; x < inputs[channel].capacity(); x++)
            {
                if (bitIndex == WINDOW_SIZE)
                {
                    populate();
                    windowIndex = 0;
                }
                if (SMOOTH && bitIndex != 0 && bs.get(bitIndex - 1) != bs.get(bitIndex))
                {
                    isSmoothing = true;
                    if (DEBUG)
                    {
                        System.err.println("smoothing called...");
                    }
                }

                if (SMOOTH && isSmoothing && x != 0)
                {

                    smoothDown = !bs.get(windowIndex);
                    isSmoothing = true;

                    if (smoothDown)
                    {
                        smoothIndex = SMOOTH_LENGTH;
                    }
                    else
                    {
                        smoothIndex = 0;
                    }

                    outputs[channel].put(x, inputs[channel].get(x) * ((float) smoothIndex / (float) SMOOTH_LENGTH));

                    if (smoothDown)
                    {
                        smoothIndex--;
                    }
                    else
                    {
                        smoothIndex++;
                    }

                    if (smoothIndex == 0 || smoothIndex == SMOOTH_LENGTH)
                    {
                        isSmoothing = false;
                        if (DEBUG)
                        {
                            System.err.println("finished smoothing.");
                        }
                    }
                }
                else if (!isSmoothing)
                {
                    outputs[channel].put(x, inputs[channel].get(x) * (bs.get(x + (windowIndex * bufferSize)) ? LOW_MULTIPLIER : 1.0f));
                }

            }
        }

        windowIndex++;
    }

    private static void populate()
    {
        if (DEBUG)
        {
            System.err.println("populating...");
        }
        bitIndex = 0;
        while (bitIndex < WINDOW_SIZE)
        {
            boolean isSet = r.nextDouble() > CUTOFF;
            long runLength = Math.round(r.nextGaussian() * RUN_MULT);
            for (int j = 0; j < runLength; j++)
            {
                bs.set(bitIndex, isSet);
                bitIndex++;
            }
            bitIndex++;
        }
        if (DEBUG)
        {
            System.err.println("finished populating.");
        }
    }

    @Override
    public void shutdown()
    {
        System.out.println("Audio Processor shutdown");
    }
}
