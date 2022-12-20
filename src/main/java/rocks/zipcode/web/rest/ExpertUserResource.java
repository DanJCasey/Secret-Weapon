package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.ExpertUser;
import rocks.zipcode.repository.ExpertUserRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.ExpertUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExpertUserResource {

    private final Logger log = LoggerFactory.getLogger(ExpertUserResource.class);

    private static final String ENTITY_NAME = "expertUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpertUserRepository expertUserRepository;

    public ExpertUserResource(ExpertUserRepository expertUserRepository) {
        this.expertUserRepository = expertUserRepository;
    }

    /**
     * {@code POST  /expert-users} : Create a new expertUser.
     *
     * @param expertUser the expertUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expertUser, or with status {@code 400 (Bad Request)} if the expertUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expert-users")
    public ResponseEntity<ExpertUser> createExpertUser(@RequestBody ExpertUser expertUser) throws URISyntaxException {
        log.debug("REST request to save ExpertUser : {}", expertUser);
        if (expertUser.getId() != null) {
            throw new BadRequestAlertException("A new expertUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpertUser result = expertUserRepository.save(expertUser);
        return ResponseEntity
            .created(new URI("/api/expert-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expert-users/:id} : Updates an existing expertUser.
     *
     * @param id the id of the expertUser to save.
     * @param expertUser the expertUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertUser,
     * or with status {@code 400 (Bad Request)} if the expertUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expertUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expert-users/{id}")
    public ResponseEntity<ExpertUser> updateExpertUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpertUser expertUser
    ) throws URISyntaxException {
        log.debug("REST request to update ExpertUser : {}, {}", id, expertUser);
        if (expertUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpertUser result = expertUserRepository.save(expertUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expert-users/:id} : Partial updates given fields of an existing expertUser, field will ignore if it is null
     *
     * @param id the id of the expertUser to save.
     * @param expertUser the expertUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertUser,
     * or with status {@code 400 (Bad Request)} if the expertUser is not valid,
     * or with status {@code 404 (Not Found)} if the expertUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the expertUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expert-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpertUser> partialUpdateExpertUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpertUser expertUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpertUser partially : {}, {}", id, expertUser);
        if (expertUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpertUser> result = expertUserRepository
            .findById(expertUser.getId())
            .map(existingExpertUser -> {
                if (expertUser.getExpertise() != null) {
                    existingExpertUser.setExpertise(expertUser.getExpertise());
                }

                return existingExpertUser;
            })
            .map(expertUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertUser.getId().toString())
        );
    }

    /**
     * {@code GET  /expert-users} : get all the expertUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expertUsers in body.
     */
    @GetMapping("/expert-users")
    public List<ExpertUser> getAllExpertUsers() {
        log.debug("REST request to get all ExpertUsers");
        return expertUserRepository.findAll();
    }

    /**
     * {@code GET  /expert-users/:id} : get the "id" expertUser.
     *
     * @param id the id of the expertUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expertUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expert-users/{id}")
    public ResponseEntity<ExpertUser> getExpertUser(@PathVariable Long id) {
        log.debug("REST request to get ExpertUser : {}", id);
        Optional<ExpertUser> expertUser = expertUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(expertUser);
    }

    /**
     * {@code DELETE  /expert-users/:id} : delete the "id" expertUser.
     *
     * @param id the id of the expertUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expert-users/{id}")
    public ResponseEntity<Void> deleteExpertUser(@PathVariable Long id) {
        log.debug("REST request to delete ExpertUser : {}", id);
        expertUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
