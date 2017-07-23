package com.mypersonalupdates.log;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.UpdatesConsumer;
import com.mypersonalupdates.db.DBException;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Log implements UpdatesConsumer{
    private final ConcurrentLinkedQueue<Update> queue;
    private final Thread thread;

    // TODO: hacer singleton en diagrama
    private static final Log instance = new Log();
    public static Log getInstance() {
        return Log.instance;
    }

    private Log() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.thread = new Thread(() -> {
            try {
                LogUpdate.create(this.queue);
            } catch (DBException e) {
                e.printStackTrace();
            }
        });
    }

    // TODO: quitar runThread del diagrama

    @Override
    public void handleUpdate(Update update) {
        this.queue.add(update);

        if(!this.thread.isAlive())
            this.thread.run();
    }
}
