DROP FUNCTION IF EXISTS `my_stocks_db`.`get_preco_medio_ativo_by_data` ;

CREATE FUNCTION `my_stocks_db`.`get_preco_medio_ativo_by_data`(p_ativo_id INT, p_corretora_id INT, p_data_negociacao DATE) RETURNS decimal(10,2)
    READS SQL DATA
    DETERMINISTIC
BEGIN
DECLARE no_more_rows boolean default false;

DECLARE w_preco_medio DECIMAL(10,2);

DECLARE w_data_negociacao DATE;
DECLARE w_tipo_operacao INT;
DECLARE w_qtd_negociada INT;
DECLARE w_preco_unitario DECIMAL(10,2);
DECLARE w_custo_negociacao DECIMAL(10,2);

DECLARE w_total_qtd_negociada INT;
DECLARE w_total_valor_negociado DECIMAL(10,2);

DECLARE cur1 CURSOR FOR select fmv.movement_date
                             , fmv.finantial_movement_type_id
                             , fmv.quantity
                             , fmv.unit_price
                             , fmv.cost
					      from finantial_movement fmv
                         where fmv.stock_id  		= p_ativo_id
						   and fmv.movement_date  	<= p_data_negociacao
                           and fmv.broker_id     	= p_corretora_id
                           and fmv.finantial_movement_type_id in (6,7,8,9,10,12)
				         order by 1;

declare continue handler for not found  set no_more_rows := true;

  set w_total_qtd_negociada := 0;
  set w_total_valor_negociado := 0;
  set w_preco_medio := 0;

  open cur1;
  loop1:LOOP
  FETCH cur1 INTO w_data_negociacao
                , w_tipo_operacao
                , w_qtd_negociada
                , w_preco_unitario
                , w_custo_negociacao;
    IF no_more_rows THEN
      close cur1;
      LEAVE loop1;
    END IF;

    #LÓGICA DE CÁLCULO DE PREÇO MÉDIO
    if w_tipo_operacao in (6,9) then
      set w_total_qtd_negociada := w_total_qtd_negociada + w_qtd_negociada;
	  set w_total_valor_negociado := w_total_valor_negociado + (w_qtd_negociada * w_preco_unitario) ;
      set w_preco_medio := (w_total_valor_negociado + w_custo_negociacao)/ w_total_qtd_negociada;

    elseif w_tipo_operacao in (7,10) then
      set w_total_qtd_negociada := w_total_qtd_negociada - w_qtd_negociada;
      set w_total_valor_negociado := w_total_qtd_negociada * w_preco_medio ;

    elseif w_tipo_operacao = 8 then
      set w_preco_medio := ((w_preco_medio * w_total_qtd_negociada) - w_preco_unitario) / w_total_qtd_negociada;
      set w_total_valor_negociado := w_total_qtd_negociada * w_preco_medio ;

    elseif w_tipo_operacao = 12 then
      set w_preco_medio := ((w_preco_medio * w_total_qtd_negociada) + w_preco_unitario) / w_total_qtd_negociada;
      set w_total_valor_negociado := w_total_qtd_negociada * w_preco_medio ;
    end if;

END LOOP loop1;

return round(if(w_preco_medio is null,0, w_preco_medio),2) ;

END