/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjackclipper;

import java.nio.FloatBuffer;
import java.util.Random;
import java.util.Scanner;
import org.jaudiolibs.jnajack.util.SimpleAudioClient;

/**
 *
 * @author daph hiebert <cepha.fish@gmail.com>
 */
public class MyJackClipper implements SimpleAudioClient.Processor
{
    private static final boolean DEBUG = false;
    private static Random r;
    private static final int TABLE_SIZE = 2000;
    private int left_phase = 0;
    private int right_phase = 0;
    private float[] data;
    private double ampCV = 0.5;
    private double freqCV = 2;
    private float ceiling = 0.3f;

    public static void main(String[] args) throws Exception
    {
        Scanner in = new Scanner(System.in);
        r = new Random();

        SimpleAudioClient client = SimpleAudioClient.create("clipper", new String[]
        {
            "input-L", "input-R"
        },
                new String[]
                {
                    "output-L", "output-R"
                }, true, true, new MyJackClipper());
        client.activate();

        while (true)
        {
            Thread.sleep(1000);
        }
//        client.shutdown();
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

        for (int channel = 0; channel < 2; channel++)
        {
            int size = inputs[channel].capacity();
            for (int x = 0; x < size; x++)
            {
                if (inputs[channel].get(x) > ceiling)
                {
                    outputs[channel].put(x, ceiling);
                }
                else
                {
                    outputs[channel].put(x, inputs[channel].get(x));
                }
            }
        }
    }

    @Override
    public void shutdown()
    {
        System.out.println("Audio Source shutdown");
    }

}
