package br.com.ms.produto;

import br.com.ms.produto.dto.ProdutoDto;
import br.com.ms.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(required = false) String id,
            @PageableDefault(sort = "nome", size = 15, direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(this.produtoService.buscar(id, pageable));
    }

    @PostMapping
    public ResponseEntity<ProdutoDto.Response.Produto> cadastrar(
            @RequestBody @Valid ProdutoDto.Request.Produto dto){
        return ResponseEntity.status(CREATED).body(this.produtoService.cadastrar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id){
        this.produtoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
