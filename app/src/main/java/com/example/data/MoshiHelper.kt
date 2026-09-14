package com.example.data

import com.example.model.AlloyComponent
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiHelper {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, AlloyComponent::class.java)
    private val adapter = moshi.adapter<List<AlloyComponent>>(listType)

    fun toJson(alloys: List<AlloyComponent>): String {
        return adapter.toJson(alloys)
    }

    fun fromJson(json: String): List<AlloyComponent> {
        return try {
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
