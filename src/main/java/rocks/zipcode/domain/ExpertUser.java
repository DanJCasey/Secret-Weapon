package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import rocks.zipcode.domain.enumeration.Expertise;

/**
 * A ExpertUser.
 */
@Entity
@Table(name = "expert_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpertUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "expertise")
    private Expertise expertise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "expertUsers" }, allowSetters = true)
    private UserAccount userAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExpertUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Expertise getExpertise() {
        return this.expertise;
    }

    public ExpertUser expertise(Expertise expertise) {
        this.setExpertise(expertise);
        return this;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }

    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public ExpertUser userAccount(UserAccount userAccount) {
        this.setUserAccount(userAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpertUser)) {
            return false;
        }
        return id != null && id.equals(((ExpertUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpertUser{" +
            "id=" + getId() +
            ", expertise='" + getExpertise() + "'" +
            "}";
    }
}
