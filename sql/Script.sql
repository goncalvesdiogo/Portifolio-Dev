/* 
-----------------------------------------------------------------------
QUERIE PARA VALIDAR O SALDO NA CORRETORA
-----------------------------------------------------------------------
*/
select sum(cost) from (
select fm.movement_date 
     , fm.broker_invoice_id 
     , sum((unit_price * quantity)* if( fmt.movement_type = 'C', 1, -1) - cost) cost
  from finantial_movement fm 
     , finantial_movement_type fmt
 where fm.finantial_movement_type_id = fmt.id
   and fm.movement_date < last_day('2022-12-01') 
   and fm.broker_id = 1
 group by fm.movement_date,
          fm.broker_invoice_id 
 order by fm.movement_date
)aux;


/* 
-----------------------------------------------------------------------
QUERIE PARA PREENCHER BENS E DIREITOS - POSIÇÃO DAS COTAS NO DIA 31/12
UTILIZAR TANTO PARA AÇÕES COMO PARA FUNDOS IMOBILIÁRIOS
-----------------------------------------------------------------------
*/
############################ NOVA QUERIE COM CUSTO CORRETO ############################
select stk.code       
      ,CONCAT('DESCRIÇÃO: ', stk.company_name
             ,' - CÓDIGO ATIVO: ', stk.code
             ,' - CNPJ: ', stk.company_cnpj
             ,if(stk.stock_type_id = 2,' - ADMINISTRADORA: ',''), if(stk.stock_type_id = 2,stk.holder_company_name ,'')
             ,if(stk.stock_type_id = 2,' - CNPJ: ',''), if(stk.stock_type_id = 2,stk.holder_company_cnpj  ,'')    
             ,' - QTD DE COTAS: ', sum(if(fmv.finantial_movement_type_id in(6,9), fmv.quantity, 0) ) -  sum(if(fmv.finantial_movement_type_id in(7,10), fmv.quantity, 0) ) 
             ,' - PREÇO MÉDIO: R$ ', REPLACE(cast(get_preco_medio_ativo_by_data(stk.id, fmv.broker_id , last_day('2022-12-01')) as decimal (9,2)) ,'.',',') 
             ,' - VALOR TOTAL: R$ ', REPLACE(cast((sum(if(fmv.finantial_movement_type_id in(6,9), fmv.quantity, 0) ) - sum(if(fmv.finantial_movement_type_id in(7,10), fmv.quantity, 0) )) * get_preco_medio_ativo_by_data(stk.id, fmv.broker_id, last_day('2022-12-01')) as decimal (9,2)) ,'.',',')) DESCR
  from finantial_movement 	fmv 
      ,stock 				stk        
 where fmv.movement_date  <= last_day('2022-12-01') 
   and stk.id  = fmv.stock_id     
   and fmv.finantial_movement_type_id  in (6,7,9,10)
   and fmv.broker_id = 1
  GROUP BY stk.code
         , stk.company_name
         , stk.company_cnpj
         , stk.id
         , stk.stock_type_id 
         , stk.holder_company_name
         , stk.holder_company_cnpj
         , fmv.broker_id         
 having sum(if(fmv.finantial_movement_type_id in(6,9), fmv.quantity, 0) ) > sum(if(fmv.finantial_movement_type_id in(7,10), fmv.quantity, 0) )
  order by 1;


/* 
--------------------------------------------------------------------------------------------------------------
QUERIE PARA PREENCHER RENDIMENTOS ISENTOS E NAO TRIBUTAVEIS - DIVIDENDOS E RENDIMENTOS DE FUNDOS IMOBILIARIOS
UTILIZAR TANTO PARA AÇÕES COMO PARA FUNDOS IMOBILIÁRIOS
--------------------------------------------------------------------------------------------------------------
*/
select stk.id
     , stk.code    
     , stp.description 
     , fmt.description
     , if(stp.id = 2, stk.holder_company_cnpj, stk.company_cnpj) CNPJ_FONTE_PAGADORA
     , if(stp.id = 2, stk.holder_company_name, stk.company_name) NOME_FONTE_PAGADORA     
     , CONCAT(if(stp.id = 2,'PROVENTOS RECEB. DO FII ', 'PROVENTOS RECEB. DA AÇÃO '), stk.code, ' NO TOTAL DE R$ ',  REPLACE((cast(sum(fmv.unit_price) as decimal (9,2))),'.',',')) TEXTO
  from finantial_movement      fmv
     , finantial_movement_type fmt   
     , stock 			       stk        
     , stock_type 			   stp
 where fmv.movement_date between '2022-01-01' and last_day('2022-12-01')
   and fmt.id = fmv.finantial_movement_type_id
   and fmt.id in (2, 3, 4, 8)
   and stk.id = fmv.stock_id
   and stp.id = stk.stock_type_id     
 group by stk.id
         ,stk.code    
         ,fmt.description
         ,stp.description
 order by 3, 4;
/*
--------------------------------------ativo------------------------------------------------------------------------------------
QUERIE PARA SEPARAR MENSALMENTE OS LUCROS E PREJUÍZOS OBTIDOS NAS NEGOCIAÇÕES DE FUNDOS IMOBILIARIOS - RENDA VARIAVEL
--------------------------------------------------------------------------------------------------------------------------
*/
select vendas.code
     , vendas.ano_negociacao
     , vendas.mes_negociacao
     , cast(sum(vendas.valor_venda) as decimal(9,2)) total_vendas
     , cast(sum(vendas.valor_venda) - sum(vendas.valor_aquisicao)as decimal(9,2)) lucro_prejuizo     
from (select vendas.code     
           , year(vendas.movement_date) ano_negociacao
           , month(vendas.movement_date) mes_negociacao      
           , sum((fmv.quantity * fmv.unit_price) + fmv.cost) valor_aquisicao       
           , vendas.valor_venda 
        from (SELECT stk.code
                   , fmv.movement_date  
                   , fmv.stock_id 
                   , fmv.broker_id 
                   , lag(fmv.movement_date,1) OVER ( PARTITION BY fmv.stock_id, fmv.broker_id ORDER BY fmv.movement_date  ) last_dt_venda 
                   , cast(sum((fmv.quantity * fmv.unit_price) - fmv.cost) as decimal(9,2)) valor_venda
                FROM finantial_movement fmv
                   , stock              stk
               WHERE fmv.movement_date  < last_day('2022-12-01')
                 and stk.id = fmv.stock_id    
                 and stk.stock_type_id = 2
                 and fmv.finantial_movement_type_id in (7)
               group by stk.code
                   , fmv.movement_date  
                   , fmv.stock_id 
                   , fmv.broker_id
             ) vendas
             , finantial_movement fmv
         where fmv.stock_id  = vendas.stock_id         
           and fmv.movement_date between coalesce(vendas.last_dt_venda,fmv.movement_date) and vendas.movement_date
           and fmv.finantial_movement_type_id in(6,9)
           and fmv.broker_id  = vendas.broker_id
		 group by  1,2,3,5 
    ) vendas 
    where ano_negociacao = 2022
group by 1,2,3 
ORDER BY 1,2,3;

/*
-------------------------------------------------------------------------------------------------------------------------------
QUERIE PARA SEPARAR OS LUCROS E PREJUÍZOS OBTIDOS NAS NEGOCIAÇÕES DE AÇÕES - RENDIMENTOS ISENTOS E NÃO TRIBUTÁVEIS
OBS: APENAS QUANDO A NEGOCIAÇÃO DO MÊS NÃO ULTRAPASSAR OS R$ 20.000,00
-------------------------------------------------------------------------------------------------------------------------------
*/
select vendas.codigo
     ,concat(if(  cast(sum(vendas.valor_venda) - sum(vendas.valor_aquisicao)as decimal(9,2)) > 0, 'LUCRO VENDA ATIVO: ', 'PREJUÍZO VENDA ATIVO: '), vendas.codigo) DISCRIMINAÇÃO
     , cast(sum(vendas.valor_venda) - sum(vendas.valor_aquisicao)as decimal(9,2)) lucro_prejuizo     
from (select vendas.codigo     
           , vendas.ativo_id          
           , sum((opr.QTD_NEGOCIADA * opr.preco_unitario)) valor_aquisicao       
           , vendas.valor_venda 
        from (SELECT atv.CODIGO
                   , opr.ativo_id
                   , opr.corretora_id
                   , cast(sum((opr.QTD_NEGOCIADA * opr.preco_unitario)) as decimal(9,2)) valor_venda
                FROM operacoes opr
                   , ativo atv
               WHERE opr.data_negociacao between '2019-01-01' and '2022-12-31' 
                 and atv.id = opr.ativo_id   
                 and atv.categoria_ativo_id = 1
                 and opr.tipo_operacao = 'V'            
               group by atv.CODIGO            
	               , opr.ativo_id
                   , opr.corretora_id
             ) vendas
             , operacoes opr
         where opr.ativo_id = vendas.ativo_id
           and opr.tipo_operacao = 'C'
           and opr.corretora_id = vendas.corretora_id
           and opr.data_negociacao < (select max(data_negociacao) from operacoes where ativo_id = opr.ativo_id and tipo_operacao = 'V' and corretora_id = opr.corretora_id)
		 group by  1,2,4
    ) vendas
    , ativo atv
    where vendas.ativo_id = atv.id
group by 1
ORDER BY 1;
/*
------------------------------------------------------------------------------------
QUERIE PARA IDENTIFICAR QUANTIDADE DE ATIVOS DE AÇÕES NEGOCIADAS EM UM MÊS
------------------------------------------------------------------------------------
*/
SELECT concat(year(opr.DATA_NEGOCIACAO), '/', lpad(month(opr.DATA_NEGOCIACAO),2,0) ) ano_mes
     , cast(sum((opr.QTD_NEGOCIADA * opr.preco_unitario)) as decimal(9,2)) total_vendido
  FROM operacoes opr
     , ativo atv
 WHERE atv.id = opr.ativo_id   
   and atv.categoria_ativo_id in (1)
   and opr.tipo_operacao = 'V'
 group by 1 
 ORDER BY 1;
 
 /*
------------------------------------------------------------------------------------
QUERIE RECUPERAR AS INFORMACOES DE ATIVOS EM CARTEIRA
------------------------------------------------------------------------------------
*/
 
 
 # VER SALDO POR ATIVO 
 select v.*, v.vendas - v.compras lucro_prejuizo from(
 select atv.id
     , atv.codigo
     , ( select sum((qtd_negociada * preco_unitario)-custo_negociacao) from operacoes where ativo_id = atv.id and tipo_operacao = 'V') vendas
     , ( select sum((qtd_negociada * preco_unitario)+custo_negociacao) from operacoes where ativo_id = atv.id and tipo_operacao = 'C' and data_negociacao <= (select max(data_negociacao) from operacoes where ativo_id = atv.id and tipo_operacao = 'V')) compras
  from ativo atv)v;


#EXIBIR LUCRO/PREJUÍZO DE OPERAÇÕES DE VENDA
select opr.id
     , opr.data_negociacao
     , opr.nota_corretagem_id
     , atv.id
     , atv.codigo
     , opr.qtd_negociada
     , opr.preco_unitario
     , opr.custo_negociacao
     , get_preco_medio_ativo_by_data(atv.id, opr.corretora_id, opr.data_negociacao) preco_medio
     , (opr.qtd_negociada * PRECO_UNITARIO) - ((opr.qtd_negociada * get_preco_medio_ativo_by_data(atv.id, opr.corretora_id, opr.data_negociacao)) + opr.CUSTO_NEGOCIACAO)   lucro_prejuizo 
  from operacoes opr 
	 , ativo atv
 where opr.tipo_operacao = 'V'
   and atv.id = opr.ativo_id
 order by atv.id, opr.data_negociacao; 
 
 select v.id
      , v.codigo
      , sum(v.lucro_prejuizo) lucro_prejuizo
   from (select atv.id
              , atv.codigo
              , (opr.qtd_negociada * PRECO_UNITARIO) - ((opr.qtd_negociada * get_preco_medio_ativo_by_data(atv.id, opr.corretora_id, opr.data_negociacao)) + opr.CUSTO_NEGOCIACAO)   lucro_prejuizo 
           from operacoes opr 
	          , ativo atv
          where opr.tipo_operacao = 'V'
            and atv.id = opr.ativo_id) v
  group by v.id
	     , v.codigo
  order by v.codigo ;
  
  
  
  
  
  
  
  
  
  
  
  
  
  /*
  CREATE DEFINER=`root`@`localhost` FUNCTION `get_preco_medio_ativo_by_data`(p_ativo_id INT, p_corretora_id INT, p_data_negociacao DATE) RETURNS decimal(10,2)
    READS SQL DATA
    DETERMINISTIC
BEGIN
DECLARE no_more_rows boolean default false;
  
DECLARE w_preco_medio DECIMAL(10,2);

DECLARE w_oper_id INT;
DECLARE w_data_negociacao DATE;
DECLARE w_tipo_operacao VARCHAR(1);
DECLARE w_qtd_negociada INT;
DECLARE w_preco_unitario DECIMAL(10,2);
DECLARE w_custo_negociacao DECIMAL(10,2);

DECLARE w_total_qtd_negociada INT;
DECLARE w_total_valor_negociado DECIMAL(10,2);

DECLARE cur1 CURSOR FOR select id
                             , data_negociacao  
                             , tipo_operacao
                             , qtd_negociada
                             , preco_unitario
                             , custo_negociacao
					      from operacoes
                         where ativo_id = p_ativo_id
						   and corretora_id = p_corretora_id
                           and data_negociacao <= p_data_negociacao
                         order by data_negociacao;
  
declare continue handler for not found  set no_more_rows := true;
 
  set w_total_qtd_negociada   := 0;
  set w_total_valor_negociado := 0;
  set w_total_qtd_negociada   := 0;
  set w_preco_medio           := 0;    
  set w_qtd_negociada         := 0;

  open cur1; 
  loop1:LOOP
  FETCH cur1 INTO w_oper_id
                , w_data_negociacao  
                , w_tipo_operacao
                , w_qtd_negociada
                , w_preco_unitario
                , w_custo_negociacao;   
    IF no_more_rows THEN
      close cur1;
      LEAVE loop1;
    END IF;  

      #LÓGICA DE CÁLCULO DE PREÇO MÉDIO 
      if w_tipo_operacao = 'C' then
        
        set w_total_qtd_negociada := w_total_qtd_negociada + w_qtd_negociada;
		set w_total_valor_negociado := w_total_valor_negociado + (w_qtd_negociada * w_preco_unitario) ;
        set w_preco_medio := round((w_total_valor_negociado + w_custo_negociacao)/ w_total_qtd_negociada,2); 
        INSERT INTO `my_stocks`.`logs`(`chave`,`valor_texto`,`valor_numerico1`,`valor_numerico2`,`valor_numerico3`)
             VALUES ('DRG', 'COMPRA', w_total_qtd_negociada, w_total_valor_negociado, w_preco_medio);


      elseif w_tipo_operacao = 'V' then
        
        set w_total_qtd_negociada := w_total_qtd_negociada - w_qtd_negociada;
        set w_total_valor_negociado := w_total_qtd_negociada * w_preco_medio ;
      
         INSERT INTO `my_stocks`.`logs`(`chave`,`valor_texto`,`valor_numerico1`,`valor_numerico2`,`valor_numerico3`)
             VALUES ('DRG', 'VENDA', w_total_qtd_negociada, w_total_valor_negociado, w_preco_medio);

      end if;     
      
END LOOP loop1;

return if(w_preco_medio is null,0, w_preco_medio) ;

END
  */
   


