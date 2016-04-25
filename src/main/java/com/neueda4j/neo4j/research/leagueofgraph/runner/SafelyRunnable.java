package com.neueda4j.neo4j.research.leagueofgraph.runner;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SafelyRunnable {

    private static final Logger log = LoggerFactory.getLogger(SafelyRunnable.class);

    public interface Operation<T> {
        T run();
    }

    public static <T> T safelyExecute(Operation<T> operation) {
        for (int i = 0; i < 5; i++) {
            try {
                return operation.run();
            } catch (Exception e) {
                try {
                    Thread.sleep(1000 * i + 1);
                } catch (InterruptedException interrupted) {
                    Throwables.propagate(interrupted);
                }
                log.error("Operation error. Retrying {}/{}", i + 1, 5);
            }
        }
        return operation.run();
    }
}
