package br.com.ms.pagamento;

import br.com.ms.pagamento.dto.PagamentoDto;
import br.com.ms.pagamento.service.PagamentoService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String pagamentoId,
            @PageableDefault(sort = "dataCriacao", size = 15, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(this.pagamentoService.buscar(id, pagamentoId, pageable));
    }

    @PostMapping
    public ResponseEntity<PagamentoDto.Response.Pagamento> cadastrar(
            @RequestBody @Valid PagamentoDto.Request.Pagamento dto){
        return ResponseEntity.status(CREATED).body(this.pagamentoService.cadastrar(dto));
    }

    @PostMapping("/validar-pagamento")
    public ResponseEntity<PagamentoDto.Response.Pagamento> validarPagamento(
            @RequestBody @Valid PagamentoDto.Request.Validar dto){
        return ResponseEntity.status(CREATED).body(this.pagamentoService.validarPagamento(dto));
    }
}
