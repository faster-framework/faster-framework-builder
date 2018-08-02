<#list permissionList as item>
INSERT INTO sys_permission VALUES(${item.id?c},'${item.name}','${item.code}',${item.parentId?c},'${item.parentIds}',0,0,'${item.createDate}','${item.updateDate}',0,NULL,0);
</#list>