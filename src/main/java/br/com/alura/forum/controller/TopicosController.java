package br.com.alura.forum.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
		public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, 
						@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
//                                 @RequestParam int pagina,@RequestParam int qtd, @RequestParam String ordenacao) {

//        Pageable paginacao = PageRequest.of(pagina, qtd, Sort.Direction.DESC, ordenacao);

        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDTO.converter(topicos);
        }

    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm,
                                               UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

//         URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
//         return ResponseEntity.created(uri).body(new TopicoDTO(topico));

        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicoDTO(topico));
    }
    
    // Detalhar um Tópico
    @GetMapping("/{id}")
    public DetalhesDoTopicoDTO detalhar(@PathVariable Long id) {
    	Topico topico = topicoRepository.getOne(id);
    	return new DetalhesDoTopicoDTO(topico);
    	
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
    	Topico topico = form.atualizar(id, topicoRepository);
    	
    	return ResponseEntity.ok(new TopicoDTO(topico));
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
    	topicoRepository.deleteById(id);
    	
    	System.out.println("Tópico removido com sucesso!");
    	
    	return ResponseEntity.ok().build();
    }

}