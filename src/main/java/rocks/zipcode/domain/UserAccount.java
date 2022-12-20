package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "userAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userAccount" }, allowSetters = true)
    private Set<ExpertUser> expertUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserAccount name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public UserAccount password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccount user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<ExpertUser> getExpertUsers() {
        return this.expertUsers;
    }

    public void setExpertUsers(Set<ExpertUser> expertUsers) {
        if (this.expertUsers != null) {
            this.expertUsers.forEach(i -> i.setUserAccount(null));
        }
        if (expertUsers != null) {
            expertUsers.forEach(i -> i.setUserAccount(this));
        }
        this.expertUsers = expertUsers;
    }

    public UserAccount expertUsers(Set<ExpertUser> expertUsers) {
        this.setExpertUsers(expertUsers);
        return this;
    }

    public UserAccount addExpertUser(ExpertUser expertUser) {
        this.expertUsers.add(expertUser);
        expertUser.setUserAccount(this);
        return this;
    }

    public UserAccount removeExpertUser(ExpertUser expertUser) {
        this.expertUsers.remove(expertUser);
        expertUser.setUserAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
