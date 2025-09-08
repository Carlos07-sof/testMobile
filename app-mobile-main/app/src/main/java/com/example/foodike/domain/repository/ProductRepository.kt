package com.example.foodike.domain.repository

import com.example.foodike.data.repository.Results
import com.example.foodike.domain.model.Producto

interface ProductRepository {
    suspend fun getAllProducts(): Results<List<Producto>>
    suspend fun getProductById(id: Int): Results<Producto?>
    suspend fun addProduct(product: Producto): Results<Unit>
    suspend fun updateProduct(product: Producto): Results<Unit>
    suspend fun deleteProduct(id: Int): Results<Unit>
}
