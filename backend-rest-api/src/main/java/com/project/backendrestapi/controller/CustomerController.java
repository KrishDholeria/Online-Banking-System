package com.project.backendrestapi.controller;

import com.project.backendrestapi.utils.Util;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hibernate.dialect.SybaseASEDialect;

import com.project.backendrestapi.dto.*;
import com.project.backendrestapi.model.*;
import com.project.backendrestapi.service.*;
import lombok.AllArgsConstructor;
import org.aspectj.util.UtilClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @Autowired
    private final BranchService branchService;

    @Autowired
    private final EmailService emailService;

    @PostMapping("/setusername")
    public ResponseEntity<?> setUsername(@RequestBody CustomerSignupDto customer) {
        Optional<Account> accountOptional = accountService.getAccountByAccountNo(customer.getAccountNo());
        Account account;
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
        } else {
            return new ResponseEntity<>(CustomerDto.builder()
                    .responseCode(Util.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(Util.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build(), HttpStatus.OK);
        }
        if(!customer.getBranchCode().equals(account.getBranch().getBranchCode())){
            return ResponseEntity.ok(CustomerDto.builder()
                            .responseCode(Util.INVALID_BRANCH_CODE_CODE)
                            .responseMessage(Util.INVALID_BRANCH_CODE_MESSAGE)
                    .build());
        }

        System.out.println(customer.getUserName());
        if (!customerService.customerExistByUserName(customer.getUserName())) {
            if(account.getCustomer().getPassword() != null){
                return new ResponseEntity<>(CustomerDto.builder()
                        .responseCode(Util.CUSTOMER_ALREADY_EXIST_CODE)
                        .responseMessage(Util.CUSTOMER_ALREADY_EXIST_MESSAGE)
                        .build(), HttpStatus.OK);
            }
            CustomerDto customerDto1 = new CustomerDto();
            customerDto1.setUserName(customer.getUserName());
            customerService.updateCustomer(account.getCustomer().getCustomerId(),customerDto1);
            CustomerDto customerDto = customerService.convertToCustomerDto(account.getCustomer());
            customerDto.setUserName(customer.getUserName());
            customerDto.setResponseCode(Util.USERNAME_UPDATED_SUCCESSFULLY_CODE);
            customerDto.setResponseMessage(Util.USERNAME_UPDATED_SUCCESSFULLY_MESSAGE);
            return ResponseEntity.ok(customerDto);
        } else {
            return new ResponseEntity<>(CustomerDto.builder()
                    .responseCode(Util.USERNAME_ALREADY_EXIST_CODE)
                    .responseMessage(Util.USERNAME_ALREADY_EXIST_MESSAGE)
                    .build(),HttpStatus.OK);
        }
        // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        Boolean res = smsService.verifyOTP(otpRequest.getOtp());
        System.out.println("controller: " + res);
        if (res) {
            return ResponseEntity.ok("OTP Verified");
        }
        return new ResponseEntity<>("Invalid Otp!!!", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @PostMapping("/setpassword")
    public ResponseEntity<?> setPassword(@RequestBody CustomerDto customerDto) {
        System.out.println(customerDto.getPassword() + " " + customerDto.getUserName());
        String username = customerDto.getUserName();
        String password = customerDto.getPassword();
        System.out.println(username+ "  "  + password);
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            System.out.println(customer.getPerson().getFirstName());
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            customer.setPassword(b.encode(password));
            customerService.updateCustomer(customer.getCustomerId(), customerService.convertToCustomerDto(customer));
            CustomerDto customerDto1 = customerService.convertToCustomerDto(customer);
            customerDto1.setResponseCode(Util.PASSWORD_SET_SUCCESSFULLY_CODE);
            customerDto1.setResponseMessage(Util.PASSWORD_SET_SUCCESSFULLY_MESSAGE);
            return ResponseEntity.ok(customerDto1);
        }
        return new ResponseEntity<>(CustomerDto.builder()
                .responseCode(Util.ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(Util.ACCOUNT_NOT_FOUND_MESSAGE)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/addbeneficiary/{username}")
    public ResponseEntity<?> addBeneficiary(@RequestBody BeneficiaryDto beneficiaryDto, @PathVariable String username) {
        return ResponseEntity.ok(beneficiaryService.addBeneficiary(beneficiaryDto, username));
    }

    @GetMapping("/getbeneficieries/{username}")
    public ResponseEntity<?> getBeneficieries(@PathVariable String username) {
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaryFromCustomer(username);
        List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
        for (Beneficiary b : beneficiaries) {
            beneficiaryDtos.add(beneficiaryService.entityToDto(b));
        }
        return new ResponseEntity<>(beneficiaryDtos, HttpStatus.OK);
    }

    @GetMapping("/getaccount/{accountNo}")
    public AccountDto getAccount(@PathVariable String accountNo) {
        // return accountService.getAccountByAccountNo(accountNo).get();
        Account account = accountService.getAccountByAccountNo(accountNo).get();
        return accountService.fromEntity(account);
    }

    @PostMapping("/maketransacion/{username}")
    public TransactionResponse makeTransaction(@RequestBody TransactionDto transactionDto,
            @PathVariable String username) {
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        Customer customer = customerOptional.get();
        Account from = customer.getAccount();
        Optional<Account> accountOptional = accountService.getAccountByAccountNo((transactionDto.getAccountNo()));
        if (accountOptional.isEmpty()) {
            return TransactionResponse.builder()
                    .responseCode(Util.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(Util.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }
        Account to = accountOptional.get();

        StringBuilder refId = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            if (i == 0) {
                refId.append(rand.nextInt(1, 9));
            } else {
                refId.append(rand.nextInt(10));
            }
        }
        Date date = new Date();

        int amount = Integer.parseInt(transactionDto.getAmount());
        if (from.getAccountBalance() < amount) {
            return TransactionResponse.builder()
                    .responseCode(Util.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(Util.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        }

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

        String creditMsg = "Hello, " + to.getCustomer().getPerson().getFirstName() + "\n"
                + "Your A/cX" + to.getAccountNumber().substring(8) + " credited by Rs" + amount + " on " + add.getTransactionDate().toString() + " by Ref no " + add.getReferenceId() + ".";
        EmailDetails creditAccount = EmailDetails.builder()
                .recipient(to.getCustomer().getPerson().getEmail())
                .subject("Money recieved in your account.")
                .msgBody(creditMsg)
                .build();
        emailService.sendSimpleMail(creditAccount);
        String debitMsg = "Hello, " + from.getCustomer().getPerson().getFirstName() + "\n"
                + "Your A/cX" + from.getAccountNumber().substring(8) + " debited by Rs" + amount + " on " + remove.getTransactionDate().toString() + " to " + to.getCustomer().getPerson().getFirstName() + " Ref no " + remove.getReferenceId() + ".";
        EmailDetails debitAccount = EmailDetails.builder()
                .recipient(customer.getPerson().getEmail())
                .subject("Money debited from your account.")
                .msgBody(debitMsg)
                .build();
        emailService.sendSimpleMail(debitAccount);

        return TransactionResponse.builder()
                .responseMessage(Util.TRANSFER_COMPLETE_MESSAGE)
                .responseCode(Util.TRANSFER_COMPLETE_CODE)
                .accountTo(to.getAccountNumber())
                .accountFrom(from.getAccountNumber())
                .type(transactionDto.getTransactionType())
                .refId(refId.toString())
                .amount(transactionDto.getAmount())
                .build();

    }


    @GetMapping("/transactions/{username}")
    public List<TransactionResponse> getTransactionsByDuration(@PathVariable String username,
            @RequestParam String duration) {
        List<Transaction> transactions = transactionService.getTransactionsByDuration(username, duration);

        // Convert Transaction entities to TransactionResponse objects
        List<TransactionResponse> transactionResponses = transactionService.convertToResponse(transactions);

        return transactionResponses;
    }

    @GetMapping("/getbalance/{username}")
    ResponseEntity<?> getBalance(@PathVariable String username) {
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return new ResponseEntity<>(customer.getAccount().getAccountBalance(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin(originPatterns = "http://localhost:3000")
    @PutMapping("/updateBeneficiary/{username}/{accountNo}")
    ResponseEntity<?> updateBeneficiary(@PathVariable String username,@PathVariable String accountNo, @RequestBody BeneficiaryDto beneficiaryDto){
        Optional<Account> optionalAccount = accountService.getAccountByAccountNo(beneficiaryDto.getAccountNo());
        if(optionalAccount.isEmpty()){
            return ResponseEntity.ok(BeneficiaryResponse.builder()
                    .responseCode(Util.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(Util.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build());
        }
        Optional<Branch> optionalBranch = branchService.getBranchByBranchCode(beneficiaryDto.getBranchCode());
        if(optionalBranch.isEmpty()){
            return ResponseEntity.ok(BeneficiaryResponse.builder()
                            .responseCode(Util.BRANCH_NOT_FOUND_CODE)
                            .responseMessage(Util.BRANCH_NOT_FOUND_MESSAGE)
                    .build());
        }
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            List<Beneficiary> beneficiaries = customer.getBeneficiaries();
            for(Beneficiary b: beneficiaries){
                if(!b.getAccountNumber().equals(accountNo) && b.getAccountNumber().equals(beneficiaryDto.getAccountNo())){
                    return ResponseEntity.ok(BeneficiaryResponse.builder()
                                    .responseCode(Util.BENEFICIARY_ALREADY_EXISTS_CODE)
                                    .responseMessage(Util.BENEFICIARY_ALREADY_EXISTS_MESSAGE)
                            .build());
                }
            }
            for(int i=0; i< beneficiaries.size(); i++){
                if(beneficiaries.get(i).getAccountNumber().equals(accountNo)){
                    Beneficiary b = beneficiaryService.updateBeneficiary(beneficiaries.get(i).getBeneficiaryId(), beneficiaryDto);
                    beneficiaries.set(i, b);
                    break;
                }
            }
            List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
            for(Beneficiary b: beneficiaries){
                beneficiaryDtos.add(beneficiaryService.entityToDto(b));
            }
            return new ResponseEntity<>(BeneficiaryResponse.builder()
                    .responseCode(Util.BENEFICIARY_UPDATED_SUCCESSFULLY_CODE)
                    .responseMessage(Util.BENEFICIARY_UPDATED_SUCCESSFULLY_MESSAGE)
                    .beneficiaryDtoList(beneficiaryDtos)
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Customer Not Found!!", HttpStatus.NO_CONTENT);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteBeneficiary/{username}/{accountNo}")
    ResponseEntity<?> deleteBenefiaiary(@PathVariable String username, @PathVariable String accountNo) {
        Optional<Customer> optionalCustomer = customerService.getCustomerByUserName(username);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            for (Beneficiary b : customer.getBeneficiaries()) {
                if (b.getAccountNumber().equals(accountNo)) {
                    Boolean res = beneficiaryService.deleteBeneficiary(b.getBeneficiaryId());
                    return res ? new ResponseEntity<>("Beneficiary deleted succesfully!!", HttpStatus.OK)
                            : new ResponseEntity<>("There is an error deleting beneficiary", HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Beneficiary not found!!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Customer not found", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/sendotp")
    ResponseEntity<?> sendOtp() {
        smsService.genrateOTP();
        return new ResponseEntity<>("OTP sent!!", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCustomerProfile(@RequestParam String username) {
        // Assume you have a method in your service to fetch the customer profile
        ProfileDto profileDto = customerService.getCustomerProfile(username);
        // System.out.println(profileDto);
        if (profileDto != null) {
            return ResponseEntity.ok((profileDto));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/getCustomer/{username}")
    public ResponseEntity<?> getCustomer(@PathVariable String username){
        Optional<Customer> optionalCustomer = customerService.getCustomerByUserName(username);
        if(optionalCustomer.isPresent()){
            CustomerDto customerDto = customerService.convertToCustomerDto(optionalCustomer.get());
            customerDto.setResponseCode(Util.CUSTOMER_FOUND_CODE);
            customerDto.setResponseMessage(Util.CUSTOMER_FOUND_MESSAGE);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(CustomerDto.builder()
                .responseCode(Util.INVALID_USERNAME_CODE)
                .responseMessage(Util.INVALID_USERNAME_MESSAGE)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest customerDto){
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(customerDto.getUsername());
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            if(b.matches(customerDto.getOldPassword(), customer.getPassword())){
                customer.setPassword(b.encode(customerDto.getNewPassword()));
                CustomerDto customerDto1 = customerService.convertToCustomerDto(customer);
                customerService.updateCustomer(customer.getCustomerId(), customerDto1);
                customerDto1.setResponseCode(Util.PASSWORD_SET_SUCCESSFULLY_CODE);
                customerDto1.setResponseMessage(Util.PASSWORD_SET_SUCCESSFULLY_MESSAGE);
                return ResponseEntity.ok(customerDto1);
            }
        }
        return ResponseEntity.ok(CustomerDto.builder()
                        .responseMessage(Util.NEW_AND_OLD_PASSWORD_DOESNT_MATCH_MESSAGE)
                        .responseCode(Util.NEW_AND_OLD_PASSWORD_DOESNT_MATCH_CODE)
                .build());
    }



    // @GetMapping("/getbeneficieries/{username}")
    // ResponseEntity<?> getBeneficieries(@PathVariable String username){
    // Optional<Customer> customerOptional =
    // customerService.getCustomerByUserName(username);
    // if(customerOptional.isPresent()){
    // Customer customer = customerOptional.get();
    // if(customer.getBeneficiaries().isEmpty()){
    // return new ResponseEntity<>("No Beneficiery added", HttpStatus.NO_CONTENT);
    // }
    // List<BeneficiaryDto> beneficiaryDtos = new ArrayList<>();
    // for(Beneficiary b : customer.getBeneficiaries()){
    // beneficiaryDtos.add(beneficiaryService.entityToDto(b));
    // }
    // return new ResponseEntity<>(beneficiaryDtos, HttpStatus.OK);
    // }
    // return new ResponseEntity<>("customer not found!!", HttpStatus.NO_CONTENT);
    // }

}
