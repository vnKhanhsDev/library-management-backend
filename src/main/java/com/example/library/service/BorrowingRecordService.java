package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.BorrowingRecordDTO;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.model.BorrowingRecord;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowerRepository;
import com.example.library.repository.BorrowingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowingRecordService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BorrowingRecordDTO createBorrowingRecord(BorrowingRecordDTO recordDTO) {
        Book book = bookRepository.findById(recordDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + recordDTO.getBookId()));

        if (book.getQuantity() <= 0) {
            throw new BadRequestException("Book is not available for borrowing");
        }

        Borrower borrower = borrowerRepository.findById(recordDTO.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + recordDTO.getBorrowerId()));

        // Check if the book is already borrowed by the same borrower
        List<BorrowingRecord> existingRecords = borrowingRecordRepository.findByBorrowerIdAndStatus(
                borrower.getId(), BorrowingRecord.BorrowingStatus.BORROWED);
        
        if (existingRecords.stream().anyMatch(record -> record.getBook().getId().equals(book.getId()))) {
            throw new BadRequestException("Book is already borrowed by this borrower");
        }

        // Create new borrowing record
        BorrowingRecord record = BorrowingRecord.builder()
                .book(book)
                .borrower(borrower)
                .borrowDate(LocalDate.now())
                .status(BorrowingRecord.BorrowingStatus.BORROWED)
                .build();

        // Update book quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        return convertToDTO(borrowingRecordRepository.save(record));
    }

    public BorrowingRecordDTO markAsReturned(Long recordId) {
        BorrowingRecord record = borrowingRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found with id: " + recordId));

        if (record.getStatus() == BorrowingRecord.BorrowingStatus.RETURNED) {
            throw new BadRequestException("Book is already returned");
        }

        record.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        // Update book quantity
        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        return convertToDTO(borrowingRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBooksBorrowedByBorrower(Long borrowerId) {
        List<BorrowingRecord> records = borrowingRecordRepository.findByBorrowerIdAndStatus(
                borrowerId, BorrowingRecord.BorrowingStatus.BORROWED);

        return records.stream()
                .map(record -> BookDTO.builder()
                        .id(record.getBook().getId())
                        .title(record.getBook().getTitle())
                        .author(record.getBook().getAuthor())
                        .isbn(record.getBook().getIsbn())
                        .quantity(record.getBook().getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private BorrowingRecordDTO convertToDTO(BorrowingRecord record) {
        return BorrowingRecordDTO.builder()
                .id(record.getId())
                .borrowerId(record.getBorrower().getId())
                .bookId(record.getBook().getId())
                .borrowDate(record.getBorrowDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus())
                .build();
    }
} 