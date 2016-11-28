package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Represents task's entity
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Task implements Serializable{

    /**
     * Identification number
     */
    @Id
    @GeneratedValue
    Integer id;

    /**
     * Name of the task
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Description of the task
     */
    @Column(name = "description")
    private String description;

    /**
     * Date of task's start
     */
    @Column(name = "start")
    private Date start;

    /**
     * Date of task's deadline
     */
    @Column(name = "deadline")
    private Date deadline;

    /**
     * Task's priority
     */
    @Column(name = "priority")
    private Priority priority;

    public Task() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
