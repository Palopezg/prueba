package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AccExecTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccExec.class);
        AccExec accExec1 = new AccExec();
        accExec1.setId(1L);
        AccExec accExec2 = new AccExec();
        accExec2.setId(accExec1.getId());
        assertThat(accExec1).isEqualTo(accExec2);
        accExec2.setId(2L);
        assertThat(accExec1).isNotEqualTo(accExec2);
        accExec1.setId(null);
        assertThat(accExec1).isNotEqualTo(accExec2);
    }
}
