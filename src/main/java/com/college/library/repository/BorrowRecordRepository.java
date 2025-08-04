package com.college.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.college.library.entity.BorrowRecord;
import com.college.library.entity.User;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    Page<BorrowRecord> findByUser(User user, Pageable pageable);

    List<BorrowRecord> findByUserId(Long userId);

    List<BorrowRecord> findByReturnDateIsNull(); // Currently borrowed

    List<BorrowRecord> findByReturnDateIsNullAndUser(User user); // User's current borrowings
    Page<BorrowRecord> findByReturnDateIsNull(Pageable pageable);

}
