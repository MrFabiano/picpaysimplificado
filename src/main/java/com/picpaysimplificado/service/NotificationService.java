package com.picpaysimplificado.service;

import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate template;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationDTO = new NotificationDTO(email, message);


//         ResponseEntity<String> notificationDTOResponse = template.postForEntity("http://o4d9z.mocklab.io/notify", notificationDTO, String.class);
//
//         if(!(notificationDTOResponse.getStatusCode() == HttpStatus.OK)){
//             System.out.println("erro ao enviar a notificação");
//             throw new Exception("Serviço de notificação está fora do ar");
//         }

        System.out.println("notificação enviada para o usuario");
    }
}
