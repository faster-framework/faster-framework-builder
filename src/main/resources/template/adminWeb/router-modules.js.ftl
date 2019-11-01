const routes = [
<#list tableColumnList as item>
  {
    path: '/${item.businessEnName}',
    name:'${item.businessCnName}管理',
    component: './${item.businessEnName}',
    authority: '${item.businessEnName}:manage',
    icon: 'smile'
  },
</#list>
];

export default routes;