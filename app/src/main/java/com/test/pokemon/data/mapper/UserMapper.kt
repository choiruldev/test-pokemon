package com.test.pokemon.data.mapper

import com.test.pokemon.data.local.entity.UserEntity
import com.test.pokemon.domain.model.User

fun UserEntity.toDomain() = User(id = id, name = name, email = email)
