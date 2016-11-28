package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Represents manager's entity
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Entity
@Table(name = "managers")
public class Manager extends Person {

    /**
     * List of manager's projects
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "manager")
    private List<Project> projects;

    public Manager() {

    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Manager && Objects.equals(id, ((Manager) obj).getId());
    }

}
