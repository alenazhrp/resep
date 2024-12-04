package com.alena.myrecipe

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val myRef = database.getReference("recipes")
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<DataClass>()
    lateinit var search: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.search)
        search.setOnClickListener {
            val intent = Intent(this, SearchActivity2::class.java)
            startActivity(intent)
        }

        // Inisialisasi RecyclerView
        val rvPopular = findViewById<RecyclerView>(R.id.rv_popular)
        rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            // OnClick listener for each recipe item
            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("recipeId", recipe.id)
            startActivity(intent)
        }

// Attach the adapter to the RecyclerView
        rvPopular.adapter = recipeAdapter


        // Fetch popular recipes from Firebase
        fetchPopularRecipes()

        // Setup category clicks
        setupCategoryClicks()

        // Handle window insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up "Add Recipe" click action
        val addRecipeTextView = findViewById<TextView>(R.id.addrecipe)
        addRecipeTextView.setOnClickListener {
            // Open AddActivity when clicked
            val intent = Intent(this, AddActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPopularRecipes() {
        // Query to get popular recipes (e.g., based on views or ratings)
        myRef.orderByChild("popularity").limitToLast(10).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(DataClass::class.java)
                    recipe?.let { recipeList.add(it) }
                }
                recipeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun setupCategoryClicks() {
        findViewById<ImageView>(R.id.ivkategori1).setOnClickListener {
            openCategoryActivity("Breakfast")
        }
        findViewById<ImageView>(R.id.ivkategori2).setOnClickListener {
            openCategoryLunchActivity("Lunch")
        }
        findViewById<ImageView>(R.id.ivkategori3).setOnClickListener {
            openCategoryDinnerActivity("Dinner")
        }
        findViewById<ImageView>(R.id.ivkategori4).setOnClickListener {
            openCategoryDessertActivity("Dessert")
        }
    }

    private fun openCategoryActivity(category: String) {
        val intent = Intent(this, CategoryActivity2::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
    private fun openCategoryLunchActivity(category: String) {
        val intent = Intent(this, CategoryLunchActivity2::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
    private fun openCategoryDinnerActivity(category: String) {
        val intent = Intent(this, CategoryDinnerActivity2::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
    private fun openCategoryDessertActivity(category: String) {
        val intent = Intent(this, CategoryDessertActivity2::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}
