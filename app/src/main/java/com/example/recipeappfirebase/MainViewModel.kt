package com.example.recipeappfirebase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.recipeappfirebase.Retrofit.Information
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val connection= FireBaseConnection(application)

    fun gettingAllRecipes(): LiveData<List<Data>>{
        return connection.gettingAllRecipes()
    }

    fun addNewRecipe(recipe: Data){
        CoroutineScope(IO).launch {
            connection.addNewRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Data){
        CoroutineScope(IO).launch {
            connection.updateRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Data){
        CoroutineScope(IO).launch {
            connection.deleteRecipe(recipe)
        }
    }
}