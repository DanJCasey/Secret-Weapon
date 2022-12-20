package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class ExpertUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpertUser.class);
        ExpertUser expertUser1 = new ExpertUser();
        expertUser1.setId(1L);
        ExpertUser expertUser2 = new ExpertUser();
        expertUser2.setId(expertUser1.getId());
        assertThat(expertUser1).isEqualTo(expertUser2);
        expertUser2.setId(2L);
        assertThat(expertUser1).isNotEqualTo(expertUser2);
        expertUser1.setId(null);
        assertThat(expertUser1).isNotEqualTo(expertUser2);
    }
}
