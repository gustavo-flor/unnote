package com.github.gustavoflor.unnote.entity;

import com.github.gustavoflor.unnote.dto.PutNotePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes", language = "pt")
@Getter
@Setter
public class Note {
    @Id
    private String id;

    @TextIndexed(weight = 2)
    private String title;

    @TextIndexed
    private String content;

    public Note merge(final PutNotePayload payload) {
        final Note note = new Note();
        note.setId(getId());
        note.setTitle(payload.title());
        note.setContent(payload.content());
        return note;
    }

    public static Note of(final PutNotePayload payload) {
        return new Note().merge(payload);
    }
}
