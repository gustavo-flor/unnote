package com.github.gustavoflor.unnote.controller;

import com.github.gustavoflor.unnote.dto.PutNotePayload;
import com.github.gustavoflor.unnote.entity.Note;
import com.github.gustavoflor.unnote.exception.NoteNotFoundException;
import com.github.gustavoflor.unnote.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private static final int DEFAULT_PAGE_SIZE = 16;

    private final NoteService noteService;

    @GetMapping
    public Page<Note> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") final int page,
                              @RequestParam(required = false, name = "term") final String[] terms) {
        final Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        if (isNull(terms) || terms.length == 0) {
            return noteService.findAll(pageable);
        }
        return noteService.findAllByTerms(terms, pageable);
    }

    @GetMapping("/{id}")
    public Note findById(@PathVariable final String id) throws NoteNotFoundException {
        return noteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note create(@Valid @RequestBody final PutNotePayload payload) {
        return noteService.create(payload);
    }

    @PatchMapping("/{id}")
    public Note updateById(@PathVariable final String id,
                           @Valid @RequestBody final PutNotePayload payload) throws NoteNotFoundException {
        return noteService.updateById(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable final String id) {
        noteService.deleteById(id);
    }
}
