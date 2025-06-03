package br.com.ms.categoria;

import br.com.ms.categoria.dto.CategoriaDto;
import br.com.ms.categoria.service.CategoriaService;
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
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(required = false) String id,
            @PageableDefault(sort = "nome", size = 15, direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(this.categoriaService.buscar(id, pageable));
    }

    @PostMapping
    public ResponseEntity<CategoriaDto.Response.Categoria> cadastrar(
            @RequestBody @Valid CategoriaDto.Request.Categoria dto){
        return ResponseEntity.status(CREATED).body(this.categoriaService.cadastrar(dto));
    }
}
