package edu.vuum.mocca;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 *
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject.  It must implement both "Fair" and
 *        "NonFair" semaphore semantics, just liked Java Semaphores. 
 */
public class SimpleSemaphore {
    /**
     * Constructor initialize the data members.  
     */
    public SimpleSemaphore (int permits,
                            boolean fair)
    {
        // DONE - you fill in here
        this.permits = permits;
        this.mLock = new ReentrantLock(fair);

        permitsAvailable = this.mLock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can
     * be interrupted.
     */
    public void acquire() throws InterruptedException {
        // DONE - you fill in here
        mLock.lockInterruptibly();

        try {
            // Guarded suspension should be in a try block here since await() can be
            // interrupted, which would throw an InterruptedException in that case
            while (permits == 0)
                permitsAvailable.await();

            --permits;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Acquire one permit from the semaphore in a manner that
     * cannot be interrupted.
     */
    public void acquireUninterruptibly() {
        // DONE - you fill in here
        mLock.lock();


        try {
            // Guarded suspension should be in a try block under any circumstance.
            // in theory, it's not necessary, but much safer and acceptable to do so anyways.
            while (permits == 0)
                permitsAvailable.awaitUninterruptibly();

            --permits;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // DONE - you fill in here
        mLock.lock();

        try {
            ++permits;

            if (permits > 0) { //if block is an optimization, not necessary
                permitsAvailable.signal();
            }
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // DONE? - you fill in here
    private ReentrantLock mLock;

    /**
     * Define a ConditionObject to wait while the number of
     * permits is 0.
     */
    // DONE - you fill in here
    private Condition permitsAvailable;

    /**
     * Define a count of the number of available permits.
     */
    // DONE? - you fill in here
    int permits;
}
