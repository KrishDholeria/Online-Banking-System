package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.BeneficiaryDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.BeneficiaryRepository;

import com.project.backendrestapi.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BeneficiaryService {

    @Autowired
    private final BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final CustomerRepository customerRepository;




    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<Beneficiary> getBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId);
    }

    public List<Beneficiary> getBeneficiaryFromCustomer(String userName){
        Customer customer = customerRepository.findByUserName(userName).get();
        return customer.getBeneficiaries();
    }

    public Beneficiary updateBeneficiary(Long beneficiaryId, BeneficiaryDto beneficiary) {
        Optional<Beneficiary> existingBeneficiary = beneficiaryRepository.findById(beneficiaryId);
        Account account = accountService.getAccountByAccountNo(beneficiary.getAccountNo()).get();
        if (existingBeneficiary.isPresent()) {
            Beneficiary existing = existingBeneficiary.get();
            existing.setBeneficiaryId(beneficiaryId);
            existing.setBeneficiaryName(beneficiary.getBeneficiaryName());
            existing.setBranchCode(beneficiary.getBranchCode());
            existing.setAccountNumber(beneficiary.getAccountNo());
            beneficiaryRepository.save(existing);

            return existing;
        } else {
            return null;
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
    public List<BeneficiaryDto> addBeneficiary(BeneficiaryDto beneficiaryDto, String userName) {
        Beneficiary beneficiary = Beneficiary.builder()
                .beneficiaryName(beneficiaryDto.getBeneficiaryName())
                .accountNumber(beneficiaryDto.getAccountNo())
                .branchCode(beneficiaryDto.getBranchCode())
                .build();
        Customer customer = customerRepository.findByUserName(userName).get();
        beneficiary.setCustomer(customer);
        beneficiaryRepository.save(beneficiary);
        customer.addBeneficiary(beneficiary);
        customerRepository.save(customer);
        System.out.println("Inside add bene: " + customer);
        List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
        for (Beneficiary b : customer.getBeneficiaries()){
            beneficiaryDtos.add(entityToDto(b));
        }
        return beneficiaryDtos;
    }

    public BeneficiaryDto entityToDto(Beneficiary beneficiary){
        return BeneficiaryDto.builder()
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .accountNo(beneficiary.getAccountNumber())
                .branchCode(beneficiary.getBranchCode())
                .build();
    }
}
