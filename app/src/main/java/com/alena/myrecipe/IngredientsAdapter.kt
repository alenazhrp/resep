package com.alena.myrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(
    private val ingredients: MutableList<String>,
    private val onDeleteClick: (Int) -> Unit // Callback for delete button
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIngredient: TextView = itemView.findViewById(R.id.tvIngredient)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.tvIngredient.text = ingredient

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position) // Invoke callback with item position
        }
    }

    override fun getItemCount(): Int = ingredients.size
}
