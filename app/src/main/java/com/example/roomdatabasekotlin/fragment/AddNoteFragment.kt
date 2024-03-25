package com.example.roomdatabasekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.roomdatabasekotlin.R
import com.example.roomdatabasekotlin.activity.MainActivity
import com.example.roomdatabasekotlin.databinding.FragmentAddNoteBinding
import com.example.roomdatabasekotlin.viewmodel.NoteViewModel

class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private var fragmentAddNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = fragmentAddNoteBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddNoteBinding = FragmentAddNoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel

        addNoteView = view

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.etAddNoteTitle.text.toString()
        val noteContent = binding.etAddNoteContent.text.toString()
        if (noteTitle.isNotEmpty()) {
            val note = com.example.roomdatabasekotlin.model.Note(0, noteTitle, noteContent)
            noteViewModel.addNote(note)
            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addNoteView.context, "Please enter note title!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.add_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.saveMenuItem -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAddNoteBinding = null
    }
}