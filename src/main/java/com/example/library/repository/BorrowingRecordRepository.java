package com.example.library.repository;

import com.example.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByBorrowerIdAndStatus(Long borrowerId, BorrowingRecord.BorrowingStatus status);
    List<BorrowingRecord> findByBookIdAndStatus(Long bookId, BorrowingRecord.BorrowingStatus status);
} 