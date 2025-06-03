package br.com.ms.email.service.utils;

import br.com.ms.email.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailServiceUtils {

    @Value("${spring.mail.username}")
    private String remetente;

    @Autowired
    private JavaMailSender mailSender;

    private final FileSystemResource BANNER = new FileSystemResource(
            new File("src/main/resources/arquivos/banner/msrabbit-banner.png"));

    public boolean enviarEmailPendente(EmailDto.Request.Email dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(obterTituloPendente());
            helper.setFrom(remetente);
            helper.setTo(dto.getDestinatario());
            helper.setText(
                    obterTextoPendente(
                            dto.getNome(), dto.getPedidoId(), dto.getValorPedido()), true);
            helper.addInline("bannerFinal", BANNER);

            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            return false;
        }

        return true;
    }

    private String obterTituloPendente(){
        return "Seu pedido está esperando o pagamento!!";
    }

    private String obterTextoPendente(String nome, UUID numeroPedido, BigDecimal valor){
        String pedido = numeroPedido.toString();
        String nrPedido = pedido.substring(pedido.length() - 11);

        return  "<html><body>"
                + "<p>Olá <b>" + nome + "</b>!</p>"
                + "<p>O pagamento de <b>R$ " + valor + "</b> referente ao seu pedido <b>#"
                + nrPedido + "</b> vence em <b>" + LocalDate.now().plusDays(3) + "</b>.</p>"
                + "<p>Para evitar cancelamento, conclua o pagamento o quanto antes.</p>"
                + "<br><img src='cid:bannerFinal' style='max-width: 600px; height: auto; display: inline-block;'/>"
                + "</body></html>";
    }

    public boolean enviarEmailProcessando(EmailDto.Request.Email dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(obterTituloProcessando());
            helper.setFrom(remetente);
            helper.setTo(dto.getDestinatario());
            helper.setText(obterTextoProcessando(dto.getNome(), dto.getPedidoId()), true);
            helper.addInline("bannerFinal", BANNER);

            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            return false;
        }

        return true;
    }

    private String obterTituloProcessando(){
        return "Pagamento recebido! Estamos processando seu pedido.";
    }

    private String obterTextoProcessando(String nome, UUID numeroPedido) {
        String pedido = numeroPedido.toString();
        String nrPedido = pedido.substring(pedido.length() - 11);

        return "<html><body>"
                + "<p>Olá, <b>" + nome + "</b>!</p>"
                + "<p>O seu pedido de número <b>#" + nrPedido + "</b> realizado em <b>" + LocalDate.now()
                + "</b> está sendo processado.</p>"
                + "<p>Em breve, você receberá mais atualizações por e-mail.</p>"
                + "<br><img src='cid:bannerFinal' style='max-width: 600px; height: auto; display: inline-block;'/>"
                + "</body></html>";
    }

    public boolean enviarEmailConcluido(EmailDto.Request.Email dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(obterTituloConcluido());
            helper.setFrom(remetente);
            helper.setTo(dto.getDestinatario());
            helper.setText(obterTextoConcluido(dto.getNome(), dto.getPedidoId(), dto.getDataPagamento()), true);
            helper.addInline("bannerFinal", BANNER);

            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            return false;
        }

        return true;
    }

    private String obterTituloConcluido() {
        return "Pedido concluído com sucesso!";
    }

    private String obterTextoConcluido(String nomeCliente, UUID numeroPedido, LocalDateTime dataFinalizacao) {
        String pedido = numeroPedido.toString();
        String nrPedido = pedido.substring(pedido.length() - 11);

        return "<html><body>"
                + "<p>Olá, <b>" + nomeCliente + "</b>!</p>"
                + "<p>Seu pedido <b>#" + nrPedido + "</b> foi concluído com sucesso em <b>"
                + dataFinalizacao + "</b>.</p>"
                + "<p>Agradecemos pela sua compra!</p>"
                + "<br><img src='cid:bannerFinal' style='max-width: 600px; height: auto; display: inline-block;'/>"
                + "</body></html>";
    }

    public boolean enviarEmailCancelado(EmailDto.Request.Email dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(obterTituloCancelado());
            helper.setFrom(remetente);
            helper.setTo(dto.getDestinatario());
            helper.setText(obterTextoCancelado(dto.getNome(), dto.getPedidoId(), dto.getDataCancelamento()), true);
            helper.addInline("bannerFinal", BANNER);

            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            return false;
        }

        return true;
    }

    private String obterTituloCancelado() {
        return "Seu pedido foi cancelado";
    }

    private String obterTextoCancelado(String nomeCliente, UUID numeroPedido, LocalDateTime dataCancelamento) {
        String pedido = numeroPedido.toString();
        String nrPedido = pedido.substring(pedido.length() - 11);

        return "<html><body>"
                + "<p>Olá, <b>" + nomeCliente + "</b>.</p>"
                + "<p>Informamos que o pedido <b>#" + nrPedido + "</b> foi cancelado em <b>"
                + dataCancelamento + "</b>.</p>"
                + "<p>Se tiver qualquer dúvida ou precisar de ajuda, estamos à disposição.</p>"
                + "<br><img src='cid:bannerFinal' style='max-width: 600px; height: auto; display: inline-block;'/>"
                + "</body></html>";
    }

}
