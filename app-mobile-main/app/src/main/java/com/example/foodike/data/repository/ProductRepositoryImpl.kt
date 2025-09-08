package com.example.foodike.data.repository

import com.example.foodike.domain.model.Producto
import com.example.foodike.domain.repository.ProductRepository

class ProductRepositoryImpl(): ProductRepository {

    private val productList = mutableListOf(
        Producto(1, "Hamburguesa", 5.99),
        Producto(2, "Pizza", 8.99),
        Producto(3, "Sushi", 12.50)
    )

    override suspend fun getAllProducts(): Results<List<Producto>> {
        return Results.Success(productList)

    }

    override suspend fun getProductById(id: Int): Results<Producto?> {
        val product = productList.find { it.id == id }
        return if (product != null) {
            Results.Success(product)
        } else {
            Results.Error("Producto no encontrado")
        }
    }

    override suspend fun addProduct(product: Producto): Results<Unit> {
        return try {
            productList.add(product)
            Results.Success(Unit)
        } catch (e: Exception) {
            Results.Error("Error al agregar el producto")
        }
    }

    override suspend fun updateProduct(product: Producto): Results<Unit> {
        val index = productList.indexOfFirst { it.id == product.id }
        return if (index != -1) {
            productList[index] = product
            Results.Success(Unit)
        } else {
            Results.Error("Producto no encontrado")
        }
    }

    override suspend fun deleteProduct(id: Int): Results<Unit> {

        val removed = productList.removeIf { it.id == id }
        return if (removed) {
            Results.Success(Unit)
        } else {
            Results.Error("Producto no encontrado")
        }
    }

}