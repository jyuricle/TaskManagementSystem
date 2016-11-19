package entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Entity
@Table(name = "subtasks")
public class Subtask extends Task{

    @ManyToOne(cascade = CascadeType.MERGE)
    private Subordinate subordinate;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;

    @Column(name = "status")
    private Status status;

    public Subtask() {

    }

    public Subordinate getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Subordinate subordinate) {
        this.subordinate = subordinate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subtask && Objects.equals(id, ((Subtask) obj).getId());
    }

}
