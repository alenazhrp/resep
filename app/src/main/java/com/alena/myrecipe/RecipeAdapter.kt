package com.alena.myrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(
    private val recipeList: List<DataClass>,
    private val onClick: (DataClass) -> Unit  // Updated to be more specific
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.resepimage)
        val recipeName: TextView = itemView.findViewById(R.id.recipeNameS)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_search, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeName.text = recipe.name

        // Set image based on the category
        when (recipe.category) {
            "Breakfast" -> holder.recipeImage.setImageResource(R.drawable.breakfastdua)
            "Lunch" -> holder.recipeImage.setImageResource(R.drawable.lunchdua)
            "Dinner" -> holder.recipeImage.setImageResource(R.drawable.dinnerdua)
            "Dessert" -> holder.recipeImage.setImageResource(R.drawable.dessertdua)
            else -> holder.recipeImage.setImageResource(R.drawable.pizza_sample)
        }

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onClick(recipe)  // Pass the clicked recipe to the callback
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}
