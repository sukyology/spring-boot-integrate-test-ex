package com.example.springbootintegratetestex

import com.example.springbootintegratetestex.api.TeamResponse
import com.example.springbootintegratetestex.persistence.Team
import com.example.springbootintegratetestex.persistence.TeamRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForObject
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.profiles.active=test"]) // can override any propertysource
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) // autowire without @autowired
class SpringBootIntegrateTestExApplicationTests(
    private val teamRepository: TeamRepository,
    private val restTemplate: TestRestTemplate
) {

    @Test
    @Transactional
    fun contextLoads() {
        println("context loads")
        println(dbContainer.containerInfo)
        val response = restTemplate.postForObject<TeamResponse>("/team")
        println(response?.id)

    }

    companion object {
        @JvmStatic
        protected val dbContainer = PostgreSQLContainer<Nothing>("postgres:13.4-alpine").apply {
            withDatabaseName("test-database")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", dbContainer::getJdbcUrl)
            registry.add("spring.datasource.password", dbContainer::getPassword)
            registry.add("spring.datasource.username", dbContainer::getUsername)
        }

        init {
            dbContainer.start()
        }
    }
}
