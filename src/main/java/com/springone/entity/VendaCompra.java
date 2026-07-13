package com.springone.entity;

import java.util.Date;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "seq_compravenda", sequenceName = "seq_compravenda", allocationSize = 1, initialValue = 1)

public class VendaCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_compravenda")
	private Long id;
	private double totalVendaFinal;
	private double desconto;
	private String observacao;
	private Date data;

	@ManyToOne
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalVendaFinal() {
		return totalVendaFinal;
	}

	public void setTotalVendaFinal(double totalVendaFinal) {
		this.totalVendaFinal = totalVendaFinal;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
