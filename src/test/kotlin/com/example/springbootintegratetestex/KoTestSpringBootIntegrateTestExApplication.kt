package com.example.springbootintegratetestex

import com.example.springbootintegratetestex.persistence.Player
import com.example.springbootintegratetestex.persistence.Team
import com.example.springbootintegratetestex.persistence.TeamRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import net.ttddyy.dsproxy.QueryCountHolder
import net.ttddyy.dsproxy.listener.ChainListener
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.profiles.active=test"]) // can override any propertysource
class KoTestSpringBootIntegrateTestExApplication(
    private val teamRepository: TeamRepository,
    private val restTemplate: TestRestTemplate
) : WordSpec({


}) {

    override fun extensions(): List<Extension> {
        return listOf(SpringExtension)
    }

    init {
        "context 생성" should {
            "context 생성" {
                dbContainer.isRunning shouldBe true
            }
        }

        //테스트로 인식을 못함
        "data 생성" should {

            teamRepository.save(Team(
            ).apply {
                players.add(Player())
                players.add(Player())
                players.add(Player())
            })

            val insertCount = QueryCountHolder.getGrandTotal().insert

            insertCount shouldBe 4

            val team = teamRepository.findAll()

            val queryCount = QueryCountHolder.getGrandTotal().select

            queryCount shouldBe 1


            
        }
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
