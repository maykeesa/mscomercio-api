package br.com.ms.email.service;

import br.com.ms.email.dto.EmailDto;
import br.com.ms.email.service.utils.EmailServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.ms.email.enums.EmailStatus.ENVIADO;
import static br.com.ms.email.enums.EmailStatus.NAO_ENVIADO;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private EmailServiceUtils emailServiceUtils;

    @Override
    public EmailDto.Response.Email enviarEmail(EmailDto.Request.Email dto) {
        EmailDto.Response.Email response = new EmailDto.Response.Email();
        boolean enviado = false;

        if(dto.isEmailPendente()){
            enviado = this.emailServiceUtils.enviarEmailPendente(dto);
        }

        if(dto.isEmailProcessando()){
            enviado = this.emailServiceUtils.enviarEmailProcessando(dto);
        }

        if(dto.isEmailConcluido()){
            enviado = this.emailServiceUtils.enviarEmailConcluido(dto);
        }

        if(dto.isEmailCancelado()){
            enviado = this.emailServiceUtils.enviarEmailCancelado(dto);
        }

        response.setDestinatario(dto.getDestinatario());
        response.setStatus(enviado ? ENVIADO.getMensagem() : NAO_ENVIADO.getMensagem());
        response.setDescricao(enviado ? ENVIADO.getDescricao() : NAO_ENVIADO.getDescricao());

        return response;
    }
}
