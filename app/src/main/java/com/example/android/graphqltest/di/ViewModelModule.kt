package com.example.android.graphqltest.di

import com.example.android.graphqltest.api.repository.CharactersRepository
import com.example.android.graphqltest.api.repository.CharactersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: CharactersRepositoryImpl): CharactersRepository

}