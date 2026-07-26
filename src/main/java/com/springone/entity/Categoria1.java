package com.springone.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Lombok gera o construtor vazio exigido pelo JPA.
@Entity // Mapeia a classe para uma tabela do banco.
@Table(name = "Categoria1")
// Configura a sequence do banco usada para gerar os IDs automaticamente (de 1 em 1).
@SequenceGenerator(name = "seq_categoria", sequenceName = "seq_categoria", allocationSize = 1, initialValue = 1)
public class Categoria1 {

	@Id // Chave primária.
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria")
	private Long id;

	// Nome não pode ser nulo/vazio (obs: as duas anotações juntas são redundantes,
	// @NotBlank já cobre o que @NotNull cobre).
	@NotNull(message = "Nome da categoria deve ser infromada")
	@NotBlank(message = "Nome da categroia deve ser informada")
	@Column(unique = true) // Não pode haver duas categorias com o mesmo nome no banco.
	private String nome;

	/* Uma Categoria para muitos produtos */
	// Evita loop infinito ao converter para JSON (Categoria -> Produtos ->
	// Categoria...).
	@JsonBackReference
	// Relação 1:N com ProdutoJL. LAZY = só carrega a lista quando for acessada.
	// mappedBy indica que a chave estrangeira fica na tabela de ProdutoJL, não
	// aqui.
	@OneToMany(mappedBy = "categoria1", fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.MERGE)
	private List<ProdutoJL> produtoJLs = new ArrayList<ProdutoJL>();

	// Getters e setters (usados pelo Hibernate e pelo Jackson).
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ProdutoJL> getProdutoJLs() {
		return produtoJLs;
	}

	public void setProdutoJLs(List<ProdutoJL> produtoJLs) {
		this.produtoJLs = produtoJLs;
	}

}