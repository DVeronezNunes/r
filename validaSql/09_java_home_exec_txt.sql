--09_java_home_exec_txt--
declare 
      vCont number := 0;
begin      
    for vDEPARA in (select *
                    from   de_para_geral
                    where  s_de in ('#JAVA_HOME#', '#SAP_EXEC_TXT#')
                    order by s_de asc
                    ) loop
    -- 
       vCont := vCont + 1;
       if vCont = 1 then
          dbms_output.put_line(rpad('VARIAVEL',25) || rpad('VALOR',50));
          dbms_output.put_line(rpad('-',100,'-'));
       end if;
    --
       dbms_output.put_line(rpad(vDEPARA.s_de,25) || rpad(vDEPARA.s_para,50));
    --
    end loop;
    --
    dbms_output.put_line('OK');
    --
end;    
/