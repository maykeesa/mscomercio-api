package br.com.ms.email;

import br.com.ms.email.dto.EmailDto;
import br.com.ms.email.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<EmailDto.Response.Email> enviarEmail(
            @RequestBody @Valid EmailDto.Request.Email dto){
        return ResponseEntity.status(CREATED).body(this.emailService.enviarEmail(dto));
    }
}
