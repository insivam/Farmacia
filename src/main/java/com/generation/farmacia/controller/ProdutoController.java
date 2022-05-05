package com.generation.farmacia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping // listar todos os produtos
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}") // listar produtos por id
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("nome/{nome}") // listar produtos por nome
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@GetMapping("/{nome}/ou/{laboratorio}") // listar produtos por nome ou laboratorio
	public ResponseEntity<List<Produto>> getByNomeOuLaboratorio(@PathVariable String nome,
			@PathVariable String laboratorio) {
		return ResponseEntity.ok(produtoRepository.findByNomeOrLaboratorio(nome, laboratorio));
	}

	@GetMapping("/{nome}/e/{laboratorio}") // listar produtos por nome e laboratorio
	public ResponseEntity<List<Produto>> findByNomeAndLaboratorio(@PathVariable String nome,
			@PathVariable String laboratorio) {
		return ResponseEntity.ok(produtoRepository.findByNomeAndLaboratorio(nome, laboratorio));
	}

	@GetMapping("/crescente") // listar produtos em ordem crescente de preço
	public ResponseEntity<List<Produto>> getByPrecoCrescente() {
		return ResponseEntity.ok(produtoRepository.findAllByOrderByPrecoAsc());
	}

	@GetMapping("/decrescente") // listar produtos em ordem decrescente de preço
	public ResponseEntity<List<Produto>> getByPrecoDecrescente() {
		return ResponseEntity.ok(produtoRepository.findAllByOrderByPrecoDesc());
	}

	@GetMapping("/maior/{preco}") // método get para listar produtos com preço maior que o preço passado
	public ResponseEntity<List<Produto>> getByPrecoMaior(@PathVariable Double preco) {
		return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThan(preco));
	}

	@GetMapping("/menor/{preco}") // método get para listar produtos com preço menor que o preço passado
	public ResponseEntity<List<Produto>> getByPrecoMenor(@PathVariable Double preco) {
		return ResponseEntity.ok(produtoRepository.findAllByPrecoLessThan(preco));
	}

	@GetMapping("/entre/{preco_inicial}/{preco_final}") // listar produtos com preço entre os preços passados
	public ResponseEntity<List<Produto>> getByPrecoEntre(@PathVariable Double preco_inicial,
			@PathVariable Double preco_final) {
		return ResponseEntity.ok(produtoRepository.findByPrecoBetween(preco_inicial, preco_final));
	}

	@PostMapping // criar um produto
	public ResponseEntity<Produto> postProduto(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PutMapping // atualizar um produto
	public ResponseEntity<Produto> putProduto(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getId())) {
			if (categoriaRepository.existsById(produto.getCategoria().getId())) {
				return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@DeleteMapping("/{id}") // deletar um produto
	public ResponseEntity<?> deleteProduto(@PathVariable Long id) {
		return produtoRepository.findById(id).map(resposta -> {
			produtoRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
