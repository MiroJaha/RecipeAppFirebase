package com.example.recipeappfirebase

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappfirebase.Retrofit.Information

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(this).get(
            MainViewModel::class.java
        )
    }

    private lateinit var addButton: Button
    private lateinit var rvInformation: RecyclerView
    private lateinit var informationList: ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.addButton)
        rvInformation = findViewById(R.id.rvInformation)
        informationList = arrayListOf()

        addButton.setOnClickListener {
            startActivity(Intent(this, AddNewRecipe::class.java))
        }
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")

        val intent = Intent(this@MainActivity, ShowRecipe::class.java)

        val adapter = RVAdapter(informationList)
        rvInformation.adapter = adapter
        rvInformation.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter.setOnItemClickListener(object : RVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                intent.putExtra("Key",informationList[position].key)
                intent.putExtra("pk", informationList[position].pk)
                intent.putExtra("title", informationList[position].title)
                intent.putExtra("author", informationList[position].author)
                intent.putExtra("ingredients", informationList[position].ingredients)
                intent.putExtra("instructions", informationList[position].instructions)
                startActivity(intent)
            }
        })

        mainViewModel.gettingAllRecipes().observe(this) { data ->
            progressDialog.show()
            informationList.clear()
            informationList.addAll(data)
            adapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }
    }
}