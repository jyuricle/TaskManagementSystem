package entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents single person in system
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person implements Serializable{

    /**
     * Identification number of person
     */
    @Id
    @GeneratedValue
    Integer id;

    /**
     * First name of person
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Last name of person
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * User's information in system
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Person() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
