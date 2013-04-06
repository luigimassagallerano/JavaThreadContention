Let's test some Thread Contetion in Java!
====================

The test wants to show how, when it is possible, it's better to avoid Thread Contention,
making the Threads work on different memory location and, only in the end, building the final result.
 
The test needs three args:

1. <b>N</b>: Number of Threads
2. <b>Modality</b>: 0 = AVOID CONTENTION; 1 = WITH CONTENTION (AtomicLong); 2 = WITH CONTENTION (Synchronized Method)
3. <b>M</b>: Number of loops (Increment operations)
 
The test will create and start <b>N Threads</b> in the specified <b>Modality</b>.
The Threads will perform <b>M loops</b>, incrementing a variable in every loop.

+ If the modality is <b>CONTENTION_ATOMIC_LONG</b>, every Thread will contribute directly to the final result, mantaining consistency through java.util.concurrent.atomic.AtomicInteger.

+ If the modality is <b>CONTENTION_SYNC</b>, every Thread will contribute directly to the final result, synchronizing each other through a synchornized method.

+ If the modality is <b>AVOID_CONTENTION</b>, every Thread will avoid contention incrementing a long in a reserved array position. Only in the end, after the main Thread have joined all the other Threads, the final result will be built summing all the array values.

In the end the Test will print the result and the time it took to calculate it.

<b>Performance Graphs</b> can be found [on my blog](http://mydevelopedworld.wordpress.com).

You can compile the source code and then run the test with: 

1. ant clean compile (or, if your prefer with: javac -d bin src/com/mydevelopedworld/contention/*
2. java -classpath bin com.mydevelopedworld.contention.TestContention N Modality N


