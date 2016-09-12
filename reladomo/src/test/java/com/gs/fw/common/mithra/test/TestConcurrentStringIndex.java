/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.test;

import com.gs.fw.common.mithra.cache.StringIndex;
import com.gs.fw.common.mithra.util.ExceptionCatchingThread;
import junit.framework.TestCase;

import java.text.ParseException;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;



public abstract class TestConcurrentStringIndex extends TestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    private String[] shuffle(String[] array)
    {
        String[] result = new String[array.length];
        Random rand = new Random(12345678912345L);
        int left = array.length;
        for (int i = 0; i < array.length; i++)
        {
            int chosen = rand.nextInt(left);
            result[i] = array[chosen];
            left--;
            array[chosen] = array[left];
            array[left] = null;
        }
        System.arraycopy(result, 0, array, 0, array.length);
        return array;
    }

    public void testGetIfAbsentPut() throws Exception
    {
        StringIndex index = createStringPool();
        int max = 10000;
        String data[] = new String[max];
        for(int i=0;i<max;i++)
        {
            String o = createData(i);
            data[i] = o;
        }
        data = shuffle(data);
        String[] results = new String[max];
        for(int i=0;i<max;i++)
        {
            results[i] = index.getIfAbsentPut(data[i], (i & 1) == 0);
            assertSame(results[i], index.getIfAbsentPut(data[i], false));
            assertEquals(i+1, index.size());
        }
        for(int i=0;i<max;i++)
        {
            assertSame(results[i], index.getIfAbsentPut(data[i], false));
        }
        assertEquals(max, index.size());
    }

    protected abstract StringIndex createStringPool();

    private String createData(int i)
    {
        return ""+i;
    }

    public void testGetIfAbsentPutWithManyThreads() throws Exception
    {
        int max = 1000000;
        String data[] = new String[max];
        for(int i=0;i<max;i++)
        {
            String o = createData(i);
            data[i] = o;
        }
        data = shuffle(data);
        runGetIfAbsentPut(1, data, max);
        runGetIfAbsentPut(2, data, max);
        runGetIfAbsentPut(5, data, max);
        runGetIfAbsentPut(10, data, max);
        runGetIfAbsentPut(50, data, max);
        runGetIfAbsentPut(100, data, max);
    }

    private void runGetIfAbsentPut(int threads, String[] data, int max)
            throws ParseException
    {
        StringIndex index = createStringPool();
        ExceptionCatchingThread[] runners = new ExceptionCatchingThread[threads];
        GetIfAbsentPutRunnable[] runnables = new GetIfAbsentPutRunnable[threads];
        int chunkSize = max/threads;
        for(int i = 0;i<threads;i++)
        {
            runnables[i] = new GetIfAbsentPutRunnable(chunkSize * i, chunkSize * (i+1), data, index);
            runners[i] = new ExceptionCatchingThread(runnables[i]);
        }
        System.gc();
        Thread.yield();
        System.gc();
        Thread.yield();
        long start = System.currentTimeMillis();
        for(int i = 0;i<threads;i++)
        {
            runners[i].start();
        }

        for(int i = 0;i<threads;i++)
        {
            runners[i].joinWithExceptionHandling();
        }
        System.out.println("running with "+threads+" threads took "+(System.currentTimeMillis() - start)/1000.0+" s");
        for(int i = 0;i<threads;i++)
        {
            runnables[i].verifyExistence();
        }

        assertEquals(max, index.size());
    }

    private static class GetIfAbsentPutRunnable implements Runnable
    {
        private int start;
        private int max;
        private String[] data;
        private StringIndex index;
        private String[] results;

        private GetIfAbsentPutRunnable(int start, int max, String[] data, StringIndex index)
        {
            this.start = start;
            this.max = max;
            this.data = data;
            this.index = index;
            this.results = new String[max - start];
        }

        public void run()
        {
            for(int i=start;i<max;i++)
            {
                results[i-start] = index.getIfAbsentPut(data[i], (i & 1) == 0);
                assertSame(results[i-start], index.getIfAbsentPut(data[i], false));
            }
            verifyExistence();
        }

        public void verifyExistence()
        {
            for(int i=start;i<max;i++)
            {
                assertSame(results[i - start], index.getIfAbsentPut(data[i], false));
            }
        }
    }

    public void testWeakCollection() throws Exception
    {
        final int max = 1 << 18;
        final String[] toKeep = new String[1 << 14];
        final StringIndex index = createStringPool();
        final AtomicInteger done = new AtomicInteger(0);
        ExceptionCatchingThread putter = new ExceptionCatchingThread(new Runnable()
        {
            public void run()
            {
                for(int i=0;i<max;i++)
                {
                    String data = createData(i);
                    String result = index.getIfAbsentPut(data, false);
                    if (i % ((1 << 4)-1) == 0) toKeep[i >> 4] = result;
                }
                done.set(1);
            }
        });
        ExceptionCatchingThread evictor = new ExceptionCatchingThread(new Runnable()
        {
            public void run()
            {
                while(done.get() == 0)
                    index.evictCollectedReferences();
                index.evictCollectedReferences();
            }
        });
        evictor.start();
        putter.start();
        putter.joinWithExceptionHandling();
        evictor.joinWithExceptionHandling();
        System.gc();
        Thread.yield();
        System.gc();
        Thread.yield();
        assertTrue(index.size() >= toKeep.length);
        for(int i=0;i<toKeep.length;i++)
        {
            assertSame(toKeep[i], index.getIfAbsentPut(toKeep[i], false));
        }
    }

    protected static Object waitForOtherThread(final Exchanger exchanger)
    {
        return waitForOtherThreadAndPassObject(exchanger, null);
    }

    protected static Object waitForOtherThreadAndPassObject(final Exchanger exchanger, Object object)
    {
        Object result = null;
        try
        {
            result = exchanger.exchange(object);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public void testContendedGetIfAbsentPut() throws Exception
    {
        final int max = 1000000;
        String orderedData[] = new String[max];
        for(int i=0;i<max;i++)
        {
            String o = createData(i);
            orderedData[i] = o;
        }
        final String data[] = shuffle(orderedData);
        final Exchanger exchanger = new Exchanger();
        final StringIndex index = createStringPool();
        GetIfAbsentPutRunnableWithExchange first = new GetIfAbsentPutRunnableWithExchange(0, max, data, index, exchanger);
        GetIfAbsentPutRunnableWithExchange second = new GetIfAbsentPutRunnableWithExchange(0, max, data, index, exchanger);
        ExceptionCatchingThread firstThread = new ExceptionCatchingThread(first);
        ExceptionCatchingThread secondThread = new ExceptionCatchingThread(second);
        firstThread.start();
        secondThread.start();
        firstThread.joinWithExceptionHandling();
        secondThread.joinWithExceptionHandling();
        assertEquals(max, index.size());
        first.verifyExistence();
        second.verifyExistence();
    }

    private static class GetIfAbsentPutRunnableWithExchange implements Runnable
    {
        private int start;
        private int max;
        private String[] data;
        private StringIndex index;
        private String[] results;
        private Exchanger exchanger;

        private GetIfAbsentPutRunnableWithExchange(int start, int max, String[] data, StringIndex index, Exchanger exchanger)
        {
            this.start = start;
            this.max = max;
            this.data = data;
            this.index = index;
            this.results = new String[max - start];
            this.exchanger = exchanger;
        }

        public void run()
        {
            for(int i=start;i<max;i++)
            {
                waitForOtherThread(exchanger);
                results[i-start] = index.getIfAbsentPut(data[i], false);
                assertSame(results[i-start], index.getIfAbsentPut(data[i], false));
            }
            verifyExistence();
        }

        public void verifyExistence()
        {
            for(int i=start;i<max;i++)
            {
                assertSame(results[i - start], index.getIfAbsentPut(data[i], false));
            }
        }
    }
}