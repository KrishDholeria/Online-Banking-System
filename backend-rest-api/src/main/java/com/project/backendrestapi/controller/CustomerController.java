package com.project.backendrestapi.controller;


import com.project.backendrestapi.dto.*;
import com.project.backendrestapi.model.*;
import com.project.backendrestapi.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final SMSService smsService;

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final BeneficiaryService beneficiaryService;

    @PostMapping("/setusername")
    public ResponseEntity<?> setUsername(@RequestBody CustomerSignupDto customer){
        Optional<Account> accountOptional = accountService.getAccountByAccountNo(customer.getAccountNo());
        Account account;
        if(accountOptional.isPresent()){
            account = accountOptional.get();
        }
        else {
            return new ResponseEntity<>("Account not found", HttpStatus.NO_CONTENT);
        }

        System.out.println("Helloooooooo!!!!!!");
        if(!customerService.customerExistByUserName(customer.getUserName())){
            Optional<Customer> customer1 = customerService.updateCustomer(account.getCustomer().getUserName(), CustomerDto.builder().userName(customer.getUserName()).build());
            if(customer1.get().getPassword() != null){
                return new ResponseEntity<>("Customer already exist!!!", HttpStatus.ALREADY_REPORTED);
            }
            Person person = customer1.get().getPerson();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CustomerDto customerDto = CustomerDto.builder()
                    .panNo(customer1.get().getPanNo())
                    .userName(customer.getUserName())
                    .account(AccountDto.builder()
                            .accountBalance(account.getAccountBalance())
                            .branchId(account.getBranch().getBranchId())
                            .cutomerId(customer1.get().getCustomerId())
                            .build())
                    .person(PersonDto.builder()
                            .dob(sdf.format(person.getDob()))
                            .email(person.getEmail())
                            .address(person.getAddress())
                            .phoneNo(person.getPhoneNo())
                            .firstName(person.getFirstName())
                            .lastName(person.getLastName())
                            .build())
                    .build();
            String s = smsService.genrateOTP();
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest){
        Boolean res = smsService.verifyOTP(otpRequest.getOtp());
        if(res){
            return ResponseEntity.ok("OTP Verified");
        }
        return new ResponseEntity<>("Invalid Otp!!!", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @PostMapping("/setpassword")
    public ResponseEntity<?> setPassword(@RequestBody CustomerDto customerDto){
        System.out.println(customerDto.getPassword() + " " + customerDto.getUserName());
        String username = customerDto.getUserName();
        String password = customerDto.getPassword();
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            customer.setPassword(b.encode(password));
            customerService.updateCustomer(username, customerService.convertToCustomerDto(customer));
            return ResponseEntity.ok(customerService.convertToCustomerDto(customer));
        }
        return new ResponseEntity<>("Customer not found", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/addbeneficiary/{username}")
    public ResponseEntity<?> addBeneficiary(@RequestBody BeneficiaryDto beneficiaryDto, @PathVariable String username) {
        try {
            List<BeneficiaryDto> beneficiaryDtos = beneficiaryService.addBeneficiary(beneficiaryDto, username);
            return new ResponseEntity<>(beneficiaryDtos, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Account Not Found", HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping("/getbeneficieries/{username}")
    public ResponseEntity<?> getBeneficieries(@PathVariable String username){
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaryFromCustomer(username);
        List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
        for (Beneficiary b : beneficiaries){
            beneficiaryDtos.add(beneficiaryService.entityToDto(b));
        }
        return new ResponseEntity<>(beneficiaryDtos, HttpStatus.OK);
    }

    @GetMapping("/getaccount/{accountNo}")
    public AccountDto getAccount(@PathVariable String accountNo){
//        return accountService.getAccountByAccountNo(accountNo).get();
        Account account = accountService.getAccountByAccountNo(accountNo).get();
        return accountService.fromEntity(account);
    }

    @PostMapping("/maketransacion/{username}")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionDto transactionDto, @PathVariable String username){
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        Customer customer = customerOptional.get();
        Account from = customer.getAccount();
        Account to = accountService.getAccountByAccountNo(transactionDto.getAccountNo()).get();

        StringBuilder refId = new StringBuilder();
        Random rand = new Random();
        for(int i=0; i<16; i++){
            if(i==0) {
                refId.append(rand.nextInt(1, 9));
            }
            else {
                refId.append(rand.nextInt(10));
            }
        }
        Date date = new Date();

        int amount = Integer.parseInt(transactionDto.getAmount());

        to.setAccountBalance(to.getAccountBalance() + amount);
        from.setAccountBalance(from.getAccountBalance() - amount);

        accountService.updateAccount(to.getAccountId(), to);
        accountService.updateAccount(from.getAccountId(), from);


        Transaction add = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .account(to)
                .amount(amount)
                .transactionDate(date)
                .referenceId(refId.toString())
                .build();
        add = transactionService.createTransaction(add);
        Transaction remove = Transaction.builder()
                .transactionDate(date)
                .transactionType(transactionDto.getTransactionType())
                .account(from)
                .amount(amount * -1)
                .relatedTransaction(add)
                .referenceId(refId.toString())
                .build();
        remove = transactionService.createTransaction(remove);
        add.setRelatedTransaction(remove);
        transactionService.updateTransaction(add.getTransactionId(), add);



        TransactionResponse response = TransactionResponse.builder()
                .accountTo(to.getAccountNumber())
                .accountFrom(from.getAccountNumber())
                .type(transactionDto.getTransactionType())
                .refId(refId.toString())
                .amount(transactionDto.getAmount())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
