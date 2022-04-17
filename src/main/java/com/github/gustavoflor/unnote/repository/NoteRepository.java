package com.github.gustavoflor.unnote.repository;

import com.github.gustavoflor.unnote.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    Page<Note> findAllBy(final TextCriteria textCriteria, final Pageable pageable);
}
