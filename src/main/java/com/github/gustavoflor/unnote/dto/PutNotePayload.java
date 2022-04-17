package com.github.gustavoflor.unnote.dto;

import javax.validation.constraints.NotBlank;

public record PutNotePayload(String title, @NotBlank String content) {
}
