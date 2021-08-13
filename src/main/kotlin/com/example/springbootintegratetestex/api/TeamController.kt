package com.example.springbootintegratetestex.api

import com.example.springbootintegratetestex.persistence.Player
import com.example.springbootintegratetestex.persistence.Team
import com.example.springbootintegratetestex.persistence.TeamRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/team")
class TeamController(
    private val teamRepository: TeamRepository
) {

    @PostMapping("")
    fun createTeam() = teamRepository.save(Team().also {
        it.players.add(Player().apply { team = it })
    }).let {
        TeamResponse(it.id)
    }
}

data class TeamResponse(
    val id: Long?
)