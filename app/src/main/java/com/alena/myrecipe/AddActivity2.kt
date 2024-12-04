package com.alena.myrecipe

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AddActivity2 : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("recipes")
    private val ingredients = mutableListOf<String>()
    private lateinit var ingredientAdapter: IngredientsAdapter

    private lateinit var rvIngredients: RecyclerView
    private lateinit var btnAddIngredient: Button
    private lateinit var etNewIngredient: EditText
    private lateinit var btnSaveRecipe: Button
    private lateinit var etRecipeName: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etCookingTime: EditText
    private lateinit var etSteps: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the appropriate layout depending on the requirement
        setContentView(R.layout.activity_add2)  // Use your actual layout here (activity_add2 or activity_add)

        // Inisialisasi views
        rvIngredients = findViewById(R.id.rvIngredients)
        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        etNewIngredient = findViewById(R.id.etNewIngredient)
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe)
        etRecipeName = findViewById(R.id.etRecipeName)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etCookingTime = findViewById(R.id.etCookingTime)
        etSteps = findViewById(R.id.etSteps)

        // Initialize the RecyclerView adapter with a delete click listener
        ingredientAdapter = IngredientsAdapter(ingredients) { position ->
            // Handle delete click for the ingredient at the given position
            ingredients.removeAt(position)  // Remove the ingredient from the list
            ingredientAdapter.notifyItemRemoved(position)  // Notify adapter to update UI
        }

        // Setup RecyclerView
        rvIngredients.layoutManager = LinearLayoutManager(this)
        rvIngredients.adapter = ingredientAdapter

        // Button to add ingredients
        btnAddIngredient.setOnClickListener {
            val ingredient = etNewIngredient.text.toString()
            if (ingredient.isNotEmpty()) {
                ingredients.add(ingredient)
                ingredientAdapter.notifyDataSetChanged()
                etNewIngredient.text.clear()
            } else {
                Toast.makeText(this, "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        val categories = arrayOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")

        // Inisialisasi spinner
        spinnerCategory = findViewById(R.id.spinnerCategory)

        // Button to save recipe
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter ke spinner
        spinnerCategory.adapter = categoryAdapter

        // Ambil kategori terpilih saat menyimpan resep
        btnSaveRecipe.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem.toString()
            saveRecipe(selectedCategory)
        }
    }

    private fun saveRecipe(selectedCategory: String) {
        val name = etRecipeName.text.toString()
        val cookingTime = etCookingTime.text.toString()
        val steps = etSteps.text.toString()

        if (name.isEmpty() || cookingTime.isEmpty() || steps.isEmpty() || ingredients.isEmpty()) {
            Toast.makeText(this, "Silahkan isi semua kolom", Toast.LENGTH_SHORT).show()
            return
        }

        val recipeId = database.push().key ?: ""
        val recipe = DataClass(recipeId, name, ingredients, steps, cookingTime, selectedCategory)

        database.child(recipeId).setValue(recipe)
            .addOnSuccessListener {
                Toast.makeText(this, "Resep tersimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan resep", Toast.LENGTH_SHORT).show()
            }
    }
}
