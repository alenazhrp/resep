package com.alena.myrecipe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateActivity2 : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var recipeId: String
    private val ingredients = mutableListOf<String>()
    private lateinit var ingredientAdapter: IngredientsAdapter
    private lateinit var recipeCategory: String  // Variabel untuk menyimpan kategori

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update2)

        // Ambil recipeId dari intent
        recipeId = intent.getStringExtra("recipeId") ?: ""
        databaseRef = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("recipes")
            .child(recipeId)

        // Setup RecyclerView untuk ingredients
        ingredientAdapter = IngredientsAdapter(ingredients, onDeleteClick = { position ->
            ingredients.removeAt(position)
            ingredientAdapter.notifyItemRemoved(position)
        })

        val rvIngredients = findViewById<RecyclerView>(R.id.rvIngredients)
        rvIngredients.layoutManager = LinearLayoutManager(this)
        rvIngredients.adapter = ingredientAdapter

        // Load data resep yang ada
        loadRecipeData()

        // Button untuk menambah ingredient
        val btnAddIngredient = findViewById<Button>(R.id.btnAddIngredient)
        val etNewIngredient = findViewById<EditText>(R.id.etNewIngredient)

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

        // Button untuk update recipe
        val btnSaveRecipe = findViewById<Button>(R.id.btnSaveRecipe)
        btnSaveRecipe.setOnClickListener {
            updateRecipe()
        }
    }

    private fun loadRecipeData() {
        val etRecipeName = findViewById<EditText>(R.id.etRecipeName)
        val etCookingTime = findViewById<EditText>(R.id.etCookingTime)
        val etSteps = findViewById<EditText>(R.id.etSteps)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipe = snapshot.getValue(DataClass::class.java)
                recipe?.let {
                    etRecipeName.setText(it.name)
                    etCookingTime.setText(it.cookingTime)
                    etSteps.setText(it.steps)

                    // Simpan kategori yang ada
                    recipeCategory = it.category  // Simpan kategori untuk digunakan nanti

                    ingredients.clear()
                    ingredients.addAll(it.ingredients)  // Menggunakan list ingredients yang ada
                    ingredientAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateActivity2, "Failed to load recipe", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecipe() {
        val etRecipeName = findViewById<EditText>(R.id.etRecipeName)
        val etCookingTime = findViewById<EditText>(R.id.etCookingTime)
        val etSteps = findViewById<EditText>(R.id.etSteps)

        val name = etRecipeName.text.toString()
        val cookingTime = etCookingTime.text.toString()
        val steps = etSteps.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please fill out the recipe name", Toast.LENGTH_SHORT).show()
            return
        }

        if (cookingTime.isEmpty()) {
            Toast.makeText(this, "Please fill out the cooking time", Toast.LENGTH_SHORT).show()
            return
        }

        if (steps.isEmpty()) {
            Toast.makeText(this, "Please fill out the steps", Toast.LENGTH_SHORT).show()
            return
        }

        if (ingredients.isEmpty()) {
            Toast.makeText(this, "Please add at least one ingredient", Toast.LENGTH_SHORT).show()
            return
        }

        // Gunakan kategori yang sudah ada tanpa mengubahnya
        val recipe = DataClass(
            id = recipeId,
            name = name,
            ingredients = ingredients,  // Menggunakan ingredients sebagai List<String>
            steps = steps,
            cookingTime = cookingTime,
            category = recipeCategory  // Kategori yang sudah ada
        )

        databaseRef.setValue(recipe)
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update recipe", Toast.LENGTH_SHORT).show()
            }
    }
}
