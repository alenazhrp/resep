package com.alena.myrecipe

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SearchActivity2 : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val myRef = database.getReference("recipes")
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<DataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search2)

        // Inisialisasi RecyclerView
        val rvSearch = findViewById<RecyclerView>(R.id.rv_search)
        rvSearch.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            // OnClick listener untuk setiap item resep
            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("recipeId", recipe.id)
            startActivity(intent)
        }
        rvSearch.adapter = recipeAdapter

        // Inisialisasi SearchView
        val searchBar = findViewById<SearchView>(R.id.searchBar)

        // Muat semua resep awal
        loadRecipes()

        // Fungsi pencarian
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchRecipe(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchRecipe(newText)
                }
                return true
            }
        })
    }

    private fun loadRecipes() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                if (snapshot.exists()) {
                    for (recipeSnapshot in snapshot.children) {
                        val recipe = recipeSnapshot.getValue(DataClass::class.java)
                        recipe?.let { recipeList.add(it) }
                    }
                    recipeAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error jika diperlukan
            }
        })
    }

    private fun searchRecipe(query: String) {
        val searchQuery = query.lowercase()
        myRef.orderByChild("name").startAt(searchQuery).endAt("$searchQuery\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    recipeList.clear()
                    if (snapshot.exists()) {
                        for (recipeSnapshot in snapshot.children) {
                            val recipe = recipeSnapshot.getValue(DataClass::class.java)
                            recipe?.let { recipeList.add(it) }
                        }
                        recipeAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tangani error jika diperlukan
                }
            })
    }
}
