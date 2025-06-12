package com.hackaton.desafio;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties =
        "spring.datasource.url=jdbc:h2:mem:testdb" +
        "spring.jpa.show-sql=true," +
        "spring.jpa.hibernate.ddl-auto=create-drop")
public class BenefityRepositoryTest {
}
