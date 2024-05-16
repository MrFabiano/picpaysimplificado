package com.picpaysimplificado.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.service.TransactionService;
import com.picpaysimplificado.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Autowired
    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testPostCreateTransaction() throws Exception {
        User sender = new User();
        sender.setId(1L);
        sender.setFirstName("John");
        sender.setLastName("Doe");
        sender.setEmail("john.doe@example.com");

        User receiver = new User();
        receiver.setId(2L);
        receiver.setFirstName("Jane");
        receiver.setLastName("Doe");
        receiver.setEmail("jane.doe@example.com");

        // Prepare TransactionDTO
        TransactionDTO transactionDTO = new TransactionDTO(
                new BigDecimal("100.00"),
                1L,  // sender ID
                2L   // receiver ID
        );

        // Prepare Transaction entity
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        // Mock the service call
        when(transactionService.createTransaction(transactionDTO)).thenReturn(transaction);

        // Perform the POST request and assert the results
        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.sender.id").value(1L))
                .andExpect(jsonPath("$.receiver.id").value(2L));
    }
}
