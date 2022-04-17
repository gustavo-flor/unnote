package com.github.gustavoflor.unnote.service;

import com.github.gustavoflor.unnote.dto.PutNotePayload;
import com.github.gustavoflor.unnote.entity.Note;
import com.github.gustavoflor.unnote.exception.NoteNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Note findById(final String id) throws NoteNotFoundException;

    Note create(final PutNotePayload payload);

    Note updateById(final String id, final PutNotePayload payload) throws NoteNotFoundException;

    Page<Note> findAll(final Pageable pageable);

    Page<Note> findAllByTerms(final String[] query, final Pageable pageable);

    void deleteById(final String id);
}
