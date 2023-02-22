package com.example.android.graphqltest.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.graphqltest.R
import com.example.android.graphqltest.databinding.ActivityMainBinding
import com.example.android.graphqltest.ui.recycler.CharactersListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: CharacterViewModel by viewModels()
    private lateinit var charactersListAdapter: CharactersListAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        charactersListAdapter = CharactersListAdapter(listener2 = {

        }, listener = {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })

        binding.charactersList.apply {
            adapter = charactersListAdapter
        }
        observeUiState()
        setObservers()

    }

    private fun setObservers() {
        viewModel.charactersList.observe(this) {
            charactersListAdapter.submitList(it)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collectLatest {
                        when (it) {
                            is CharacterViewModel.UiEvent.ErrorMessage -> {
                                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}