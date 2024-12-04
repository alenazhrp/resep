package com.alena.myrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val recipeList: List<DataClass>) : RecyclerView.Adapter<SearchAdapter.RecipeViewHolder>() {

    // ViewHolder class that holds the view components for each recipe item
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.resepimage) // ImageView for the recipe image
        val recipeName: TextView = itemView.findViewById(R.id.recipeNameS)  // TextView for the recipe name
    }

    // Inflating the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_search, parent, false)
        return RecipeViewHolder(view)
    }

    // Binding data (recipe name and category image) to each item in the RecyclerView
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeName.text = recipe.name

        // Setting the recipe image based on the category
        when (recipe.category) {
            "Breakfast" -> holder.recipeImage.setImageResource(R.drawable.breakfastdua)
            "Lunch" -> holder.recipeImage.setImageResource(R.drawable.lunchdua)
            "Dinner" -> holder.recipeImage.setImageResource(R.drawable.dinnerdua)
            "Dessert" -> holder.recipeImage.setImageResource(R.drawable.dessertdua)
            else -> holder.recipeImage.setImageResource(R.drawable.pizza_sample) // Default image
        }
    }

    // Returning the size of the recipe list
    override fun getItemCount(): Int {
        return recipeList.size
    }
}
