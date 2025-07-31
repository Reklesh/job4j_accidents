package ru.job4j.accidents;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DataSourceConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void whenCheckContextThenAllExpectedBeansAreLoaded() {
        assertThat(context).isNotNull();

        assertThat(context.getBean(DataSource.class)).isNotNull();
        assertThat(context.getBean(JdbcTemplate.class)).isNotNull();
        assertThat(context.getBean(LocalSessionFactoryBean.class)).isNotNull();

        assertThat(context.getBean("entityManagerFactory")).isNotNull();
        assertThat(context.getBean("transactionManager")).isNotNull();
        assertThat(context.getBean("htx")).isNotNull();

        assertThat(context.getBean("userRepository")).isNotNull();
        assertThat(context.getBean("authorityRepository")).isNotNull();
        assertThat(context.getBean("jpaAccidentRepository")).isNotNull();
        assertThat(context.getBean("hbmAccidentRepository")).isNotNull();

        assertThat(context.getBean("jpaAccidentService")).isNotNull();
        assertThat(context.getBean("simpleAccidentService")).isNotNull();
    }

    @Test
    void whenGetConnectionThenH2DatabaseIsUsed() throws SQLException {
        String url = dataSource.getConnection().getMetaData().getURL();
        assertThat(url).isEqualTo("jdbc:h2:./testdb");
    }

    @Test
    void whenCheckDataSourceThenH2DriverIsUsed() throws SQLException {
        String driverName = dataSource.getConnection().getMetaData().getDriverName();
        assertThat(driverName).contains("H2");
    }

    @Test
    void whenCheckJdbcTemplateThenItWorksWithH2() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1 + 1", Integer.class);
        assertThat(result).isEqualTo(2);
    }

    @Test
    void whenCheckDatabasePropertiesThenTestConfigIsApplied() throws SQLException {
        String username = dataSource.getConnection().getMetaData().getUserName();
        assertThat(username).isNullOrEmpty();
    }
}
