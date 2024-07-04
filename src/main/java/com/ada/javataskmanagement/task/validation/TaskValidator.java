package com.ada.javataskmanagement.task.validation;

import com.ada.javataskmanagement.task.model.Task;

public abstract class TaskValidator {

    private TaskValidator next;

    public static TaskValidator link(TaskValidator first, TaskValidator... chain) {
        TaskValidator head = first;
        for (TaskValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Task task);

    protected boolean checkNext(Task task) {
        if (next == null) {
            return true;
        }
        return next.check(task);
    }
}

