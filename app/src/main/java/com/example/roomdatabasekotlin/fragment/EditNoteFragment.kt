package com.example.roomdatabasekotlin.fragment

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.example.roomdatabasekotlin.R
import com.example.roomdatabasekotlin.activity.MainActivity
import com.example.roomdatabasekotlin.databinding.FragmentEditNoteBinding
import com.example.roomdatabasekotlin.model.Note
import com.example.roomdatabasekotlin.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    private var fragmentEditNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = fragmentEditNoteBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var editNoteView: View
    private lateinit var currentNote: Note
    private val args: EditNoteFragmentArgs by navArgs<EditNoteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentEditNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        editNoteView = view
        currentNote = args.note!!

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.etEditNoteTitle.setText(currentNote.title)
        binding.etEditNoteContent.setText(currentNote.content)
        binding.fabEditNote.setOnClickListener {
            editeNote(view)
        }
    }

    private fun editeNote(view: View) {
        val noteTitle = binding.etEditNoteTitle.text.toString().trim()
        val noteContent = binding.etEditNoteContent.text.toString().trim()
        val note = Note(currentNote.id, noteTitle, noteContent)
        if(noteTitle.isNotEmpty()) {
            noteViewModel.updateNote(note)
            Toast.makeText(editNoteView.context, "Note Edited", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(editNoteView.context, "Please enter note title!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote(view: View) {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure to delete this note?")
            setPositiveButton("Delete") {_, _ ->
                noteViewModel.deleteNote(currentNote)
                Toast.makeText(editNoteView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.deleteMenuItem -> {
                deleteNote(editNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEditNoteBinding = null
    }

}