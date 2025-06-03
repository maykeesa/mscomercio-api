package br.com.ms.pedido;

import br.com.ms.pagamento.dto.PagamentoDto;
import br.com.ms.pagamento.service.PagamentoService;
import br.com.ms.pedido.dto.PedidoDto;
import br.com.ms.pedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(required = false) String id,
            @PageableDefault(sort = "dataCriacao", size = 15, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(this.pedidoService.buscar(id, pageable));
    }

    @PostMapping
    public ResponseEntity<PedidoDto.Response.Pedido> cadastrar(
            @RequestBody @Valid PedidoDto.Request.Pedido dto){
        log.info("----------------------------------------------------");
        log.info("Pedido | Inicio cadastrar()");
        PedidoDto.Response.Pedido pedido = this.pedidoService.cadastrar(dto);
        log.info("Pedido | Fim cadastrar()");
        log.info("----------------------------------------------------");

        log.info("Pagamento | Início cadastrar()");
        PagamentoDto.Request.Pagamento pagamento = new PagamentoDto.Request.Pagamento();
        pagamento.setPedidoId(pedido.getId().toString());
        pagamento.setMetodoPagamento(pedido.getMetodoPagamento());
        pagamentoService.cadastrar(pagamento);
        log.info("Pagamento | Fim cadastrar()");
        log.info("----------------------------------------------------");

        return ResponseEntity.status(CREATED).body(pedido);
    }
}
