import org.junit.Test
import kotlin.test.assertEquals

import org.junit.Assert.*

class NoteRepositoryTest {

    @Test
    fun add() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        val addNote = newNoteRepository.add(newNote1)
        assertEquals(newNote1, addNote)
    }

    @Test
    fun createComment() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        val createComment = newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        assertTrue(createComment)
    }

    @Test
    fun delete() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val resultToDeleting = newNoteRepository.delete(newNote1.noteId)
        assertTrue(resultToDeleting)
    }

    @Test
    fun deleteComment() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        val resultToDeletingComment = newNoteRepository.deleteComment(newNote1.noteId, comment1ByNote1.commentId)
        assertTrue(resultToDeletingComment)
    }

    @Test
    fun edit() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val resultToEdit = newNoteRepository.edit(
            newNote1.noteId,
            "Edit the Note # 1",
            "New text after edit Note # 1")
        assertTrue(resultToEdit)
    }

    @Test(expected = IllegalStateException::class)
    fun editCommentWithException() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        val resultToEditComment = newNoteRepository.editComment(
            newNote1.noteId,
            34,
            "New text of the comment after it edit")
        assertTrue(resultToEditComment)
    }

    @Test
    fun editCommentWithoutException() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        val resultToEditComment = newNoteRepository.editComment(
            newNote1.noteId,
            comment1ByNote1.commentId,
            "New text of the comment after it edit")
        assertTrue(resultToEditComment)
    }

    @Test
    fun get() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val result = newNoteRepository.get(newNote1.ownerId)
        assertEquals(listOf(newNote1), result)
    }

    @Test
    fun getById() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val result = newNoteRepository.getById(newNote1.noteId)
        assertEquals(newNote1, result)
    }

    @Test
    fun getComments() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        val comment2ByNote1 = Comment(textComment = "Comment #2 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment2ByNote1)
        val result = newNoteRepository.getComments(newNote1.noteId)
        assertEquals(newNote1.comments, result)
    }

    @Test
    fun restoreComment() {
        val newNoteRepository = NoteRepository<Note>()
        val newNote1 = Note(title = "The first Note")
        newNoteRepository.add(newNote1)
        val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
        newNoteRepository.createComment(newNote1.noteId, comment1ByNote1)
        newNoteRepository.deleteComment(newNote1.noteId, comment1ByNote1.commentId)
        val resultToRestoreComment = newNoteRepository.restoreComment(newNote1.noteId, comment1ByNote1.commentId)
        assertTrue(resultToRestoreComment)
    }
}