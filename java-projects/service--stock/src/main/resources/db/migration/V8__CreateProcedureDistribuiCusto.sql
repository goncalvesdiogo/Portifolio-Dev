DROP PROCEDURE IF EXISTS `my_stocks_db`.`distribuir_custo_negociacoes` ;

CREATE PROCEDURE `my_stocks_db`.`distribuir_custo_negociacoes`()
BEGIN
  declare no_more_rows boolean default false;
  DECLARE w_id INT;
  DECLARE w_total_venda DECIMAL(10,2);
  DECLARE w_total_compra DECIMAL(10,2);
  DECLARE w_custo_nota DECIMAL(10,2);

  DECLARE w_custo DECIMAL(10,2);
  DECLARE w_custo_rateado DECIMAL(10,2);
  DECLARE w_custo_ratear DECIMAL(10,2);
  DECLARE w_id_opr_custo_ratear INT;

  DECLARE w_id_operacao INT;
  DECLARE w_qtd_negociada INT;
  DECLARE w_preco_unitario DECIMAL(10,2);

  DECLARE cur1 CURSOR FOR SELECT id
                               , total_sold
                               , total_bought
                               , (total_liquidation_tax +
                                 total_fees		+
                                 total_tax		+
                                 total_brokerage)
							FROM broker_invoice
						   where invoice_date > '2021-01-01';

   DECLARE cur2 CURSOR  FOR SELECT id
                                 , quantity
                                 , unit_price
							  FROM finantial_movement
						     where broker_invoice_id  = w_id;

  declare continue handler for not found  set no_more_rows := true;

  open cur1;
  loop1:LOOP
  FETCH cur1 INTO w_id
                , w_total_venda
                , w_total_compra
                , w_custo_nota;
    IF no_more_rows THEN
      close cur1;
      LEAVE loop1;
    END IF;

    open cur2;
    loop2:loop
    FETCH cur2 INTO w_id_operacao
                  , w_qtd_negociada
                  , w_preco_unitario;
      IF no_more_rows THEN
      set no_more_rows := false;
        close cur2;
        LEAVE loop2;
      END IF;

      set w_custo := (w_qtd_negociada*w_preco_unitario)/(w_total_venda + w_total_compra) * w_custo_nota;

      update finantial_movement set cost = w_custo where id = w_id_operacao;

      END LOOP loop2;

      select sum(cost)
        into w_custo_rateado
        from finantial_movement
	   where broker_invoice_id = w_id;

      if (w_custo_nota - w_custo_rateado) <> 0 then

        set w_custo_ratear = w_custo_nota - w_custo_rateado;

        select id
          into w_id_opr_custo_ratear
          from finantial_movement
	     where broker_invoice_id = w_id
         order by cost desc
         limit 1 offset 1;

        update finantial_movement
           set cost = custo_negociacao + w_custo_ratear
		 where id = w_id_opr_custo_ratear;

      end if;

  END LOOP loop1;

END
