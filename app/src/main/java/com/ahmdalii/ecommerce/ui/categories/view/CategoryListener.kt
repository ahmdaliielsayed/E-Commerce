package com.ahmdalii.ecommerce.ui.categories.view

class CategoryListener(val clickListener: (categoryName: String) -> Unit) {

    fun onClick(categoryName: String) = clickListener(categoryName)
}