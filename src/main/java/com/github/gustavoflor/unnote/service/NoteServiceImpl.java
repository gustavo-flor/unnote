package com.github.gustavoflor.unnote.service;

import com.github.gustavoflor.unnote.dto.PutNotePayload;
import com.github.gustavoflor.unnote.entity.Note;
import com.github.gustavoflor.unnote.exception.NoteNotFoundException;
import com.github.gustavoflor.unnote.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public Note findById(final String id) throws NoteNotFoundException {
        return noteRepository.findById(id).orElseThrow(NoteNotFoundException::new);
    }

    @Override
    public Note create(final PutNotePayload payload) {
        return noteRepository.save(Note.of(payload));
    }

    @Override
    public Note updateById(final String id, final PutNotePayload payload) throws NoteNotFoundException {
        return noteRepository.save(findById(id).merge(payload));
    }

    @Override
    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    @Override
    public Page<Note> findAllByTerms(final String[] terms, final Pageable pageable) {
        return noteRepository.findAllBy(TextCriteria.forDefaultLanguage().matchingAny(terms), pageable);
    }

    @Override
    public void deleteById(final String id) {
        noteRepository.deleteById(id);
    }
}
