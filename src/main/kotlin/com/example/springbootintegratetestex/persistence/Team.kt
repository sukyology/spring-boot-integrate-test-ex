package com.example.springbootintegratetestex.persistence

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "team")
    var players: MutableList<Player> = mutableListOf()
}

interface TeamRepository: JpaRepository<Team, Long>

@Entity
class Player(
    val name: String = "익명"
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "id")
    lateinit var team: Team
}

interface PlayerRepository: JpaRepository<Player, Long>