package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AccountExecutiveTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountExecutive.class);
        AccountExecutive accountExecutive1 = new AccountExecutive();
        accountExecutive1.setId(1L);
        AccountExecutive accountExecutive2 = new AccountExecutive();
        accountExecutive2.setId(accountExecutive1.getId());
        assertThat(accountExecutive1).isEqualTo(accountExecutive2);
        accountExecutive2.setId(2L);
        assertThat(accountExecutive1).isNotEqualTo(accountExecutive2);
        accountExecutive1.setId(null);
        assertThat(accountExecutive1).isNotEqualTo(accountExecutive2);
    }
}
