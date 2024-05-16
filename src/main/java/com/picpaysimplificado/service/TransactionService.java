package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repository.TransactionRepository;
import com.picpaysimplificado.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AuthorizationService authorizationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        userService.validateTransaction(sender, transactionDTO.value());

        boolean isAuthorized = this.authorizationService.autorizeTransaction(sender, transactionDTO.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));


        this.repository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transação enviada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return transaction;
    }
}
