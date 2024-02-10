package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.BeneficiaryDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.BeneficiaryRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BeneficiaryService {

    @Autowired
    private final BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private final AccountService accountService;




    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<Beneficiary> getBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId);
    }

    public Beneficiary createBeneficiary(BeneficiaryDto beneficiary) throws Exception {
        Account account = accountService.getAccountByAccountNo(beneficiary.getAccountNo());
        if(account!= null){
            Beneficiary beneficiary1 = Beneficiary.builder()
                    .beneficiaryName(beneficiary.getBeneficiaryName())
                    .account(account)
                    .build();
            return beneficiaryRepository.save(beneficiary1);
        }
        throw new Exception("Account Doesn't Exist");

    }

    public Optional<Beneficiary> updateBeneficiary(Long beneficiaryId, BeneficiaryDto beneficiary) {
        Optional<Beneficiary> existingBeneficiary = beneficiaryRepository.findById(beneficiaryId);
        Account account = accountService.getAccountByAccountNo(beneficiary.getAccountNo());
        if (existingBeneficiary.isPresent()) {
            Beneficiary existing = existingBeneficiary.get();
            existing.setBeneficiaryId(beneficiaryId);
            existing.setBeneficiaryName(beneficiary.getBeneficiaryName());
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
    public Account addBeneficiary(BeneficiaryDto beneficiaryDto, Long accountId) throws Exception {
        Beneficiary beneficiary = createBeneficiary(beneficiaryDto);
        Account account = accountService.getAccountById(accountId).get();
        account.addBeneficiary(beneficiary);
        return account;
    }
}
