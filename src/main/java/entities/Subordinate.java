package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Entity
@Table(name = "subordinates")
public class Subordinate extends Person {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subordinate", fetch = FetchType.EAGER)
    private List<Subtask> subtasks;

    public Subordinate() {
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subordinate && Objects.equals(id, ((Subordinate) obj).getId());
    }
}
