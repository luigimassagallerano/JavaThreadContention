Let's test some Thread Contetions in Java!

The test wants to show how, when it is possible, it's better to avoid Thread Contention,
making the Threads work on different memory location and, only in the end, building the final result.
 
The test needs three args:
1) N: Number of Thread
2) Modality: 0 = AVOID CONTENTION or 1 = WITH CONTENTION
3) M: Number of loops
 
The test will create and start N Threads in the specified Modality.
The Threads will perform M loops of operation.

If the modality is CONTENTION, every Thread will build directly the final result, synchronizing each other
through a java.util.concurrent.atomic.AtomicInteger.

If the modality is AVOID CONTENTION, every Thread will avoid contention incrementing an int
in a reserved array position. Only in the end, after the main Thread joins the other Threads,
the final result will be build summing all the value of the array.

In the end the Test will print the result and the time it took to calculate it.

Graphs of performance results can be found at: mydevelopedworld.wordpress.com

You can compile the source code with: ant clean compile
and then run the test with: java -classpath bin com.mydevelopedworld.contention.TestContention


