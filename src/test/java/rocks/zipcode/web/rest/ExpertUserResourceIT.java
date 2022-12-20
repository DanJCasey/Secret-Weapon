package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.ExpertUser;
import rocks.zipcode.domain.enumeration.Expertise;
import rocks.zipcode.repository.ExpertUserRepository;

/**
 * Integration tests for the {@link ExpertUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpertUserResourceIT {

    private static final Expertise DEFAULT_EXPERTISE = Expertise.PLUMBING;
    private static final Expertise UPDATED_EXPERTISE = Expertise.ELECTRICIAN;

    private static final String ENTITY_API_URL = "/api/expert-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpertUserRepository expertUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpertUserMockMvc;

    private ExpertUser expertUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertUser createEntity(EntityManager em) {
        ExpertUser expertUser = new ExpertUser().expertise(DEFAULT_EXPERTISE);
        return expertUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertUser createUpdatedEntity(EntityManager em) {
        ExpertUser expertUser = new ExpertUser().expertise(UPDATED_EXPERTISE);
        return expertUser;
    }

    @BeforeEach
    public void initTest() {
        expertUser = createEntity(em);
    }

    @Test
    @Transactional
    void createExpertUser() throws Exception {
        int databaseSizeBeforeCreate = expertUserRepository.findAll().size();
        // Create the ExpertUser
        restExpertUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expertUser)))
            .andExpect(status().isCreated());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExpertUser testExpertUser = expertUserList.get(expertUserList.size() - 1);
        assertThat(testExpertUser.getExpertise()).isEqualTo(DEFAULT_EXPERTISE);
    }

    @Test
    @Transactional
    void createExpertUserWithExistingId() throws Exception {
        // Create the ExpertUser with an existing ID
        expertUser.setId(1L);

        int databaseSizeBeforeCreate = expertUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpertUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expertUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExpertUsers() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        // Get all the expertUserList
        restExpertUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expertUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].expertise").value(hasItem(DEFAULT_EXPERTISE.toString())));
    }

    @Test
    @Transactional
    void getExpertUser() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        // Get the expertUser
        restExpertUserMockMvc
            .perform(get(ENTITY_API_URL_ID, expertUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expertUser.getId().intValue()))
            .andExpect(jsonPath("$.expertise").value(DEFAULT_EXPERTISE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExpertUser() throws Exception {
        // Get the expertUser
        restExpertUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpertUser() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();

        // Update the expertUser
        ExpertUser updatedExpertUser = expertUserRepository.findById(expertUser.getId()).get();
        // Disconnect from session so that the updates on updatedExpertUser are not directly saved in db
        em.detach(updatedExpertUser);
        updatedExpertUser.expertise(UPDATED_EXPERTISE);

        restExpertUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpertUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpertUser))
            )
            .andExpect(status().isOk());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
        ExpertUser testExpertUser = expertUserList.get(expertUserList.size() - 1);
        assertThat(testExpertUser.getExpertise()).isEqualTo(UPDATED_EXPERTISE);
    }

    @Test
    @Transactional
    void putNonExistingExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expertUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expertUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpertUserWithPatch() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();

        // Update the expertUser using partial update
        ExpertUser partialUpdatedExpertUser = new ExpertUser();
        partialUpdatedExpertUser.setId(expertUser.getId());

        restExpertUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpertUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpertUser))
            )
            .andExpect(status().isOk());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
        ExpertUser testExpertUser = expertUserList.get(expertUserList.size() - 1);
        assertThat(testExpertUser.getExpertise()).isEqualTo(DEFAULT_EXPERTISE);
    }

    @Test
    @Transactional
    void fullUpdateExpertUserWithPatch() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();

        // Update the expertUser using partial update
        ExpertUser partialUpdatedExpertUser = new ExpertUser();
        partialUpdatedExpertUser.setId(expertUser.getId());

        partialUpdatedExpertUser.expertise(UPDATED_EXPERTISE);

        restExpertUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpertUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpertUser))
            )
            .andExpect(status().isOk());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
        ExpertUser testExpertUser = expertUserList.get(expertUserList.size() - 1);
        assertThat(testExpertUser.getExpertise()).isEqualTo(UPDATED_EXPERTISE);
    }

    @Test
    @Transactional
    void patchNonExistingExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expertUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expertUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expertUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpertUser() throws Exception {
        int databaseSizeBeforeUpdate = expertUserRepository.findAll().size();
        expertUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expertUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpertUser in the database
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpertUser() throws Exception {
        // Initialize the database
        expertUserRepository.saveAndFlush(expertUser);

        int databaseSizeBeforeDelete = expertUserRepository.findAll().size();

        // Delete the expertUser
        restExpertUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, expertUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpertUser> expertUserList = expertUserRepository.findAll();
        assertThat(expertUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
