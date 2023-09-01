package com.picpaysimplificado.controller;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.service.TransactionService;
import com.picpaysimplificado.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> postCreateTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception {
        Transaction newTransaction = this.transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }

}
