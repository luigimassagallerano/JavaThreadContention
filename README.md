Let's test some Thread Contetion in Java!
====================

The test wants to show how, when it is possible, it's better to avoid Thread Contention,
making the Threads work on different memory location and, only in the end, building the final result.
 
The test needs three args:

1. <b>N</b>: Number of Thread
2. <b>Modality</b>: 0 = AVOID CONTENTION or 1 = WITH CONTENTION
3. <b>M</b>: Number of loops
 
The test will create and start <b>N Threads</b> in the specified <b>Modality</b>.
The Threads will perform <b>M loops</b> of operation.

+ If the modality is <b>CONTENTION</b>, every Thread will build directly the final result, synchronizing each other
through a java.util.concurrent.atomic.AtomicInteger.

+ If the modality is <b>AVOID CONTENTION</b>, every Thread will avoid contention incrementing an int
in a reserved array position. Only in the end, after the main Thread joins the other Threads,
the final result will be built summing all the value of the array.

In the end the Test will print the result and the time it took to calculate it.

<b>Performance Graphs</b> can be found [on my blog](http://mydevelopedworld.wordpress.com).

You can compile the source code and then run the test with: 

1. ant clean compile
2. java -classpath bin com.mydevelopedworld.contention.TestContention


