package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Represents single project in system
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Entity
@Table(name = "projects")
public class Project extends Task {

    /**
     * List of project's subtasks
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Subtask> subtasks;

    /**
     * Responsible manager for project
     */
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Manager manager;

    public Project() {

    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subtask && Objects.equals(id, ((Subtask) obj).getId());
    }
}
