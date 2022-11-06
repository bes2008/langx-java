package com.jn.langx.util.concurrent;

import com.jn.langx.util.Preconditions;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.NANOSECONDS;


/**
 * Utilities for treating interruptible operations as uninterruptible. In all cases, if a thread is
 * interrupted during such a call, the call continues to block until the result is available or the
 * timeout elapses, and only then re-interrupts the thread.
 *
 * @since 2.10.3
 */
public final class Uninterrupteds {

    // Implementation Note: As of 3-7-11, the logic for each blocking/timeout
    // methods is identical, save for method being invoked.

    /**
     * Invokes {@code latch.}{@link CountDownLatch#await() await()} uninterrupted.
     */
    public static void awaitUninterrupted(CountDownLatch latch) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    latch.await();
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code latch.}{@link CountDownLatch#await(long, TimeUnit) await(timeout, unit)}
     * uninterrupted.
     */
    public static boolean awaitUninterrupted(CountDownLatch latch, long timeout, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // CountDownLatch treats negative timeouts just like zero.
                    return latch.await(remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code toJoin.}{@link Thread#join() join()} uninterrupted.
     */
    public static void joinUninterrupted(Thread toJoin) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    toJoin.join();
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code future.}{@link Future#get() get()} uninterrupted.
     *
     * @throws ExecutionException    if the computation threw an exception
     * @throws CancellationException if the computation was cancelled
     */
    public static <V> V getUninterrupted(Future<V> future) throws ExecutionException {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code future.}{@link Future#get(long, TimeUnit) get(timeout, unit)} uninterrupted.
     *
     * @throws ExecutionException    if the computation threw an exception
     * @throws CancellationException if the computation was cancelled
     * @throws TimeoutException      if the wait timed out
     */
    public static <V> V getUninterrupted(Future<V> future, long timeout, TimeUnit unit)
            throws ExecutionException, TimeoutException {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // Future treats negative timeouts just like zero.
                    return future.get(remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code unit.}{@link TimeUnit#timedJoin(Thread, long) timedJoin(toJoin, timeout)}
     * uninterrupted.
     */
    public static void joinUninterrupted(Thread toJoin, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.timedJoin() treats negative timeouts just like zero.
                    NANOSECONDS.timedJoin(toJoin, remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code queue.}{@link BlockingQueue#take() take()} uninterrupted.
     */
    public static <E> E takeUninterrupted(BlockingQueue<E> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code queue.}{@link BlockingQueue#put(Object) put(element)} uninterrupted.
     *
     * @throws ClassCastException       if the class of the specified element prevents it from being added
     *                                  to the given queue
     * @throws IllegalArgumentException if some property of the specified element prevents it from
     *                                  being added to the given queue
     */
    public static <E> void putUninterrupted(BlockingQueue<E> queue, E element) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    queue.put(element);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code unit.}{@link TimeUnit#sleep(long) sleep(sleepFor)} uninterrupted.
     */
    public static void sleepUninterrupted(long sleepFor, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.sleep() treats negative timeouts just like zero.
                    NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(1,
     * timeout, unit)} uninterrupted.
     *
     * @since 2.10.3
     */
    public static boolean tryAcquireUninterrupted(
            Semaphore semaphore, long timeout, TimeUnit unit) {
        return tryAcquireUninterrupted(semaphore, 1, timeout, unit);
    }

    /**
     * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(permits,
     * timeout, unit)} uninterrupted.
     *
     * @since 2.10.3
     */
    public static boolean tryAcquireUninterrupted(
            Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // Semaphore treats negative timeouts just like zero.
                    return semaphore.tryAcquire(permits, remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private Uninterrupteds() {
    }
}
