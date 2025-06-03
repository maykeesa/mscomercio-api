package br.com.ms.email.service;

import br.com.ms.email.dto.EmailDto;

public interface EmailService {

    EmailDto.Response.Email enviarEmail(EmailDto.Request.Email dto);
}
