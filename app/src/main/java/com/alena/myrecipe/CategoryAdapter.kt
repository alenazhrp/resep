package com.alena.myrecipe

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val recipeList: List<DataClass>,  // List of recipes
    private val onRecipeClick: (DataClass) -> Unit,  // Click listener for viewing recipe details
    private val onUpdateClick: (DataClass) -> Unit,  // Click listener for updating recipe
    private val onDeleteClick: (DataClass) -> Unit   // Click listener for deleting recipe
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // ViewHolder class to hold views for each recipe item
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.img)  // Recipe image
        val recipeName: TextView = itemView.findViewById(R.id.recipeNameC)  // Recipe name
        val ivMenu: ImageView = itemView.findViewById(R.id.ivMenu)  // Popup menu icon

        // Bind data to the views
        fun bind(
            recipe: DataClass,  // Recipe data class
            onRecipeClick: (DataClass) -> Unit,  // Listener for viewing details
            onUpdateClick: (DataClass) -> Unit,  // Listener for updating recipe
            onDeleteClick: (DataClass) -> Unit   // Listener for deleting recipe
        ) {
            recipeName.text = recipe.name  // Set recipe name

            // Set image based on the category
            when (recipe.category) {
                "Breakfast" -> recipeImage.setImageResource(R.drawable.breakfastdua)
                "Lunch" -> recipeImage.setImageResource(R.drawable.lunchdua)
                "Dinner" -> recipeImage.setImageResource(R.drawable.dinnerdua)
                "Dessert" -> recipeImage.setImageResource(R.drawable.dessertdua)
                else -> recipeImage.setImageResource(R.drawable.pizza_sample)  // Default image
            }

            // Set item click listener to view recipe details
            itemView.setOnClickListener { onRecipeClick(recipe) }

            // Set click listener for the popup menu
            ivMenu.setOnClickListener {
                showPopupMenu(it, recipe, onUpdateClick, onDeleteClick)
            }
        }

        // Function to show the popup menu (for Update/Delete)
        private fun showPopupMenu(
            view: View,
            recipe: DataClass,
            onUpdateClick: (DataClass) -> Unit,  // Listener for updating recipe
            onDeleteClick: (DataClass) -> Unit   // Listener for deleting recipe
        ) {
            val popup = PopupMenu(view.context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu, popup.menu)  // Inflate the menu resource file

            // Handle menu item clicks (Update/Delete)
            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_update -> {
                        onUpdateClick(recipe)  // Call the update listener
                        true
                    }
                    R.id.action_delete -> {
                        onDeleteClick(recipe)  // Call the delete listener
                        true
                    }
                    else -> false
                }
            }
            popup.show()  // Show the popup menu
        }
    }

    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_rv, parent, false)
        return CategoryViewHolder(view)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe, onRecipeClick, onUpdateClick, onDeleteClick)  // Bind the data
    }

    // Return the total count of items
    override fun getItemCount(): Int {
        return recipeList.size
    }
}
