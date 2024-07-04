package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.worker.model.Worker;

public abstract class WorkerValidator {

    private WorkerValidator next;

    public static WorkerValidator link(WorkerValidator first, WorkerValidator... chain) {
        WorkerValidator head = first;
        for (WorkerValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Worker worker);

    protected boolean checkNext(Worker worker) {
        if (next == null) {
            return true;
        }
        return next.check(worker);
    }
}
