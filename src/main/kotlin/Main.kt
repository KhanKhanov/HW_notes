import com.sun.source.doctree.CommentTree

data class Note(
    val ownerId: Int = 0,
    var title: String = "",
    var text: String = "",
    var isDeleteNotes: Boolean = false,
    var comments: MutableList<Comment> = mutableListOf()
) {
    val noteId: Int

    companion object {
        private var noteIdCounter = 0
    }

    private fun generateNoteId(): Int {
        return ++noteIdCounter
    }

    init {
        noteId = generateNoteId()
    }

}

data class Comment (
    var textComment: String = "",
    var isDeleteComment: Boolean = false
) {
    val commentId: Int

    companion object {
        private var commentIdCounter = 0
    }

    private fun generateCommentId(): Int {
        return ++commentIdCounter
    }

    init {
        commentId = generateCommentId()
    }
}


class NoteRepository<T> where T: Note {
    private val notes: MutableList<T> = mutableListOf()

    fun add(note: T): T {
        notes.add(note)
        return note
    }

    fun createComment(noteId: Int, comment: Comment): Boolean {
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        return note?.comments?.add(comment) ?: false
    }

    fun delete(noteId: Int): Boolean {
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        if (note != null) {
            note.isDeleteNotes = true
            return true
        } else {
            return false
        }
    }
    fun deleteComment(noteId: Int, commentId: Int): Boolean {
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        return if (note != null) {
            note.comments.find { it.commentId == commentId }?.isDeleteComment = true
            true
        } else {
            false
        }
    }

    fun edit(noteId: Int, title: String, text: String): Boolean {
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        return if (note != null) {
            note.title = title
            note.text = text
            true
        } else {
            false
        }
    }

    fun editComment(noteId: Int, commentId: Int, text: String): Boolean {
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        val commentToNote = note?.comments?.find { it.commentId == commentId }?.isDeleteComment
        if (commentToNote == false) {
            note.comments.find { it.commentId == commentId }?.textComment = text
            return true
        } else {
            throw IllegalStateException("Невозможно редактировать удалённый комментарий. " +
                    "Сначала получите право на его восстановление.")
        }
    }

    fun get(userId: Int): List<T> {
        return notes.filter { it.ownerId == userId && !it.isDeleteNotes }
    }

    fun getById(noteId: Int): T? {                          // Возвращает заметку по ее ID
        return notes.find { it.noteId == noteId && !it.isDeleteNotes }
    }

    fun getComments(noteId: Int): List<Comment> {          // возврат списка комментариев к заметке
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes}
        return note?.comments?.filter { !it.isDeleteComment } ?: emptyList()
    }


    fun restoreComment(noteId: Int, commentId: Int): Boolean {     // восстановление удалённого комментария
        val note = notes.find { it.noteId == noteId && !it.isDeleteNotes }
        return if (note != null) {
            note.comments.find { it.commentId == commentId }?.isDeleteComment = false
            true
        } else {
            false
        }
    }
}

fun main() {
    val noteRepository = NoteRepository<Note>()

    val newNote1 = Note(title = "The first Note")
    val newNote2 = Note(title = "The second Note")

    val comment1ByNote1 = Comment(textComment = "Comment #1 by Note 1")
    val comment2ByNote1 = Comment(textComment = "Comment #2 by Note 1")
    val commentByNote2 = Comment(textComment = "Comment by Note 2")

    noteRepository.add(newNote1)
    noteRepository.createComment(newNote1.noteId, comment1ByNote1)
    noteRepository.createComment(newNote1.noteId, comment2ByNote1)
    println(noteRepository.getComments(newNote1.noteId).joinToString())


    noteRepository.deleteComment(newNote1.noteId, comment1ByNote1.commentId)
    println(noteRepository.getComments(newNote1.noteId).joinToString())

    println("${newNote1.comments}, ${newNote2.noteId}")
}

