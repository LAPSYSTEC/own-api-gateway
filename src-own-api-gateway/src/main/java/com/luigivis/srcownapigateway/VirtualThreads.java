package com.luigivis.srcownapigateway;

public class VirtualThreads {

    public static void VirtualThreadsExecutor(Runnable runnable) {
        Thread.startVirtualThread(runnable);
    }

}
