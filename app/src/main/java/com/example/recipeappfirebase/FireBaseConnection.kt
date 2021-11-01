package com.example.recipeappfirebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeappfirebase.Retrofit.Information
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.muddz.styleabletoast.StyleableToast

class FireBaseConnection (private val context: Context) {

    private val firebase = Firebase.firestore
    private val liveData: MutableLiveData<List<Data>> = MutableLiveData<List<Data>>()

    fun gettingAllRecipes(): LiveData<List<Data>> {
        firebase.collection("Recipes")
            .addSnapshotListener{
                    snapshot, e ->

                //if there is an exception we want to skip
                if (e != null){
                    Log.d("MyData","Error: $e")
                    StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                    return@addSnapshotListener
                }

                if (snapshot != null){
                    //we have a populated snapshot
                    val list= arrayListOf<Data>()

                    val document= snapshot.documents
                    document.forEach{
                        // getting the note and save it
                        val pk= it.getString("PK")?.toInt()
                        val title= it.getString("Title")
                        val author= it.getString("Author")
                        val ingredients= it.getString("Ingredients")
                        val instructions= it.getString("instructions")
                        if (pk != null)
                            list.add(Data(it.id,pk,title,author,ingredients,instructions))
                    }
                    liveData.value = list
                }
            }
        return liveData
    }

    fun addNewRecipe(recipe: Data){
        // Create a new note with a pk and the note
        val newNote = hashMapOf(
            "PK" to recipe.pk.toString(),
            "Title" to recipe.title,
            "Author" to recipe.author,
            "Ingredients" to recipe.ingredients,
            "instructions" to recipe.instructions
        )

        // Add a new document with a generated ID
        firebase.collection("Recipes")
            .add(newNote)
            .addOnSuccessListener { documentReference ->
                StyleableToast.makeText(context, "Add Success", R.style.myToast).show()
                Log.d("MyData", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.d("MyData", "Error adding document", e)
            }
    }

    fun updateRecipe(recipe: Data){
        firebase.collection("Recipes")
            .document(recipe.key)
            .update("PK" , recipe.pk.toString(),
                "Title" , recipe.title,
                "Author" , recipe.author,
                "Ingredients" , recipe.ingredients,
                "instructions" , recipe.instructions)
            .addOnSuccessListener {
                StyleableToast.makeText(context, "Updated Success", R.style.myToast).show()
                Log.d("MyData", "Updated ID: ${recipe.key}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.d("MyData", "Error Updating document", e)
            }
    }

    fun deleteRecipe(recipe: Data){
        firebase.collection("Recipes")
            .document(recipe.key)
            .delete()
            .addOnSuccessListener {
                StyleableToast.makeText(context, "Deleted Success", R.style.myToast).show()
                Log.d("MyData", "Deleted ID: ${recipe.key}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.d("MyData", "Error Deleting document", e)
            }
    }
}