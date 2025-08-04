package com.college.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    @NotBlank
    private String title;

    private String author;

    private String category;

    @Min(1)
    private int totalCopies;
}
