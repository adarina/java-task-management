package com.ada.javataskmanagement.project.validation;

import com.ada.javataskmanagement.project.model.Project;
import com.ada.javataskmanagement.worker.model.Worker;

public abstract class ProjectValidator {

    private ProjectValidator next;

    public static ProjectValidator link(ProjectValidator first, ProjectValidator... chain) {
        ProjectValidator head = first;
        for (ProjectValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Project project, Worker worker);
    protected boolean checkNext(Project project, Worker worker) {
        if (next == null) {
            return true;
        }
        return next.check(project, worker);
    }
}
