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
   and fm.movement_date < last_day('2023-12-01') 
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
############## CUSTODIA - NOVA QUERIE COM CUSTO CORRETO ###############
select stk.code       
      ,CONCAT('DESCRIÇÃO: ', stk.company_name
             ,' - CÓDIGO ATIVO: ', stk.code
             ,' - CNPJ: ', stk.company_cnpj
             ,if(stk.stock_type_id = 2,' - ADMINISTRADORA: ',''), if(stk.stock_type_id = 2,stk.holder_company_name ,'')
             ,if(stk.stock_type_id = 2,' - CNPJ: ',''), if(stk.stock_type_id = 2,stk.holder_company_cnpj  ,'')    
             ,' - QTD DE COTAS: ', sum(if(fmv.finantial_movement_type_id in(6,9), fmv.quantity, 0) ) -  sum(if(fmv.finantial_movement_type_id in(7,10), fmv.quantity, 0) ) 
             ,' - PREÇO MÉDIO: R$ ', REPLACE(cast(get_preco_medio_ativo_by_data(stk.id, fmv.broker_id , last_day('2023-12-01')) as decimal (9,2)) ,'.',',') 
             ,' - VALOR TOTAL: R$ ', REPLACE(cast((sum(if(fmv.finantial_movement_type_id in(6,9), fmv.quantity, 0) ) - sum(if(fmv.finantial_movement_type_id in(7,10), fmv.quantity, 0) )) * get_preco_medio_ativo_by_data(stk.id, fmv.broker_id, last_day('2023-12-01')) as decimal (9,2)) ,'.',',')) DESCR
  from finantial_movement 	fmv 
      ,stock 				stk        
 where fmv.movement_date  <= last_day('2023-12-01') 
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
 where fmv.movement_date between '2023-01-01' and last_day('2023-12-01')
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
--------------------------------------------------------------------------------------------------------------------------
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
               WHERE fmv.movement_date  < last_day('2023-12-01')
                 and stk.id = fmv.stock_id    
                 and stk.stock_type_id = 2
                 and fmv.finantial_movement_type_id in (7,10)
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
    where ano_negociacao = 2023
group by 1,2,3 
ORDER BY 1,2,3;

/*
-------------------------------------------------------------------------------------------------------------------------------
QUERIE PARA SEPARAR OS LUCROS E PREJUÍZOS OBTIDOS NAS NEGOCIAÇÕES DE AÇÕES - RENDIMENTOS ISENTOS E NÃO TRIBUTÁVEIS
OBS: APENAS QUANDO A NEGOCIAÇÃO DO MÊS NÃO ULTRAPASSAR OS R$ 20.000,00
-------------------------------------------------------------------------------------------------------------------------------
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
               WHERE fmv.movement_date  < last_day('2023-12-01')
                 and stk.id = fmv.stock_id    
                 and stk.stock_type_id = 1
                 and fmv.finantial_movement_type_id in (7,10)
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
    where ano_negociacao = 2023
group by 1,2,3 
ORDER BY 1,2,3;
/*
------------------------------------------------------------------------------------
QUERIE PARA IDENTIFICAR QUANTIDADE DE ATIVOS DE AÇÕES NEGOCIADAS EM UM MÊS
------------------------------------------------------------------------------------
*/
SELECT concat(year(fm.movement_date), '/', lpad(month(fm.movement_date),2,0) ) ano_mes
     , cast(sum((fm.quantity * fm.unit_price)) as decimal(9,2)) total_vendido
  FROM finantial_movement fm 
     , stock s  
 WHERE s.id = fm.stock_id   
   and s.stock_type_id = 1
   and fm.finantial_movement_type_id in(7,10)
 group by 1 
 ORDER BY 1;
 
 /*
------------------------------------------------------------------------------------
QUERIE RECUPERAR AS INFORMACOES DE ATIVOS EM CARTEIRA
------------------------------------------------------------------------------------
*/
 
 
 # VER SALDO POR ATIVO 
 select v.id
      , v.code
      , coalesce(v.compras, 0) as compras
      , coalesce(v.vendas, 0) as vendas      
      , coalesce(v.rendimentos, 0) as rendimentos
      , coalesce(v.vendas, 0) - coalesce(v.compras, 0) + coalesce(v.rendimentos, 0) as lucro_prejuizo       
  from ( select s.id
              , s.code
              , ( select sum((quantity * unit_price)-cost) from finantial_movement where stock_id = s.id and finantial_movement_type_id in(7,10)) vendas
              , ( select sum((quantity * unit_price)+cost) from finantial_movement where stock_id = s.id and finantial_movement_type_id in(6,9)) compras
              , ( select sum((quantity * unit_price)) from finantial_movement where stock_id = s.id and finantial_movement_type_id in(2,3,4,8,10)) rendimentos
           from stock s
	   )v
   order by 6 desc;
  
 
 