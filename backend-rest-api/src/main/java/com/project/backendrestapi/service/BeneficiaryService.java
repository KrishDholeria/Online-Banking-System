package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.repository.BeneficiaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    @Autowired
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<Beneficiary> getBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId);
    }

    public Beneficiary createBeneficiary(Beneficiary beneficiary, Account account) {
        beneficiary.setAccount(account);
        return beneficiaryRepository.save(beneficiary);
    }

    public Optional<Beneficiary> updateBeneficiary(Long beneficiaryId, Beneficiary updatedBeneficiary,
            Account account) {
        Optional<Beneficiary> existingBeneficiary = beneficiaryRepository.findById(beneficiaryId);

        if (existingBeneficiary.isPresent()) {
            Beneficiary existing = existingBeneficiary.get();
            existing.setBeneficiaryId(beneficiaryId);
            existing.setBeneficiaryName(updatedBeneficiary.getBeneficiaryName());
            existing.setAccount(account);

            beneficiaryRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBeneficiary(Long beneficiaryId) {
        if (beneficiaryRepository.existsById(beneficiaryId)) {
            beneficiaryRepository.deleteById(beneficiaryId);
            return true;
        } else {
            return false;
        }
    }
}
