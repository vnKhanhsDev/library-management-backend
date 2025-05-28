package com.example.library.dto;

import com.example.library.model.BorrowingRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRecordDTO {
    private Long id;

    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    private LocalDate borrowDate;
    private LocalDate returnDate;
    private BorrowingRecord.BorrowingStatus status;
} 