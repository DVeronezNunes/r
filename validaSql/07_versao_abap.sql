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
    dbms_output.put_line('OK');
exception
  when NO_DATA_FOUND then    
    dbms_output.put_line('NOK');    
    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface asdasdas dasd as dasd asd asd asd asdasd asd asd asda sdas dasd asd asd asd a');

    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface');

    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface');

    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface');

    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface');

    dbms_output.put_line('Não é integração RFC ou ainda não foi executado interface');
end;    
/