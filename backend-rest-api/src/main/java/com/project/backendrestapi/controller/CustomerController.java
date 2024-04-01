package com.project.backendrestapi.controller;

import com.project.backendrestapi.utils.Util;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.project.backendrestapi.dto.*;
import com.project.backendrestapi.model.*;
import com.project.backendrestapi.service.*;
import lombok.AllArgsConstructor;
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

    @PostMapping("/setusername")
    public ResponseEntity<?> setUsername(@RequestBody CustomerSignupDto customer) {
        Optional<Account> accountOptional = accountService.getAccountByAccountNo(customer.getAccountNo());
        Account account;
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NO_CONTENT);
        }

        System.out.println("Helloooooooo!!!!!!");
        if (!customerService.customerExistByUserName(customer.getUserName())) {
            Optional<Customer> customer1 = customerService.updateCustomer(account.getCustomer().getUserName(),
                    CustomerDto.builder().userName(customer.getUserName()).build());
            if (customer1.get().getPassword() != null) {
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
        } else {
            return new ResponseEntity<>(HttpStatus.IM_USED);
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
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if (customerOptional.isPresent()) {
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

    // @GetMapping("/statement/{username}")
    // public ResponseEntity<byte[]> generateStatement(@PathVariable String
    // username, @RequestParam String duration) {
    // // Fetch transactions based on the specified duration
    // List<Transaction> transactions =
    // transactionService.getTransactionsByDuration(username, duration);

    // // Generate PDF document based on the filtered transactions
    // try (PDDocument document = new PDDocument()) {
    // PDPage page = new PDPage();
    // document.addPage(page);

    // try (PDPageContentStream contentStream = new PDPageContentStream(document,
    // page)) {
    // contentStream.beginText();
    // contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
    // contentStream.newLineAtOffset(100, 700);
    // contentStream.showText("Transaction Statement for Customer: " + username);
    // contentStream.newLine();
    // contentStream.setFont(PDType1Font.HELVETICA, 10);

    // // Write transaction details to the PDF
    // int y = 680;
    // for (Transaction transaction : transactions) {
    // contentStream.showText("Transaction ID: " + transaction.getTransactionId());
    // contentStream.newLine();
    // contentStream.showText("Amount: " + transaction.getAmount());
    // contentStream.newLine();

    // y -= 20;
    // if (y < 20) {

    // contentStream.endText();
    // contentStream.close();
    // page = new PDPage();
    // document.addPage(page);
    // contentStream.beginText();
    // contentStream.setFont(PDType1Font.HELVETICA, 10);
    // y = 700;
    // }
    // }

    // contentStream.endText();
    // }

    // // Convert PDF document to byte array
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // document.save(baos);
    // byte[] pdfBytes = baos.toByteArray();

    // // Set headers for PDF response
    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_PDF);
    // headers.setContentDispositionFormData("filename",
    // "transaction_statement.pdf");

    // return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    // } catch (IOException e) {
    // e.printStackTrace();
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

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
    ResponseEntity<?> updateBeneficiary(@PathVariable String username, @PathVariable String accountNo,
            @RequestBody BeneficiaryDto beneficiaryDto) {
        Optional<Customer> customerOptional = customerService.getCustomerByUserName(username);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            Beneficiary beneficiary = null;
            for (Beneficiary b : customer.getBeneficiaries()) {
                if (b.getAccountNumber().equals(accountNo)) {
                    beneficiary = beneficiaryService.updateBeneficiary(b.getBeneficiaryId(), beneficiaryDto);
                    break;
                }
            }
            if (beneficiary != null) {
                return new ResponseEntity<>(beneficiaryService.entityToDto(beneficiary), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Beneficiary not found!!", HttpStatus.NO_CONTENT);
            }
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
