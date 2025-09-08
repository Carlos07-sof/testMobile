package com.example.foodike.presentation.components

import Image
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImagenViewModel: ViewModel() {

    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images: StateFlow<List<Image>> = _images

    fun updateImages(newImages: List<Image>) {
        _images.value = newImages
    }
}