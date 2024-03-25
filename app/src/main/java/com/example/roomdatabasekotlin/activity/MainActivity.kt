package com.example.roomdatabasekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabasekotlin.R
import com.example.roomdatabasekotlin.database.NoteDatabase
import com.example.roomdatabasekotlin.repository.NoteRepository
import com.example.roomdatabasekotlin.viewmodel.NoteViewModel
import com.example.roomdatabasekotlin.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }
    private fun setupViewModel() {
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}