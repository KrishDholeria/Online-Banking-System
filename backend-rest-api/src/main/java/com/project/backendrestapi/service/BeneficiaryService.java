package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.BeneficiaryDto;
import com.project.backendrestapi.dto.BeneficiaryResponse;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.BeneficiaryRepository;

import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.utils.Util;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    @Autowired
    private final BranchService branchService;




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
    public BeneficiaryResponse addBeneficiary(@NotNull BeneficiaryDto beneficiaryDto, String userName) {
        Optional<Account> optionalAccount = accountService.getAccountByAccountNo(beneficiaryDto.getAccountNo());
        if(optionalAccount.isEmpty()){
            return BeneficiaryResponse.builder()
                    .responseCode(Util.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(Util.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }

        Optional<Branch> optionalBranch = branchService.getBranchByBranchCode(beneficiaryDto.getBranchCode());
        if(optionalBranch.isEmpty()){
            return BeneficiaryResponse.builder()
                    .responseCode(Util.BRANCH_NOT_FOUND_CODE)
                    .responseMessage(Util.BRANCH_NOT_FOUND_MESSAGE)
                    .build();
        }
        if(!optionalBranch.get().getBranchCode().equals(optionalAccount.get().getBranch().getBranchCode())){
            return BeneficiaryResponse.builder()
                    .responseCode(Util.INVALID_BRANCH_CODE_CODE)
                    .responseMessage(Util.INVALID_BRANCH_CODE_MESSAGE)
                    .build();
        }

        Customer customer = customerRepository.findByUserName(userName).get();
        for(Beneficiary b: customer.getBeneficiaries()){
            if(b.getAccountNumber().equals(beneficiaryDto.getAccountNo())){
                return BeneficiaryResponse.builder()
                        .responseCode(Util.BENEFICIARY_ALREADY_EXISTS_CODE)
                        .responseMessage(Util.BENEFICIARY_ALREADY_EXISTS_MESSAGE)
                        .build();
            }
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .beneficiaryName(beneficiaryDto.getBeneficiaryName())
                .accountNumber(beneficiaryDto.getAccountNo())
                .branchCode(beneficiaryDto.getBranchCode())
                .build();
        beneficiary.setCustomer(customer);
        beneficiaryRepository.save(beneficiary);
        customer.addBeneficiary(beneficiary);
        customerRepository.save(customer);
        List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
        for (Beneficiary b : customer.getBeneficiaries()){
            beneficiaryDtos.add(entityToDto(b));
        }
        return BeneficiaryResponse.builder()
                .responseCode(Util.BENEFICIARY_ADDED_SUCCESSFULLY_CODE)
                .responseMessage(Util.BENEFICIARY_ADDED_SUCCESSFULLY_MESSAGE)
                .beneficiaryDtoList(beneficiaryDtos)
                .build();
    }

    public BeneficiaryDto entityToDto(Beneficiary beneficiary){
        return BeneficiaryDto.builder()
                .beneficiaryName(beneficiary.getBeneficiaryName())
                .accountNo(beneficiary.getAccountNumber())
                .branchCode(beneficiary.getBranchCode())
                .build();
    }
}
