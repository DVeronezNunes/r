--06_versao --
begin      
  for vSistema in (SELECT sv.cod_sistema, sv.cod_versao, ss.nome
                        FROM  SFW_SISTEMA_VERSAO SV, 
                              SFW_SISTEMA SS 
                        WHERE SV.COD_SISTEMA = SS.COD_SISTEMA
                        AND   SV.VALIDO = 'S'
                        AND   SV.USUARIO = '&IT_USER'
                       ORDER BY NOME ) loop
      
    if vSistema.cod_sistema = 607  then -- IT_COMUM
       if vSistema.cod_versao = '&IT_COMUM' then
          dbms_output.put_line(vSistema.nome || 'OK');
       else
          dbms_output.put_line(vSistema.nome || 'NOK');
       end if;
    else
       if vSistema.cod_versao = '&IT_SAP' then
          dbms_output.put_line(vSistema.nome || 'OK');
       else
          dbms_output.put_line(vSistema.nome || 'NOK');
       end if;
    end if;
    --
  end loop; 
end;    
/