-- quantos objetos inválidos temos aqui?--
declare 
      vCont    number := 0;
      vObjname number;
      vObjid   number;
      vObjtype number;
      vCreated number;
      vLastddl number;
      vTime    number;  
begin
  for cObjects in (select OBJECT_NAME, OBJECT_ID, OBJECT_TYPE, CREATED, LAST_DDL_TIME, TIMESTAMP
                    from  USER_OBJECTS
                   where  STATUS = 'INVALID'
                     and  OBJECT_TYPE in ('SEQUENCE',
                                         'PROCEDURE',
                                         'LOB',
                                         'PACKAGE',
                                         'PACKAGE BODY',
                                         'TRIGGER',
                                         'SYNONYM',
                                         'TABLE',
                                         'INDEX',
                                         'FUNCTION',
                                         'VIEW',
                                         'TYPE')
                  ) loop
                  
    -- 
       vCont := vCont + 1;
       if vCont = 1 then
          vObjname := length(cObjects.Object_Name)   + 10;
          vObjid   := length(cObjects.Object_Id)     + 10;
          vObjtype := length(cObjects.Object_Type)   + 10;
          vCreated := length(cObjects.Created)       + 10;
          vLastddl := length(cObjects.Last_Ddl_Time) + 10;
          vTime    := length(cObjects.Timestamp)     + 10;
          dbms_output.put_line(rpad('OBJECT_NAME',vObjname) || rpad('OBJECT_ID',vObjid) || rpad('OBJECT_TYPE',vObjtype) || rpad('CREATED',vCreated) || rpad('LAST_DDL_TIME',vLastddl) 
                            || rpad('TIMESTAMP',vTime));
          dbms_output.put_line(rpad('-',200,'-'));
       end if;
    --             
       dbms_output.put_line(rpad(cObjects.Object_Name,vObjname) || rpad(cObjects.Object_Id,vObjid) || rpad(cObjects.Object_Type,vObjtype) || rpad(cObjects.Created,vCreated) 
                         || rpad(cObjects.Last_Ddl_Time,vLastddl) || rpad(cObjects.Timestamp,vTime));
    --                              
  end loop;
  --
  if vCont = 0 then
    dbms_output.put_line('OK');
  else
    dbms_output.put_line('NOK');
  end if;  
  -- 
end;
/