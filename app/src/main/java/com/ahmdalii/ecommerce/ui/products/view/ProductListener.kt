package com.ahmdalii.ecommerce.ui.products.view

import com.ahmdalii.ecommerce.data.model.Product

class ProductListener(val clickListener: (product: Product) -> Unit) {

    fun onClick(product: Product) = clickListener(product)
}