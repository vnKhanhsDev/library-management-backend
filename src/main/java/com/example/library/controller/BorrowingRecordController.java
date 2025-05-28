package com.example.library.controller;

import com.example.library.dto.BorrowingRecordDTO;
import com.example.library.service.BorrowingRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowing-records")
@RequiredArgsConstructor
public class BorrowingRecordController {
    private final BorrowingRecordService borrowingRecordService;

    @PostMapping
    public ResponseEntity<BorrowingRecordDTO> createBorrowingRecord(@Valid @RequestBody BorrowingRecordDTO recordDTO) {
        return new ResponseEntity<>(borrowingRecordService.createBorrowingRecord(recordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowingRecordDTO> markAsReturned(@PathVariable Long id) {
        return ResponseEntity.ok(borrowingRecordService.markAsReturned(id));
    }
} 