package com.college.library.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.college.library.dto.BorrowRecordResponse;
import com.college.library.entity.Book;
import com.college.library.entity.BorrowRecord;
import com.college.library.entity.User;
import com.college.library.repository.BookRepository;
import com.college.library.repository.BorrowRecordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    private static final int BORROW_DAYS = 14;
    private static final double DAILY_FINE = 2.0;

    @Transactional
    public Optional<BorrowRecord> borrowBook(User user, Book book) {
        if (book.getAvailableCopies() <= 0)
            return Optional.empty();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(BORROW_DAYS))
                .fine(0.0)
                .build();

        return Optional.of(borrowRecordRepository.save(record));
    }

    @Transactional
    public Optional<BorrowRecord> returnBook(User user, Long bookId) {
        List<BorrowRecord> activeRecords = borrowRecordRepository.findByReturnDateIsNullAndUser(user);

        for (BorrowRecord record : activeRecords) {
            if (record.getBook().getId().equals(bookId)) {
                LocalDate now = LocalDate.now();
                record.setReturnDate(now);

                long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), now);
                if (daysLate > 0) {
                    record.setFine(daysLate * DAILY_FINE);
                }

                Book book = record.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookRepository.save(book);

                return Optional.of(borrowRecordRepository.save(record));
            }
        }

        return Optional.empty();
    }

    public Page<BorrowRecord> getUserHistory(User user, Pageable pageable) {
        return borrowRecordRepository.findByUser(user, pageable);
    }

    public Page<BorrowRecord> getAllActiveBorrows(Pageable pageable) {
        return borrowRecordRepository.findByReturnDateIsNull(pageable);
    }

    public BorrowRecordResponse mapToResponse(BorrowRecord record) {
        boolean overdue = record.getReturnDate() == null &&
                record.getDueDate().isBefore(LocalDate.now());

        return BorrowRecordResponse.builder()
                .recordId(record.getId())
                .bookTitle(record.getBook().getTitle())
                .username(record.getUser().getUsername())
                .issueDate(record.getIssueDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fine(record.getFine())
                .overdue(overdue)
                .build();
    }

    public List<BorrowRecordResponse> mapToResponseList(List<BorrowRecord> records) {
        return records.stream().map(this::mapToResponse).toList();
    }

}
