package com.college.library.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.library.dto.BorrowRecordResponse;
import com.college.library.entity.Book;
import com.college.library.entity.BorrowRecord;
import com.college.library.entity.User;
import com.college.library.service.BookService;
import com.college.library.service.BorrowService;
import com.college.library.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BookService bookService;
    private final UserService userService;

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Book book = bookService.getBookById(bookId).orElse(null);

        if (book == null) {
            return ResponseEntity.badRequest().body("Book not found");
        }

        return borrowService.borrowBook(user, book)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("No available copies"));
    }

    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();

        return borrowService.returnBook(user, bookId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("No matching borrowed book found"));
    }

    @GetMapping("/my-history")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<BorrowRecordResponse>> getMyHistory(Authentication authentication, Pageable pageable) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();

        Page<BorrowRecord> page = borrowService.getUserHistory(user, pageable);
        Page<BorrowRecordResponse> responsePage = page.map(borrowService::mapToResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Page<BorrowRecordResponse>> getAllActiveBorrows(Pageable pageable) {
        Page<BorrowRecord> page = borrowService.getAllActiveBorrows(pageable);
        Page<BorrowRecordResponse> responsePage = page.map(borrowService::mapToResponse);
        return ResponseEntity.ok(responsePage);
    }

}
