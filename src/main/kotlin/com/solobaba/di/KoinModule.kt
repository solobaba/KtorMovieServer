package com.solobaba.di

import com.solobaba.repository.HeroRepository
import com.solobaba.repository.HeroRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepository> {
        HeroRepositoryImpl()
    }
}