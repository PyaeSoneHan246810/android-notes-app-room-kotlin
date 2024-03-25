package com.example.roomdatabasekotlin.repository

import com.example.roomdatabasekotlin.database.NoteDatabase
import com.example.roomdatabasekotlin.model.Note

class NoteRepository(private val db: NoteDatabase) {
    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    fun getAllNotes() = db.getNoteDao().getAllNotes()
}