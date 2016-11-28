package entities;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Represents user's information for logging in
 *
 * @author Yurii Krat
 * @version 1.0, 11.11.16
 */

@Entity
@Table(name = "users")
public class User {

    /**
     * Identification number
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * User's name
     */
    @Column(name = "user_name", unique = true)
    private String userName;

    /**
     * User's password
     */
    @Column(name = "password")
    private String password;

    /**
     * User's role in system
     */
    @Column(name = "role")
    private String role;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = getEncodedPassword(password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Transforms user's password into hashed password
     *
     * @param password password for hashing
     * @return hashed password
     * @throws NoSuchAlgorithmException
     */
    private static String getEncodedPassword(String password) throws NoSuchAlgorithmException {
        return new String(Base64.getEncoder().encode(encryption(password)));
    }

    /**
     * Transforms user's password into hashed password
     * with SHA-256 algorithm
     *
     * @param password password for hashing
     * @return hashed password
     * @throws NoSuchAlgorithmException
     */
    private static byte[] encryption(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        return md.digest();
    }

}
