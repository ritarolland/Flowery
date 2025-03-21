package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory class responsible for creating instances of ViewModels.
 * It is used to inject dependencies into ViewModels and provide them when required.
 *
 * @param creators A map of ViewModel classes to their corresponding creators (providers).
 *
 * @author Sofia Bakalskaya
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    /**
     * Creates an instance of the requested ViewModel class.
     *
     * @param modelClass The ViewModel class to create.
     * @return An instance of the requested ViewModel class.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}