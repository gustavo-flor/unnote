package com.github.gustavoflor.unnote.service;

import com.github.gustavoflor.unnote.dto.PutNotePayload;
import com.github.gustavoflor.unnote.entity.Note;
import com.github.gustavoflor.unnote.exception.NoteNotFoundException;
import com.github.gustavoflor.unnote.repository.NoteRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {
    static final Faker FAKER = new Faker();

    @InjectMocks
    NoteServiceImpl noteService;

    @Mock
    NoteRepository noteRepository;

    @Test
    @DisplayName("GIVEN a valid id WHEN findById THEN return the matching note")
    void givenAValidIdWhenFindByIdThenReturnTheMatchingNote() throws NoteNotFoundException {
        final String noteId = UUID.randomUUID().toString();
        final Note note = note(noteId);
        doReturn(Optional.of(note)).when(noteRepository).findById(noteId);
        final Note founded = noteService.findById(noteId);
        verify(noteRepository).findById(noteId);
        assertThat(founded).isEqualTo(note);
    }

    @Test
    @DisplayName("GIVEN an invalid id WHEN findById THEN throw NoteNotFoundException")
    void givenAnInvalidIdWhenFindByIdThenThrowNoteNotFoundException() {
        final String noteId = UUID.randomUUID().toString();
        doReturn(Optional.empty()).when(noteRepository).findById(noteId);
        assertThatThrownBy(() -> noteService.findById(noteId)).isInstanceOf(NoteNotFoundException.class);
    }

    @Test
    @DisplayName("GIVEN a valid payload WHEN create THEN save note")
    void givenAValidPayloadWhenCreateThenSaveNote() {
        final PutNotePayload payload = putNotePayload();
        doAnswer(answer -> answer.getArgument(0, Note.class)).when(noteRepository).save(any());
        final Note created = noteService.create(payload);
        verify(noteRepository).save(created);
        assertThat(created.getTitle()).isEqualTo(payload.title());
        assertThat(created.getContent()).isEqualTo(payload.content());
    }

    @Test
    @DisplayName("GIVEN an id WHEN deleteById THEN call delete")
    void givenAnIdWhenDeleteByIdThenCallDelete() {
        final String noteId = UUID.randomUUID().toString();
        noteService.deleteById(noteId);
        verify(noteRepository).deleteById(noteId);
    }

    @Test
    @DisplayName("GIVEN an invalid id WHEN updateById THEN throw NoteNotFoundException")
    void givenAnInvalidIdWhenUpdateByIdThenThrowNoteNotFoundException() {
        final String noteId = UUID.randomUUID().toString();
        final PutNotePayload payload = putNotePayload();
        doReturn(Optional.empty()).when(noteRepository).findById(noteId);
        assertThatThrownBy(() -> noteService.updateById(noteId, payload)).isInstanceOf(NoteNotFoundException.class);
    }

    @Test
    @DisplayName("GIVEN a valid id WHEN updateById THEN update note")
    void givenAValidIdWhenUpdateByIdThenUpdateNote() throws NoteNotFoundException {
        final String noteId = UUID.randomUUID().toString();
        final PutNotePayload payload = putNotePayload();
        final Note note = note(noteId);
        doReturn(Optional.of(note)).when(noteRepository).findById(noteId);
        doAnswer(answer -> answer.getArgument(0, Note.class)).when(noteRepository).save(any());
        final Note updated = noteService.updateById(noteId, payload);
        assertThat(updated.getId()).isEqualTo(noteId);
        assertThat(updated.getTitle()).isEqualTo(payload.title());
        assertThat(updated.getContent()).isEqualTo(payload.content());
    }

    @Test
    @DisplayName("GIVEN a valid page request WHEN findAll THEN return notes")
    void givenAValidPageRequestWhenFindAllThenReturnNotes() {
        final Pageable pageable = Pageable.unpaged();
        final List<Note> notes = List.of(note(), note(), note());
        doReturn(new PageImpl<>(notes)).when(noteRepository).findAll(pageable);
        final Page<Note> found = noteService.findAll(pageable);
        assertThat(found.getTotalElements()).isEqualTo(notes.size());
        assertThat(found.getSize()).isEqualTo(notes.size());
        assertThat(found.getContent()).containsAll(notes);
    }

    @Test
    @DisplayName("GIVEN a valid page request WHEN findAllByTerms THEN return notes")
    void givenAValidPageRequestWhenFindAllByTermsThenReturnNotes() {
        final Pageable pageable = Pageable.unpaged();
        final String[] terms = terms();
        final List<Note> notes = List.of(note(), note(), note());
        doReturn(new PageImpl<>(notes)).when(noteRepository).findAllBy(any(), eq(pageable));
        final Page<Note> found = noteService.findAllByTerms(terms, pageable);
        assertThat(found.getTotalElements()).isEqualTo(notes.size());
        assertThat(found.getSize()).isEqualTo(notes.size());
        assertThat(found.getContent()).containsAll(notes);
    }

    Note note() {
        return note(UUID.randomUUID().toString());
    }

    Note note(final String id) {
        final Note note = Note.of(putNotePayload());
        note.setId(id);
        return note;
    }

    String[] terms() {
        return new String[] { FAKER.leagueOfLegends().champion(), FAKER.leagueOfLegends().rank() };
    }

    PutNotePayload putNotePayload() {
        final String title = FAKER.leagueOfLegends().champion();
        final String content = FAKER.leagueOfLegends().quote();
        return new PutNotePayload(title, content);
    }
}
