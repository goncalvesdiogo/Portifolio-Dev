INSERT INTO my_stocks_db.stock_type (id,description) VALUES
	 (1,'AÇÕES'),
	 (2,'FUNDOS IMOBILIÁRIOS');


INSERT INTO my_stocks_db.finantial_movement_type (id,description,movement_type) VALUES
	 (1,'DEPOSITO CORRETORA','C'),
	 (2,'RENDIMENTOS','C'),
	 (3,'JUROS SOBRE CAPITAL PROPRIO','C'),
	 (4,'DIVIDENDOS','C'),
	 (5,'SAQUE CORRETORA','D'),
	 (6,'OPERACAO - COMPRA','D'),
	 (7,'OPERACAO - VENDA','C'),
	 (8,'AMORTIZACAO ','C'),
	 (9,'SUBSCRICAO - COMPRA','D'),
	 (10,'SUBSCRICAO - VENDA','C');
INSERT INTO my_stocks_db.finantial_movement_type (id,description,movement_type) VALUES
	 (11,'IRRF S/ OPERACOES','D'),
	 (12,'SUBSCRICAO - PGTO','D');
