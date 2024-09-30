package com.theo.openablelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.theo.openablelist.data.Category
import com.theo.openablelistdemo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CategoryAdapter(
            categories = makeCategoryByGroup(6),
            onClickParent = {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            },
            onClickChild = {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    private fun makeCategoryByGroup(size: Int): List<Group<String, String>> {
        val random = Random(size)
        return List(size) {
            makeGroup(
                title = "Category#${it + 1}",
                subCount = random.nextInt(0, 10)
            )
        }
    }

    private fun makeGroup(title: String, subCount: Int): Group<String, String> {
        return Category(
            parent = title,
            children = List(subCount) {
                "SubCategory#${it + 1}"
            }
        )
    }
}