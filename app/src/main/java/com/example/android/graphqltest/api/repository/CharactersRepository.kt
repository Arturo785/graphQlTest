package com.example.android.graphqltest.api.repository

import com.apollographql.apollo.api.Response
import com.example.android.graphqltest.api.callWrappers.ResultWrapper
import com.example.graphqlapollo.CharactersListQuery

interface CharactersRepository {

    suspend fun getCharacters(): ResultWrapper<Response<CharactersListQuery.Data>>

}