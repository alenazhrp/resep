package com.alena.myrecipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://my-recipe-bbbe7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private lateinit var recipeId: String

    // Declare views
    private lateinit var iv5: ImageView
    private lateinit var btnIngridient: Button
    private lateinit var btnStep: Button
    private lateinit var scrollIng: ScrollView
    private lateinit var scrollSteps: ScrollView
    private lateinit var title: TextView
    private lateinit var time: TextView
    private lateinit var ingData: TextView
    private lateinit var stepsData: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)

        // Initialize views
        iv5 = findViewById(R.id.iv5)
        btnIngridient = findViewById(R.id.btnIngridient)
        btnStep = findViewById(R.id.btnStep)
        scrollIng = findViewById(R.id.scrollIng)
        scrollSteps = findViewById(R.id.scrollSteps)
        title = findViewById(R.id.title)
        time = findViewById(R.id.time)
        ingData = findViewById(R.id.ing_data)
        stepsData = findViewById(R.id.steps_data)

        // Get the recipe ID from the intent
        recipeId = intent.getStringExtra("recipeId") ?: ""

        // Load recipe data if the recipe ID is valid
        if (recipeId.isNotEmpty()) {
            loadRecipeData(recipeId)
        } else {
            Toast.makeText(this, "Recipe ID not found", Toast.LENGTH_SHORT).show()
        }

        // Handle back button click
        iv5.setOnClickListener {
            onBackPressed()
        }

        // Set listeners for buttons to filter data
        btnIngridient.setOnClickListener {
            scrollIng.visibility = View.VISIBLE
            scrollSteps.visibility = View.GONE
        }

        btnStep.setOnClickListener {
            scrollSteps.visibility = View.VISIBLE
            scrollIng.visibility = View.GONE
        }
    }

    // Function to load recipe data from Firebase Realtime Database
    private fun loadRecipeData(recipeId: String) {
        val recipeRef = database.getReference("recipes").child(recipeId)

        recipeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val recipe = snapshot.getValue(DataClass::class.java)
                    recipe?.let {
                        title.text = it.name
                        time.text = it.cookingTime

                        // Menampilkan data ingredients
                        ingData.text = it.ingredients.joinToString(separator = "\n") { ingredient -> ingredient }

                        // Menampilkan data steps
                        stepsData.text = it.steps

                        // Set image based on category (for now using local drawables)
                        when (it.category) {
                            "Breakfast" -> iv5.setImageResource(R.drawable.breakfastdua)
                            "Lunch" -> iv5.setImageResource(R.drawable.lunchdua)
                            "Dinner" -> iv5.setImageResource(R.drawable.dinnerdua)
                            "Dessert" -> iv5.setImageResource(R.drawable.dessertdua)
                            else -> iv5.setImageResource(R.drawable.pizza_sample)
                        }
                    }
                } else {
                    Toast.makeText(this@RecipeActivity, "Recipe not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RecipeActivity, "Failed to load recipe", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
