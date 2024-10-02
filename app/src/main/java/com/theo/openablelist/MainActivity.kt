package com.theo.openablelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.theo.openablelist.data.Category
import com.theo.openablelist.model.Child
import com.theo.openablelist.model.Group
import com.theo.openablelist.model.Parent
import com.theo.openablelist.model.Section
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
        val adapter2 = CategoryListAdapter(
            categories = makeCategoryBySection(5)
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter2
        adapter2.update()
    }

    private fun makeCategoryBySection(size: Int): List<Section<String, String>> {
        val random = Random(size)
        return List(size) {
            makeSection(
                title = "Category#${it + 1}",
                subCount = random.nextInt(0, 10)
            )
        }
    }

    private fun makeSection(title: String, subCount: Int): Section<String, String> {
        return Section(
            parent = Parent(data = title, isOpened = false, key = title),
            children = List(subCount) {
                Child(
                    data = "SubCategory#${it + 1}",
                    isSelected = false,
                    key = "$title${it + 1}"
                )
            },
        )
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
            parent = Parent(data = title, key = title),
            children = List(subCount) {
                Child(
                    data = "SubCategory#${it + 1}",
                    key = "$title${it + 1}"
                )
            }
        )
    }
}