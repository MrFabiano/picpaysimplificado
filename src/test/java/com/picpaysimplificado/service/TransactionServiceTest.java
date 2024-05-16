package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.domain.UserType;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach// executa antes do test
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everthing is OK")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L, "Maria", "Teixeira", "3454365", "teste@gmail.com", "12342", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Candoca", "Borracha", "345436522", "teste@gmail.com", "12342", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.autorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transação enviada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");

    }

    @Test
    @DisplayName("Should throw Exception when Transaction is not allowed")
    void createTransactionCase2() throws Exception {
        User sender = new User(1L, "Maria", "Teixeira", "3454365", "teste@gmail.com", "12342", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Candoca", "Borracha", "345436522", "teste@gmail.com", "12342", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.autorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }
}