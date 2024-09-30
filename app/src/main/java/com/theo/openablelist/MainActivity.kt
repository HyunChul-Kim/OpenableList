package com.theo.openablelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theo.openablelistdemo.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CategoryAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setData(makeCategory())
    }

    private fun makeCategory(): List<Section<String, String>> {
        return listOf(
            makeSection("Category#1", 5),
            makeSection("Category#2", 2),
            makeSection("Category#3", 3),
            makeSection("Category#4", 9),
            makeSection("Category#5", 7),
        )
    }

    private fun makeSection(title: String, subCount: Int): Section<String, String> {
        return Section(
            data = title,
            isOpen = true,
            items = Array(subCount) {
                "SubCategory#$it"
            }.toList()
        )
    }
}