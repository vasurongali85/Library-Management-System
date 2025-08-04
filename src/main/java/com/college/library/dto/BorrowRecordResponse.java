package com.college.library.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BorrowRecordResponse {
    private Long recordId;
    private String bookTitle;
    private String username;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private boolean overdue;
}
