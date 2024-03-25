package com.example.roomdatabasekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roomdatabasekotlin.R
import com.example.roomdatabasekotlin.activity.MainActivity
import com.example.roomdatabasekotlin.adapter.RecyclerViewAdapter
import com.example.roomdatabasekotlin.databinding.FragmentHomeBinding
import com.example.roomdatabasekotlin.databinding.LayoutNoteBinding
import com.example.roomdatabasekotlin.model.Note
import com.example.roomdatabasekotlin.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home){
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteRecyclerViewAdapter: RecyclerViewAdapter<Note>
    private lateinit var layoutNoteBinding: LayoutNoteBinding
    private lateinit var notes: List<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = (activity as MainActivity).noteViewModel

        notes = mutableListOf()
        setupNoteRecyclerView()

        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.getAllNotes().observe(viewLifecycleOwner) {notesObserved ->
            noteRecyclerViewAdapter.refreshData(notesObserved)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentHomeBinding = null
    }

    private fun setupNoteRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteRecyclerViewAdapter = RecyclerViewAdapter(R.layout.layout_note, notes, true) {view, item, position ->
            layoutNoteBinding = LayoutNoteBinding.bind(view)
            layoutNoteBinding.tvNoteTitle.text = item.title
            layoutNoteBinding.tvNoteContent.text = item.content
            layoutNoteBinding.root.setOnClickListener {
                val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(item)
                it.findNavController().navigate(direction)
            }
        }
        binding.rvNotes.apply {
            layoutManager = staggeredGridLayoutManager
            setHasFixedSize(true)
            adapter = noteRecyclerViewAdapter
        }

        activity?.let {
            noteViewModel.getAllNotes().observe(viewLifecycleOwner) {notesObserved ->
                notes = notesObserved
                updateUI(notes)
            }
        }
    }

    private fun updateUI(notes: List<Note>?) {
        if (notes != null) {
            if (notes.isNotEmpty()) {
                binding.ivEmptyNotes.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
            } else {
                binding.ivEmptyNotes.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            }
        }
    }
}