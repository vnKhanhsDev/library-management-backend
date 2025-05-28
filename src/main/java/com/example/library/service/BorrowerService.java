package com.example.library.service;

import com.example.library.dto.BorrowerDTO;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Borrower;
import com.example.library.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;

    public BorrowerDTO createBorrower(BorrowerDTO borrowerDTO) {
        if (borrowerRepository.existsByEmail(borrowerDTO.getEmail())) {
            throw new BadRequestException("Borrower with email " + borrowerDTO.getEmail() + " already exists");
        }

        Borrower borrower = Borrower.builder()
                .name(borrowerDTO.getName())
                .email(borrowerDTO.getEmail())
                .phone(borrowerDTO.getPhone())
                .build();

        return convertToDTO(borrowerRepository.save(borrower));
    }

    @Transactional(readOnly = true)
    public BorrowerDTO getBorrower(Long id) {
        return convertToDTO(findBorrowerById(id));
    }

    @Transactional(readOnly = true)
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BorrowerDTO updateBorrower(Long id, BorrowerDTO borrowerDTO) {
        Borrower borrower = findBorrowerById(id);
        
        if (!borrower.getEmail().equals(borrowerDTO.getEmail()) && 
            borrowerRepository.existsByEmail(borrowerDTO.getEmail())) {
            throw new BadRequestException("Borrower with email " + borrowerDTO.getEmail() + " already exists");
        }

        borrower.setName(borrowerDTO.getName());
        borrower.setEmail(borrowerDTO.getEmail());
        borrower.setPhone(borrowerDTO.getPhone());

        return convertToDTO(borrowerRepository.save(borrower));
    }

    public void deleteBorrower(Long id) {
        if (!borrowerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Borrower not found with id: " + id);
        }
        borrowerRepository.deleteById(id);
    }

    private Borrower findBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));
    }

    private BorrowerDTO convertToDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .name(borrower.getName())
                .email(borrower.getEmail())
                .phone(borrower.getPhone())
                .build();
    }
} 