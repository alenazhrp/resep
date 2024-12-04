package com.alena.myrecipe

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryActivity2 : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference("recipes")
    private lateinit var categoryAdapter: CategoryAdapter
    private val recipeList = mutableListOf<DataClass>()

    // Declare views
    private lateinit var rv_category: RecyclerView
    private lateinit var iv4: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category2)

        // Initialize views
        rv_category = findViewById(R.id.rv_category)
        iv4 = findViewById(R.id.iv4)
        title = findViewById(R.id.title)

        // Get category from intent, default to "Breakfast"
        val categoryName = intent.getStringExtra("Breakfast") ?: "Breakfast"
        title.text = categoryName

        // Setup RecyclerView
        categoryAdapter = CategoryAdapter(
            recipeList,
            onRecipeClick = { recipe ->
                // Open RecipeActivity to view recipe details
                val intent = Intent(this, RecipeActivity::class.java)
                intent.putExtra("recipeId", recipe.id)
                startActivity(intent)
            },
            onUpdateClick = { recipe ->
                // Open UpdateActivity2 to update the recipe
                val intent = Intent(this, UpdateActivity2::class.java)
                intent.putExtra("recipeId", recipe.id)
                startActivity(intent)
            },
            onDeleteClick = { recipe ->
                // Confirm and delete the recipe
                deleteRecipe(recipe.id)
            }
        )

        rv_category.layoutManager = LinearLayoutManager(this)
        rv_category.adapter = categoryAdapter

        // Load recipes based on selected category
        loadRecipesByCategory(categoryName)

        // Handle back button
        iv4.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Updated method for onBackPressed
        }
    }

    private fun loadRecipesByCategory(category: String) {
        database.orderByChild("category").equalTo("Breakfast")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    recipeList.clear()
                    if (snapshot.exists()) {
                        for (recipeSnapshot in snapshot.children) {
                            val recipe = recipeSnapshot.getValue(DataClass::class.java)
                            recipe?.let { recipeList.add(it) }
                        }
                        categoryAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@CategoryActivity2, "No recipes found in this category", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CategoryActivity2, "Failed to load recipes", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun deleteRecipe(recipeId: String) {
        val recipeRef = database.child(recipeId)
        recipeRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe deleted successfully", Toast.LENGTH_SHORT).show()
                // Optionally refresh the list after deletion
                loadRecipesByCategory(title.text.toString())
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show()
            }
    }
}
