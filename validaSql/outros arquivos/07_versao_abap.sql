--07_versao_abap--
declare
    vAbapversion varchar2(100);
begin      
    SELECT OBS as ABAP_VERSION
      into vAbapversion
      FROM (SELECT *
              FROM LOG_IMPORTACAO
             WHERE OBS LIKE '%  Interface SAP        :%'
             ORDER BY DATA_HORA DESC)
     WHERE ROWNUM = 1;
    --  
    dbms_output.put_line(vAbapversion);
    dbms_output.put_line(' ***OK***');
exception
  when NO_DATA_FOUND then    
    dbms_output.put_line(' ***OK***');    
    dbms_output.put_line(' ***N�o � integra��o RFC ou ainda n�o foi executado interface***');
end;    
/