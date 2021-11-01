package com.example.recipeappfirebase

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recipeappfirebase.Retrofit.APIClient
import com.example.recipeappfirebase.Retrofit.APIInterface
import com.example.recipeappfirebase.Retrofit.Information
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewRecipe : AppCompatActivity() {

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var saveButton: Button
    private lateinit var viewButton: Button
    private lateinit var callData: Button
    private lateinit var titleEntry: EditText
    private lateinit var authorEntry: EditText
    private lateinit var ingredientsEntry: EditText
    private lateinit var instructionsEntry: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        saveButton= findViewById(R.id.saveButton)
        viewButton= findViewById(R.id.viewButton)
        callData= findViewById(R.id.callData)
        titleEntry= findViewById(R.id.titleEntry)
        authorEntry= findViewById(R.id.authorEntry)
        ingredientsEntry= findViewById(R.id.ingredientsEntry)
        instructionsEntry= findViewById(R.id.instructionsEntry)

        viewButton.setOnClickListener{
            finish()
        }

        callData.setOnClickListener{
            callFromApi()
        }

        saveButton.setOnClickListener{
            if (checkEntry()){
                addNewRecipe()
                titleEntry.text.clear()
                authorEntry.text.clear()
                ingredientsEntry.text.clear()
                instructionsEntry.text.clear()
                val view: View? = this@AddNewRecipe.currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            else{
                StyleableToast.makeText(this,"Please Enter Correct Values",R.style.myToast).show()
            }
        }
    }

    private fun callFromApi() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.getInformation()?.enqueue(object : Callback<List<Information>> {
            override fun onResponse(call: Call<List<Information>>, response: Response<List<Information>>) {
                try {
                    for (recipe in response.body()!!) {
                        mainViewModel.addNewRecipe(
                            Data(
                                "",
                                recipe.pk,
                                recipe.title!!,
                                recipe.author!!,
                                recipe.ingredients!!,
                                recipe.instructions!!
                            )
                        )
                    }
                    progressDialog.dismiss()
                } catch (e: Exception) {
                    Log.d("MyInformation", "failed")
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<Information>>, t: Throwable) {
                StyleableToast.makeText(this@AddNewRecipe, "Failed ", R.style.myToast)
                    .show()
                progressDialog.dismiss()
            }
        })
    }

    private fun checkEntry() : Boolean{
        return when {
            titleEntry.text.isBlank() -> false
            authorEntry.text.isBlank() -> false
            ingredientsEntry.text.isBlank() -> false
            else -> instructionsEntry.text.isNotBlank()
        }
    }

    private fun addNewRecipe(){
        mainViewModel.addNewRecipe(
            Data(
                "",
                0,
                titleEntry.text.toString(),
                authorEntry.text.toString(),
                ingredientsEntry.text.toString(),
                instructionsEntry.text.toString()
            )
        )
    }
}