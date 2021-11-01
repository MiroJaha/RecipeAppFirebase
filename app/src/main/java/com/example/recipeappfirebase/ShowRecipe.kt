package com.example.recipeappfirebase

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappfirebase.Retrofit.Information
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.muddz.styleabletoast.StyleableToast

class ShowRecipe : AppCompatActivity() {

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var titleTV: EditText
    private lateinit var authorTV: EditText
    private lateinit var ingredientsTV: EditText
    private lateinit var instructionsTV: EditText
    private lateinit var backButton: FloatingActionButton
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var bundle: Bundle
    private lateinit var key: String
    private var pk= 0
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var ingredients: String
    private lateinit var instructions: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_recipe_information)

        titleTV= findViewById(R.id.titleTV)
        authorTV= findViewById(R.id.authorTV)
        ingredientsTV= findViewById(R.id.ingredientsTv)
        instructionsTV= findViewById(R.id.instructionsTV)
        backButton= findViewById(R.id.backButton)
        editButton= findViewById(R.id.editButton)
        deleteButton= findViewById(R.id.deleteButton)

        bundle= intent.extras!!
        key= bundle.getString("Key")!!
        pk= bundle.getInt("pk")
        title= bundle.getString("title")!!
        author= bundle.getString("author")!!
        ingredients= bundle.getString("ingredients")!!
        instructions= bundle.getString("instructions")!!

        setHint()

        editButton.setOnClickListener{
            if (checkEntry()){
                mainViewModel.updateRecipe(Data(key,pk, title, author, ingredients, instructions))
                clearEntry()
                setHint()
            }
            else
                StyleableToast.makeText(
                    this,
                    "Please Enter Valid Values!!",
                    R.style.myToast
                ).show()
        }
        deleteButton.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Are You Sure You Want To Delete This Recipe")
                .setCancelable(false)
                .setPositiveButton("YES"){_,_ ->
                    mainViewModel.deleteRecipe(Data(key,pk, title, author, ingredients, instructions))
                    finish()
                }
                .setNegativeButton("Cancel"){dialog,_ -> dialog.cancel() }
                .show()
        }
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun clearEntry() {
        titleTV.text.clear()
        titleTV.clearFocus()
        authorTV.text.clear()
        authorTV.clearFocus()
        ingredientsTV.text.clear()
        ingredientsTV.clearFocus()
        instructionsTV.text.clear()
        instructionsTV.clearFocus()
    }

    private fun checkEntry(): Boolean {
        var check= false
        if (titleTV.text.isNotBlank()) {
            title= titleTV.text.toString()
            check= true
        }
        if (authorTV.text.isNotBlank()) {
            author= authorTV.text.toString()
            check= true
        }
        if (ingredientsTV.text.isNotBlank()) {
            ingredients= ingredientsTV.text.toString()
            check= true
        }
        if (instructionsTV.text.isNotBlank()) {
            instructions= instructionsTV.text.toString()
            check= true
        }
        return check
    }

    private fun setHint() {
        titleTV.hint= "Recipe Name: $title"
        authorTV.hint= "Author Name: $author"
        ingredientsTV.hint= "Ingredients:\n\n$ingredients"
        instructionsTV.hint= "Instructions:\n\n$instructions"
    }
}